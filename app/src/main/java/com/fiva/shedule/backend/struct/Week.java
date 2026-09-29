package com.fiva.shedule.backend.struct;

import java.util.ArrayList;

public class Week {
    private ArrayList<Day> week;

    public Week() {}

    public Week(ArrayList<Day> week) {
        this.week = week;
    }
    public ArrayList<Day> getWeek() {
        return week;
    }

    public void setWeek(ArrayList<Day> week) {
        this.week = week;
    }
}
