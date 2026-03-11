package com.arsto.mediacatch.callback;

import com.arsto.mediacatch.model.DownloadItem;

public interface Callback {
    void onResult(DownloadItem item);
}
