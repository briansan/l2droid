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
import com.breadtech.breadgrader.model.Course;
import com.breadtech.breadgrader.model.Criteria;


import java.util.ArrayList;

/**
 * @brief use this adapter to hook up to a list view
 */
public class CriteriaArrayAdapter extends ArrayAdapter<Criteria>
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
    public CriteriaArrayAdapter( Context ctxt, int res, ArrayList<Criteria> items) {
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
        Criteria course = getItem(pos);

        // if the view doesn't exist yet...
        if (v == null) {
            // inflate the view from the R.layout for the player cell
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.criteria_list_item, p, false);

            // make the cell
            cell = new Cell();

            // instantiating pointers to the cell component fields
            cell.title_label = (TextView)v.findViewById(R.id.title_label);
            cell.grade_label = (TextView)v.findViewById(R.id.grade_label);

            // ? setting the tag i guess...
            v.setTag( cell );
        } else {
            // setting the cell to be the tag of v
            cell = (Cell)v.getTag();
        }

        // set the label text to the strings
        String g_s = course.avg == null ? "NG" : course.avg.toString();
        cell.title_label.setText(course.type + " (" + course.weight + "%)");
        cell.grade_label.setText(g_s);

        // return the view
        return v;
    }
}
