package com.example.android.movieshowapp1.FetchTask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieshowapp1.BaseClasses.MovieData;
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

import org.json.JSONArray;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by veeral on 05/05/2016.
 */
public class FetchTrailer extends AsyncTask<String,Void,ArrayList<TrailerData>> {
    private String LOG_TAG = FetchTrailer.class.getSimpleName();

    private TrailerListener trailerListener;
    public FetchTrailer(TrailerListener trailerListener){
        this.trailerListener = trailerListener;
    }

    public interface TrailerListener{
        public void fetchTrailerFinished(ArrayList<TrailerData> trailerList);
    }

    @Override
    protected ArrayList<TrailerData> doInBackground(String...params) {
        OkHttpClient client = new OkHttpClient();
        String jsonString = null;

        if (params[0] != null) {
            try {
                URL url = MovieDbURL.getTrailerURL(params[0]);
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();
                jsonString = response.body().string();
                response.body().close();

                return getJason(jsonString);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "error "+ e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "error "+ e);
            }

        }
        return null;
    }

    private ArrayList<TrailerData> getJason(String jsonString){

        ArrayList<TrailerData> trailerList=null;
        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(jsonString);
        if (element.isJsonObject()) {
            JsonObject results = element.getAsJsonObject();
            JsonArray trailer = results.getAsJsonArray("results");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            trailerList = new ArrayList<TrailerData>();
            if (trailer != null) {
                for (int i = 0; i < trailer.size(); i++) {
                    JsonObject movie = trailer.get(i).getAsJsonObject();
                    TrailerData current = gson.fromJson(movie, TrailerData.class);
                    trailerList.add(current);
                }
            }
        }
        return trailerList;
    }
    @Override
    protected void onPostExecute(ArrayList<TrailerData> trailerList){
        if(trailerList!=null){
            trailerListener.fetchTrailerFinished(trailerList);
        }
    }

}
