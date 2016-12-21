package ru.ifmo.android_2016.runcity.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by -- on 17.12.2016.
 */

public class RuncityApi {

  // "http://www.runcity.misteraverin.com/api/";
    private static final String BASE_URL =  "http://www.pastegraph.esy.es/";

    public static HttpURLConnection createCompetitionsRequest(Integer id) throws IOException {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("competition.json")
                .appendEncodedPath(id.toString())
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }

    public static HttpURLConnection createTasksRequest(Integer id) throws IOException {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("questions/")
                .appendEncodedPath(id.toString())
                .build();
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }

    private RuncityApi() {}
}
