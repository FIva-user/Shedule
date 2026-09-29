package com.fiva.shedule.backend;

import android.util.Log;

import com.fiva.shedule.backend.struct.Day;
import com.fiva.shedule.backend.struct.Group;
import com.fiva.shedule.backend.struct.Lesson;
import com.fiva.shedule.backend.struct.Week;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convert {

    public Week getWeek(Document doc){

        String fullText = doc.text();
        fullText = fullText.substring(0, fullText.indexOf("Обновлено"));
        String[] dayLines = fullText.split("(?=\\b\\d{2}\\.\\d{2}\\.\\d{4})");

        Pattern datePattern = Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}");
        Pattern lessonPattern = Pattern.compile("(\\d+)\\s+Пара:\\s+([\\d.]+)-([\\d.]+)\\s+([А-Яа-яЁё\\d\\s.і\\-]+?)\\s+(\\d{3,4}(?:-\\d)?)\\s+([А-Яа-яЁё\\s.і\\-]+)");

        ArrayList<Day> days = new ArrayList<>();


        for (String dayLine : dayLines) {
            Day day = new Day();
            if (dayLine.matches("^\\d{2}\\.\\d{2}\\.\\d{4}.*")) {

                //date
                Matcher dateMatcher = datePattern.matcher(dayLine);
                if (dateMatcher.find()) {
                    day.setDate(dateMatcher.group());
                }

                //lessons
                ArrayList<Lesson> lessons = new ArrayList<>();
                Matcher lessonMatcher = lessonPattern.matcher(dayLine);

                while (lessonMatcher.find()) {
                    Lesson lesson = new Lesson(
                            lessonMatcher.group(1).trim(),
                            lessonMatcher.group(2).trim(),
                            lessonMatcher.group(3).trim(),
                            lessonMatcher.group(4).trim(),
                            lessonMatcher.group(5).trim(),
                            lessonMatcher.group(6).trim()
                    );
                    lessons.add(lesson);
                }
                if (lessons != null) {
                    day.setLessons(lessons);
                }
                days.add(day);
            }
        }
        Week week = new Week();
        week.setWeek(days);
        return week;
    }
    public ArrayList<Group> getGroups(Document doc){

        Elements elements = doc.select("a.z0");
        ArrayList<Group> groups = new ArrayList<>();

        if (elements == null) {
            return null;
        } else {
            for (Element element : elements) {
                Group group = new Group();
                group.setName(element.text().trim());
                group.setLink(element.absUrl("href"));
                groups.add(group);
            }
            return groups;
        }
    }
}
