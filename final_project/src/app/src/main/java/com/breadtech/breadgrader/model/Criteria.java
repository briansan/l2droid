package com.breadtech.breadgrader.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by bk on 12/12/14.
 */
public class Criteria {

    //
    // keys (used to store in sqlite db)
    //
    protected final static String
            TB_CRITERIA = "criteria",
            S_ID = BaseColumns._ID,
            COURSE = "course",
            TYPE = "type",
            WEIGHT = "weight";

    //=============================================
    // instance methods
    //=============================================

    //
    // constructors
    //
    public Criteria() { this( "Test" ); }
    public Criteria( String type) { this( type, 20 ); }
    public Criteria( String type, int weight ) { this( type, weight, null ); }
    public Criteria( String type, int weight, Course course ) { this( type, weight, course, -1 ); }
    public Criteria( String type, int weight, Course course, int id )
    {
        this.course = course;
        this.type = type;
        this.weight = weight;
        this.id = id;
    }

    //
    // instance variables
    //
    public int id;
    public Course course;
    public String type;
    public int weight;
    public Grade avg;

    //
    // utility methods
    //
    public ContentValues contentValues()
    {
        // creating the content value object to store all the values to insert
        ContentValues cv = new ContentValues();
        cv.put(TYPE, this.type );
        cv.put(WEIGHT, this.weight );
        cv.put(COURSE, this.course.id );
        return cv;
    }

    @Override
    public String toString() {
        return this.type + " (" + this.weight + "%)";
    }
    //=============================================
    // static methods
    //=============================================

    //
    // db methods
    //

    public static final void create_table( SQLiteDatabase db ) {
        db.execSQL("CREATE TABLE  " + TB_CRITERIA + " (" +
                S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                TYPE + " TEXT NOT NULL, " +
                WEIGHT + " INTEGER NOT NULL," +
                COURSE + " INTEGER NOT NULL);" );

    }

    //
    // get semester by id
    public static final Criteria get_criteria( SQLiteDatabase db, int id, boolean graded )
    {
        // define the columns to received from the cursor
        String[] cols = { S_ID, TYPE, WEIGHT, COURSE};

        // create the selection statement and arguments
        String sel = S_ID+"=?";
        String[] sel_args = new String[] { String.valueOf(id) };;

        // get the cursor
        Cursor cursor = db.query(TB_CRITERIA, cols, sel, sel_args, null, null, null);

        // creating the return variable
        Criteria y;

        // make sure we have a match
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            // get the course
            Course s = Course.get_course( db, cursor.getInt(3), false );

            // type | weight | course | id
            y = new Criteria( cursor.getString(1), cursor.getInt(2), s, id );
            if (graded)
                y.avg = Criteria.get_criteria_avg( db, y );
        } else y = null;

        // cleanup
        cursor.close();


        // either return the Semester constructed by cursor values or null
        return y;
    }


    //
    // modifies the values of an semester. id cannot equal -1
    public static final int set_criteria( SQLiteDatabase db, Criteria x )
    {
        // creating the content value object to store all the values to insert
        ContentValues cv = x.contentValues();

        int y;

        // add a semester
        if (x.id == -1) {
            // send the command to the db to add the assignment defined by cv
            // returns number of entries inserted (should be 1)
            y = (int)db.insert(TB_CRITERIA, null, cv);
        }
        else {
            // update the semester
            // defining the id as a String
            String id_s = "" + x.id;

            // send the command to the db to update the semester defined by cv
            // selected by the semester's id
            y = db.update(TB_CRITERIA, cv, S_ID + "=?", new String[]{id_s});
        }


        return y;
    }

    //
    // deletes an assignment from a sqldatabase
    public static final boolean delete_criteria( SQLiteDatabase db, Criteria x )
    {
        // calling delete into the db given an id and return whether or not it is greater than 0
        boolean y = db.delete(TB_CRITERIA, S_ID+"=?", new String[] {""+x.id}) > 0;
        return y;
    }

    public static final ArrayList<Criteria> get_criterion_for_course( SQLiteDatabase db, Course x, boolean graded )
    {
        //
        // columns
        String[] cols = new String[] {TYPE, WEIGHT, COURSE, S_ID };

        // create the selection statement and arguments
        String sel = COURSE +"=?";
        String[] sel_args = new String[] { String.valueOf(x.id) };;

        //
        // get all semesters
        Cursor cursor = db.query(TB_CRITERIA, cols, sel, sel_args, null, null, null );

        // make the array of semesters
        ArrayList<Criteria> y = new ArrayList<Criteria>( cursor.getCount() );

        // make the cursor point to the first el
        cursor.moveToFirst();

        // iterate through all the rows and create semesters
        for (int i = 0; i < cursor.getCount(); i++) {
            Criteria cr = new Criteria( cursor.getString(0), cursor.getInt(1), x, cursor.getInt(3));
            if (graded)
                cr.avg = Criteria.get_criteria_avg( db, cr );
            y.add( cr );
            cursor.moveToNext();
        }

        // cleanup the cursor
        cursor.close();

        return y;
    }

    public static final Grade get_criteria_avg( SQLiteDatabase db, Criteria cr ) {
        //
        // get all assignments for cr
        ArrayList<Assignment> assignments = Assignment.get_assignments_for_criteria( db, cr );

        // create the proper variables
        int n = assignments.size();

        // grades
        ArrayList<Grade> grades = new ArrayList<Grade>(n);

        for (Assignment x : assignments) {
            grades.add( x.grade );
        }

        return Grader.avg(grades);
    }

}
