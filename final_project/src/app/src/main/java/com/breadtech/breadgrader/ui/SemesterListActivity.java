package com.breadtech.breadgrader.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.database.Cursor;
import android.util.Patterns;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.breadtech.breadgrader.R;
import com.breadtech.breadgrader.model.BreadGraderSQLHelper;
import com.breadtech.breadgrader.model.Semester;
import com.breadtech.breadinterface.BIListActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SemesterListActivity extends BIListActivity {


    //
    // instance variables
    //
    private BreadGraderSQLHelper db_helper;
    private ArrayList<Semester> semesters;
    private String username;

    //
    // breadinterface button clicks
    //

    @Override
    public String tm_label() { return "Semesters"; }

    @Override
    public void tr_clicked() {
        // open settings
        Intent i = new Intent(this, SettingsActivity.class );
        startActivity(i);
    }


    @Override
    public String bm_label() {
        return "average: " + this.db_helper.get_cumulative_avg();
    }


    @Override
    public void br_clicked() {
        // add a semester
        Intent i = new Intent( this, SemesterInfoActivity.class );
        startActivity(i);
    }

    //
    // list itemonclick listener class def
    //
    private class SemesterListItemOnClickListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        @Override
        public void onItemClick( AdapterView<?> p, View v, int i , long id) {
            // create an intent to view a semester's info
            Intent intent = new Intent( SemesterListActivity.this, CourseListActivity.class );
            Semester s = semesters.get(i);
            intent.putExtra("semester_id", s.id);
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick( AdapterView<?> p, View v, int i , long id) {
            // create an intent to view a semester's info
            Intent intent = new Intent( SemesterListActivity.this, SemesterInfoActivity.class );
            Semester s = semesters.get(i);
            intent.putExtra("semester_id", s.id);
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
        this.setContentView(R.layout.activity_semester_list);

        // model
        this.db_helper = new BreadGraderSQLHelper(SemesterListActivity.this);

        // ui
        this.getListView().setOnItemLongClickListener( new SemesterListItemOnClickListener() );
        this.getListView().setOnItemClickListener( new SemesterListItemOnClickListener() );
    }

    @Override
    public void update() {
        super.update();

        // refeshing the list of semesters
        this.semesters = db_helper.get_all_semesters(true);

        // make an adapter
        SemesterArrayAdapter adapter = new SemesterArrayAdapter( this, R.layout.semester_list_item, this.semesters );

        // set the adapter
        this.setListAdapter( adapter );
    }
}
