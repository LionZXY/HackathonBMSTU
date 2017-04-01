package ru.skafcats.crypto.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.skafcats.crypto.enums.Constants;
import ru.skafcats.crypto.models.InfoAboutSecureInfo;

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

    public static boolean isImage(String filename) {
        return filename != null && filename.matches(Constants.REGEXP_IMAGE_NAME);
    }

    public static void download(InfoAboutSecureInfo info, String packageName, String token) throws Exception {
        File output = info.getFile(packageName);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).
                addFormDataPart("token", token).
                addFormDataPart("path", FileHelper.getNameByFile(info.getFile(packageName))).build();
        Request request = new Request.Builder().method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .url(Constants.API_URL + "/storage/get")
                .build();
        Response response = client.newCall(request).execute();

        InputStream in = response.body().byteStream();
        output.getParentFile().mkdirs();
        output.createNewFile();
        FileOutputStream fos = new FileOutputStream(output);
        int result = 0;
        byte[] buffer = new byte[4096];

        while ((result = in.read(buffer)) != -1) {
            fos.write(buffer, 0, result);
        }
        fos.close();
        in.close();
        response.body().close();
        Log.i(TAG, "Download file is " + output.exists() + " " + output.getAbsolutePath());
    }
}
