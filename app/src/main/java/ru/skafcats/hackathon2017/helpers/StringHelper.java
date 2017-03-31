package ru.skafcats.hackathon2017.helpers;

import java.util.Arrays;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class StringHelper {
    public static String getStringWithSize(String src, int size) {
        String toExit = src;
        if (src != null) {
            char charToReplace = 0;
            if (toExit.length() > size)
                toExit = toExit.substring(0, size);
            else {
                while (toExit.indexOf(charToReplace) != -1 && charToReplace < Character.MAX_VALUE) {
                    charToReplace++;
                }

                int min = Integer.MAX_VALUE;
                int tmpVar = 0;
                if (charToReplace == Character.MAX_VALUE) {
                    for (char tmpChar = 0; tmpChar < Character.MAX_VALUE; tmpChar++) {
                        tmpVar = toExit.length() - toExit.replace(".", "").length();
                        if (tmpVar < min) {
                            min = tmpVar;
                            charToReplace = tmpChar;
                        }
                    }
                    toExit = toExit.replaceAll(String.valueOf(charToReplace), "");
                }
                char[] array = new char[size - toExit.length() - 1];
                Arrays.fill(array, charToReplace);
                toExit = charToReplace + toExit + String.valueOf(array);
            }
        }
        return toExit;
    }

    public static String getStringFromBigString(String src) {
        String toExit = src;
        if (toExit != null) {
            char charToReplace = toExit.charAt(0);
            toExit = toExit.substring(1);
            if (toExit.indexOf(charToReplace) == -1)
                toExit = src;
            else {
                toExit = toExit.substring(0, toExit.indexOf(charToReplace));
            }
        }
        return toExit;
    }
}
