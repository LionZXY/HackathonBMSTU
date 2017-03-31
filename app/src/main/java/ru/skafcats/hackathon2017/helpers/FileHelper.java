package ru.skafcats.hackathon2017.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import ru.skafcats.hackathon2017.enums.Constants;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class FileHelper {
    public static final String TAG = "FileHelper";

    public static String encodeToBase64(File file) {
        String toExit = null;
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream baos = null;

        if (file == null || !file.exists())
            return null;

        if (isImage(file)) {
            toExit = encodeToBase64(BitmapFactory.decodeFile(file.getAbsolutePath()));
        } else
            try {
                fileInputStream = new FileInputStream(file);
                baos = new ByteArrayOutputStream();
                byte buffer[] = new byte[4096];
                int read = 0;
                while ((read = fileInputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                buffer = baos.toByteArray();
                toExit = Base64.encodeToString(buffer, Base64.DEFAULT);
            } catch (Exception e) {
                Log.e(TAG, "encodeToBase64(File)", e);
            } finally {
                try {
                    if (fileInputStream != null)
                        fileInputStream.close();
                } catch (Exception e) {
                    Log.e(TAG, "encodeToBase64(File)", e);
                }
                try {
                    if (baos != null)
                        baos.close();
                } catch (Exception e) {
                    Log.e(TAG, "encodeToBase64(File)", e);
                }
            }
        return toExit;
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static String getNameByFile(File file) {
        String toExit = file.getAbsolutePath();
        int slashIndex = toExit.lastIndexOf("/") + 1;
        String filename = toExit.substring(slashIndex, toExit.indexOf(".", slashIndex));
        String extension = toExit.substring(toExit.lastIndexOf("."));
        if (filename.length() + extension.length() >= Constants.MAX_SIZE)
            filename = filename.substring(0, Constants.MAX_SIZE - extension.length());
        toExit = filename + extension;
        return toExit;
    }

    public static boolean isImage(File file) {
        return getNameByFile(file).matches(Constants.REGEXP_IMAGE_NAME);
    }
}
