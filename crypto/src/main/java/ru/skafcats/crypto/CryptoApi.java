package ru.skafcats.crypto;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.securepreferences.SecurePreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import ru.skafcats.crypto.enums.Constants;
import ru.skafcats.crypto.helpers.TaskHelper;
import ru.skafcats.crypto.interfaces.ITask;
import ru.skafcats.crypto.interfaces.ITaskAnswerListener;
import ru.skafcats.crypto.interfaces.OnLogin;
import ru.skafcats.crypto.models.InfoAboutSecureInfo;
import ru.skafcats.crypto.models.SecureInfo;
import ru.skafcats.crypto.services.MultiResultReciever;
import ru.skafcats.crypto.storage.MySecureSharedPreference;
import ru.skafcats.crypto.tasks.LoginTask;
import ru.skafcats.crypto.tasks.RegisterTask;
import ru.skafcats.crypto.tasks.SyncTask;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class CryptoApi implements ITaskAnswerListener {
    public static final String TAG = "CryptoApi";
    private static final String PASSWORD_CHECK_STRING = "ckXIjZb06c07hCzAJu9Y";
    private Context context = null;
    private Realm realm = null;
    private String password = null;
    private String packageName = null;

    private CryptoApi(Context context, String password) {
        this.context = context;
        Realm.init(context);
        realm = Realm.getInstance(new RealmConfiguration.Builder().schemaVersion(1).deleteRealmIfMigrationNeeded().name(Constants.BASE_NAME).build());
        this.password = password;
        this.packageName = context.getPackageName();
    }

    public static void setMasterPassword(Context context, String password){
        new MySecureSharedPreference(context, password, 0).edit().putString("auth", PASSWORD_CHECK_STRING).apply();
    }

    public static CryptoApi getInstance(Context context, String password) {
        //new MySecureSharedPreference()
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


    private OnLogin loginListener = null;

    public void register(String email, String password, OnLogin listener) {
        this.loginListener = listener;
        TaskHelper.addListener(context, new RegisterTask(email, password), this);
    }

    public void login(String email, String password, OnLogin listener) {
        this.loginListener = listener;
        TaskHelper.addListener(context, new LoginTask(email, password), this);
    }

    private void saveToken(String token) {
        new SecurePreferences(context).edit().putString("token", token).apply();
    }

    private String getToken() {
        return new SecurePreferences(context).getString("token", null);
    }

    public boolean isLogin() {
        return getToken() != null;
    }

    public void updateElement(SecureInfo secureInfo) {
        new MySecureSharedPreference(context, password, secureInfo).setSecureInfo(secureInfo);
        realm.beginTransaction();

        RealmResults<InfoAboutSecureInfo> results = realm.where(InfoAboutSecureInfo.class).equalTo("id", secureInfo.getId()).findAll();
        if (results != null && results.size() != 0) {
            results.get(0).setInfo(new InfoAboutSecureInfo(secureInfo));
        } else {
            InfoAboutSecureInfo infoAboutSecureInfo = realm.createObject(InfoAboutSecureInfo.class, secureInfo.getId());
            infoAboutSecureInfo.setSecureInfo(secureInfo);
        }

        realm.commitTransaction();
    }

    public void sync() {
        ArrayList<InfoAboutSecureInfo> infos = new ArrayList<>();
        for (InfoAboutSecureInfo info : getInfo())
            infos.add(info);
        TaskHelper.addListener(context, new SyncTask(infos, context.getPackageName(), getToken()), this);
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
                        if (infoAboutSecureInfos != null) {
                            realm.beginTransaction();

                            for (InfoAboutSecureInfo info : infoAboutSecureInfos) {
                                results = realm.where(InfoAboutSecureInfo.class).equalTo("id", info.getId()).findAll();
                                if (results != null && results.size() != 0) {
                                    results.get(0).setInfo(info);
                                } else {
                                    SecureInfo secureInfo = new MySecureSharedPreference(context, password, info).getSecureInfo();
                                    InfoAboutSecureInfo infoAboutSecureInfo = realm.createObject(InfoAboutSecureInfo.class, info.getId());
                                    infoAboutSecureInfo.setSecureInfo(secureInfo);
                                }
                            }

                            realm.commitTransaction();
                        }
                    }
                    break;
                }
                case Constants.ID_REGISTER: {
                    if (data.getInt(Constants.KEY_RESULT_CODE, MultiResultReciever.CODE_RESULT_ERROR_TASK) == MultiResultReciever.CODE_RESULT_FINISH_TASK) {
                        String answer = data.getString(Constants.KEY_RESPONSE);
                        try {
                            JSONObject jsonObject = new JSONObject(answer);
                            if (jsonObject.has("error")) {
                                if (loginListener != null)
                                    loginListener.onResponse(false, jsonObject.getString("error"));
                            } else if (jsonObject.has("email")) {
                                if (loginListener != null)
                                    loginListener.onResponse(false, jsonObject.getJSONArray("email").getString(0));
                            } else if (jsonObject.has("token")) {
                                saveToken(jsonObject.getString("token"));
                                if (loginListener != null)
                                    loginListener.onResponse(true, null);
                            } else {
                                if (loginListener != null)
                                    loginListener.onResponse(false, jsonObject.toString());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onRegister", e);

                            if (loginListener != null)
                                loginListener.onResponse(false, "Ошибка в программе");
                        }
                    }
                    break;
                }
                case Constants.ID_LOGIN: {
                    if (data.getInt(Constants.KEY_RESULT_CODE, MultiResultReciever.CODE_RESULT_ERROR_TASK) == MultiResultReciever.CODE_RESULT_FINISH_TASK) {
                        String answer = data.getString(Constants.KEY_RESPONSE);
                        Log.i(TAG, answer);
                        try {
                            JSONObject jsonObject = new JSONObject(answer);
                            if (jsonObject.has("error")) {
                                if (loginListener != null)
                                    loginListener.onResponse(false, jsonObject.getString("error"));
                            } else if (jsonObject.has("email")) {
                                if (loginListener != null)
                                    loginListener.onResponse(false, jsonObject.getJSONArray("email").getString(0));
                            } else if (jsonObject.has("token")) {
                                saveToken(jsonObject.getString("token"));
                                if (loginListener != null)
                                    loginListener.onResponse(true, null);
                            } else {
                                if (loginListener != null)
                                    loginListener.onResponse(false, jsonObject.toString());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onLogin", e);

                            if (loginListener != null)
                                loginListener.onResponse(false, "Ошибка в программе");
                        }
                    }
                    break;
                }
            }
        }
    }

    public boolean checkPassword() {
        return new MySecureSharedPreference(context, password, 0).getString("auth", "").equals(PASSWORD_CHECK_STRING);
    }
}
