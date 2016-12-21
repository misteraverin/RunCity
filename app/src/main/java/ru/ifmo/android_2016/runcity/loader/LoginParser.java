package ru.ifmo.android_2016.runcity.loader;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by -- on 20.12.2016.
 */

public class LoginParser {

    public static Pair<String, String> parseLogin(File file) throws IOException, JSONException {
        InputStream is = new FileInputStream(file);
        String jsonTxt = org.apache.commons.io.IOUtils.toString(is);
        JSONObject json  = new JSONObject(jsonTxt);
        String Login = json.getString("email");
        String Password  = json.getString("password");
        return new Pair<>(Login, Password);
    }
}
