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
import com.breadtech.breadgrader.model.Criteria;
import com.breadtech.breadgrader.model.Semester;
import com.breadtech.breadinterface.BIListActivity;

import java.util.ArrayList;

public class CriteriaListActivity extends BIListActivity {


    //
    // instance variables
    //
    private BreadGraderSQLHelper db_helper;
    private Course course;

    private ArrayList<Criteria> criterion;

    //
    // breadinterface button clicks
    //

    @Override
    public String tm_label() { return this.course.title; }

    @Override
    public void tl_clicked() {
        // back
        finish();
    }
    @Override
    public void tr_clicked() {
        // open settings
        Intent i = new Intent(this, CourseInfoActivity.class );
        i.putExtra( "course_id", this.course.id );
        startActivity(i);
    }

    @Override
    public String bm_label() {
        return "average: " + this.db_helper.get_course_avg(this.course).toString();
    }

    @Override
    public void br_clicked() {
        // add a semester
        Intent i = new Intent( this, CriteriaInfoActivity.class );
        i.putExtra( "course_id", this.course.id );
        startActivity(i);
    }

    //
    // list itemonclick listener class def
    //
    private class CourseListItemOnClickListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        @Override
        public void onItemClick( AdapterView<?> p, View v, int i , long id) {
            // create an intent to view a semester's info
            Intent intent = new Intent( CriteriaListActivity.this, AssignmentListActivity.class ); // should be assignment list
            Criteria s = criterion.get(i);
            intent.putExtra("criteria_id", s.id);
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick( AdapterView<?> p, View v, int i , long id) {
            // create an intent to view a semester's info
            Intent intent = new Intent( CriteriaListActivity.this, CriteriaInfoActivity.class );
            Criteria s = criterion.get(i);
            intent.putExtra("criteria_id", s.id);
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
        this.course = db_helper.get_course( getIntent().getIntExtra("course_id",-1), false );

        // ui
        this.getListView().setOnItemLongClickListener( new CourseListItemOnClickListener() );
        this.getListView().setOnItemClickListener( new CourseListItemOnClickListener() );
    }

    @Override
    public void update() {
        super.update();

        // refeshing the list of semesters
        this.criterion = db_helper.get_criterion_for_course(this.course,true);

        // make an adapter
        CriteriaArrayAdapter adapter = new CriteriaArrayAdapter( this, R.layout.course_list_item, this.criterion );

        // set the adapter
        this.setListAdapter( adapter );
    }
}
