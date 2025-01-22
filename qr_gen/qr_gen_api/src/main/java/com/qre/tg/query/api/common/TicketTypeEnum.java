package com.qre.tg.query.api.common;

public enum TicketTypeEnum {


    ADULT(1),
    CHILD(2),
    SENIOR(3);

    private final int value;

    TicketTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Optional: You can also add a method to get the enum value from the integer value
    public static TicketTypeEnum fromValue(int value) {
        for (TicketTypeEnum type : TicketTypeEnum.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TicketTypeEnum value: " + value);
    }
}
