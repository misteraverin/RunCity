package ru.ifmo.android_2016.runcity.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by -- on 08.11.2016.
 */

public class Task {
    public final Integer taskId;
    public final String taskName;
    public final Integer taskPoint;
    public final String TextDisplayed;
    public final taskState state;
    public final taskType type;
    public final String taskAnswer;

    public Task(int taskId, String taskName, Integer taskPoint, taskState state, taskType type, String taskAnswer) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskPoint = taskPoint;
        this.state = state;
        this.type = type;
        this.taskAnswer = taskAnswer;
        this.TextDisplayed = taskId + ". " + taskName;
    }
}
