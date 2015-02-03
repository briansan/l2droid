package com.breadtech.breadgrader.ui;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.breadtech.breadgrader.R;
import com.breadtech.breadgrader.model.BreadGraderSQLHelper;
import com.breadtech.breadgrader.model.Course;
import com.breadtech.breadgrader.model.Criteria;
import com.breadtech.breadgrader.model.Semester;
import com.breadtech.breadinterface.BIActivity;

public class CriteriaInfoActivity extends BIActivity {

    //
    // constants
    //
    public static int CRITERIA_SAVED = 1;

    //
    // instance variables
    //

    // model
    BreadGraderSQLHelper db_helper;
    Criteria criteria;
    int criteria_id;
    int course_id;

    // ui
    EditText type_et, weight_et;

    //
    // utility methods
    //

    public boolean check_fields() {
        boolean y = true;
        if (type_et.getText().toString().length() == 0) {
            type_et.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Missing Field: type", Toast.LENGTH_SHORT).show();
            y = false;
        } else if (weight_et.getText().toString().length() == 0) {
            weight_et.setBackgroundColor(Color.RED);
            Toast.makeText( this, "Missing Field: weight", Toast.LENGTH_SHORT).show();
            y = false;
        }
        return y;
    }

    public void save() {
        if (!this.check_fields()) return;
        // update the model
        this.criteria.type = this.type_et.getText().toString();
        this.criteria.weight= Integer.parseInt(this.weight_et.getText().toString());
        if (this.criteria.course == null) {
            this.criteria.course = this.db_helper.get_course(this.course_id,false);
        }
        // ask the db to save it
        this.db_helper.set_criteria(this.criteria);

        finish();
    }

    public void delete() {
        this.db_helper.delete_criteria(this.criteria);
    }

    //
    // bi buttons
    //

    @Override
    public String tm_label() {
        return this.criteria_id != -1 ? "Criteria Info" : "Add Criteria" ;
    }

    @Override
    public void tl_clicked() { finish(); }
    // cancel

    @Override
    public void tr_clicked() { save();  }
    // save

    @Override
    public void br_clicked() {
        this.delete();
        finish();
    }

    //
    // bi lifecycle
    //
    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_criteria_info);

        // model
        this.db_helper = new BreadGraderSQLHelper(this);
        this.criteria_id = getIntent().getIntExtra("criteria_id",-1);
        this.course_id = getIntent().getIntExtra("course_id",-1); // -1 means add

        // ui
        this.type_et = (EditText)this.findViewById(R.id.type_et);
        this.weight_et = (EditText)this.findViewById(R.id.weight_et);

        // removing the bottom bar if adding semester
        if (this.criteria_id == -1) {
            LinearLayout bottom_bar = (LinearLayout) this.findViewById(R.id.bottom_bar);
            Button bl = (Button)this.findViewById(R.id.bl);
            Button br = (Button)this.findViewById(R.id.br);
            bottom_bar.removeView(bl);
            bottom_bar.removeView(br);
        }
    }

    @Override
    public void resume() {
        super.resume();


        // get the semester
        this.criteria = this.db_helper.get_criteria(criteria_id,false);

        if (this.criteria == null) {
            this.criteria = new Criteria();
        }
        else {
            // set the ui
            this.type_et.setText(this.criteria.type);
            this.weight_et.setText("" + this.criteria.weight);
        }
    }

}
