package ru.ifmo.android_2016.runcity.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.ifmo.android_2016.runcity.model.Task;
import ru.ifmo.android_2016.runcity.model.taskState;
import ru.ifmo.android_2016.runcity.model.taskType;
import ru.ifmo.android_2016.runcity.utils.IOUtils;

/**
 * Created by -- on 17.12.2016.
 */

public class TasksParser {
    public static Task parseTasks(InputStream in) throws
            IOException,
            JSONException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseTasks(json);
    }

    private static Task parseTasks(JSONObject json) throws IOException, JSONException {
        final Integer taskId = json.getInt("id");
        final String taskName = json.getString("text");
        final Integer taskPoint = json.optInt("value", 0);
        final taskState state = json.optInt("state", 1) == 1 ? taskState.OPEN : taskState.ANSWERED;
        final taskType type = json.getString("type").equals("text") ? taskType.QUESTION : taskType.SIGNATURE;
        final String taskAnswer = "";

        return new Task(taskId, taskName, taskPoint, state, type, taskAnswer);
    }

    public static ArrayList<Task> parseTasks(File file, ArrayList<Integer> ids) throws IOException, JSONException {
        ArrayList<Task> itog = new ArrayList<>();
        InputStream is = new FileInputStream(file);
        String jsonTxt = org.apache.commons.io.IOUtils.toString(is);
        JSONObject json  = new JSONObject(jsonTxt);
        JSONArray array = json.getJSONArray("results");
        for (int i = 0; i != ids.size(); i++) {
            if (i < array.length()) {
                JSONObject task = array.getJSONObject(ids.get(i) - 1);
                Integer id = task.getInt("id");
                String name = task.getString("name");
                Integer point = task.getInt("point");
                String st = task.getString("state");
                taskState state = null;
                switch (st) {
                    case "OPEN": {state = taskState.OPEN; break;}
                    case "RIGHT": {state = taskState.RIGHT; break;}
                    case "WRONG": {state = taskState.WRONG; break;}
                    default:state = taskState.OPEN;
                }
                taskType type = task.getString("type").equals("QUESTION") ? taskType.QUESTION : taskType.SIGNATURE;
                String answer = task.getString("answer");
                itog.add(new Task(id, name, point, state, type, answer));
            }
        }

        return itog;
    }
}
