package com.arsto.mediacatch.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtils {

    public static String getClipboardText(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null && manager.hasPrimaryClip()) {
            ClipData data = manager.getPrimaryClip();
            if (data != null && data.getItemCount() > 0) {
                CharSequence text = data.getItemAt(0).getText();
                return text != null ? text.toString() : "";
            }
        }
        return "";
    }

    public static boolean isURLInClipboard(String text) {
        return text != null && (text.startsWith("https://") || text.startsWith("http://"));
    }

    public static String getClipBoardLink(Context context) {
        try {
            String text = getClipboardText(context);
            if (text == null || text.isEmpty()) return "";
            return isURLInClipboard(text) ? text : "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
