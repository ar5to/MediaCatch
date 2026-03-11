package com.arsto.mediacatch.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arsto.mediacatch.callback.Callback;
import com.arsto.mediacatch.detector.PlatformDetector;
import com.arsto.mediacatch.fetcher.VideoDataFetcher;
import com.arsto.mediacatch.fetcher.VideoDataFetcherFactory;
import com.arsto.mediacatch.model.DownloadItem;
import com.arsto.mediacatch.ui.MainActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class VideoUtils {

    private static final String TAG = "VideoUtils";

    public static void fetchVideoData(Context context, LinearProgressIndicator progressBar,
                                      TextView tvWait, String url) {
        tvWait.setVisibility(View.VISIBLE);
        String platform = PlatformDetector.getPlatformName(url);
        Toast.makeText(context, "Fetching from " + platform, Toast.LENGTH_SHORT).show();

        VideoDataFetcher fetcher = VideoDataFetcherFactory.getFetcher(platform);
        fetcher.fetchVideoData(url, new Callback() {
            @Override
            public void onResult(DownloadItem item) {
                Log.d(TAG, "URL: " + item.getUrl());
                Log.d(TAG, "Name: " + item.getName());
                Log.d(TAG, "Platform: " + item.getSource());
                handleResult(context, progressBar, tvWait, item);
            }
        });
    }

    private static void handleResult(Context context, LinearProgressIndicator progressBar,
                                     TextView tvWait, DownloadItem item) {
        if (item.getUrl() == null || item.getUrl().startsWith("Error")) {
            Log.e(TAG, "Error: " + item.getUrl());
            Toast.makeText(context, "Failed to fetch download link", Toast.LENGTH_SHORT).show();
            DialogUtils.showFailedDialog(context);
            tvWait.setText("Download Failed");
            MainActivity.downloadFroze = false;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            DownloadUtils.downloadFileFromURL(context, item, progressBar, tvWait);
        }
    }
}
