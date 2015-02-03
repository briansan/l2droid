/**
 * @file MainActivity.java
 * @author bk
 * @brief the HTTP JSON client activity that creates a socket
 *        and can send a message through the socket
 */

package com.breadtech.httpjsonecho;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;


public class MainActivity extends Activity {
    //==================================================
    // constants
    //==================================================
    protected static final String TAG = "HTTPJSONMainAcitivity";
    protected static final String DEFAULT_SERVER_ADDRESS = "10.0.2.2";
    protected static final String DEFAULT_SERVER_PORT = "8124";

    //==================================================
    // instance variables
    //==================================================

    // view
    private EditText server_addr_et, server_port_et, msg_et;
    private Button send_button;
    private TextView response_tv;

    // http socket
    protected HttpURLConnection client;

    //==========================================================
    // inner classes
    //==========================================================

    /**
     * @brief POST's the msg to the specified address and port
     */
    private class SendButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            // get a handle to the main activity instance
            final MainActivity self = MainActivity.this;

            // create some convenience variables
            String addr = self.server_addr_et.getText().toString();
            String port = self.server_port_et.getText().toString();
            String msg = self.msg_et.getText().toString();

            try {
                // create the msg
                final JSONObject data = new JSONObject();
                data.put("msg", msg);

                // create the url
                final URL url = new URL( "http://"+addr+":"+port+"/echo"); // using /echo as request

                // asynchronously make the http request
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // connect2server
                            conn.setRequestMethod("POST"); // set the http request to post
                            conn.setDoOutput(true); // request for POST to upload data
                            conn.setFixedLengthStreamingMode(data.toString().getBytes().length); // fix the streaming length

                            OutputStream out = conn.getOutputStream(); // open up an output stream
                            out.write(data.toString().getBytes()); // send out the json as byte stream
                            out.close(); // close the stream

                            // react to the server's response
                            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                            {
                                Scanner scan = new Scanner(conn.getInputStream()); // create a scanner
                                String response_data = scan.nextLine(); // get the response
                                JSONObject response_json = new JSONObject(response_data);
                                final String response_s = response_json.getString("response");

                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        self.response_tv.append(response_s+'\n'); // add to response tv
                                        self.msg_et.setText(""); // clear out the message field
                                    }
                                });
                            }

                            // close the connection at the end
                            conn.disconnect();
                        }
                        catch (Exception e) { e.printStackTrace(); }

                    }
                }.start();
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    //==================================================
    // Activity lifecycle methods
    //==================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        // init the ivars
        //
        this.server_addr_et = (EditText)this.findViewById(R.id.server_addr_editText);
        this.server_port_et = (EditText)this.findViewById(R.id.server_port_editText);
        this.msg_et = (EditText)this.findViewById(R.id.msg_editText);
        this.send_button = (Button)this.findViewById(R.id.send_button);
        this.response_tv = (TextView)this.findViewById(R.id.response_textView);

        // set the default uri to the edit text
        this.server_addr_et.setText( this.DEFAULT_SERVER_ADDRESS );
        this.server_port_et.setText( this.DEFAULT_SERVER_PORT );

        // hook up the send button's onclick listener
        this.send_button.setOnClickListener( new SendButtonOnClickListener() );
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
