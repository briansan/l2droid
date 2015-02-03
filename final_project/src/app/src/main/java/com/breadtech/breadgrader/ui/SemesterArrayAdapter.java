package com.breadtech.breadgrader.ui;

/**
 * Created by bk on 12/12/14.
 */

//======================
// view
//======================


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.breadtech.breadgrader.R;
import com.breadtech.breadgrader.model.Semester;


import java.util.ArrayList;

/**
 * @brief use this adapter to hook up to a list view
 */
public class SemesterArrayAdapter extends ArrayAdapter<Semester>
{

    /**
     * @brief Semester cell class definition
     */
    private static class Cell {
        private TextView title_label;
        private TextView grade_label;
    }

    /**
     * @brief constructor
     * @param ctxt the Activity
     * @param res the R.layout that contains the player cell components
     * @param items the list of semesters that the adapter will be processing
     */
    public SemesterArrayAdapter( Context ctxt, int res, ArrayList<Semester> items) {
        super( ctxt, res, items );
    }

    /**
     * @brief the view that contains the cell components
     * @param pos the index of the cell in the list
     * @param v the return variable that will have semester data in it
     * @param p the parent of this cell
     * @return v
     */
    public View getView( int pos, View v, ViewGroup p ) {

        // make a cell
        Cell cell;
        // get the player
        Semester semester = getItem(pos);

        // if the view doesn't exist yet...
        if (v == null) {
            // inflate the view from the R.layout for the player cell
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.semester_list_item, p, false);

            // make the cell
            cell = new Cell();

            // instantiating pointers to the cell component fields
            cell.title_label = (TextView)v.findViewById(R.id.semester_title_label);
            cell.grade_label = (TextView)v.findViewById(R.id.grade_label);

            // ? setting the tag i guess...
            v.setTag( cell );
        } else {
            // setting the cell to be the tag of v
            cell = (Cell)v.getTag();
        }

        // create the strings to display on the cell
        String title = semester.season + " " + semester.year;

        // set the label text to the strings
        String g_s = semester.avg == null ? "NG" : semester.avg.toString();
        cell.title_label.setText(title);
        cell.grade_label.setText(g_s);

        // return the view
        return v;
    }
}
