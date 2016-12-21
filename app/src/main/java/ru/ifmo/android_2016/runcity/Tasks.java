package ru.ifmo.android_2016.runcity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ru.ifmo.android_2016.runcity.loader.LoadResult;
import ru.ifmo.android_2016.runcity.loader.ResultType;
import ru.ifmo.android_2016.runcity.loader.TasksLoader;
import ru.ifmo.android_2016.runcity.loader.TasksParser;
import ru.ifmo.android_2016.runcity.model.Task;
import ru.ifmo.android_2016.runcity.utils.RecyclerDividersDecorator;
import ru.ifmo.android_2016.runcity.utils.TasksRecyclerAdapter;

public class Tasks extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<LoadResult<ArrayList<Task>>> {

    public static final String TASKS = "id_of_tasks";

    private final String URI = "http://www.pastegraph.esy.es/questions.json";
    ArrayList<Task> tasks = null;

    private RecyclerView recyclerView;
    private TasksRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecyclerDividersDecorator(getResources().getColor(R.color.gray_a)));

        final File file = new File(getFilesDir(), "questions.json");
        if (!file.exists()) {
            new Thread(
                    new Runnable() {
                        public void run() {
                            InputStream in = null;
                            FileOutputStream out = null;
                            try {
                                in = new BufferedInputStream((new URL(URI)).openStream());
                                out = new FileOutputStream(file);
                                int counter;
                                byte[] buffer = new byte[1024];
                                while ((counter = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, counter);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (in != null) {
                                        in.close();
                                    }
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
            ).start();
        }

        final ArrayList<Integer> ids;
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(TASKS)) {
            ids = getIntent().getExtras().getIntegerArrayList(TASKS);
        } else {
            ids = new ArrayList<>();
        }

        try {
            tasks = TasksParser.parseTasks(file, ids);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (tasks == null) {
            displayError(ResultType.ERROR);
        } else {
            displayNonEmptyData(tasks);
        }
    }

    @Override
    public Loader<LoadResult<ArrayList<Task>>> onCreateLoader(int id, Bundle args) {
        final ArrayList<Integer> tasks;
        if (args != null && args.containsKey(TASKS)) {
            tasks = args.getIntegerArrayList(TASKS);
        } else {
            tasks = new ArrayList<>();
        }
        return new TasksLoader(this, tasks);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<ArrayList<Task>>> loader, LoadResult<ArrayList<Task>> result) {
        if (result.resultType == ResultType.OK) {
            if (result.data != null && !result.data.isEmpty()) {
                displayNonEmptyData(result.data);
            } else {
                displayEmptyData();
            }
        } else {
            displayError(result.resultType);
        }
    }

    @Override
    public void onLoaderReset(Loader<LoadResult<ArrayList<Task>>> loader) {
        displayEmptyData();
    }

    private void displayNonEmptyData(ArrayList<Task> tasks) {
        if (adapter == null) {
            adapter = new TasksRecyclerAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.setTasks(tasks);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayEmptyData() {
        //Toast.makeText(getApplicationContext(), "Задания не найдены", Toast.LENGTH_SHORT).show();
    }

    private void displayError(ResultType resultType) {
        final String err;
        if (resultType == ResultType.NO_INTERNET) {
            err = "Отсутствует соединение с интернетом";
        } else {
            err = "Ошибка при загрузке";
        }
        Toast.makeText(getApplicationContext(), err, Toast.LENGTH_SHORT).show();
    }
}
