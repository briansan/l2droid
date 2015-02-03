package com.breadtech.sharedpreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

    // constants
    private static final String STOCK_PREF = "stocks";
    private static final String PREF_KEY = "stock";
    private static final String HTTP_QUERY = "http://finance.yahoo.com/q?s=";

    // ivars
    protected SharedPreferences prefs;
    protected EditText symbolEditText;
    protected TextView queryTextView;
    protected Button getQuoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize ivars
        this.prefs = getSharedPreferences( STOCK_PREF, Activity.MODE_PRIVATE );

        this.symbolEditText = (EditText)findViewById(R.id.symbol_edit_text);
        this.queryTextView = (TextView)findViewById(R.id.uri_query_label);
        this.getQuoteButton = (Button)findViewById(R.id.get_quote_button);

        // hook up the button to get the quote on click
        this.getQuoteButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the stock symbol
                String stock = symbolEditText.getText().toString();
                // build an intent to display the stock quote uri from yahoo
                Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse( HTTP_QUERY + stock ) );
                // get the preference editor
                SharedPreferences.Editor editor = prefs.edit();
                // set the stock symbol in the preference editor
                editor.putString( PREF_KEY, stock );
                // save it
                editor.apply();
                // reset the symbol edit text content
                symbolEditText.setText("");
                // start the activity
                startActivity( i );
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        String stock = prefs.getString( PREF_KEY, "" );
        if (stock.length() > 0) this.queryTextView.setText( HTTP_QUERY + stock );
    }

}
