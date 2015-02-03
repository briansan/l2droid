package com.breadtech.breadgrader.ui;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.breadtech.breadgrader.R;
import com.breadtech.breadgrader.model.BreadGraderSQLHelper;
import com.breadtech.breadgrader.model.Course;
import com.breadtech.breadgrader.model.Semester;
import com.breadtech.breadinterface.BIActivity;

public class CourseInfoActivity extends BIActivity {

    //
    // constants
    //
    public static int SEMESTER_SAVED = 1;

    //
    // instance variables
    //

    // model
    BreadGraderSQLHelper db_helper;
    Course course;
    int course_id;
    int semester_id;

    // ui
    EditText title_et, subject_et;

    //
    // utility methods
    //

    public boolean check_fields() {
        boolean y = true;
        if (title_et.getText().toString().length() == 0) {
            title_et.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Missing Field: title", Toast.LENGTH_SHORT).show();
            y = false;
        } else if (subject_et.getText().toString().length() == 0) {
            subject_et.setBackgroundColor(Color.RED);
            Toast.makeText( this, "Missing Field: subject", Toast.LENGTH_SHORT).show();
            y = false;
        }
        return y;
    }

    public void save() {
        if (!this.check_fields()) return;
        // update the model
        this.course.title = this.title_et.getText().toString();
        this.course.subject = this.subject_et.getText().toString();
        if (this.course.semester == null) {
            this.course.semester = this.db_helper.get_semester(this.semester_id);
        }
        // ask the db to save it
        this.db_helper.set_course(this.course);
        finish();
    }

    public void delete() {
        this.db_helper.delete_course(this.course);
    }

    //
    // bi buttons
    //

    @Override
    public String tm_label() {
        return this.course_id != -1 ? "Course Info" : "Add Course" ;
    }

    @Override
    public void tl_clicked() { finish(); }
    // cancel

    @Override
    public void tr_clicked() { save(); finish(); }
    // save

    @Override
    public void br_clicked() {
        this.delete();
    }

    //
    // bi lifecycle
    //
    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_course_info);

        // model
        this.db_helper = new BreadGraderSQLHelper(this);
        this.semester_id = getIntent().getIntExtra("semester_id",-1);
        this.course_id = getIntent().getIntExtra("course_id",-1); // -1 means add

        // ui
        this.title_et = (EditText)this.findViewById(R.id.title_et);
        this.subject_et = (EditText)this.findViewById(R.id.subject_et);

        // removing the bottom bar if adding semester
        if (this.course_id == -1) {
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
        this.course = this.db_helper.get_course(course_id,false);

        if (this.course == null) {
            this.course = new Course();
        }
        else {
            // set the ui
            this.title_et.setText(this.course.title);
            this.subject_et.setText("" + this.course.subject);
        }
    }

}
