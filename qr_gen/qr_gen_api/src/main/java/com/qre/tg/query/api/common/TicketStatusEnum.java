package com.qre.tg.query.api.common;

public enum TicketStatusEnum {

    ACTIVE(1),
    ENTRY(2),
    EXIT(3),
    ENTRY_UPGRADE(4),
    EXIT_UPGRADE(5),
    CANCELED(6),
    REFUND_IN_PROGRESS(7),
    REFUND_APPROVED(8),
    REFUND_REJECT(9),
    INACTIVE(0);

    private final int value;

    TicketStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Optional: You can also add a method to get the enum value from the integer value
    public static TicketStatusEnum fromValue(int value) {
        for (TicketStatusEnum type : TicketStatusEnum.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid TicketStatusEnum value: " + value);
    }
}
