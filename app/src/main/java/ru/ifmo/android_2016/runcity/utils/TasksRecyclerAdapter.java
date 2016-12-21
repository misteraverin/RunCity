package ru.ifmo.android_2016.runcity.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.ifmo.android_2016.runcity.DrawingCanvas;
import ru.ifmo.android_2016.runcity.R;
import ru.ifmo.android_2016.runcity.TaskQuestion;
import ru.ifmo.android_2016.runcity.TaskSignature;
import ru.ifmo.android_2016.runcity.model.Task;
import ru.ifmo.android_2016.runcity.model.taskState;

/**
 * Created by -- on 08.11.2016.
 */

public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.TasksViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private ArrayList<Task> tasks = new ArrayList<>();

    public TasksRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setTasks(@NonNull ArrayList<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TasksViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, final int position){
        final Task task = tasks.get(position);
        holder.taskPoint.setText(task.taskPoint.toString());
        holder.taskName.setText(task.TextDisplayed);

        switch (task.state) {
            case ANSWERED: {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.task_answered));
                break;
            }
            case RIGHT: {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.task_right));
                break;
            }
            case WRONG:{
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.task_wrong));
                break;
            }
            default: break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task.state.equals(taskState.OPEN)) {
                    switch (task.type) {
                        case SIGNATURE: {
                            final Intent intent = new Intent(context.getApplicationContext(), DrawingCanvas.class);
                            intent.putExtra(TaskQuestion.TASK_NAME, task.taskName);
                            context.startActivity(intent);
                            break;
                        }
                        case QUESTION: {
                            final Intent intent = new Intent(context.getApplicationContext(), TaskQuestion.class);
                            intent.putExtra(TaskQuestion.TASK_NAME, task.taskName);
                            intent.putExtra(TaskQuestion.TASK_ID, task.taskId);
                            intent.putExtra(TaskQuestion.TASK_ANSWER, task.taskAnswer);
                            context.startActivity(intent);
                            break;
                        }
                    }
                }
                else {
                    Toast.makeText(context.getApplicationContext(), "Ответ уже отправлен", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TasksViewHolder extends RecyclerView.ViewHolder {

        final TextView taskName;
        final TextView taskPoint;

        private TasksViewHolder(View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            taskPoint = (TextView) itemView.findViewById(R.id.task_point);
        }

        static TasksViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_task, parent, false);
            return new TasksViewHolder(view);
        }
    }
}
