package com.breadtech.breadgrader.ui;

import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.breadtech.breadgrader.R;
import com.breadtech.breadgrader.model.Assignment;
import com.breadtech.breadgrader.model.BreadGraderSQLHelper;
import com.breadtech.breadgrader.model.Course;
import com.breadtech.breadgrader.model.Criteria;
import com.breadtech.breadgrader.model.Semester;
import com.breadtech.breadinterface.BIListActivity;

import java.util.ArrayList;

public class AssignmentListActivity extends BIListActivity {


    //
    // instance variables
    //
    private BreadGraderSQLHelper db_helper;
    private Criteria criteria;

    private ArrayList<Assignment> assignments;

    //
    // breadinterface button clicks
    //

    @Override
    public String tm_label() { return "Assignments"; }

    @Override
    public String bm_label() {
        return "average: " + this.db_helper.get_criteria_avg(this.criteria).toString();
    }

    @Override
    public void tl_clicked() {
        // back
        finish();
    }

    @Override
    public void tr_clicked() {
        // open criteria info
        Intent i = new Intent(this, CriteriaInfoActivity.class );

        i.putExtra( "criteria_id", this.criteria.id );
        startActivity(i);
    }

    @Override
    public void br_clicked() {
        // add assignment
        Intent i = new Intent( this, AssignmentInfoActivity.class );
        i.putExtra( "criteria_id", this.criteria.id );
        i.putExtra("index", assignments.size()+1 );
        startActivity(i);
    }

    //
    // list itemonclick listener class def
    //
    private class CourseListItemOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick( AdapterView<?> p, View v, int i , long id) {
            // create an intent to view a semester's info
            Intent intent = new Intent( AssignmentListActivity.this, AssignmentInfoActivity.class );
            Assignment s = assignments.get(i);
            intent.putExtra("assignment_id", s.id);
            startActivity(intent);
        }
    }

    //
    // breadinterface lifecycle
    //

    @Override
    public void init() {
        super.init();
        this.setContentView(R.layout.activity_assignment_list);

        // model
        this.db_helper = new BreadGraderSQLHelper(this);
        this.criteria = db_helper.get_criteria( getIntent().getIntExtra("criteria_id",-1), false);

        // u
        this.getListView().setOnItemClickListener( new CourseListItemOnClickListener() );
    }

    @Override
    public void update() {
        super.update();

        // refeshing the list of semesters
        this.assignments = db_helper.get_assignments_for_criteria(this.criteria);

        // make an adapter
        AssignmentArrayAdapter adapter = new AssignmentArrayAdapter( this, R.layout.assignment_list_item, this.assignments );

        // set the adapter
        this.setListAdapter( adapter );
    }
}
