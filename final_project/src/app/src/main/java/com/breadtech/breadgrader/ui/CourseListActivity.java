package com.breadtech.breadgrader.ui;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.breadtech.breadgrader.R;
import com.breadtech.breadgrader.model.BreadGraderSQLHelper;
import com.breadtech.breadgrader.model.Course;
import com.breadtech.breadgrader.model.Semester;
import com.breadtech.breadinterface.BIListActivity;

import java.util.ArrayList;

public class CourseListActivity extends BIListActivity {


    //
    // instance variables
    //
    private BreadGraderSQLHelper db_helper;
    private Semester semester;

    private ArrayList<Course> courses;

    //
    // breadinterface button clicks
    //

    @Override
    public String tm_label() { return this.semester.season + " " + this.semester.year; }

    @Override
    public void tl_clicked() {
        // back
        finish();
    }
    @Override
    public void tr_clicked() {
        // open settings
        Intent i = new Intent(this, SemesterInfoActivity.class );
        i.putExtra( "semester_id", this.semester.id );
        startActivity(i);
    }

    @Override
    public String bm_label() {
        return "average: " + this.db_helper.get_semester_avg(this.semester).toString();
    }

    @Override
    public void br_clicked() {
        // add a semester
        Intent i = new Intent( this, CourseInfoActivity.class );
        i.putExtra( "semester_id", this.semester.id );
        startActivity(i);
    }

    //
    // list itemonclick listener class def
    //
    private class CourseListItemOnClickListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        @Override
        public void onItemClick( AdapterView<?> p, View v, int i , long id) {
            // create an intent to view a semester's info
            Intent intent = new Intent( CourseListActivity.this, CriteriaListActivity.class );
            Course s = courses.get(i);
            intent.putExtra("course_id", s.id);
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick( AdapterView<?> p, View v, int i , long id) {
            // create an intent to view a semester's info
            Intent intent = new Intent( CourseListActivity.this, CourseInfoActivity.class );
            Course s = courses.get(i);
            intent.putExtra("course_id", s.id);
            startActivity(intent);
            return true;
        }
    }

    //
    // breadinterface lifecycle
    //

    @Override
    public void init() {
        super.init();
        this.setContentView(R.layout.activity_course_list);

        // model
        this.db_helper = new BreadGraderSQLHelper(this);
        this.semester = db_helper.get_semester( getIntent().getIntExtra("semester_id",-1));

        // ui
        this.getListView().setOnItemLongClickListener( new CourseListItemOnClickListener() );
        this.getListView().setOnItemClickListener( new CourseListItemOnClickListener() );
    }

    @Override
    public void update() {
        super.update();

        // refeshing the list of semesters
        this.courses = db_helper.get_courses_for_semester(this.semester,true);

        // make an adapter
        CourseArrayAdapter adapter = new CourseArrayAdapter( this, R.layout.course_list_item, this.courses );

        // set the adapter
        this.setListAdapter( adapter );
    }
}
