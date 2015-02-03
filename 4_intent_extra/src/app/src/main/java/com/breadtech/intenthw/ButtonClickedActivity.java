package com.breadtech.intenthw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ButtonClickedActivity extends Activity {

    //
    // instance variables
    //
    protected EditText info_et;
    protected Button run_b;

    //
    // convenience pointers
    //
    private Intent info_intent;
    private ButtonClickedActivity self = this;

    //
    // run button callback method definition
    protected class runButtonOnClickListener implements View.OnClickListener
    {
        final static String INFO_TAG = "info";

        @Override
        public void onClick( View caller )
        {
            // get the info as a String
            String info_s = info_et.getText().toString();

            // create the intent
            info_intent.putExtra( INFO_TAG, info_s );

            // set the intent with result
            setResult(0,info_intent);

            // go back
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_clicked);

        //
        // set the ivars
        //

        // the intent
        this.info_intent = getIntent();
        setResult(0);

        //
        // set up the info edit text

        // get from xml
        this.info_et = (EditText)findViewById(R.id.info_editText);
        // set the edit text
        String info_s = info_intent.getStringExtra(runButtonOnClickListener.INFO_TAG);
        this.info_et.setText( info_s );

        //
        // set up the button
        this.run_b = (Button)findViewById(R.id.run_button);

        // hook up the onClickListener
        this.run_b.setOnClickListener( new runButtonOnClickListener() );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.button_clicked, menu);
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
