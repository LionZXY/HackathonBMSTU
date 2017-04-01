package ru.skafcats.hackathon2017.interfaces;

/**
 * Created by Nikita Kulikov on 01.04.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public interface OnLogin {
    public void onResponse(boolean isAuth, String message);
}
