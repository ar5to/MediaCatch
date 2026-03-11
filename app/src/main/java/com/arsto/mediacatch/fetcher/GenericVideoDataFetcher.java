package com.arsto.mediacatch.fetcher;

import android.os.AsyncTask;

import com.arsto.mediacatch.callback.Callback;
import com.arsto.mediacatch.model.DownloadItem;
import com.arsto.mediacatch.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GenericVideoDataFetcher implements VideoDataFetcher {

    @Override
    public void fetchVideoData(String url, Callback callback) {
        new FetchVideoDataTask(callback).execute(url);
    }

    private static class FetchVideoDataTask extends AsyncTask<String, Void, DownloadItem> {
        private final Callback callback;

        FetchVideoDataTask(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected DownloadItem doInBackground(String... params) {
            String videoUrl = params[0];
            String apiUrl = "https://social-download-all-in-one.p.rapidapi.com/v1/social/autolink";
            String apiKey = AppUtils.RAPID_API_KEY;

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"url\":\"" + videoUrl + "\"}");

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .addHeader("x-rapidapi-key", apiKey)
                    .addHeader("x-rapidapi-host", "social-download-all-in-one.p.rapidapi.com")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return parseResponse(response.body().string());
                } else {
                    DownloadItem item = new DownloadItem();
                    item.setName("Error: " + response.message());
                    item.setUrl("Error: Request failed");
                    return item;
                }
            } catch (IOException e) {
                e.printStackTrace();
                DownloadItem item = new DownloadItem();
                item.setName("Exception: " + e.getMessage());
                item.setUrl("Error: Network failure");
                return item;
            }
        }

        @Override
        protected void onPostExecute(DownloadItem item) {
            callback.onResult(item);
        }

        private DownloadItem parseResponse(String responseBody) {
            DownloadItem item = new DownloadItem();
            try {
                JSONObject json = new JSONObject(responseBody);
                JSONArray medias = json.getJSONArray("medias");

                if (medias.length() > 0) {
                    JSONObject media = medias.getJSONObject(0);
                    item.setUrl(media.getString("url"));
                    item.setFile_extension(media.getString("extension"));
                } else {
                    item.setUrl("Error: No video URL found");
                    item.setFile_extension("unknown");
                }

                item.setName(json.optString("title", "MediaCatch Download"));
                item.setSource(json.optString("source", "unknown"));

            } catch (JSONException e) {
                e.printStackTrace();
                item.setName("Error: JSON parsing failed");
                item.setUrl("Error: JSON parsing failed");
                item.setFile_extension("unknown");
                item.setSource("unknown");
            }
            return item;
        }
    }
}
