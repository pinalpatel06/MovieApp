package com.example.android.movieshowapp1.FetchTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieshowapp1.BaseClasses.MovieDbURL;
import com.example.android.movieshowapp1.BaseClasses.TrailerData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by veeral on 08/05/2016.
 */
public class FetchReview extends AsyncTask<String,Void,ArrayList<String>> {

        private String LOG_TAG = FetchReview.class.getSimpleName();

       @Override
        protected ArrayList<String> doInBackground(String...params) {
            OkHttpClient client = new OkHttpClient();
            String jsonString = null;

            if (params[0] != null) {
                try {
                    URL url = MovieDbURL.getReviewURL(params[0]);
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = client.newCall(request).execute();
                    jsonString = response.body().string();
                    response.body().close();

                    return getJason(jsonString);
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "error " + e);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "error "+ e);
                }

            }
            return null;
        }

        private ArrayList<String> getJason(String jsonString){

            ArrayList<String> reviewList=new ArrayList<String>();
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(jsonString);

            if (element.isJsonObject()) {
                JsonObject results = element.getAsJsonObject();
                JsonArray review = results.getAsJsonArray("results");

                if (review != null) {
                    for (int i = 0; i < review.size(); i++) {
                        JsonObject movieReview = review.get(i).getAsJsonObject();
                        JsonElement writter = movieReview.get("author");
                        JsonElement content = movieReview.get("content");

                        reviewList.add(writter.getAsString() + "\n" + content.getAsString());
                    }
                }
            }
            return reviewList;
        }
       /* @Override
        protected void onPostExecute(ArrayList<String> reviewList){
            if(reviewList!=null){

            }
        }*/
}
