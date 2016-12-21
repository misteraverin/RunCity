package ru.ifmo.android_2016.runcity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import ru.ifmo.android_2016.runcity.loader.CompetitionsLoader;
import ru.ifmo.android_2016.runcity.loader.CompetitionsParser;
import ru.ifmo.android_2016.runcity.loader.LoadResult;
import ru.ifmo.android_2016.runcity.loader.ResultType;
import ru.ifmo.android_2016.runcity.model.Competition;
import ru.ifmo.android_2016.runcity.utils.CompetitionsRecyclerAdapter;
import ru.ifmo.android_2016.runcity.utils.RecyclerDividersDecorator;

/**
 * Created by -- on 11.11.2016.
 */

public class Competitions extends Drawer
        implements LoaderManager.LoaderCallbacks<LoadResult<ArrayList<Competition>>> {

    private RecyclerView recyclerCompetitions;
    private CompetitionsRecyclerAdapter adapterCompetitions;

    private static final String COMPETITIONS = "id_of_competitions";

    private final String URI = "http://www.pastegraph.esy.es/competition.json";
    ArrayList<Competition> comps = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_competitiotns);

        recyclerCompetitions = (RecyclerView) findViewById(R.id.recycler_competitions);
        recyclerCompetitions.setLayoutManager(new LinearLayoutManager(this));
        recyclerCompetitions.addItemDecoration(
                new RecyclerDividersDecorator(getResources().getColor(R.color.gray_a)));


        final File file = new File(getFilesDir(), "competitions.json");
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


        try {
            comps = CompetitionsParser.parseCompetitions(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (comps == null) {
            displayError(ResultType.ERROR);
        } else {
            displayNonEmptyData(comps);
        }

    }

    private void displayOpenCompetitions(ArrayList<Competition> competitions) {
        if (adapterCompetitions == null) {
            adapterCompetitions = new CompetitionsRecyclerAdapter(this);
            recyclerCompetitions.setAdapter(adapterCompetitions);
        }
        adapterCompetitions.setCompetitions(competitions);
        recyclerCompetitions.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<LoadResult<ArrayList<Competition>>> onCreateLoader(int id, Bundle args) {
        final ArrayList<Integer> comps;
        if (args != null && args.containsKey(COMPETITIONS)) {
           comps = args.getIntegerArrayList(COMPETITIONS);
        } else {
            comps = new ArrayList<>();
        }
        return new CompetitionsLoader(this, comps);
    }

    @Override
    public void onLoadFinished(Loader<LoadResult<ArrayList<Competition>>> loader, LoadResult<ArrayList<Competition>> result) {
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
    public void onLoaderReset(Loader<LoadResult<ArrayList<Competition>>> loader) {
        displayEmptyData();
    }

    private void displayNonEmptyData(ArrayList<Competition> competitions) {
        if (adapterCompetitions == null) {
            adapterCompetitions = new CompetitionsRecyclerAdapter(this);
            recyclerCompetitions.setAdapter(adapterCompetitions);
        }
        adapterCompetitions.setCompetitions(competitions);
        recyclerCompetitions.setVisibility(View.VISIBLE);
    }

    private void displayEmptyData() {
        //Toast.makeText(getApplicationContext(), "Соревнования не найдены", Toast.LENGTH_SHORT).show();
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
