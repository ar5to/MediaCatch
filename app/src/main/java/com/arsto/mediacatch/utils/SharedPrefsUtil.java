package com.arsto.mediacatch.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtil {

    private static final String PREF_NAME = "MediaCatchPrefs";
    private static final String KEY_API_KEY = "api_key";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveApiKey(Context context, String apiKey) {
        getPrefs(context).edit().putString(KEY_API_KEY, apiKey).apply();
    }

    public static String getApiKey(Context context) {
        return getPrefs(context).getString(KEY_API_KEY, null);
    }

    public static boolean isApiKeyPresent(Context context) {
        return getPrefs(context).contains(KEY_API_KEY);
    }

    public static void clearApiKey(Context context) {
        getPrefs(context).edit().remove(KEY_API_KEY).apply();
    }
}
