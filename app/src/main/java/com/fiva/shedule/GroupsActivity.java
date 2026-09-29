package com.fiva.shedule;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fiva.shedule.backend.Convert;
import com.fiva.shedule.backend.DownloadCallback;
import com.fiva.shedule.backend.Downloader;
import com.fiva.shedule.backend.PreferenceManager;
import com.fiva.shedule.backend.struct.Group;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity {

    private static final String LINK_GROUPS = "http://raspisanie.pgt.su/cg.htm";
    private PreferenceManager preferenceManager;
    private final Downloader downloader = new Downloader();
    private GroupGridAdapter gridAdapter;
    private FrameLayout progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.groups_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.groups_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        startView();

        @SuppressLint({"LocalSuppress", "MissingInflatedId"})
        Button choose = findViewById(R.id.chooseButton);
        RecyclerView recyclerView = findViewById(R.id.recycleView);

        gridAdapter = new GroupGridAdapter(isAnySelected -> {
            choose.setEnabled(isAnySelected);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(gridAdapter);

        choose.setOnClickListener(view -> {

            preferenceManager = new PreferenceManager(this);

            if (gridAdapter.getSelectedItem() != null) {
                preferenceManager.setGroup(gridAdapter.getSelectedItem());
            } else {
                Toast.makeText(this, "Произошла ошибка. Перезапустите приложение", Toast.LENGTH_LONG).show();
            }

            if (preferenceManager.getGroup() != null) {
                finish();
            } else {
                Toast.makeText(this, "Произошла ошибка. Перезапустите приложение", Toast.LENGTH_LONG).show();
            }
        });

        downloadGroup();
    }

    private void downloadGroup() {
        downloader.downloadDocument(LINK_GROUPS, new DownloadCallback() {
            @Override
            public void onDownloadSuccess(Object object) {
                Log.d("myTag", "downloadGroup is SUCCESS");
                Convert convert = new Convert();
                if (object != null) {
                    ArrayList<Group> groups = convert.getGroups((Document) object);
                    gridAdapter.updateData(groups);
                    progressOverlay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDownloadError(Exception e) {
                Log.d("asd", "downloadGroup is NOT SUCCESS");
            }
        });
    }





    private void startView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.toolbar));
        }
        progressOverlay = findViewById(R.id.progressOverlay);
        progressOverlay.setVisibility(View.VISIBLE);
    }


}
