package com.breadtech.breadgrader.ui;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.breadtech.breadgrader.R;
import com.breadtech.breadgrader.model.BreadGraderSQLHelper;
import com.breadtech.breadgrader.model.Semester;
import com.breadtech.breadinterface.BIActivity;

public class SemesterInfoActivity extends BIActivity
{
    //
    // constants
    //
    public static int SEMESTER_SAVED = 1;

    //
    // instance variables
    //

    // model
    BreadGraderSQLHelper db_helper;
    Semester semester;
    int semester_id;

    // ui
    EditText season_et, year_et;

    //
    // utility methods
    //

    public boolean check_fields() {
        boolean y = true;
        if (season_et.getText().toString().length() == 0) {
            season_et.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Missing Field: name", Toast.LENGTH_SHORT).show();
            y = false;
        } else if (year_et.getText().toString().length() == 0) {
            year_et.setBackgroundColor(Color.RED);
            Toast.makeText( this, "Missing Field: index", Toast.LENGTH_SHORT).show();
            y = false;
        }
        return y;
    }

    public void save() {
        if (!this.check_fields()) return;

        // update the model
        this.semester.season = this.season_et.getText().toString();
        this.semester.year = Integer.parseInt(this.year_et.getText().toString());
        // ask the db to save it
        this.db_helper.set_semester(this.semester);

        finish();
    }

    public void delete() {
        this.db_helper.delete_semester(this.semester);
    }

    //
    // bi buttons
    //

    @Override
    public String tm_label() {
        return this.semester_id != -1 ? "Semester Info" : "Add Semester" ;
    }

    @Override
    public void tl_clicked() { finish(); }
    // cancel

    @Override
    public void tr_clicked() { save(); }
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
        setContentView(R.layout.activity_semester_info);

        // model
        this.db_helper = new BreadGraderSQLHelper(this);
        this.semester_id = getIntent().getIntExtra("semester_id",-1); // -1 means add

        // ui
        this.season_et = (EditText)this.findViewById(R.id.season_et);
        this.year_et = (EditText)this.findViewById(R.id.year_et);

        // removing the bottom bar if adding semester
        if (this.semester_id == -1) {
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
        this.semester = this.db_helper.get_semester(semester_id);

        if (this.semester == null) {
            this.semester = new Semester();
        }
        else {
            // set the ui
            this.season_et.setText(this.semester.season);
            this.year_et.setText("" + this.semester.year);
        }
    }

}
