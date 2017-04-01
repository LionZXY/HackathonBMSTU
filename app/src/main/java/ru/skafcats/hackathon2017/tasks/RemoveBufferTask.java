package ru.skafcats.hackathon2017.tasks;

import android.os.Bundle;
import android.os.Parcel;

import ru.skafcats.crypto.enums.TaskType;
import ru.skafcats.crypto.interfaces.IExecutor;
import ru.skafcats.crypto.interfaces.ITask;
import ru.skafcats.crypto.services.MultiResultReciever;

/**
 * Created by Nikita Kulikov on 01.04.17.
 * <p>
 * Возможно полное или частичное копирование
 */

public class RemoveBufferTask extends ITask {
    long time = 0;

    private RemoveBufferTask(Parcel in) {
        super(TaskType.PROCCESS_TASK, 16);
        time = in.readLong();
    }

    public RemoveBufferTask(long time) {
        super(TaskType.PROCCESS_TASK, 16);
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RemoveBufferTask;
    }

    @Override
    public void runInBackground(IExecutor executor) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.onProgressNotify(MultiResultReciever.CODE_RESULT_FINISH_TASK, new Bundle());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
    }

    public static final Creator<ITask> CREATOR = new Creator<ITask>() {
        @Override
        public ITask createFromParcel(Parcel in) {
            return new RemoveBufferTask(in);
        }

        @Override
        public ITask[] newArray(int size) {
            return new RemoveBufferTask[size];
        }
    };
}
