package ru.skafcats.hackathon2017.enums;

import io.realm.RealmObject;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */


public class RealmEnum extends RealmObject {
    private int enumId;

    public RealmEnum saveEnum(InfoType val) {
        this.enumId = val.getId();
        return this;
    }

    public InfoType getEnum() {
        return InfoType.getKeyFromId(enumId);
    }
}
