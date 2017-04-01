package ru.skafcats.crypto.storage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.util.Log;

import com.securepreferences.SecurePreferences;

import java.io.File;
import java.util.Set;

import ru.skafcats.crypto.models.FileObject;
import ru.skafcats.crypto.models.InfoAboutSecureInfo;
import ru.skafcats.crypto.models.SecureInfo;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class MySecureSharedPreference extends SecurePreferences {
    File prefFile = null;

    public MySecureSharedPreference(Context context, String password, SecureInfo secureInfo) {
        this(context, password, secureInfo.getId());
    }

    public MySecureSharedPreference(Context context, String password, long id) {
        super(context, password, String.valueOf(id));

        prefFile = new File("/data/data/" + context.getPackageName() + "/shared_prefs/" + id + ".xml");
        Log.i("Test", prefFile.getAbsolutePath());
    }

    public MySecureSharedPreference(Context context, String password, InfoAboutSecureInfo infoAboutSecureInfo) {
        this(context, password, infoAboutSecureInfo.getId());
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

            for (String keyField : getStringSet("fields", new ArraySet<String>()))
                if (getString(keyField, null) != null)
                    secureInfo.addField(keyField, getString(keyField, null));
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
            editor.putStringSet("fields", secureInfo.getKeys());
            for (String key : secureInfo.getKeys())
                editor.putString(key, secureInfo.getByKey(key));

            editor.apply();
        }
        return this;
    }

    public File toFile() {
        return prefFile;
    }

    public static File getFile(InfoAboutSecureInfo infoAboutSecureInfo, String packageName) {
        return new File("/data/data/" + packageName + "/shared_prefs/" + infoAboutSecureInfo.getId() + ".xml");

    }
}