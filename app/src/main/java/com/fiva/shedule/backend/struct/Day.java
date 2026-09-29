package com.fiva.shedule.backend.struct;

import java.util.ArrayList;

public class Day {

    private String date;
    private ArrayList<Lesson> lessons = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }



}
