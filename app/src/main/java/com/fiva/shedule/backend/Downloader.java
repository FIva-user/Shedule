package com.fiva.shedule.backend;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class Downloader {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private Document download(String link) {
        try {
            Document doc = Jsoup.connect(link)
                    .timeout(10000)
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .get();

            doc.outputSettings().charset("windows-1251");
            return doc;
        } catch (Exception e) {
            return null;
        }
    }

    public String downloadString(String link) {
        Document doc = download(link);
        if (doc != null) {
            return doc.text();
        } else {
            return "Ошибка при скачивании";
        }
    }

    public void downloadDocument(String link, DownloadCallback callback) {
        executor.execute(() -> {
            try {
                Document doc = Jsoup.connect(link)
                        .timeout(10000)
                        .header("Cache-Control", "no-cache, no-store, must-revalidate")
                        .header("Pragma", "no-cache")
                        .get();
                doc.outputSettings().charset("windows-1251");
                mainHandler.post(() -> callback.onDownloadSuccess(doc));
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> callback.onDownloadError(e));
            }
        });

    }
}

