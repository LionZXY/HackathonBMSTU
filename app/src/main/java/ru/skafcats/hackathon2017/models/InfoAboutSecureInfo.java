package ru.skafcats.hackathon2017.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import ru.skafcats.hackathon2017.enums.InfoType;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class InfoAboutSecureInfo implements Serializable, Parcelable {
    private long id = -1;
    private String name = null;
    private String login = null;
    private InfoType type = InfoType.UNKNOWN;
    private ServiceModel serviceModel = null;

    public InfoAboutSecureInfo(SecureInfo secureInfo) {
        name = secureInfo.getName();
        login = secureInfo.getName();
        id = secureInfo.getId();
        if (secureInfo.getByKey("type") != null)
            type = InfoType.getKeyFromId(Integer.parseInt(secureInfo.getByKey("type")));
        serviceModel = new ServiceModel(secureInfo);
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

    public long getId(){
        return id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public InfoType getType() {
        return type;
    }

    public void setType(InfoType type) {
        this.type = type;
    }

    public ServiceModel getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(ServiceModel serviceModel) {
        this.serviceModel = serviceModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected InfoAboutSecureInfo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        login = in.readString();
        type = InfoType.getKeyFromId(in.readInt());
        Serializable tmp = in.readSerializable();
        if (tmp instanceof ServiceModel)
            serviceModel = (ServiceModel) tmp;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(login);
        dest.writeInt(type.getId());
        dest.writeSerializable(serviceModel);
    }
}
