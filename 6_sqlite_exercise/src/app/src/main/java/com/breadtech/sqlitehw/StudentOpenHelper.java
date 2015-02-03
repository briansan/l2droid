package com.breadtech.sqlitehw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

/**
 * Created by bk on 10/20/14.
 */
public class StudentOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "student.db";
    private static final int DB_VERSION = 1;
    private static final String TB_STUDENT = "student";
    private static final String S_ID = BaseColumns._ID; // _id
    public static final String S_NAME = "name";
    public static final String S_MAJOR = "major";
    public static final String S_YEAR = "year";
    public static final String[] TB_COLS = new String[] {S_ID, S_NAME, S_MAJOR, S_YEAR};


    StudentOpenHelper(Context ctxt) {
        super(ctxt, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE  " + TB_STUDENT + " (" +
                        S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        S_NAME + " TEXT NOT NULL, " +
                        S_MAJOR + " TEXT NOT NULL, " +
                        S_YEAR + " INTEGER NOT NULL" + ");"
        );

        // insert a sample student
        ContentValues cv = new ContentValues();
        cv.put( S_NAME, "Brian Kim");
        cv.put( S_MAJOR, "Computer Engineering" );
        cv.put( S_YEAR, 2015 );
        sqLiteDatabase.insert( TB_STUDENT, null, cv );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {

    }

    public Cursor getStudents()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query( TB_STUDENT, TB_COLS, null, null, null, null, S_NAME );
    }

    public Cursor getStudent(int id)
    {
        String sel = S_ID + "=?";
        String[] selArgs = new String[] { String.valueOf(id) };
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query( TB_STUDENT, TB_COLS, sel, selArgs, null, null, null );
    }

    public long numStudents()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement stmt = db.compileStatement( "SELECT COUNT(1) " + "FROM " + TB_STUDENT + ";");
        return stmt.simpleQueryForLong();
    }

    public long addStudent( String name, String major, int year )
    {
        ContentValues cv = new ContentValues();
        cv.put( S_NAME, name );
        cv.put( S_MAJOR, major );
        cv.put( S_YEAR, year );
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert( TB_STUDENT, null, cv );
    }

    public boolean delStudent( int id )
    {
        String sel = S_ID + "=?";
        String[] selArgs = new String[] { String.valueOf( id ) };
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(TB_STUDENT, sel, selArgs) > 0);
    }

}
