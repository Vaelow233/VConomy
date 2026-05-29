package org.vaelow233.vconomy.storage.action;

public enum SqlActionType {
    OP_ADD(0),
    OP_SUBTRACT(1),
    OP_SET(2),
    OP_RESET(3),
    OP_RESET_ALL(4);

    private final byte id;

    SqlActionType(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}
