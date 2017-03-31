package ru.skafcats.hackathon2017;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import ru.skafcats.hackathon2017.enums.Constants;
import ru.skafcats.hackathon2017.interfaces.ITask;
import ru.skafcats.hackathon2017.interfaces.ITaskAnswerListener;
import ru.skafcats.hackathon2017.models.InfoAboutSecureInfo;
import ru.skafcats.hackathon2017.models.SecureInfo;
import ru.skafcats.hackathon2017.services.MultiResultReciever;
import ru.skafcats.hackathon2017.storage.MySecureSharedPreference;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class CryptoApi implements ITaskAnswerListener {
    private Context context = null;
    private Realm realm = null;
    private String password = null;
    private String packageName = null;

    private CryptoApi(Context context, String password) {
        this.context = context;
        Realm.init(context);
        realm = Realm.getInstance(new RealmConfiguration.Builder().schemaVersion(0).name(Constants.BASE_NAME).build());
        this.password = password;
        this.packageName = context.getPackageName();
    }

    public static CryptoApi getInstance(Context context, String password) {
        return new CryptoApi(context, password);
    }

    public void addInfo(SecureInfo info) {
        MySecureSharedPreference secureSharedPreference = new MySecureSharedPreference(context, password, info);
        secureSharedPreference.setSecureInfo(info);
        realm.beginTransaction();
        InfoAboutSecureInfo infoAboutSecureInfo = realm.createObject(InfoAboutSecureInfo.class, info.getId());
        infoAboutSecureInfo.setSecureInfo(info);
        realm.commitTransaction();
    }

    public RealmResults<InfoAboutSecureInfo> getInfo() {
        return realm.where(InfoAboutSecureInfo.class).findAll();
    }

    public SecureInfo getSecureInfo(InfoAboutSecureInfo info) {
        return new MySecureSharedPreference(context, password, info).getSecureInfo();
    }

    public void onDestroy() {
        realm.close();
    }

    @Override
    public void onAnswer(Bundle data) {
        if (data.getParcelable(Constants.KEY_TASK) != null) {
            ITask task = data.getParcelable(Constants.KEY_TASK);
            switch (task.hashCode()) {
                case Constants.ID_TASK_SYNC: {
                    if (data.getInt(Constants.KEY_RESULT_CODE, MultiResultReciever.CODE_RESULT_ERROR_TASK) == MultiResultReciever.CODE_RESULT_FINISH_TASK) {
                        ArrayList<InfoAboutSecureInfo> infoAboutSecureInfos = data.getParcelableArrayList(Constants.KEY_RESPONSE);
                        RealmResults<InfoAboutSecureInfo> results = null;
                        if (infoAboutSecureInfos == null) {
                            realm.beginTransaction();

                            for (InfoAboutSecureInfo info : infoAboutSecureInfos) {
                                results = realm.where(InfoAboutSecureInfo.class).equalTo("id", info.getId()).findAll();
                                if (results != null && results.size() != 0) {
                                    results.get(0).setInfo(info);
                                } else {
                                    InfoAboutSecureInfo infoAboutSecureInfo = realm.createObject(InfoAboutSecureInfo.class, info.getId());
                                    infoAboutSecureInfo.setInfo(info);
                                }
                            }

                            realm.commitTransaction();
                        }
                    }
                }
            }
        }
    }
}
