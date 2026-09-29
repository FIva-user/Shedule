package com.fiva.shedule.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fiva.shedule.R;
import com.fiva.shedule.backend.Convert;
import com.fiva.shedule.backend.DateUtils;
import com.fiva.shedule.backend.DownloadCallback;
import com.fiva.shedule.backend.Downloader;
import com.fiva.shedule.backend.PreferenceManager;
import com.fiva.shedule.backend.struct.Day;
import com.fiva.shedule.backend.struct.Week;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScheduleRemoteViewsFactory(this.getApplicationContext());
    }
}
class ScheduleRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private Week week;

    public ScheduleRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        changeData();
    }

    @Override
    public void onDataSetChanged() {
        Downloader downloader = new Downloader();
        PreferenceManager preferenceManager = new PreferenceManager(context);
        String LINK_GROUPS = preferenceManager.getGroup().getLink();
        downloader.downloadDocument(LINK_GROUPS, new DownloadCallback() {
            @Override
            public void onDownloadSuccess(Object object) {
                Log.d("myTagWg", "Download schedule id SUCCESS");
                Convert convert = new Convert();
                if (object != null) {
                    //конвертируем скачанный документ в объект недели
                    Week week1 = convert.getWeek((Document) object);
                    DateUtils dateUtils = new DateUtils();

                    Log.d("myTagWg", "Date of new schedule is " + week1.getWeek().get(0).getDate());
                    Log.d("myTagWg", "Current date is " + dateUtils.getCurrentDate());
                    Log.d("myTagWg", "Date in memory is " + week.getWeek().get(0).getDate());

                        // Если дата не устаревшая
                    if (week1 != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM && !dateUtils.compareDate1AfterDate2(dateUtils.getCurrentDate(), week.getWeek().get(0).getDate())) {
                        //если есть расписание на сегодня обновляем его
                        if (week1.getWeek().get(0).getDate().equals(dateUtils.getCurrentDate())) {
                            Log.d("myTagWg", "The new schedule is current");
                            week = week1;
                            preferenceManager.setWeek(week);
                        } else if (dateUtils.compareTime1AfterTime2(dateUtils.getCurrentTime(), week.getWeek().get(0).getLessons().getLast().getTimeEnd())) {
                            Log.d("myTagWg", "Lessons is end");
                            week = week1;
                            preferenceManager.setWeek(week);
                        }
                    } else {
                        week = week1;
                        preferenceManager.setWeek(week);
                    }
                }
            }

            @Override
            public void onDownloadError(Exception e) {
                Log.d("myTagWg", "Download error");
            }
        });
        changeData();
    }

    private void changeData() {
        week = new PreferenceManager(context).getWeek();
    }

    @Override
    public int getCount() {
        return week.getWeek().get(0).getLessons().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_item_lesson);

        Day day = week.getWeek().get(0);

        row.setTextViewText(R.id.widget_tvLessonNum, day.getLessons().get(position).getNumber());
        row.setTextViewText(R.id.widget_tvLessonTime, day.getLessons().get(position).getTimeStart() + " - " + day.getLessons().get(position).getTimeEnd());
        row.setTextViewText(R.id.widget_tvLessonName, day.getLessons().get(position).getName() + " (" + day.getLessons().get(position).getClassNumber() + ")");
        row.setTextViewText(R.id.widget_tvLessonTeacher, day.getLessons().get(position).getTeacher());

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
        week.getWeek().clear();
    }
}