package ru.ifmo.android_2016.runcity.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.ifmo.android_2016.runcity.R;
import ru.ifmo.android_2016.runcity.Tasks;
import ru.ifmo.android_2016.runcity.model.Competition;
import ru.ifmo.android_2016.runcity.model.competitionState;

/**
 * Created by -- on 11.11.2016.
 */

public class CompetitionsRecyclerAdapter extends RecyclerView.Adapter<CompetitionsRecyclerAdapter.CompetitionsViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;

    @NonNull
    private ArrayList<Competition> competitions = new ArrayList<>();

    public CompetitionsRecyclerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setCompetitions(@NonNull ArrayList<Competition> competitions) {
        this.competitions = competitions;
        notifyDataSetChanged();
    }

    @Override
    public CompetitionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CompetitionsViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(CompetitionsViewHolder holder, final int position){
        final Competition competition = competitions.get(position);
        holder.competitionDescription.setText(competition.competitionName);
        if (competition.state.equals(competitionState.OPEN)) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.open_comp));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.closed_comp));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context.getApplicationContext(), Tasks.class);
                intent.putExtra(Tasks.TASKS, competition.questions);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return competitions.size();
    }

    static class CompetitionsViewHolder extends RecyclerView.ViewHolder {

        final TextView competitionDescription;

        private CompetitionsViewHolder(View itemView) {
            super(itemView);
            competitionDescription = (TextView) itemView.findViewById(R.id.comp_description);
        }

        static CompetitionsViewHolder newInstance(LayoutInflater layoutInflater, ViewGroup parent) {
            final View view = layoutInflater.inflate(R.layout.item_competition, parent, false);
            return new CompetitionsViewHolder(view);
        }
    }
}