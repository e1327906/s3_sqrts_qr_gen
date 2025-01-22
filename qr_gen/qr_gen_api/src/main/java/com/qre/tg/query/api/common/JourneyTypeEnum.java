package com.qre.tg.query.api.common;

public enum JourneyTypeEnum {


    SINGLE(1),
    RETURN_TICKET(2),
    GROUP(3);

    private final int value;

    JourneyTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Optional: You can also add a method to get the enum value from the integer value
    public static JourneyTypeEnum fromValue(int value) {
        for (JourneyTypeEnum type : JourneyTypeEnum.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid JourneyTypeEnum value: " + value);
    }
}
