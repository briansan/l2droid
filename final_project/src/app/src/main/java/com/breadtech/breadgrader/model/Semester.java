package com.breadtech.breadgrader.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
/**
 * Created by bk on 12/10/14.
 */
public class Semester
{
    //
    // keys (used to store in sqlite db)
    //
    protected final static String
            TB_SEMESTER = "semester",
            S_ID = BaseColumns._ID, SEASON  = "season", YEAR = "year";

    //=============================================
    // instance methods
    //=============================================

    //
    // constructors
    //
    public Semester() { this( "Fall", 2014 ); }
    public Semester( String season ) { this( season, 2014 ); }
    public Semester( String season, int year ) { this( season, year, -1 ); }
    public Semester( String season, int year, int id )
    {
        this.season = season;
        this.year = year;
        this.id = id;
    }

    //
    // instance variables
    //
    public int id;
    public String season;
    public int year;
    public Grade avg;

    //
    // utility methods
    //
    public ContentValues contentValues()
    {
        // creating the content value object to store all the values to insert
        ContentValues cv = new ContentValues();
        cv.put( SEASON, this.season );
        cv.put( YEAR, this.year );
        return cv;
    }

    //=============================================
    // static methods
    //=============================================

    //
    // db methods
    //

    public static final void create_table( SQLiteDatabase db ) {
        db.execSQL("CREATE TABLE  " + TB_SEMESTER + " (" +
                        S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        SEASON + " TEXT NOT NULL, " +
                        YEAR + " INTEGER NOT NULL);" );

    }

    //
    // get semester by id
    public static final Semester get_semester( SQLiteDatabase db, int id )
    {
        // define the columns to received from the cursor
        String[] cols = { S_ID, SEASON, YEAR };

        // create the selection statement and arguments
        String sel = S_ID+"=?";
        String[] sel_args = new String[] { String.valueOf(id) };;

        // get the cursor
        Cursor cursor = db.query( TB_SEMESTER, cols, sel, sel_args, null, null, null);

        // creating the return variable
        Semester y;

        // make sure we have a match
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            // name                 season             year          id
            y = new Semester( cursor.getString(1), cursor.getInt(2), id );
        } else y = null;

        // cleanup
        cursor.close();

        // either return the Semester constructed by cursor values or null
        return y;
    }


    //
    // modifies the values of an semester. id cannot equal -1
    public static final int set_semester( SQLiteDatabase db, Semester semester )
    {
        // creating the content value object to store all the values to insert
        ContentValues cv = semester.contentValues();

        int y;

        // add a semester
        if (semester.id == -1) {
            // send the command to the db to add the assignment defined by cv
            // returns number of entries inserted (should be 1)
            y = (int)db.insert(TB_SEMESTER, null, cv);
        }
        else {
            // update the semester
            // defining the id as a String
            String id_s = "" + semester.id;

            // send the command to the db to update the semester defined by cv
            // selected by the semester's id
            y = db.update(TB_SEMESTER, cv, S_ID + "=?", new String[]{id_s});
        }


        return y;
    }

    //
    // deletes an assignment from a sqldatabase
    public static final boolean delete_semester( SQLiteDatabase db, Semester semester )
    {
        // calling delete into the db given an id and return whether or not it is greater than 0
        boolean y = db.delete( TB_SEMESTER, S_ID+"=?", new String[] {""+semester.id}) > 0;
        return y;
    }

    public static final ArrayList<Semester> get_all_semesters( SQLiteDatabase db, boolean graded )
    {
        //
        // columns
        String[] cols = new String[] {  SEASON, YEAR, S_ID };

        //
        // get all semesters
        Cursor cursor = db.query( TB_SEMESTER, cols, null, null, null, null, null );

        // make the array of semesters
        ArrayList<Semester> y = new ArrayList<Semester>( cursor.getCount() );

        // make the cursor point to the first el
        cursor.moveToFirst();

        // iterate through all the rows and create semesters
        for (int i = 0; i < cursor.getCount(); i++) {
            Semester sms = new Semester( cursor.getString(0), cursor.getInt(1), cursor.getInt(2));
            if (graded)
                sms.avg = Semester.get_semester_avg( db, sms );
            y.add( sms );
            cursor.moveToNext();
        }

        // cleanup the cursor
        cursor.close();

        return y;
    }



    public static final Grade get_semester_avg( SQLiteDatabase db, Semester c ) {

        // get the criterion for the course
        ArrayList<Course> courses = Course.get_courses_for_semester( db, c, true );

        ArrayList<Grade> grades = new ArrayList<Grade>( courses.size() );
        for (Course cr : courses) {
            grades.add( cr.avg );
        }

        return Grader.avg(grades);
    }

    public static final Grade get_cumulative_avg( SQLiteDatabase db ) {

        // get the criterion for the course
        ArrayList<Semester> semesters = Semester.get_all_semesters( db, true );

        ArrayList<Grade> grades = new ArrayList<Grade>( semesters.size() );
        for (Semester cr : semesters) {
            grades.add( cr.avg );
        }

        return Grader.avg(grades);
    }
}
