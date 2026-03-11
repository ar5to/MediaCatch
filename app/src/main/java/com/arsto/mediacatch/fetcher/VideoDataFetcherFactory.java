package com.arsto.mediacatch.fetcher;

public class VideoDataFetcherFactory {
    public static VideoDataFetcher getFetcher(String platformName) {
        // Extensible: add platform-specific fetchers here
        return new GenericVideoDataFetcher();
    }
}
