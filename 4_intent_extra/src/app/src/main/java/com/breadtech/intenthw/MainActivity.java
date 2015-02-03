package com.breadtech.intenthw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    // constants
    static final String INFO_TAG = "info";
    final static String LOG_TAG = "main activity";

    //
    // instance variables
    //
    protected EditText info_et;
    protected Button run_b;

    //
    // convenience pointers
    //
    private MainActivity self = this;

    //
    // run button callback method definition
    protected class runButtonOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick( View caller )
        {
            // get the info as a String
            String info_s = info_et.getText().toString();

            // create the intent
            Intent intent = new Intent( self, ButtonClickedActivity.class );
            intent.putExtra( INFO_TAG, info_s );

            // start the activity
            startActivityForResult(intent, 1);
        }

    }

    //
    // method callback from the button activity
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if (requestCode == 1 && resultCode == 0 && data != null) {
            String info_s = data.getStringExtra(INFO_TAG);
            this.info_et.setText(info_s);
        }
    }


    //
    // activity lifecycle methods
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set the ivars
        this.info_et = (EditText)findViewById(R.id.info_editText);
        this.run_b = (Button)findViewById(R.id.run_button);

        // hook up the onClickListener
        this.run_b.setOnClickListener( new runButtonOnClickListener() );
    }

    @Override
    protected void onResume()
    {
        super.onResume();

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
