package com.breadtech.breadgrader.model;

import java.util.ArrayList;

/**
 * Created by bk on 12/13/14.
 */
public class Grader {
    public static Grade avg( ArrayList<Grade> grades ) {
        double rcv, max;
        rcv = max = 0;

        for (Grade x : grades) {
            if (x.isGraded()) {
                rcv += x.received();
                max += x.max();
            }
        }

        return new Grade( rcv, max );
    }

    public static Grade weighted_avg( ArrayList<Grade> grades, ArrayList<Double> weights ) {
        double rcv, max;
        rcv = max = 0;

        int n = grades.size();
        for (int i = 0; i < n; i++) {
            Grade x = grades.get(i);
            if (x.isGraded()) {
                Double w = weights.get(i);
                rcv += x.received()*w;
                max += x.max()*w;
            }
        }

        return new Grade( rcv, max );
    }
}
