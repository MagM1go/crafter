package com.crafter.implementation

import com.crafter.discord.commands.SlashCommand
import com.crafter.discord.t9n.text
import com.crafter.implementation.listeners.ReactionListener
import com.crafter.structure.database.repositories.BridgeRepository
import com.crafter.structure.database.repositories.RCONRepository
import com.crafter.structure.minecraft.Color
import com.crafter.structure.minecraft.Formatting
import com.crafter.structure.minecraft.rcon.RconController
import com.crafter.structure.utilities.getDefaultScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import kotlin.time.Duration.Companion.seconds

object BridgeCommand : SlashCommand(
    "bridge",
    "Makes bridge between your minecraft server and discord"
) {
    private const val WHITE_CHECK_MARK = "U+2705"
    private val WHITE_CHECK_MARK_EMOJI = Emoji.fromUnicode(WHITE_CHECK_MARK)

    override suspend fun execute(event: SlashCommandInteractionEvent) {
        when (event.subcommandName) {
            "enable" -> {
                val guildId = event.guild!!.id

                var isBridgeEnabled = BridgeRepository.isEnabled(guildId)
                val isRconEnabled = RCONRepository.get(guildId) != null

                if (!isRconEnabled && !isBridgeEnabled) {
                    event.replyLocalized(
                        "bridge.rcon_not_enabled",
                        "Sorry, but the bridge can't work without RCON. Please set up RCON and try again.",
                        event.userLocale
                    )
                    return
                }

                val bridgeData = BridgeRepository.get(guildId)
                val toUpsert = mutableMapOf<String, Any>(
                    "guildId" to guildId,
                    "isEnabled" to !isBridgeEnabled
                )

                bridgeData?.let {
                    toUpsert["channelId"] = it["channelId"].toString()
                }

                isBridgeEnabled = toUpsert["isEnabled"] as Boolean
                BridgeRepository.upsert(toUpsert)

                val statusText = text(
                    "bridge.toggle.value.${if (isBridgeEnabled) "enabled" else "disabled"}",
                    if (isBridgeEnabled) "enabled" else "disabled",
                    event.userLocale
                )

                val responseText = text(
                    "bridge.toggled",
                    "Bridge was %s",
                    event.userLocale
                ).format(statusText)

                event.reply(responseText).queue()
            }
            "channel" -> {
                val repository = BridgeRepository
                val guildId = event.guild!!.id
                val data = repository.get(guildId)
                val channelId = event.getOption("channel")!!.asChannel.id

                if (data != null && data["channelId"] == channelId) {
                    repository.delete(guildId)
                    event.replyLocalized("bridge.channel.deleted", "Channel was deleted as bridge.", event.userLocale)
                    return
                }

                repository.upsert(mapOf(
                    "guildId" to event.guild!!.id,
                    "isEnabled" to true,
                    "channelId" to channelId
                ))

                event.replyLocalized("bridge.channel.updated", "Channel was set.", event.userLocale)
            }
        }
    }

    override fun autoComplete(event: CommandAutoCompleteInteractionEvent): List<Pair<String, List<String>>>? = null

    override fun onMessageReceived(event: MessageReceivedEvent) {
        getDefaultScope().launch {
            if (event.author.isBot) return@launch

            val repository = BridgeRepository

            if (repository.isEnabled(event.guild.id)) {
                val data = repository.get(event.guild.id) ?: return@launch
                val channelId = data["channelId"]

                if (channelId == event.channel.id) {
                    val rconData = RCONRepository.get(event.guild.id) ?: return@launch
                    val listener = ReactionListener(event.channel.id, event.author.id, WHITE_CHECK_MARK_EMOJI) {
                        RconController(
                            rconData["ip"].toString(),
                            rconData["port"] as Int,
                            RCONRepository.getRconPassword(rconData["password"].toString())
                        ).use {
                            var command = "tellraw @a \"${Color.BLUE.code}[Discord]${Formatting.RESET_ADD_COLOR.code} ${event.message.author.name}: ${event.message.contentDisplay} %s\""

                            command = if (event.message.attachments.isNotEmpty()) {
                                command.format("[attachments (${event.message.attachments.size})]")
                            } else {
                                command.format("")
                            }

                            it.send(command)

                            event.message.clearReactions().queue()
                        }
                    }

                    event.message.addReaction(WHITE_CHECK_MARK_EMOJI).queue()
                    listener.register(event.jda)

                    withTimeoutOrNull(5.seconds) { listener.await() } ?: clearReactionsAndUnregister(listener, event.message)
                }
            }
        }
    }

    private fun clearReactionsAndUnregister(listener: ReactionListener, message: Message) {
        message.clearReactions().queue()
        listener.unregister(message.jda)
    }

    init {
        commandData.addSubcommands(
            SubcommandData("enable", "Toggle bridge between servers"),
            SubcommandData("channel", "Channel that should be bridge between servers")
                .addOption(OptionType.CHANNEL, "channel", "Channel", true)
        )

        commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
    }
}