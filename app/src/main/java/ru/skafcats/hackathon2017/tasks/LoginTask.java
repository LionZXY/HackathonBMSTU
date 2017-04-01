package ru.skafcats.hackathon2017.tasks;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.skafcats.hackathon2017.enums.Constants;
import ru.skafcats.hackathon2017.enums.TaskType;
import ru.skafcats.hackathon2017.interfaces.IExecutor;
import ru.skafcats.hackathon2017.interfaces.ITask;
import ru.skafcats.hackathon2017.services.MultiResultReciever;

/**
 * Created by Nikita Kulikov on 01.04.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class LoginTask extends ITask {
    public static final String TAG = "RegisterTask";
    private String email;
    private String password;

    public LoginTask(String email, String password) {
        super(TaskType.NETWORK_TASK, Constants.ID_LOGIN);
        this.email = email;
        this.password = password;
    }

    public LoginTask(Parcel in) {
        super(TaskType.NETWORK_TASK, Constants.ID_LOGIN);
        this.email = in.readString();
        this.password = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LoginTask;
    }

    @Override
    public void runInBackground(IExecutor executor) {
        OkHttpClient client = new OkHttpClient();
        Bundle bundle = new Bundle();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", email).addFormDataPart("password", password).build();

        Request request = new Request.Builder().url(Constants.API_URL + "/auth/login").method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            bundle.putString(Constants.KEY_RESPONSE, response.body().string());
            executor.onProgressNotify(MultiResultReciever.CODE_RESULT_FINISH_TASK, bundle);
        } catch (Exception e) {
            Log.e(TAG, "Error while send request", e);
            executor.onProgressNotify(MultiResultReciever.CODE_RESULT_ERROR_TASK, bundle);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
    }

    public static final Parcelable.Creator<ITask> CREATOR = new Parcelable.Creator<ITask>() {
        @Override
        public ITask createFromParcel(Parcel in) {
            return new LoginTask(in);
        }

        @Override
        public ITask[] newArray(int size) {
            return new LoginTask[size];
        }
    };
}
