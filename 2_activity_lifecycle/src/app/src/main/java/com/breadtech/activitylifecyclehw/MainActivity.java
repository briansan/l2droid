package com.breadtech.activitylifecyclehw;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends Activity {

    final static String MAIN_ACTIVITY_TAG = "MainActivity";
    protected TextView onCreateClockTimeLabel;
    protected TextView onRestartClockTimeLabel;
    protected TextView onStartClockTimeLabel;
    protected TextView onStopClockTimeLabel;
    protected TextView onResumeClockTimeLabel;
    protected TextView onPauseClockTimeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // storing references to the text view widgets
        this.onCreateClockTimeLabel = (TextView)findViewById(R.id.onCreate_clock_time_label);
        this.onRestartClockTimeLabel = (TextView)findViewById(R.id.onRestart_clock_time_label);
        this.onStartClockTimeLabel = (TextView)findViewById(R.id.onStart_clock_time_label);
        this.onResumeClockTimeLabel = (TextView)findViewById(R.id.onResume_clock_time_label);
        this.onPauseClockTimeLabel = (TextView)findViewById(R.id.onPause_clock_time_label);
        this.onStopClockTimeLabel = (TextView)findViewById(R.id.onStop_clock_time_label);

        // updating the clock for onCreate
        this.onCreateClockTimeLabel.setText( "" + SystemClock.elapsedRealtime() );

        // updating debug
        Log.d( MAIN_ACTIVITY_TAG, "onCreate() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // updating the clock for onRestart
        this.onRestartClockTimeLabel.setText( "" + SystemClock.elapsedRealtime() );

        // updating debug
        Log.d( MAIN_ACTIVITY_TAG, "onRestart() called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // updating the clock for onStart
        this.onStartClockTimeLabel.setText( "" + SystemClock.elapsedRealtime() );

        // updating debug
        Log.d(MAIN_ACTIVITY_TAG, "onStart() called");

    }

    @Override
    protected void onResume() {
        super.onRestart();

        // updating the clock for onCreate
        this.onResumeClockTimeLabel.setText( "" + SystemClock.elapsedRealtime() );

        // updating debug
        Log.d(MAIN_ACTIVITY_TAG, "onResume() called");

    }

    @Override
    protected void onPause() {
        super.onPause();

        // updating the clock for onCreate
        this.onPauseClockTimeLabel.setText( "" + SystemClock.elapsedRealtime() );

        // updating debug
        Log.d(MAIN_ACTIVITY_TAG, "onPause() called");

    }

    @Override
    protected void onStop() {
        super.onStop();

        // updating the clock for onCreate
        this.onStopClockTimeLabel.setText( "" + SystemClock.elapsedRealtime() );

        // updating debug
        Log.d(MAIN_ACTIVITY_TAG, "onStop() called");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // no clock label for onDestroy since the user will never see it

        // updating debug
        Log.d(MAIN_ACTIVITY_TAG, "onDestroy() called");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
