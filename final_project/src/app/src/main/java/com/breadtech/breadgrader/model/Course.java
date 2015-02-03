package com.breadtech.breadgrader.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by bk on 12/12/14.
 */
public class Course {

    //
    // keys (used to store in sqlite db)
    //
    protected final static String
            TB_COURSE = "course",
            S_ID = BaseColumns._ID,
            SEMESTER = "semester",
            TITLE = "title",
            SUBJECT = "subject";

    //=============================================
    // instance methods
    //=============================================

    //
    // constructors
    //
    public Course() { this( "Calculus" ); }
    public Course( String title ) { this( title, "Math" ); }
    public Course( String title, String subject ) { this( title, subject, null ); }
    public Course( String title, String subject, Semester semester ) { this( title, subject, semester, -1 ); }
    public Course( String title, String subject, Semester semester, int id )
    {
        this.title = title;
        this.subject = subject;
        this.semester = semester;
        this.id = id;
    }

    //
    // instance variables
    //
    public int id;
    public Semester semester;
    public String title;
    public String subject;
    public Grade avg;

    //
    // utility methods
    //
    public ContentValues contentValues()
    {
        // creating the content value object to store all the values to insert
        ContentValues cv = new ContentValues();
        cv.put( TITLE, this.title );
        cv.put( SUBJECT, this.subject );
        cv.put( SEMESTER, this.semester.id );
        return cv;
    }

    @Override
    public String toString()
    {
        String y = "";
        int n = this.title.length();
        if (n > 15) {
            y += this.title.charAt(0);
            boolean getNext = false;
            for (int i = 0; i < n; i++) {
                if (getNext) {
                    getNext = false;
                    y += this.title.charAt(i);
                }
                if (this.title.charAt(i) == ' ') {
                    getNext = true;
                }
            }
        }
        else y = this.title;
        return y;
    }

    //=============================================
    // static methods
    //=============================================

    //
    // db methods
    //

    public static final void create_table( SQLiteDatabase db ) {
        db.execSQL("CREATE TABLE  " + TB_COURSE + " (" +
                S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                TITLE + " TEXT NOT NULL, " +
                SUBJECT + " TEXT NOT NULL," +
                SEMESTER + " INTEGER NOT NULL);" );

    }

    //
    // get semester by id
    public static final Course get_course( SQLiteDatabase db, int id, boolean graded )
    {
        // define the columns to received from the cursor
        String[] cols = { S_ID, TITLE, SUBJECT, SEMESTER };

        // create the selection statement and arguments
        String sel = S_ID+"=?";
        String[] sel_args = new String[] { String.valueOf(id) };;

        // get the cursor
        Cursor cursor = db.query( TB_COURSE, cols, sel, sel_args, null, null, null);

        // creating the return variable
        Course y;

        // make sure we have a match
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            // get the semester
            Semester s = Semester.get_semester( db, cursor.getInt(3) );

                            // title             subject        semester  id
            y = new Course( cursor.getString(1), cursor.getString(2), s, id );

            if (graded)
                y.avg = Course.get_course_avg( db, y );

        } else y = null;

        // cleanup
        cursor.close();

        // either return the Semester constructed by cursor values or null
        return y;
    }


    //
    // modifies the values of an semester. id cannot equal -1
    public static final int set_course( SQLiteDatabase db, Course course )
    {
        // creating the content value object to store all the values to insert
        ContentValues cv = course.contentValues();

        int y;

        // add a semester
        if (course.id == -1) {
            // send the command to the db to add the assignment defined by cv
            // returns number of entries inserted (should be 1)
            y = (int)db.insert(TB_COURSE, null, cv);
        }
        else {
            // update the semester
            // defining the id as a String
            String id_s = "" + course.id;

            // send the command to the db to update the semester defined by cv
            // selected by the semester's id
            y = db.update(TB_COURSE, cv, S_ID + "=?", new String[]{id_s});
        }


        return y;
    }

    //
    // deletes an assignment from a sqldatabase
    public static final boolean delete_course( SQLiteDatabase db, Course course )
    {
        // calling delete into the db given an id and return whether or not it is greater than 0
        boolean y = db.delete( TB_COURSE, S_ID+"=?", new String[] {""+course.id}) > 0;
        return y;
    }

    public static final ArrayList<Course> get_courses_for_semester( SQLiteDatabase db, Semester s, boolean graded )
    {
        //
        // columns
        String[] cols = new String[] { TITLE, SUBJECT, SEMESTER, S_ID };

        // create the selection statement and arguments
        String sel = SEMESTER+"=?";
        String[] sel_args = new String[] { String.valueOf(s.id) };;

        //
        // get all semesters
        Cursor cursor = db.query( TB_COURSE, cols, sel, sel_args, null, null, null );

        // make the array of semesters
        ArrayList<Course> y = new ArrayList<Course>( cursor.getCount() );

        // make the cursor point to the first el
        cursor.moveToFirst();

        // iterate through all the rows and create semesters
        for (int i = 0; i < cursor.getCount(); i++) {
            Course c = new Course( cursor.getString(0), cursor.getString(1), s, cursor.getInt(3));
            if (graded)
                c.avg = Course.get_course_avg( db, c );
            y.add( c );
            cursor.moveToNext();
        }

        // cleanup the cursor
        cursor.close();

        return y;
    }

    public static final Grade get_course_avg( SQLiteDatabase db, Course c ) {

        // get the criterion for the course
        ArrayList<Criteria> criterion = Criteria.get_criterion_for_course( db, c, true );

        ArrayList<Grade> grades = new ArrayList<Grade>( criterion.size() );
        ArrayList<Double> weights = new ArrayList<Double>( criterion.size() );

        for (Criteria cr : criterion) {
            grades.add(cr.avg);
            weights.add((double)cr.weight/100);
        }

        return Grader.avg(grades);
    }

}
