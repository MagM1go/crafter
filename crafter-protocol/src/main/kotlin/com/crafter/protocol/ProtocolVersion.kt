package com.crafter.protocol

enum class ProtocolVersion(val original: String, val number: Int) {
    V1_21_1("1.21.1", 767),
    V1_21("1.21", 767),
    V1_20_1("1.20.1", 763),
    V1_20("1.20", 762),
    V1_19_4("1.19.4", 761),
    V1_19_3("1.19.3", 760),
    V1_19_2("1.19.2", 759),
    V1_19_1("1.19.1", 758),
    V1_19("1.19", 757),
    V1_18_2("1.18.2", 757),
    V1_18_1("1.18.1", 757),
    V1_18("1.18", 756),
    V1_17_1("1.17.1", 755),
    V1_17("1.17", 754),
    V1_16_5("1.16.5", 754),
    V1_16_4("1.16.4", 754),
    V1_16_3("1.16.3", 753),
    V1_16_2("1.16.2", 751),
    V1_16_1("1.16.1", 736),
    V1_16("1.16", 735),
    V1_15_2("1.15.2", 578),
    V1_15_1("1.15.1", 575),
    V1_15("1.15", 573),
    V1_14_4("1.14.4", 498),
    V1_14_3("1.14.3", 490),
    V1_14_2("1.14.2", 485),
    V1_14_1("1.14.1", 480),
    V1_14("1.14", 477),
    V1_13_2("1.13.2", 404),
    V1_13_1("1.13.1", 401),
    V1_13("1.13", 393),
    V1_12_2("1.12.2", 340),
    V1_12_1("1.12.1", 338),
    V1_12("1.12", 335),
    V1_11_2("1.11.2", 316),
    V1_11_1("1.11.1", 316),
    V1_11("1.11", 315),
    V1_10_2("1.10.2", 210),
    V1_10_1("1.10.1", 210),
    V1_10("1.10", 210),
    V1_9_4("1.9.4", 110),
    V1_9_3("1.9.3", 110),
    V1_9_2("1.9.2", 109),
    V1_9_1("1.9.1", 108),
    V1_9("1.9", 107),
    V1_8_9("1.8.9", 47),
    V1_8_8("1.8.8", 47),
    V1_8_7("1.8.7", 47),
    V1_8_6("1.8.6", 47),
    V1_8_5("1.8.5", 47),
    V1_8_4("1.8.4", 47),
    V1_8_3("1.8.3", 47),
    V1_8_2("1.8.2", 47),
    V1_8_1("1.8.1", 47),
    V1_8("1.8", 47),
    V1_7_10("1.7.10", 5),
    V1_7_9("1.7.9", 5),
    V1_7_8("1.7.8", 5),
    V1_7_7("1.7.7", 5),
    V1_7_6("1.7.6", 5),
    V1_7_5("1.7.5", 4),
    V1_7_4("1.7.4", 4),
    V1_7_2("1.7.2", 4),
    V1_6_4("1.6.4", 78),
    V1_6_2("1.6.2", 74),
    V1_6_1("1.6.1", 73),
    V1_5_2("1.5.2", 61),
    V1_5_1("1.5.1", 60),
    V1_5("1.5", 60),
    V1_4_7("1.4.7", 51),
    V1_4_6("1.4.6", 51),
    V1_4_5("1.4.5", 49),
    V1_4_4("1.4.4", 49),
    V1_4_2("1.4.2", 47),
    V1_3_2("1.3.2", 39),
    V1_3_1("1.3.1", 39),
    V1_2_5("1.2.5", 29),
    V1_2_4("1.2.4", 29),
    V1_2_3("1.2.3", 28),
    V1_2_2("1.2.2", 28),
    V1_2_1("1.2.1", 28),
    V1_1("1.1", 23),
    V1_0_0("1.0.0", 22),
    BETA_1_8("Beta 1.8", 17),
    BETA_1_7_3("Beta 1.7.3", 16),
    BETA_1_6_6("Beta 1.6.6", 15),
    BETA_1_5_2("Beta 1.5_02", 14),
    BETA_1_4("Beta 1.4", 13),
    BETA_1_3("Beta 1.3", 12),
    BETA_1_2_2("Beta 1.2_02", 11),
    BETA_1_1_2("Beta 1.1_02", 10),
    BETA_1_0("Beta 1.0", 9),
    ALPHA_1_2_6("Alpha 1.2.6", 8),
    ALPHA_1_2_5("Alpha 1.2.5", 7),
    ALPHA_1_2_4("Alpha 1.2.4", 6),
    ALPHA_1_2_3("Alpha 1.2.3", 5),
    ALPHA_1_2_2("Alpha 1.2.2", 4),
    ALPHA_1_2_1("Alpha 1.2.1", 3),
    ALPHA_1_2_0("Alpha 1.2.0", 2),
    ALPHA_1_1_2("Alpha 1.1.2_01", 1),
    ALPHA_1_0_16("Alpha 1.0.16", 0);

    infix fun <T : ProtocolVersion> greaterThan(other: T): Boolean = this.number > other.number
    infix fun <T : ProtocolVersion> lowerThan(other: T): Boolean = this.number < other.number
    infix fun <T : ProtocolVersion> greaterOrEqualThan(other: T): Boolean = this.number >= other.number
    infix fun <T : ProtocolVersion> lowerOrEqualThan(other: T): Boolean = this.number <= other.number

    infix fun greaterThan(other: Int): Boolean = this.number > other
    infix fun lowerThan(other: Int): Boolean = this.number < other
    infix fun greaterOrEqualThan(other: Int): Boolean = this.number >= other
    infix fun lowerOrEqualThan(other: Int): Boolean = this.number <= other
}

fun getByProtocolVersion(protocol: Int): ProtocolVersion = ProtocolVersion.entries.first { it.number == protocol }