package ru.skafcats.hackathon2017.enums;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public enum InfoType {
    UNKNOWN(0);
    int id = -1;

    private InfoType(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public static InfoType getKeyFromId(int id) {
        for (InfoType type : InfoType.values())
            if (type.id == id)
                return type;
        return UNKNOWN;
    }
}
