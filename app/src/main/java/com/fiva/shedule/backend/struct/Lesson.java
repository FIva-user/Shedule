package com.fiva.shedule.backend.struct;

public class Lesson {
    private String number;
    //private String time;
    private String timeStart;
    private String timeEnd;
    private String name;
    private String classNumber;
    private String teacher;

    public Lesson(){}

    public Lesson(String number, String timeStart, String timeEnd, String name, String classNumber, String teacher) {
        this.number = number;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.name = name;
        this.classNumber = classNumber;
        this.teacher = teacher;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public String getTime() {
        return time;
    }*/

    /*public void setTime(String time) {
        this.time = time;
    }*/

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
