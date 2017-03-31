package ru.skafcats.hackathon2017.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ru.skafcats.hackathon2017.enums.Constants;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class SecureInfo implements Parcelable {
    public static final String TAG = "SecureInfo";
    private String name = null;
    private long id = System.currentTimeMillis();
    private ArrayList<FileObject> files = new ArrayList<>();
    private HashMap<String, String> stringFields = new HashMap<>();

    public SecureInfo(String name) {
        this.name = name;
        id = System.currentTimeMillis() ^ name.hashCode();
    }

    public SecureInfo(String name, long id) {
        this.name = name;
        this.id = id;
    }

    private SecureInfo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        Serializable arrayList = in.readSerializable();
        if (arrayList != null && arrayList instanceof ArrayList)
            files = (ArrayList<FileObject>) arrayList;
        Serializable hashMap = in.readSerializable();
        if (hashMap != null && hashMap instanceof HashMap)
            stringFields = (HashMap<String, String>) hashMap;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void addField(String key, String value) {
        stringFields.put(key, value);
    }

    public String getByKey(String key) {
        return stringFields.get(key);
    }

    public Set<String> getKeys() {
        return stringFields.keySet();
    }

    public static final Creator<SecureInfo> CREATOR = new Creator<SecureInfo>() {
        @Override
        public SecureInfo createFromParcel(Parcel in) {
            return new SecureInfo(in);
        }

        @Override
        public SecureInfo[] newArray(int size) {
            return new SecureInfo[size];
        }
    };

    public boolean addFile(FileObject file) {
        try {
            if (file != null) {
                files.add(file);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error when attachment added", e);
        }
        return false;
    }

    public boolean addFile(File file) {
        try {
            if (file != null && file.exists() && file.isFile() && file.length() < Constants.MAX_BYTES) {
                files.add(new FileObject(file));
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error when attachment added", e);
        }
        return false;
    }

    public ArrayList<FileObject> getFiles() {
        return files;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeSerializable(files);
        dest.writeSerializable(stringFields);
    }
}
