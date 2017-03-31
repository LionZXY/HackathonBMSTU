package ru.skafcats.hackathon2017.services;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

import ru.skafcats.hackathon2017.interfaces.IExecutor;
import ru.skafcats.hackathon2017.interfaces.ITask;

/**
 * Created by lionzxy on 25.03.17.
 */

public class ThreadTask extends Thread {
    public static final String TAG = "ThreadTask";
    private MultiResultReciever resultReceiver = null;
    private final CopyOnWriteArrayList<ITask> tasks = new CopyOnWriteArrayList<>();
    private volatile boolean isRun = false;

    ThreadTask(MultiResultReciever resultReceiver) {
        this.resultReceiver = resultReceiver;
    }

    @Override
    public void run() {
        super.run();
        isRun = true;
        ITask task = null;

        while (tasks.size() > 0 && !isInterrupted()) {
            synchronized (tasks) {
                task = tasks.remove(tasks.size() - 1);
            }
            final ITask finalTask = task;
            task.runInBackground(new IExecutor() {
                @Override
                public void onProgressNotify(int resultCode, Bundle data) {
                    data.putParcelable("task", finalTask);
                    resultReceiver.send(resultCode, data);
                }
            });
        }
        isRun = false;
        tasks.clear();
    }

    public void addTask(ITask task) {
        synchronized (tasks) {
            if (task != null) {
                Log.d(TAG, "Добавление задачи...");
                if (tasks.contains(task)) {
                    tasks.remove(task);
                    tasks.add(task);
                } else tasks.add(task);
                if (!isRun && !isAlive())
                    start();
            }
        }
    }

    public void removeTask(ITask task) {
        synchronized (tasks) {
            if (task != null) {
                tasks.remove(task);
            }
        }
    }
}
