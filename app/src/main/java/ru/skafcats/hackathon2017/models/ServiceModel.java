package ru.skafcats.hackathon2017.models;

import java.io.Serializable;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class ServiceModel implements Serializable {
    public String link = null;

    public ServiceModel(SecureInfo secureInfo){
        link = secureInfo.getByKey("link");
    }

    public ServiceModel(String link){
        this.link = link;
    }
}
