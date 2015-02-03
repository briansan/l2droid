package com.breadtech.breadgrader.model;

/**
 * Created by bk on 9/25/14.
 */

public class Grade
{
    //
    // instance variables
    //
    protected double _received;
    protected double _max;

    //
    // constructor
    //
    public Grade() { this( 100 ); }
    public Grade( double max ) { this( -1, 100); }
    public Grade( double received, double max ) { this.received(received); this.max(max); }

    //
    // accessor methods
    //

    //
    // getters
    public double max() { return _max; }
    public double received() { return _received; }
    public boolean isGraded() { return _received > 0; }
    public double grade() { return _received/_max; }

    @Override
    public String toString() {
        String y;
        if (isGraded()) {
            y = String.format( "%.2f%%", this.grade()*100 );
        }
        else {
            y = "NG";
        }
        return y;
    }

    //
    // setters
    public void max( double x ) { _max = x; }
    public void received( double x ) { _received = x; }
}
