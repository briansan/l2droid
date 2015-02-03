package com.breadtech.sqlitehw;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import javax.microedition.khronos.egl.EGLDisplay;


public class MainActivity extends Activity {

    //
    // instance variables
    //

    //
    // model
    StudentOpenHelper db_helper;

    //
    // view
    ListView list;
    EditText name_et, major_et, grad_et;
    Button add_b;

    //
    // utility methods
    //

    //
    // clears out all the edit texts
    protected void clearFields()
    {
        this.name_et.setText("");
        this.major_et.setText("");
        this.grad_et.setText("");
    }

    //
    // add button click action
    protected class AddButtonOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            // make sure all the fields are filled
            if (name_et.getText().length() > 0 && major_et.getText().length() > 0 &&
                    grad_et.getText().length() > 0)
            {
                // get the values
                String name = name_et.getText().toString();
                String major = major_et.getText().toString();
                int year = Integer.parseInt(grad_et.getText().toString());

                // add the values to the table
                db_helper.addStudent( name, major, year );

                // refresh the list and clear the fields
                refreshList();
                clearFields();
            }
        }
    }

    //
    // refreshes the student list
    protected void refreshList()
    {
        // request the db helper for the student list in the form of a cursor
        Cursor cursor = this.db_helper.getStudents();

        // create an adapter that uses the cursor
        SimpleCursorAdapter adapter = new SimpleCursorAdapter( MainActivity.this, R.layout.student_list_item,
                cursor,
                new String[] { StudentOpenHelper.S_NAME, StudentOpenHelper.S_MAJOR, StudentOpenHelper.S_YEAR } ,
                new int[] { R.id.student_name, R.id.student_major, R.id.student_year }, 0 );

        // set the adapter
        this.list.setAdapter(adapter);
    }

    //
    // activity lifecycle methods
    //

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set ivars
        this.db_helper = new StudentOpenHelper(this);
        this.list = (ListView)findViewById(R.id.list);

        this.name_et = (EditText)findViewById(R.id.name_editText);
        this.major_et = (EditText)findViewById(R.id.major_editText);
        this.grad_et = (EditText)findViewById(R.id.grad_year_editText);

        this.add_b = (Button)findViewById(R.id.add_button);

        // hook up the add button
        this.add_b.setOnClickListener( new AddButtonOnClickListener() );
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.refreshList();
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
