package com.arsto.mediacatch.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arsto.mediacatch.R;
import com.arsto.mediacatch.utils.AppUtils;
import com.arsto.mediacatch.utils.SharedPrefsUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class APIKeyActivity extends AppCompatActivity {

    private TextInputEditText etApiKey;
    private MaterialButton btnSaveApiKey;
    private WebView webViewTutorial;
    private TextView tvContactSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apikey);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        etApiKey = findViewById(R.id.et_api_key);
        btnSaveApiKey = findViewById(R.id.btn_save_api_key);
        webViewTutorial = findViewById(R.id.webview_tutorial);
        tvContactSupport = findViewById(R.id.tv_contact_support);

        setupWebView();

        if (SharedPrefsUtil.isApiKeyPresent(this)) {
            etApiKey.setText(SharedPrefsUtil.getApiKey(this));
        }

        btnSaveApiKey.setOnClickListener(v -> {
            String apiKey = etApiKey.getText() != null
                    ? etApiKey.getText().toString().trim() : "";

            if (apiKey.isEmpty()) {
                Toast.makeText(this, "Please enter an API key", Toast.LENGTH_SHORT).show();
                return;
            }

            AppUtils.RAPID_API_KEY = apiKey;
            SharedPrefsUtil.saveApiKey(this, apiKey);
            Toast.makeText(this, "API key saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        tvContactSupport.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:" + getString(R.string.support_email)));
            email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject));
            try {
                startActivity(Intent.createChooser(email, "Send email…"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupWebView() {
        String videoEmbed = "<iframe width=\"100%\" height=\"100%\" "
                + "src=\"https://www.youtube.com/embed/FvOzDM7_V2g?si=_8ileuTW_PYYBkcT\" "
                + "title=\"Tutorial\" frameborder=\"0\" "
                + "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; "
                + "gyroscope; picture-in-picture\" allowfullscreen></iframe>";

        webViewTutorial.loadData(videoEmbed, "text/html", "utf-8");
        webViewTutorial.getSettings().setJavaScriptEnabled(true);
        webViewTutorial.setWebChromeClient(new WebChromeClient());
    }
}
