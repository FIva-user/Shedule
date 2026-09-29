package com.fiva.shedule.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fiva.shedule.R;
import com.fiva.shedule.backend.DateUtils;
import com.fiva.shedule.backend.PreferenceManager;
import com.fiva.shedule.backend.struct.Day;
import com.fiva.shedule.backend.struct.Week;

public class ScheduleWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_REFRESH_CLICK = "com.fiva.shedule.widget.ACTION_REFRESH_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            @SuppressLint("RemoteViewLayout") RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_day_layout);


            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.widget_lessons_list, serviceIntent);

            Intent clickIntent = new Intent(context, ScheduleWidgetProvider.class);
            clickIntent.setAction(ACTION_REFRESH_CLICK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.widget_btn_refresh, pendingIntent);


            // Установка даты в подвале таблицы
            Week week = new PreferenceManager(context).getWeek();
            views.setTextViewText(R.id.widget_tvWidgetDate, week.getWeek().get(0).getDate()  + ", " + new DateUtils().getRussianDayOfWeek(week.getWeek().get(0).getDate()));

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_lessons_list);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_REFRESH_CLICK.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, ScheduleWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_lessons_list);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}

