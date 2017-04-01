package ru.skafcats.crypto.tasks;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.skafcats.crypto.enums.Constants;
import ru.skafcats.crypto.enums.TaskType;
import ru.skafcats.crypto.helpers.FileHelper;
import ru.skafcats.crypto.interfaces.IExecutor;
import ru.skafcats.crypto.interfaces.ITask;
import ru.skafcats.crypto.models.InfoAboutSecureInfo;
import ru.skafcats.crypto.services.MultiResultReciever;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class SyncTask extends ITask {
    public static final String TAG = "SyncTask";
    private String packageName = null;
    private ArrayList<InfoAboutSecureInfo> localInfo = new ArrayList<>();
    private String token = null;

    public SyncTask(ArrayList<InfoAboutSecureInfo> info, String packageName, String token) {
        super(TaskType.NETWORK_TASK, Constants.ID_TASK_SYNC);
        this.packageName = packageName;
        for (InfoAboutSecureInfo infoOne : info)
            localInfo.add(infoOne.copy());
        this.token = token;
    }

    private SyncTask(Parcel in) {
        super(TaskType.NETWORK_TASK, Constants.ID_TASK_SYNC);
        this.packageName = in.readString();
        this.token = in.readString();
        in.readTypedList(localInfo, InfoAboutSecureInfo.CREATOR);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SyncTask;
    }

    @Override
    public void runInBackground(IExecutor executor) {
        ArrayList<InfoAboutSecureInfo> changeToDB = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Bundle bundle = new Bundle();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).
                addFormDataPart("token", token).build();
        Request request = new Request.Builder().url(Constants.API_URL + "/storage/index/").method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody).build();

        ArrayList<InfoAboutSecureInfo> toLocal = new ArrayList<>();
        ArrayList<InfoAboutSecureInfo> toServer = new ArrayList<>();

        HashMap<Long, InfoAboutSecureInfo> localHash = new HashMap<>();
        HashMap<Long, InfoAboutSecureInfo> serverHash = new HashMap<>();

        for (InfoAboutSecureInfo info : localInfo)
            localHash.put(info.getId(), info);

        try {
            Response response = client.newCall(request).execute();
            JSONArray object = new JSONArray(response.body().string());

            Log.i(TAG, object.toString());
            for (int i = 0; i < object.length(); i++) {
                JSONObject item = object.getJSONObject(i);
                long id = Long.valueOf(item.getString("name").substring(0, item.getString("name").lastIndexOf('.')));
                long updated_at = item.getLong("updated_at") * 1000;
                InfoAboutSecureInfo info = new InfoAboutSecureInfo(id);
                info.setLastEdit(updated_at);
                serverHash.put(info.getId(), info);
            }

            for (Long id : serverHash.keySet()) {
                if (localHash.containsKey(id)) {
                    InfoAboutSecureInfo local = localHash.get(id);
                    InfoAboutSecureInfo remote = serverHash.get(id);
                    if (local.getLastEdit() < remote.getLastEdit()) {
                        toLocal.add(remote);
                    } else if (local.getLastEdit() > remote.getLastEdit()) {
                        toServer.add(local);
                    }
                } else toLocal.add(serverHash.get(id));
            }

            for (Long id : localHash.keySet())
                if (!serverHash.containsKey(id))
                    toServer.add(localHash.get(id));

            for (InfoAboutSecureInfo info : toServer) {
                try {
                    MediaType mediaType = MediaType.parse("form-data");
                    requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).
                            addFormDataPart("token", token).
                            addFormDataPart("path", FileHelper.getNameByFile(info.getFile(packageName))).
                            addFormDataPart("data", FileHelper.getNameByFile(info.getFile(packageName)), RequestBody.create(mediaType, info.getFile(packageName))).build();
                    request = new Request.Builder().url(Constants.API_URL + "/storage/set").method("POST", RequestBody.create(null, new byte[0]))
                            .post(requestBody).build();
                    response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    info.setLastEdit(jsonObject.getLong("updated_at"));
                    changeToDB.add(info);
                } catch (Exception e) {
                    Log.e(TAG, "error", e);
                }
            }

            for (InfoAboutSecureInfo info : toLocal) {
                changeToDB.add(info);
                try {
                    FileHelper.download(info, packageName, token);
                } catch (Exception e) {
                    Log.e(TAG, "error", e);
                }
            }
            Log.i(TAG, toLocal.size() + " " + toServer.size());
            bundle.putParcelableArrayList(Constants.KEY_RESPONSE, changeToDB);
            executor.onProgressNotify(MultiResultReciever.CODE_RESULT_FINISH_TASK, bundle);
        } catch (Exception e) {
            Log.e(TAG, "error", e);
            executor.onProgressNotify(MultiResultReciever.CODE_RESULT_ERROR_TASK, null);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(token);
        dest.writeTypedList(localInfo);
    }

    public static final Creator<ITask> CREATOR = new Creator<ITask>() {
        @Override
        public ITask createFromParcel(Parcel in) {
            return new SyncTask(in);
        }

        @Override
        public ITask[] newArray(int size) {
            return new SyncTask[size];
        }
    };
}
