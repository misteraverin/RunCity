package ru.ifmo.android_2016.runcity.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;

import ru.ifmo.android_2016.runcity.api.RuncityApi;
import ru.ifmo.android_2016.runcity.model.Task;
import ru.ifmo.android_2016.runcity.utils.IOUtils;

/**
 * Created by -- on 17.12.2016.
 */

public class TasksLoader  extends AsyncTaskLoader<LoadResult<ArrayList<Task>>> {
    private final ArrayList<Integer> tasks;

    public TasksLoader(Context context, ArrayList<Integer> tasks) {
        super(context);
        this.tasks = tasks;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<ArrayList<Task>> loadInBackground() {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        ArrayList<Task> data = new ArrayList<>();

        for (int i = 0; i != tasks.size(); i++) {
            HttpURLConnection connection = null;
            InputStream in = null;
            Task task = null;

            try {
                connection = RuncityApi.createTasksRequest(tasks.get(i));
                Log.d(TAG, "Performing request: " + connection.getURL());

                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    in = connection.getInputStream();
                    in = stethoManager.interpretResponseStream(in);

                    task = TasksParser.parseTasks(in);

                    resultType = ResultType.OK;

                } else {
                    // consider all other codes as errors
                    throw new Exception("HTTP: " + connection.getResponseCode()
                            + ", " + connection.getResponseMessage());
                }


            } catch (MalformedURLException e) {
                Log.e(TAG, "Failed to get tasks", e);

            } catch (IOException e) {
                stethoManager.httpExchangeFailed(e);
                if (IOUtils.isConnectionAvailable(getContext(), false)) {
                    resultType = ResultType.ERROR;
                } else {
                    resultType = ResultType.NO_INTERNET;
                }

            } catch (Exception e) {
                Log.e(TAG, "Failed to get tasks: ", e);

            } finally {
                IOUtils.closeSilently(in);
                if (connection != null) {
                    connection.disconnect();
                }
            }
            if (task != null) {
                data.add(task);
            }
        }

        return new LoadResult<>(resultType, data);
    }
    private static final String TAG = "Tasks";
}
