package com.arsto.mediacatch.fetcher;

import com.arsto.mediacatch.callback.Callback;

public interface VideoDataFetcher {
    void fetchVideoData(String url, Callback callback);
}
