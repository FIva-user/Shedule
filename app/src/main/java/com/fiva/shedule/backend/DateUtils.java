package com.fiva.shedule.backend;

import android.os.Build;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {
    public String getRussianDayOfWeek(String dateString) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                LocalDate date = null;

                date = LocalDate.parse(dateString, formatter);
                String dayName = null;
                dayName = date.getDayOfWeek()
                        .getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"))
                        .toLowerCase();
                return dayName.substring(0, 1).toUpperCase() + dayName.substring(1);
            } else throw new Exception();

        } catch (Exception e) {
            return "";
        }
    }
    public String getCurrentDate() {
        LocalDate currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDate = currentDate.format(formatter);
            return formattedDate;
        } else return null;
    }
    public String getCurrentTime() {
        LocalTime currentTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = currentTime.format(timeFormatter);
            return formattedTime;
        }
        return null;
    }
    public boolean compareDate1AfterDate2(String date1, String date2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return LocalDate.parse(date1, formatter).isAfter(LocalDate.parse(date2, formatter));
        }
        return false;
    }
    public boolean compareTime1BeforeTime2(String timeStr1, String timeStr2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            String cleanedTime1 = timeStr1.replace(".", ":").trim();
            String cleanedTime2 = timeStr2.replace(".", ":").trim();

            LocalTime time1 = LocalTime.parse(cleanedTime1, formatter);
            LocalTime time2 = LocalTime.parse(cleanedTime2, formatter);

            if (time1.isBefore(time2) || time1.equals(time2)) {
                return true;
            } else return false;
        }
        return false;
    }
    public boolean compareTime1AfterTime2(String timeStr1, String timeStr2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            String cleanedTime1 = timeStr1.replace(".", ":").trim();
            String cleanedTime2 = timeStr2.replace(".", ":").trim();

            LocalTime time1 = LocalTime.parse(cleanedTime1, formatter);
            LocalTime time2 = LocalTime.parse(cleanedTime2, formatter);

            if (time1.isAfter(time2) || time1.equals(time2)) {
                return true;
            } else return false;
        }
        return false;
    }
}
