package ru.skafcats.hackathon2017.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

import ru.skafcats.hackathon2017.enums.Constants;
import ru.skafcats.hackathon2017.helpers.FileHelper;
import ru.skafcats.hackathon2017.helpers.StringHelper;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class FileObject implements Serializable, Parcelable {
    private File file = null;
    private String name = null;
    private String data = null;

    public FileObject(String name) {
        this.name = name;
    }

    public FileObject(File file) {
        this.name = FileHelper.getNameByFile(file);
        this.file = file;
    }

    private FileObject(Parcel in) {
        name = in.readString();
        Serializable tmp = in.readSerializable();
        if (tmp != null && tmp instanceof File)
            file = (File) tmp;
        data = in.readString();
    }

    public static final Creator<FileObject> CREATOR = new Creator<FileObject>() {
        @Override
        public FileObject createFromParcel(Parcel in) {
            return new FileObject(in);
        }

        @Override
        public FileObject[] newArray(int size) {
            return new FileObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeSerializable(file);
        dest.writeString(data);
    }


    public String toString() {
        String toReturn = StringHelper.getStringWithSize(name, Constants.MAX_SIZE);

        if (this.file != null)
            toReturn += FileHelper.encodeToBase64(this.file);
        else if (this.data != null)
            toReturn += this.data;

        return toReturn;
    }

    /**
     * @var data first 128 is name
     **/
    public static FileObject getFileObject(String data) {
        FileObject fileObject = null;
        if (data != null) {
            String name = data.substring(0, Constants.MAX_SIZE);
            data = data.substring(Constants.MAX_SIZE);
            name = StringHelper.getStringFromBigString(name);

            fileObject = new FileObject(name);
            fileObject.data = data;
        }
        return fileObject;
    }
}
