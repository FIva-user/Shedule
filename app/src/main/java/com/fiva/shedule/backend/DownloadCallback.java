package com.fiva.shedule.backend;

public interface DownloadCallback {
    void onDownloadSuccess(Object object);
    void onDownloadError(Exception e);
}
