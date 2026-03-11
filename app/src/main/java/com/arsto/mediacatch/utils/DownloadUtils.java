package com.arsto.mediacatch.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.arsto.mediacatch.model.DownloadItem;
import com.arsto.mediacatch.ui.MainActivity;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadUtils {

    private static final String CHANNEL_ID = "mediacatch_download_channel";
    private static final int NOTIFICATION_ID = 1001;
    private static final long UPDATE_INTERVAL_MS = 500;
    private static final String FOLDER_NAME = "MediaCatch";

    public static void downloadFileFromURL(Context context, DownloadItem item,
                                           LinearProgressIndicator progressBar, TextView textView) {
        createNotificationChannel(context);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = createNotificationBuilder(context, item.getName());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String result = downloadFile(context, item.getUrl(), item.getFile_extension(),
                    manager, builder, progressBar, textView, handler);
            handler.post(() -> postDownload(context, result, manager, builder, progressBar, textView));
        });
    }

    private static NotificationCompat.Builder createNotificationBuilder(Context context, String title) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("MediaCatch")
                .setContentText("Downloading: " + title)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true);
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Downloads", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("MediaCatch download progress");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    private static String downloadFile(Context context, String fileURL, String fileExtension,
                                       NotificationManager manager,
                                       NotificationCompat.Builder builder,
                                       LinearProgressIndicator progressBar,
                                       TextView textView, Handler handler) {
        try {
            handler.post(() -> {
                textView.setText("Downloading…");
                progressBar.setVisibility(LinearProgressIndicator.VISIBLE);
                manager.notify(NOTIFICATION_ID, builder.build());
            });

            URL url = new URL(fileURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            int totalSize = connection.getContentLength();

            if (totalSize < 10 * 1024) {
                inputStream.close();
                return null;
            }

            String filePath = saveFile(context, inputStream, totalSize, fileExtension,
                    handler, textView, progressBar, manager, builder);
            inputStream.close();
            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String saveFile(Context context, InputStream inputStream, int totalSize,
                                   String fileExtension, Handler handler, TextView textView,
                                   LinearProgressIndicator progressBar, NotificationManager manager,
                                   NotificationCompat.Builder builder) throws Exception {

        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExtension;
        boolean isAudio = fileExtension.equalsIgnoreCase("mp3");
        String mimeType = isAudio ? "audio/mpeg" : "video/mp4";
        String relativePath = (isAudio ? Environment.DIRECTORY_MUSIC : Environment.DIRECTORY_MOVIES)
                + "/" + FOLDER_NAME;

        final int[] downloadedHolder = {0};
        boolean[] isDownloading = {true};

        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (isDownloading[0] && totalSize > 0) {
                    int pct = (int) ((downloadedHolder[0] * 100L) / totalSize);
                    float mb = downloadedHolder[0] / (1024f * 1024f);
                    long totalMb = totalSize / (1024 * 1024);
                    updateProgress(pct, mb, totalMb, textView, progressBar, manager, builder);
                    handler.postDelayed(this, UPDATE_INTERVAL_MS);
                }
            }
        };
        handler.post(updateRunnable);

        String filePath;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            cv.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            cv.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);
            cv.put(MediaStore.MediaColumns.IS_PENDING, 1);

            android.net.Uri uri = isAudio
                    ? resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cv)
                    : resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, cv);

            if (uri == null) return null;

            try (OutputStream out = resolver.openOutputStream(uri)) {
                pipe(inputStream, out, downloadedHolder);
            }

            cv.clear();
            cv.put(MediaStore.MediaColumns.IS_PENDING, 0);
            resolver.update(uri, cv, null, null);

            filePath = Environment.getExternalStoragePublicDirectory(
                    isAudio ? Environment.DIRECTORY_MUSIC : Environment.DIRECTORY_MOVIES)
                    + "/" + FOLDER_NAME + "/" + fileName;

        } else {
            File dir = new File(Environment.getExternalStoragePublicDirectory(
                    isAudio ? Environment.DIRECTORY_MUSIC : Environment.DIRECTORY_MOVIES), FOLDER_NAME);
            if (!dir.exists() && !dir.mkdirs()) return null;

            File outFile = new File(dir, fileName);
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                pipe(inputStream, out, downloadedHolder);
            }

            filePath = outFile.getAbsolutePath();
            MediaScannerConnection.scanFile(context, new String[]{filePath}, new String[]{mimeType}, null);
        }

        isDownloading[0] = false;
        if (totalSize > 0) {
            int pct = (int) ((downloadedHolder[0] * 100L) / totalSize);
            float mb = downloadedHolder[0] / (1024f * 1024f);
            long totalMb = totalSize / (1024 * 1024);
            handler.post(() -> updateProgress(pct, mb, totalMb, textView, progressBar, manager, builder));
        }

        return filePath;
    }

    private static void pipe(InputStream in, OutputStream out, int[] downloaded) throws Exception {
        byte[] buffer = new byte[8192];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            downloaded[0] += read;
        }
    }

    private static void updateProgress(int pct, float mb, long totalMb, TextView textView,
                                       LinearProgressIndicator progressBar,
                                       NotificationManager manager,
                                       NotificationCompat.Builder builder) {
        String text = String.format("%d%%  %.1f MB / %d MB", pct, mb, totalMb);
        textView.setText(text);
        progressBar.setProgress(pct);

        Notification n = builder
                .setContentText(text)
                .setProgress(100, pct, false)
                .build();
        manager.notify(NOTIFICATION_ID, n);
    }

    private static void postDownload(Context context, String filePath,
                                     NotificationManager manager,
                                     NotificationCompat.Builder builder,
                                     LinearProgressIndicator progressBar, TextView textView) {
        progressBar.setVisibility(LinearProgressIndicator.INVISIBLE);

        if (filePath != null) {
            Toast.makeText(context, "Saved to: " + filePath, Toast.LENGTH_SHORT).show();
            textView.setText("Download Complete ✓");
            builder.setContentText("Download complete").setProgress(0, 0, false);
        } else {
            Toast.makeText(context, "Failed to download file", Toast.LENGTH_SHORT).show();
            textView.setText("Download Failed");
            builder.setContentText("Download failed").setProgress(0, 0, false);
        }

        manager.notify(NOTIFICATION_ID, builder.build());
        manager.cancel(NOTIFICATION_ID);
        MainActivity.downloadFroze = false;
    }

    public static boolean isURLValid(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }
}
