package com.arsto.mediacatch.ui;

import static com.arsto.mediacatch.utils.IntentUtils.openUrlInBrowser;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.arsto.mediacatch.R;
import com.arsto.mediacatch.utils.AppUtils;
import com.arsto.mediacatch.utils.ClipboardUtils;
import com.arsto.mediacatch.utils.DialogUtils;
import com.arsto.mediacatch.utils.IntentUtils;
import com.arsto.mediacatch.utils.PermissionUtils;
import com.arsto.mediacatch.utils.SharedPrefsUtil;
import com.arsto.mediacatch.utils.UIUtils;
import com.arsto.mediacatch.utils.VideoUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView cardDownload;
    private LinearProgressIndicator progressBar;
    private LinearLayout layoutTitle, layoutFollow;
    private TextView tvWait;

    private String videoURL = "";
    private static final int PERMISSION_REQUEST_CODE = 123;
    public static boolean downloadFroze = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        if (SharedPrefsUtil.isApiKeyPresent(this)) {
            AppUtils.RAPID_API_KEY = SharedPrefsUtil.getApiKey(this);
        }

        initializeUI();
        handleSharedIntent();
        setupTouchListener();
        requestPermissionsIfNeeded();
    }

    private void initializeUI() {
        layoutTitle = findViewById(R.id.layout_title);
        layoutFollow = findViewById(R.id.layout_follow);
        cardDownload = findViewById(R.id.card_download);
        tvWait = findViewById(R.id.tv_wait);
        progressBar = findViewById(R.id.progressBar);

        UIUtils.delayedVisibility(layoutTitle, layoutFollow,
                findViewById(R.id.iv_info), findViewById(R.id.ivSettings));
    }

    private void handleSharedIntent() {
        String sharedURL = IntentUtils.extractSharedText(getIntent());
        if (sharedURL != null && !sharedURL.isEmpty()) {
            videoURL = sharedURL;
            if (!downloadFroze) {
                handleActionDown();
            }
            getIntent().removeExtra(Intent.EXTRA_TEXT);
        }
    }

    private void setupTouchListener() {
        cardDownload.setOnTouchListener((v, event) -> {
            if (!downloadFroze) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        videoURL = "";
                        cardDownload.setAlpha(0.85f);
                        handleActionDown();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        cardDownload.setAlpha(1.0f);
                        break;
                }
            }
            return true;
        });
    }

    private void requestPermissionsIfNeeded() {
        if (!PermissionUtils.hasRequiredPermissions(this)) {
            ActivityCompat.requestPermissions(this, getRequiredPermissions(), PERMISSION_REQUEST_CODE);
        }
    }

    private String[] getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.POST_NOTIFICATIONS};
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        return new String[0];
    }

    private void handleActionDown() {
        tvWait.setText("Please wait…");
        if (PermissionUtils.hasRequiredPermissions(this)) {
            startAction();
        } else {
            requestPermissionsIfNeeded();
        }
    }

    private void startAction() {
        tvWait.setVisibility(View.GONE);
        checkForAPIKey();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (PermissionUtils.hasRequiredPermissions(this)) {
                startAction();
            } else {
                Toast.makeText(this, getString(R.string.permissions_required),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkForAPIKey() {
        if (AppUtils.RAPID_API_KEY.isEmpty()) {
            DialogUtils.showAPIDialog(this);
        } else {
            preStartDownload();
        }
    }

    private boolean checkURLValidity() {
        if (videoURL.isEmpty() && ClipboardUtils.getClipBoardLink(this).isEmpty()) return false;
        if (!videoURL.isEmpty() && !ClipboardUtils.isURLInClipboard(videoURL)) return false;
        if (!ClipboardUtils.getClipBoardLink(this).isEmpty()
                && !ClipboardUtils.isURLInClipboard(ClipboardUtils.getClipBoardLink(this))) return false;
        return true;
    }

    private String getVideoUrl() {
        return !videoURL.isEmpty() ? videoURL : ClipboardUtils.getClipBoardLink(this);
    }

    private void preStartDownload() {
        if (checkURLValidity()) {
            videoURL = getVideoUrl();
        } else {
            Toast.makeText(this, getString(R.string.copy_url_first), Toast.LENGTH_SHORT).show();
            videoURL = "";
            return;
        }
        startDownload();
    }

    private void startDownload() {
        downloadFroze = true;
        VideoUtils.fetchVideoData(this, progressBar, tvWait, videoURL);
        videoURL = "";
    }

    public void github(View view) {
        openUrlInBrowser(this, getString(R.string.github_com));
    }

    public void twitter(View view) {
        openUrlInBrowser(this, getString(R.string.x_com));
    }

    public void telegram(View view) {
        openUrlInBrowser(this, getString(R.string.telegram_com));
    }

    public void showInfoDialog(View view) {
        DialogUtils.showInfoDialog(this);
    }

    public void navigateSettings(View view) {
        startActivity(new Intent(this, APIKeyActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSharedIntent();
    }
}
