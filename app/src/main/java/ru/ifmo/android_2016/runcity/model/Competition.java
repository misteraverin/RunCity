package ru.ifmo.android_2016.runcity.model;

import java.util.ArrayList;

/**
 * Created by -- on 11.11.2016.
 */

public class Competition {
    public final Integer competitionId;
    public final String competitionName;
    public final competitionState state;
    public final Integer points;// -1, if competition is open; >= 0, if competition is closed
    public final ArrayList<Integer> questions;

    public Competition(Integer competitionId, String competitionName, competitionState state, Integer points, ArrayList<Integer> questions) {
        this.competitionId = competitionId;
        this.competitionName = competitionName;
        this.state = state;
        this.points = points;
        this.questions = questions;
    }
}
