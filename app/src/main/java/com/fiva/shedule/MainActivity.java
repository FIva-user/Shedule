package com.fiva.shedule;

import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.os.Bundle;
import android.util.Log;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fiva.shedule.backend.Convert;
import com.fiva.shedule.backend.DateUtils;
import com.fiva.shedule.backend.DownloadCallback;
import com.fiva.shedule.backend.Downloader;
import com.fiva.shedule.backend.PreferenceManager;
import com.fiva.shedule.backend.struct.Day;
import com.fiva.shedule.backend.struct.Group;
import com.fiva.shedule.backend.struct.Week;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private FrameLayout progressOverlay;
    private LinearLayout btnDayMode;
    private LinearLayout btnWeekMode;
    private SheduleAdapter sheduleAdapter;

    private PreferenceManager preferenceManager;
    private final Downloader downloader = new Downloader();
    private String LINK_GROUPS;
    private Week week;
    private ArrayList<Day> oneDay;
    private boolean isDayMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startView();

        preferenceManager = new PreferenceManager(this);
        progressOverlay = findViewById(R.id.progressOverlay);
        sheduleAdapter = new SheduleAdapter();
        RecyclerView recyclerView = findViewById(R.id.rvSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sheduleAdapter);

        // получаем группу
        Group group = preferenceManager.getGroup();
        //если записи о группе нет, то запускаем активити для выбора группы
        if (group == null) {
            Log.d("myTag", "Group NOT FOUND in memory");
            Intent intent = new Intent(this, GroupsActivity.class);
            startActivity(intent);
            group = preferenceManager.getGroup();

            //получаем ссылку для скачивания расписания и скачиваем его
            LINK_GROUPS = group.getLink();
            downloadSchedule();
        } else {
            //получаем ссылку для скачивания расписания
            LINK_GROUPS = group.getLink();

            //делаем попытку получить расписание из памяти
            week = preferenceManager.getWeek();
            if (week == null) {
                //если нет то запускаем скачивание расписания
                Log.d("myTag", "Week is null");
                progressOverlay.setVisibility(View.VISIBLE);
            } else {
                DateUtils dateUtils = new DateUtils();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    /*если расписание в памяти было, то проверяем:
                    * если
                    * (есть ли расписание на сегодня
                    * и
                    * пары ещё не кончились)
                    * или
                    * в памяти уже расписание на завтра,
                    * то
                    * выводим на экран расписание из памяти,
                    * иначе
                    * скачиваем расписание */


                    if ((week.getWeek().get(0).getDate().equals(dateUtils.getCurrentDate()) && dateUtils.compareTime1BeforeTime2(dateUtils.getCurrentTime(), week.getWeek().get(0).getLessons().getLast().getTimeEnd())) || dateUtils.compareDate1AfterDate2(week.getWeek().get(0).getDate(), dateUtils.getCurrentDate())) {
                        oneDay = new ArrayList<>();
                        oneDay.add(week.getWeek().get(0));
                        if (isDayMode) {
                            sheduleAdapter.updateData(new Week(oneDay));
                        } else {
                            sheduleAdapter.updateData(week);
                        }
                        
                    } else {
                        progressOverlay.setVisibility(View.VISIBLE);
                    }
                }
            }

            downloadSchedule();
        }

    }


    private void downloadSchedule() {
        downloader.downloadDocument(LINK_GROUPS, new DownloadCallback() {
            @Override
            public void onDownloadSuccess(Object object) {
                Log.d("myTag", "Download schedule id SUCCESS");
                Convert convert = new Convert();
                if (object != null) {
                    //конвертируем скачанный документ в объект недели
                    Week week1 = convert.getWeek((Document) object);
                    DateUtils dateUtils = new DateUtils();

                    // чтобы приложение не падало при первом запуске
                    if (week == null) {
                        week = week1;
                        preferenceManager.setWeek(week);
                    } else {

                        Log.d("myTag", "Date of new schedule is " + week1.getWeek().get(0).getDate());
                        Log.d("myTag", "Current date is " + dateUtils.getCurrentDate());
                        Log.d("myTag", "Date in memory is " + week.getWeek().get(0).getDate());

                        // Если дата не устаревшая
                        if (week1 != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM && !dateUtils.compareDate1AfterDate2(dateUtils.getCurrentDate(), week.getWeek().get(0).getDate())) {
                            //если есть расписание на сегодня обновляем его
                            if (week1.getWeek().get(0).getDate().equals(dateUtils.getCurrentDate())) {
                                Log.d("myTag", "The new schedule is current");
                                week = week1;
                                preferenceManager.setWeek(week);
                            } else if (dateUtils.compareTime1AfterTime2(dateUtils.getCurrentTime(), week.getWeek().get(0).getLessons().getLast().getTimeEnd())) {
                                Log.d("myTag", "Lessons is end");
                                week = week1;
                                preferenceManager.setWeek(week);
                            }
                        } else {
                            week = week1;
                            preferenceManager.setWeek(week);
                        }
                    }
                    progressOverlay.setVisibility(View.GONE);

                    oneDay = new ArrayList<>();
                    oneDay.add(week.getWeek().get(0));
                    if (isDayMode) {
                        sheduleAdapter.updateData(new Week(oneDay));
                    } else {
                        sheduleAdapter.updateData(week);
                    }
                }
            }

            @Override
            public void onDownloadError(Exception e) {
                Log.d("myTag", "NOT SUCCESS");
            }
        });
    }


    private void startView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.toolbar));
        }

        btnDayMode = findViewById(R.id.btnDayMode);
        btnWeekMode = findViewById(R.id.btnWeekMode);
        setNavigationMode();
        btnDayMode.setOnClickListener(v -> {
            isDayMode = true;
            setNavigationMode(true);
        });
        btnWeekMode.setOnClickListener(v -> {
            isDayMode = false;
            setNavigationMode(true);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings_action) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNavigationMode() {
        btnDayMode.setSelected(isDayMode);
        btnWeekMode.setSelected(!isDayMode);

    }
    private void setNavigationMode(boolean isEdit) {
        btnDayMode.setSelected(isDayMode);
        btnWeekMode.setSelected(!isDayMode);

        if (isDayMode) {
            sheduleAdapter.updateData(new Week(oneDay));
        } else {
            sheduleAdapter.updateData(week);
        }
    }
}