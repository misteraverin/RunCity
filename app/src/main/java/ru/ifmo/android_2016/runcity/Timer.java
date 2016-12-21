package ru.ifmo.android_2016.runcity;


import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.ifmo.android_2016.runcity.utils.CircleCountDownView;

public class Timer extends Drawer implements View.OnClickListener {

    protected EditText etTime;
    protected CircleCountDownView countDownView;
    protected Button startTimerBt, cancelTimerBt;

    int progress;
    int endTime;
    String timeInterval = "300"; // time in seconds
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_timer);

        countDownView = (CircleCountDownView) findViewById(R.id.circle_count_down_view);
        startTimerBt = (Button) findViewById(R.id.startTimer);

        startTimerBt.setOnClickListener(this);
    }

    protected void startCountDown(final View view) {
        view.setVisibility(View.GONE); // hide button
        countDownView.setVisibility(View.VISIBLE); // show progress view


        progress = 1;
        endTime = Integer.parseInt(timeInterval); // up to finish time

        countDownTimer = new CountDownTimer(endTime * 1000 /*finishTime**/, 1000 /*interval**/) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownView.setProgress(progress, endTime);
                progress = progress + 1;
            }

            @Override
            public void onFinish() {
                countDownView.setProgress(progress, endTime);
                view.setVisibility(View.VISIBLE);
            }
        };
        countDownTimer.start(); // start timer

    }

    public void stopCountDown(View view) {
        countDownView.setProgress(endTime, endTime);
        countDownTimer.cancel();
        startTimerBt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.startTimer)
            startCountDown(view);
    }
}
