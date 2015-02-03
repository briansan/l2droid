package com.breadtech.breadgrader.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by bk on 12/12/14.
 */
public class BreadGraderSQLHelper extends SQLiteOpenHelper {

    //
    // constants
    //
    private static final String DB_NAME = "grader.db";
    private static final int DB_VERSION = 1;

    // constructor
    public BreadGraderSQLHelper(Context ctxt) {
        super(ctxt, DB_NAME, null, DB_VERSION);
    }

    //
    // get avgs
    //
    public Grade get_cumulative_avg() { return Semester.get_cumulative_avg( getReadableDatabase() ); }
    public Grade get_semester_avg( Semester x ) { return Semester.get_semester_avg( getReadableDatabase(),x );}
    public Grade get_course_avg( Course x ) { return Course.get_course_avg( getReadableDatabase(),x );}
    public Grade get_criteria_avg( Criteria x ) { return Criteria.get_criteria_avg( getReadableDatabase(),x );}

    //
    // instance db crud methods
    //

    //
    // semester
    //

    public ArrayList<Semester> get_all_semesters( boolean graded )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Semester> y = Semester.get_all_semesters(db,graded);
        db.close();
        return y;
    }

    public Semester get_semester( int id )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Semester y = Semester.get_semester( db, id );
        db.close();
        return y;
    }

    public int set_semester( Semester x )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int y = Semester.set_semester(db, x);
        db.close();
        return y;
    }

    public boolean delete_semester( Semester x ) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean y = Semester.delete_semester( db, x );
        db.close();
        return y;
    }

    //
    // course
    //

    public ArrayList<Course> get_courses_for_semester( Semester s, boolean graded )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList y = Course.get_courses_for_semester(db,s,graded);
        db.close();
        return y;
    }

    public Course get_course( int id, boolean graded )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Course y = Course.get_course( db, id,graded );
        db.close();
        return y;
    }

    public int set_course( Course x )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int y = Course.set_course( db, x );
        db.close();
        return y;
    }

    public boolean delete_course( Course x ) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean y = Course.delete_course( db, x);
        db.close();
        return y;
    }

    //
    // criteria
    //

    public ArrayList<Criteria> get_criterion_for_course( Course x, boolean graded )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList y = Criteria.get_criterion_for_course(db,x,graded);

        db.close();
        return y;
    }

    public Criteria get_criteria( int id, boolean graded )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Criteria y = Criteria.get_criteria( db, id,graded );

        db.close();
        return y;
    }

    public int set_criteria( Criteria x )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int y = Criteria.set_criteria( db, x );
        db.close();
        return y;
    }

    public boolean delete_criteria( Criteria x ) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean y = Criteria.delete_criteria( db, x);
        db.close();
        return y;
    }

    //
    // assignment
    //

    public ArrayList<Assignment> get_assignments_for_criteria( Criteria x )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList y = Assignment.get_assignments_for_criteria(db,x);
        db.close();
        return y;
    }

    public Assignment get_assignment( int id )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Assignment y = Assignment.get_assignment( db, id );
        db.close();
        return y;
    }

    public int set_assignment( Assignment x )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int y = Assignment.set_assignment( db, x );
        db.close();
        return y;
    }

    public boolean delete_assignment( Assignment x ) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean y = Assignment.delete_assignment( db, x);
        db.close();
        return y;
    }


    //
    // sqlite open helper method overrides
    //
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Assignment.create_table( db );
        Semester.create_table( db );
        Course.create_table( db );
        Criteria.create_table( db );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {

    }
}
