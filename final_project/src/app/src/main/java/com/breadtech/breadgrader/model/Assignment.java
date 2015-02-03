package com.breadtech.breadgrader.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bk on 9/25/14.
 */
public class Assignment
{
    //
    // keys (used to store in sqlite db)
    //
    protected final static String
            TB_ASSIGNMENT = "assignments",
            S_ID = BaseColumns._ID, CRITERIA_ID = "criteria_id", NAME  = "name", INDEX = "i",
            DUE  = "due" , RECEIVED = "received", MAX = "max", NOTES = "notes";

    //
    // instance variables
    //

    // relational properties
    public int id;
    public Criteria criteria;

    // identifying properties
    public String name;
    public int index;

    // quantifying properties
    public Date due;
    public Grade grade;
    public String notes;

    //
    // constructors
    //
    public Assignment()
    { this("Assignment"); }

    public Assignment( String name )
    { this( name, 1 ); }

    public Assignment( String name, int i )
    { this( name, i, new Date() ); }

    public Assignment( String name, int i, Date due )
    { this( name, i, due, new Grade() ); }

    public Assignment( String name, int i, Date due, Grade grade )
    { this( name, i, due, grade, "" ); }

    public Assignment( String name, int i, Date due, Grade grade, String notes )
    { this( name, i, due, grade, notes, null); }

    public Assignment( String name, int i, Date due, Grade grade, String notes, Criteria cr )
    { this( name, i, due, grade, notes, cr, -1); }

    public Assignment( String name, int i, Date due, Grade grade, String notes, Criteria cr, int id )
    {
        // nominal properties
        this.name = name; this.index = i;

        // quantifying properties
        this.due = due; this.grade = grade; this.notes = notes;

        // relational properties
        this.id = id; this.criteria = cr; }

    //===========================================
    // instance methods
    //===========================================

    //
    // utility methods
    //
    public ContentValues contentValues()
    {
        // creating the content value object to store all the values to insert
        ContentValues cv = new ContentValues();
        cv.put( NAME, this.name );
        cv.put(INDEX, this.index);
        cv.put(DUE, this.due.getTime());
        cv.put(RECEIVED, this.grade.received());
        cv.put(MAX, this.grade.max());
        cv.put(NOTES, this.notes);
        cv.put( CRITERIA_ID, this.criteria.id );
        return cv;
    }


    //=================================================
    // static methods
    //=================================================

    //
    // sqlite manipulations
    //

    //
    // creating the table
    public static final void create_table( SQLiteDatabase db ) {
        db.execSQL("CREATE TABLE  " + TB_ASSIGNMENT + " (" +
                        S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        Assignment.CRITERIA_ID + " INTEGER NOT NULL, " +
                        Assignment.INDEX + " INTEGER NOT NULL, " +
                        Assignment.NAME + " TEXT NOT NULL, " +
                        Assignment.RECEIVED + " INTEGER NOT NULL," +
                        Assignment.MAX + " REAL NOT NULL," +
                        Assignment.DUE + " REAL NOT NULL," +
                        Assignment.NOTES + " STRING NOT NULL );"
        );
    }

    public static final ArrayList<Assignment> get_assignments_for_criteria( SQLiteDatabase db, Criteria x )
    {
        //
        // columns
        String[] cols = { NAME, INDEX, DUE, RECEIVED, MAX, NOTES, S_ID, CRITERIA_ID };

        // create the selection statement and arguments
        String sel = CRITERIA_ID +"=?";
        String[] sel_args = new String[] { String.valueOf(x.id) };;

        //
        // get all semesters
        Cursor cursor = db.query( TB_ASSIGNMENT, cols, sel, sel_args, null, null, null );

        // make the array of assignments
        ArrayList<Assignment> y = new ArrayList<Assignment>( cursor.getCount() );

        // make the cursor point to the first el
        cursor.moveToFirst();

        // iterate through all the rows and create semesters
        for (int i = 0; i < cursor.getCount(); i++) {
            Criteria cr = Criteria.get_criteria(db,cursor.getInt(7),false);
            // name                 index             due
            Assignment a = new Assignment( cursor.getString(0), cursor.getInt(1), new Date(cursor.getInt(2)),

                    // grade      received             max                    notes
                    new Grade( cursor.getDouble(3), cursor.getDouble(4) ), cursor.getString(5),

                    // criteria_id                id
                    cr, cursor.getInt(6) );
            y.add(a);
            cursor.moveToNext();
        }

        // cleanup the cursor
        cursor.close();


        return y;
    }

    //
    // get assignment by id
    public static final Assignment get_assignment( SQLiteDatabase db, int id )
    {
        // define the columns to received from the cursor
        String[] cols = { NAME, INDEX, DUE, RECEIVED, MAX, NOTES, S_ID, CRITERIA_ID };

        // create the selection statement and arguments
        String sel = S_ID+"=?";
        String[] sel_args = new String[] { String.valueOf(id) };;

        // get the cursor
        Cursor cursor = db.query( TB_ASSIGNMENT, cols, sel, sel_args, null, null, null);

        // creating the return variable
        Assignment y;

        cursor.moveToFirst();
        // make sure we have a match
        if (cursor.getCount() > 0)
        {
            Criteria cr = Criteria.get_criteria(db,cursor.getInt(7),false);
            // name                 index             due
            y = new Assignment( cursor.getString(0), cursor.getInt(1), new Date(cursor.getInt(2)),

                    // grade      received             max                    notes
                    new Grade( cursor.getDouble(3), cursor.getDouble(4) ), cursor.getString(5),

                    // criteria_id                id
                    cr, cursor.getInt(6) );
            cursor.moveToNext();
        } else y = null;

        // cleanup the cursor
        cursor.close();

        // either return the Assignment constructed by cursor values or null
        return y;
    }


    //
    // modifies the values of an assignment. id cannot equal -1
    public static final int set_assignment( SQLiteDatabase db, Assignment assignment )
    {
        // creating the content value object to store all the values to insert
        ContentValues cv = assignment.contentValues();

        int y;
        // add a semester
        if (assignment.id == -1) {
            // send the command to the db to add the assignment defined by cv
            // returns number of entries inserted (should be 1)
            y = (int)db.insert(TB_ASSIGNMENT, null, cv);
        }
        else {
            // update the semester
            // defining the id as a String
            String id_s = "" + assignment.id;

            // send the command to the db to update the semester defined by cv
            // selected by the semester's id
            y = db.update(TB_ASSIGNMENT, cv, S_ID + "=?", new String[]{id_s});
        }

        // send the command to the db to update the assignment defined by cv
        // selected by the assignment's id
        return y;
    }

    //
    // deletes an assignment from a sqldatabase
    public static final boolean delete_assignment( SQLiteDatabase db, Assignment assignment )
    {
        // calling delete into the db given an id and return whether or not it is greater than 0
        boolean y = db.delete( TB_ASSIGNMENT, S_ID+"=?", new String[] {""+assignment.id}) > 0;
        return y;
    }

}
