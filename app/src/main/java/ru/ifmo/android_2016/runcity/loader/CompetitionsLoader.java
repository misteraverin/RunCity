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
import ru.ifmo.android_2016.runcity.model.Competition;
import ru.ifmo.android_2016.runcity.utils.IOUtils;

/**
 * Created by -- on 17.12.2016.
 */

public class CompetitionsLoader extends AsyncTaskLoader<LoadResult<ArrayList<Competition>>> {
    private final ArrayList<Integer> comps;

    public CompetitionsLoader(Context context, ArrayList<Integer> comps) {
        super(context);
        this.comps = comps;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public LoadResult<ArrayList<Competition>> loadInBackground(){
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        ResultType resultType = ResultType.ERROR;
        ArrayList<Competition> data = new ArrayList<>();

        for (int i = 0; i != comps.size(); i++) {
            HttpURLConnection connection = null;
            InputStream in = null;
            Competition cmp = null;

            try {
                connection = RuncityApi.createCompetitionsRequest(comps.get(i));
                Log.d(TAG, "Performing request: " + connection.getURL());

                stethoManager.preConnect(connection, null);
                connection.connect();
                stethoManager.postConnect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    in = connection.getInputStream();
                    in = stethoManager.interpretResponseStream(in);

                    cmp = CompetitionsParser.parseCompetitions(in);

                    resultType = ResultType.OK;

                } else {
                    // consider all other codes as errors
                    throw new Exception("HTTP: " + connection.getResponseCode()
                            + ", " + connection.getResponseMessage());
                }


            } catch (MalformedURLException e) {
                Log.e(TAG, "Failed to get competitions", e);

            } catch (IOException e) {
                stethoManager.httpExchangeFailed(e);
                if (IOUtils.isConnectionAvailable(getContext(), false)) {
                    resultType = ResultType.ERROR;
                    break;
                } else {
                    resultType = ResultType.NO_INTERNET;
                    break;
                }

            } catch (Exception e) {
                Log.e(TAG, "Failed to get competitions: ", e);

            } finally {
                IOUtils.closeSilently(in);
                if (connection != null) {
                    connection.disconnect();
                }
            }
            if (cmp != null) {
                data.add(cmp);
            }
        }

        return new LoadResult<>(resultType, data);
    }
    private static final String TAG = "Competitions";
}
