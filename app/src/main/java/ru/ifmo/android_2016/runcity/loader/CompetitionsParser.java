package ru.ifmo.android_2016.runcity.loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import ru.ifmo.android_2016.runcity.model.Competition;
import ru.ifmo.android_2016.runcity.model.competitionState;
import ru.ifmo.android_2016.runcity.utils.IOUtils;

/**
 * Created by -- on 17.12.2016.
 */

public class CompetitionsParser {

    public static Competition parseCompetitions(InputStream in) throws
            IOException,
            JSONException {

        final String content = IOUtils.readToString(in, "UTF-8");
        final JSONObject json = new JSONObject(content);
        return parseCompetitions(json);
    }

    private static Competition parseCompetitions(JSONObject json) throws IOException, JSONException {
        final Integer competitionId = json.getInt("id");
        final String competitionName = json.getString("title");
        final competitionState state = json.getBoolean("active") ? competitionState.OPEN : competitionState.CLOSED;
        final Integer points = json.optInt("points", 0);
        final ArrayList<Integer> questions = new ArrayList<>();
        final JSONArray qlist = json.getJSONArray("question_list");
        for (int i = 0; i != qlist.length(); i++) {
             questions.add(Integer.parseInt(qlist.getString(i)));
        }

        return new Competition(competitionId, competitionName, state, points, questions);
    }

    public static ArrayList<Competition> parseCompetitions(File file) throws IOException, JSONException {
        ArrayList<Competition> itog = new ArrayList<>();
        InputStream is = new FileInputStream(file);
        String jsonTxt = org.apache.commons.io.IOUtils.toString(is);
        JSONObject json  = new JSONObject(jsonTxt);
        JSONArray array = json.getJSONArray("results");
        for (int i = 0; i != array.length(); i++) {
            JSONObject comp = array.getJSONObject(i);
            Integer id = comp.getInt("id");
            String title  = comp.getString("title");
            competitionState state = comp.getString("state").equals("OPEN") ? competitionState.OPEN : competitionState.CLOSED;
            Integer points = comp.getInt("points");
            final ArrayList<Integer> questions = new ArrayList<>();
            final JSONArray qlist = comp.getJSONArray("question_list");
            for (int j = 0; j != qlist.length(); j++) {
                questions.add(Integer.parseInt(qlist.getString(j)));
            }
            itog.add(new Competition(id, title, state, points, questions));
        }

        return itog;
    }
}
