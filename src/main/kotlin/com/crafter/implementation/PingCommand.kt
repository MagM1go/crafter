package com.crafter.implementation

import com.crafter.discord.commands.SlashCommand
import com.crafter.discord.t9n.text
import com.crafter.structure.minecraft.protocol.MinecraftProtocol
import com.crafter.structure.minecraft.protocol.packet.handshake.HandshakeState
import com.crafter.structure.utilities.UnstableApi
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

@OptIn(UnstableApi::class)
object PingCommand : SlashCommand<SlashCommandInteractionEvent>("ping", "ping.description") {
    // TODO: Move it to minecraft and add more versions
    private val protocolVersionMap = mapOf(
        "1.8" to 47,
        "1.12.2" to 340,
        "1.19.3" to 761
    )

    override fun buildOptions(): SlashCommandData = instance.addOptions(
        OptionData(OptionType.STRING, "server", "ping.server.description", true),
        OptionData(OptionType.STRING, "port", "ping.port.description", false),
        OptionData(OptionType.STRING, "version", "ping.version.description", false),
    )

    override suspend fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply().queue()

        val address = event.getOption("server")!!.asString
        val port = event.getOption("port")?.asInt ?: 25565
        val version = event.getOption("version")?.asString ?: "1.12.2"
        val protocolVersion = protocolVersionMap[version] ?: 340

        try {
            val serverInfo = MinecraftProtocol(address, port).use {
                it.sendHandshake(protocolVersion, HandshakeState.State)
            }

            event.hook.sendMessage(serverInfo).queue()
        } catch (e: Exception) {
            event.hook.sendMessage(
                text(
                    "ping.cant_retrieve_server",
                    "Make sure IP / port is valid. Other possible issues:\n" +
                            "1. Invalid version\n" +
                            "2. Server is offline",
                    event.userLocale
                )
            ).queue()
        }
    }
}