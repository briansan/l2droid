//
// title: AudioServiceActivity
// by: Brian Kim
// description: a basic interface for playing and pausing a song
//   such that it will operate outside of the app
//

package com.breadtech.audioservicehw;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;


public class AudioServiceActivity extends Activity {

    //
    // instance variables
    //
    private AudioService svc;
    private boolean isBound;
    private Button playButton, pauseButton;
    private TextView statusLabel;

    // annoymous class definition of the service connection
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // bounding the audio service
            svc = ((AudioService.AudioBinder)iBinder).getService();
            isBound = true;

            // update the ui
            playButton.setEnabled(true);
            pauseButton.setEnabled(true);
            statusLabel.setText("Ready!");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // unbounding the audio service
            svc = null;
            isBound = false;
        }
    };

    //
    // button click handlers
    //

    //
    // for the play button
    private View.OnClickListener playButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            svc.play();
        }
    };

    //
    // for the pause button
    private View.OnClickListener pauseButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick( View v ) {
            svc.pause();
        }
    };

    //
    // lifecycle methods
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_service);

        // set the on click listeners for the buttons
        playButton = (Button)findViewById(R.id.play_button);
        pauseButton = (Button)findViewById(R.id.pause_button);

        playButton.setOnClickListener(this.playButtonClicked);
        pauseButton.setOnClickListener(this.pauseButtonClicked);

        // disable the play button if the service hasn't been connected yet
        playButton.setEnabled(this.isBound);
        pauseButton.setEnabled(this.isBound);

        // set the status label
        statusLabel = (TextView)findViewById(R.id.status_label);
        statusLabel.setText("Service Unbound...");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // start up the intent to start the audio service
        Intent intent = new Intent( this, AudioService.class );
        this.bindService( intent, this.conn, Context.BIND_AUTO_CREATE );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // kill the intent
        if (isBound) {
            this.unbindService( this.conn );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.audio, menu);
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
