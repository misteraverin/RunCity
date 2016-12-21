package ru.ifmo.android_2016.runcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

/**
 * Created by -- on 09.11.2016.
 */

public class TaskQuestion extends AppCompatActivity implements View.OnClickListener {

    public static final String TASK_NAME = "task_name";
    public static final String TASK_ID = "task_id";
    public static final String TASK_ANSWER = "task_answer";

    private Button buttonSubmit;
    private Button buttonSave;
    private TextView taskDescription;
    private EditText taskAnswer;
    private String ans = "";
    private Integer id = -1;
    private String rightAns = "";

    private final String URI = "http://www.pastegraph.esy.es/questions.json";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        buttonSubmit = (Button) findViewById(R.id.submit_task_button);
        buttonSave = (Button) findViewById(R.id.save_task_button);
        taskDescription = (TextView) findViewById(R.id.task_descript);
        taskAnswer = (EditText) findViewById(R.id.task_answer);

        taskDescription.setText(getIntent().getExtras().getString(TASK_NAME, "No task got"));

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(TASK_ID)) {
            id = getIntent().getExtras().getInt(TASK_ID);
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(TASK_ANSWER)) {
            rightAns = getIntent().getExtras().getString(TASK_ANSWER);
        }

        Scanner in = null;
        try {
            in = new Scanner(new File(getFilesDir(), "question" + id + ".json"));
            ans = in.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
               in.close();
            }
        }

        taskAnswer.setText(ans);

        buttonSubmit.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Writer writer = null;
        try {
            writer = new FileWriter(new File(getFilesDir(), "question" + id + ".json"));
            writer.write(taskAnswer.getText().toString());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (v == buttonSubmit) {
            if (rightAns.equals(taskAnswer.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Правильный ответ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Неправильный ответ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Ответ сохранен", Toast.LENGTH_SHORT).show();
        }
    }
}
