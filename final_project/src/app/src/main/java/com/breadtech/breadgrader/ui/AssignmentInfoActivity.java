package com.breadtech.breadgrader.ui;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.breadtech.breadgrader.R;
import com.breadtech.breadgrader.model.Assignment;
import com.breadtech.breadgrader.model.BreadGraderSQLHelper;
import com.breadtech.breadgrader.model.Course;
import com.breadtech.breadgrader.model.Grade;
import com.breadtech.breadgrader.model.Semester;
import com.breadtech.breadinterface.BIActivity;

import org.apache.http.impl.io.ContentLengthOutputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AssignmentInfoActivity extends BIActivity {

    //
    // constants
    //
    public static int ASSIGNMENT_SAVED = 1;

    //
    // instance variables
    //

    // model
    BreadGraderSQLHelper db_helper;
    Assignment assignment;
    int criteria_id;
    int assignment_id;
    int index;
    SimpleDateFormat df;

    // ui
    EditText name_et, index_et, due_et, rcv_et, max_et, notes_et;

    //
    // utility methods
    //

    public boolean check_fields() {
        boolean y = true;
        if (index_et.getText().toString().length() == 0) {
            index_et.setBackgroundColor(Color.RED);
            Toast.makeText( this, "Missing Field: index", Toast.LENGTH_SHORT).show();
            y = false;
        } else if (due_et.getText().toString().length() == 0) {
            due_et.setBackgroundColor(Color.RED);
            Toast.makeText( this, "Missing Field: due date", Toast.LENGTH_SHORT).show();
            y = false;
        }
        return y;
    }

    public void save() {
        if (!this.check_fields()) return;

        // update the model
        String name = this.name_et.getText().toString().length() > 0 ? this.name_et.getText().toString() : "Assignment";
        this.assignment.name = name;
        this.assignment.index = Integer.parseInt(this.index_et.getText().toString());

        //
        // for due date
        Date d = null;
        try { d = df.parse(this.due_et.getText().toString()); }
        catch (Exception e) {
            Toast.makeText(this, "Invalid date format (MM/dd/yyyy)", Toast.LENGTH_SHORT).show();
        }
        Log.d("Assignment Info", "parsed date: " + d.toString());
        this.assignment.due = d;

        //
        // for grade
        double rcv = this.rcv_et.getText().toString().length() > 0 ? Double.parseDouble( this.rcv_et.getText().toString()) : -1;
        double max = this.max_et.getText().toString().length() > 0 ? Double.parseDouble( this.max_et.getText().toString()) : 100;
        this.assignment.grade = new Grade( rcv, max );

        // notes
        this.assignment.notes = this.notes_et.getText().toString();

        if (this.assignment.criteria == null) {
            this.assignment.criteria = this.db_helper.get_criteria(this.criteria_id,false);
        }
        // ask the db to save it
        this.db_helper.set_assignment(this.assignment);

        finish();
    }

    public void delete() {
        this.db_helper.delete_assignment(this.assignment);
    }

    //
    // bi buttons
    //

    @Override
    public String tm_label() {
        return this.assignment_id != -1 ? "Assignment Info" : "Add Assignment" ;
    }

    @Override
    public void tl_clicked() { finish(); }
    // cancel

    @Override
    public void tr_clicked() { save();  }
    // save

    @Override
    public void br_clicked() {
        this.delete();
        finish();
    }

    //
    // bi lifecycle
    //
    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_assignment_info);

        // model
        this.db_helper = new BreadGraderSQLHelper(this);
        this.criteria_id = getIntent().getIntExtra("criteria_id",-1); // -1 means add
        this.assignment_id = getIntent().getIntExtra("assignment_id",-1);
        this.index = getIntent().getIntExtra("index",1);
        this.df = new SimpleDateFormat("MM-dd-yyyy");

        // ui
        this.name_et = (EditText)this.findViewById(R.id.name_et);
        this.index_et = (EditText)this.findViewById(R.id.index_et);
        this.due_et = (EditText)this.findViewById(R.id.due_et);
        this.rcv_et = (EditText)this.findViewById(R.id.rcv_et);
        this.max_et = (EditText)this.findViewById(R.id.max_et);
        this.notes_et = (EditText)this.findViewById(R.id.notes_et);

        // removing the bottom bar if adding semester
        if (this.criteria_id == -1) {

            LinearLayout bottom_bar = (LinearLayout) this.findViewById(R.id.bottom_bar);
            Button bl = (Button)this.findViewById(R.id.bl);
            Button br = (Button)this.findViewById(R.id.br);
            bottom_bar.removeView(bl);
            bottom_bar.removeView(br);
        }
    }

    @Override
    public void resume() {
        super.resume();

        // get the semester
        this.assignment = this.db_helper.get_assignment(this.assignment_id);

        if (this.assignment == null) {
            this.assignment = new Assignment();

            this.index_et.setText(""+this.index);
            this.due_et.setText( this.df.format( new Date() ) );
        }
        else {
            // set the ui
            this.name_et.setText(this.assignment.name);
            this.index_et.setText(""+this.assignment.index);


            this.due_et.setText(this.df.format(this.assignment.due));
            this.rcv_et.setText(""+this.assignment.grade.received());
            this.max_et.setText(""+this.assignment.grade.max());
            this.notes_et.setText(this.assignment.notes);
        }
    }

}
