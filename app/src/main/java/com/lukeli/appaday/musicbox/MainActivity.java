package com.lukeli.appaday.musicbox;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {
    private long duration = 3000; //3s
    private long endTime;
    private final int REFRESH_RATE = 100;
    private long timeLeft;
    private boolean stopped = false;
    private Handler mHandler = new Handler();
    private String hours,minutes,seconds,milliseconds;
    private long secs,mins,hrs,msecs;
    MediaPlayer mPlayer;
    final Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideStopButton();
        mPlayer = MediaPlayer.create(this, R.raw.gsd);
        updateTimer(duration);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startCountdown(View view) {
        showStopButton();

        mPlayer.start();
        endTime = System.currentTimeMillis() + duration;
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
    }

    public void resetCountdown(View view) {
        stopped = false;
        ((TextView)findViewById(R.id.timerText)).setText(convertToTime(duration));
    }

    public void stopCountdown(View view) {
        hideStopButton();
        if(mPlayer.isPlaying()) {
            mPlayer.pause();
        } 
        mHandler.removeCallbacks(startTimer);
        stopped = true;
    }

    private void showStopButton(){
        (findViewById(R.id.startCountdownButton)).setVisibility(View.GONE);
        (findViewById(R.id.resetCountdownButton)).setVisibility(View.GONE);
        (findViewById(R.id.stopCountdownButton)).setVisibility(View.VISIBLE);
    }

    private void hideStopButton(){
        (findViewById(R.id.startCountdownButton)).setVisibility(View.VISIBLE);
        (findViewById(R.id.resetCountdownButton)).setVisibility(View.VISIBLE);
        (findViewById(R.id.stopCountdownButton)).setVisibility(View.GONE);
    }

    private void updateTimer (float time){
        ((TextView)findViewById(R.id.timerText)).setText(convertToTime(time));
    }
    private String convertToTime(float time){
        long longTime = (long) time;
        return String.format("%02d:%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(longTime),
                TimeUnit.MILLISECONDS.toMinutes(longTime) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(longTime)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(longTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(longTime)),
                TimeUnit.MILLISECONDS.toMillis(longTime) -
                        TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(longTime)));

    }
    private Runnable startTimer = new Runnable() {
        public void run() {
            timeLeft = endTime - System.currentTimeMillis();

            updateTimer(timeLeft);
            if(timeLeft<=0){
                mPlayer.stop();
                mPlayer = MediaPlayer.create(context, R.raw.gsd);
                mHandler.removeCallbacks(startTimer);
                stopped = true;
            }else{
                mHandler.postDelayed(this,REFRESH_RATE);
            }

        }
    };
}
