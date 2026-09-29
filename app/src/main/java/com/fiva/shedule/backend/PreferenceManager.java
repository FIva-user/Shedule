package com.fiva.shedule.backend;

import android.content.Context;
import android.content.SharedPreferences;

import com.fiva.shedule.backend.struct.Group;
import com.fiva.shedule.backend.struct.Week;
import com.google.gson.Gson;

public class PreferenceManager {

    private static final String PREF_NAME = "schedule";
    private String keyGroup = "group";
    private String keyWeek = "week";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;


    public PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void setGroup(Group group){
        String json = gson.toJson(group);
        sharedPreferences.edit().putString(keyGroup, json).apply();
    }
    public Group getGroup() {
        String json = sharedPreferences.getString(keyGroup, null);
        if (json == null) {
            return null;
        } else {
            return gson.fromJson(json, Group.class);
        }
    }

    public void setWeek(Week week) {
        String json = gson.toJson(week);
        sharedPreferences.edit().putString(keyWeek, json).apply();
    }
    public Week getWeek() {
        String json = sharedPreferences.getString(keyWeek, null);
        if (json == null) {
            return null;
        } else {
            return gson.fromJson(json, Week.class);
        }
    }

    public void removeAll() {
        sharedPreferences.edit().remove(keyWeek).commit();
        sharedPreferences.edit().remove(keyGroup).commit();
    }
}
