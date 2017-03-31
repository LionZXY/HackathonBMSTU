package ru.skafcats.hackathon2017.interfaces;

import android.os.Parcelable;

import ru.skafcats.hackathon2017.enums.TaskType;

/**
 * Created by lionzxy on 25.03.17.
 */

public abstract class ITask implements Parcelable {
    private TaskType taskType = null;
    private int id = -1;

    public ITask(TaskType taskType, int id) {
        this.taskType = taskType;
        this.id = id;
    }


    public TaskType getTaskType() {
        return this.taskType;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }


    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public abstract boolean equals(Object obj);

    public abstract void runInBackground(IExecutor executor);

}

