package com.arsto.mediacatch.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.arsto.mediacatch.R;
import com.arsto.mediacatch.ui.APIKeyActivity;

public class DialogUtils {

    public static void showFailedDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Widget_MediaCatch_Dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_failed, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        view.findViewById(R.id.dialog_restart_button).setOnClickListener(v -> {
            AppUtils.restartApp(context);
            dialog.dismiss();
        });
        view.findViewById(R.id.dialog_api_button).setOnClickListener(v -> {
            context.startActivity(new Intent(context, APIKeyActivity.class));
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void showAPIDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Widget_MediaCatch_Dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_api, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        view.findViewById(R.id.dialog_cancel_button).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.dialog_update_button).setOnClickListener(v -> {
            context.startActivity(new Intent(context, APIKeyActivity.class));
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void showInfoDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Widget_MediaCatch_Dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_info, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView appVersion = view.findViewById(R.id.dialog_version);
        try {
            String versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            appVersion.setText("Version " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            appVersion.setText("Version 1.0");
        }

        view.findViewById(R.id.dialog_github_button).setOnClickListener(v -> {
            IntentUtils.openUrlInBrowser(context, context.getString(R.string.github_com));
            dialog.dismiss();
        });
        view.findViewById(R.id.dialog_sponsor_button).setOnClickListener(v -> {
            IntentUtils.openUrlInBrowser(context, context.getString(R.string.telegram_com));
            dialog.dismiss();
        });

        dialog.show();
    }
}
