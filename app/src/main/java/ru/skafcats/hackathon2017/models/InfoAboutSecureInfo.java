package ru.skafcats.hackathon2017.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ru.skafcats.hackathon2017.enums.InfoType;
import ru.skafcats.hackathon2017.enums.RealmEnum;
import ru.skafcats.hackathon2017.storage.MySecureSharedPreference;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class InfoAboutSecureInfo extends RealmObject implements Serializable, Parcelable {
    @PrimaryKey
    private long id = -1;
    private long lastEdit = -1;
    private String name = null;
    private String login = null;
    private String link = null;

    public InfoAboutSecureInfo(SecureInfo secureInfo) {
        this(secureInfo.getId());
        setName(secureInfo.getName());
        setLogin(secureInfo.getName());
        setLastEdit(getId());
        setLink(secureInfo.getByKey("link"));
    }

    public InfoAboutSecureInfo(int id) {
        this.id = id;
    }

    public InfoAboutSecureInfo() {
    }

    public InfoAboutSecureInfo setSecureInfo(SecureInfo secureInfo) {
        setName(secureInfo.getName());
        setLogin(secureInfo.getByKey("login"));
        setLastEdit(getId());
        setLink(secureInfo.getByKey("link"));
        return this;
    }

    public InfoAboutSecureInfo setInfo(InfoAboutSecureInfo info) {
        setName(info.getName());
        setLogin(info.getLogin());
        setLastEdit(info.getLastEdit());
        setLink(info.getLink());
        return this;
    }

    public InfoAboutSecureInfo(long id) {
        this.id = id;
    }


    public static final Creator<InfoAboutSecureInfo> CREATOR = new Creator<InfoAboutSecureInfo>() {
        @Override
        public InfoAboutSecureInfo createFromParcel(Parcel in) {
            return new InfoAboutSecureInfo(in);
        }

        @Override
        public InfoAboutSecureInfo[] newArray(int size) {
            return new InfoAboutSecureInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public long getId() {
        return id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected InfoAboutSecureInfo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        login = in.readString();
        Serializable tmp = in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(login);
    }

    public long getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(long lastEdit) {
        this.lastEdit = lastEdit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public InfoAboutSecureInfo copy() {
        InfoAboutSecureInfo toReturn = new InfoAboutSecureInfo(getId());
        return toReturn.setInfo(this);
    }

    public File getFile(String packageName) {
        return MySecureSharedPreference.getFile(this, packageName);
    }
}
