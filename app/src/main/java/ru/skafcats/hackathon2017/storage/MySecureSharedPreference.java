package ru.skafcats.hackathon2017.storage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;

import com.securepreferences.SecurePreferences;

import java.io.File;
import java.util.Map;
import java.util.Set;

import ru.skafcats.hackathon2017.models.FileObject;
import ru.skafcats.hackathon2017.models.SecureInfo;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class MySecureSharedPreference extends SecurePreferences {
    File prefFile = null;

    public MySecureSharedPreference(Context context, String password, SecureInfo secureInfo) {
        super(context, password, String.valueOf(secureInfo.getId()));
        //prefFile = context.getSharedPreferencesPath(String.valueOf(secureInfo.getId()));
    }

    @Nullable
    public SecureInfo getSecureInfo() {
        SecureInfo secureInfo = null;
        long key = getLong("key", -1);
        String name = getString("name", null);
        if (key != -1 && name != null) {
            secureInfo = new SecureInfo(name, key);
            Set<String> files = getStringSet("files", null);
            if (files != null) {
                for (String file : files)
                    secureInfo.addFile(FileObject.getFileObject(file));
            }
            Map<String, String> map = getAll();
            map.remove("name");
            map.remove("key");
            map.remove("files");

            for (String keyField : map.keySet())
                secureInfo.addField(keyField, map.get(keyField));
        }
        return secureInfo;
    }

    public MySecureSharedPreference setSecureInfo(SecureInfo secureInfo) {
        if (secureInfo != null) {
            Editor editor = edit();

            editor.putLong("key", secureInfo.getId());
            editor.putString("name", secureInfo.getName());
            Set<String> files = new ArraySet<>();
            for (FileObject file : secureInfo.getFiles())
                files.add(file.toString());
            editor.putStringSet("files", files);
            for (String key : secureInfo.getKeys())
                editor.putString(key, secureInfo.getByKey(key));

            editor.apply();
        }
        return this;
    }

    public File toFile() {
        return prefFile;
    }
}
