package ru.skafcats.crypto.tasks;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.skafcats.crypto.enums.Constants;
import ru.skafcats.crypto.enums.TaskType;
import ru.skafcats.crypto.interfaces.IExecutor;
import ru.skafcats.crypto.interfaces.ITask;
import ru.skafcats.crypto.services.MultiResultReciever;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class RegisterTask extends ITask {
    public static final String TAG = "RegisterTask";
    private String email;
    private String password;

    public RegisterTask(String email, String password) {
        super(TaskType.NETWORK_TASK, Constants.ID_REGISTER);
        this.email = email;
        this.password = password;
    }

    public RegisterTask(Parcel in) {
        super(TaskType.NETWORK_TASK, Constants.ID_REGISTER);
        this.email = in.readString();
        this.password = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RegisterTask;
    }

    @Override
    public void runInBackground(IExecutor executor) {
        OkHttpClient client = new OkHttpClient();
        Bundle bundle = new Bundle();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", email).addFormDataPart("password", password).build();

        Request request = new Request.Builder().url(Constants.API_URL + "/auth/signup/").method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            //Log.i("Test", response.message());
            bundle.putString(Constants.KEY_RESPONSE, resp);
            executor.onProgressNotify(MultiResultReciever.CODE_RESULT_FINISH_TASK, bundle);
        } catch (IOException e) {
            Log.e(TAG, "Error while send request", e);
            executor.onProgressNotify(MultiResultReciever.CODE_RESULT_ERROR_TASK, bundle);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
    }

    public static final Creator<ITask> CREATOR = new Creator<ITask>() {
        @Override
        public ITask createFromParcel(Parcel in) {
            return new RegisterTask(in);
        }

        @Override
        public ITask[] newArray(int size) {
            return new RegisterTask[size];
        }
    };
}
