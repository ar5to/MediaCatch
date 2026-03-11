package com.arsto.mediacatch.utils;

import android.content.Context;
import android.content.Intent;

import com.arsto.mediacatch.ui.MainActivity;

public class AppUtils {
    public static String RAPID_API_KEY = "";

    public static void restartApp(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
