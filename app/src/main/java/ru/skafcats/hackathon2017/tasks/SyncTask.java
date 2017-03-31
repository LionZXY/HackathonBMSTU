package ru.skafcats.hackathon2017.tasks;

import android.os.Bundle;
import android.os.Parcel;

import java.util.ArrayList;

import ru.skafcats.hackathon2017.enums.Constants;
import ru.skafcats.hackathon2017.enums.TaskType;
import ru.skafcats.hackathon2017.interfaces.IExecutor;
import ru.skafcats.hackathon2017.interfaces.ITask;
import ru.skafcats.hackathon2017.models.InfoAboutSecureInfo;

/**
 * Created by Nikita Kulikov on 31.03.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class SyncTask extends ITask {
    private String packageName = null;
    private ArrayList<InfoAboutSecureInfo> localInfo = new ArrayList<>();
    private String token = null;

    public SyncTask(ArrayList<InfoAboutSecureInfo> info, String packageName, String token) {
        super(TaskType.NETWORK_TASK, Constants.ID_TASK_SYNC);
        this.packageName = packageName;
        this.localInfo = info;
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
        Bundle bundle = new Bundle();
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
