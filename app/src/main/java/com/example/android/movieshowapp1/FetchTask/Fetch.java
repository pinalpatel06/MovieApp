package com.example.android.movieshowapp1.FetchTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieshowapp1.BaseClasses.MovieData;
import com.example.android.movieshowapp1.BaseClasses.MovieDbURL;
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
 * Created by veeral on 11/05/2016.
 */
public class Fetch extends AsyncTask<String,Void,ArrayList<MovieData>> {

    private String LOG_TAG  = Fetch.class.getSimpleName();
    private Context mContext;
    public Fetch(Context context){
        mContext = context;

    }
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected ArrayList<MovieData> doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();
        String jsonString = null;
        ArrayList<MovieData> movieList=null;
        if (params[0] != null) {
            try {
                URL url = MovieDbURL.getMovies(params[0]);
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();
                jsonString = response.body().string();
                response.body().close();

                movieList= getJason(jsonString);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "error " + e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "error "+ e);
            }

        }
        return movieList;
    }
    public ArrayList<MovieData> getJason(String jsonString){

        ArrayList<MovieData> movieList=null;
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        if (element.isJsonObject()) {
            JsonObject results = element.getAsJsonObject();
            JsonArray movies = results.getAsJsonArray("results");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            movieList = new ArrayList<MovieData>();
            if (movies != null) {
                for (int i = 0; i < movies.size(); i++) {
                    JsonObject movie = movies.get(i).getAsJsonObject();
                    MovieData current = gson.fromJson(movie, MovieData.class);
                    current.makeCompletePath(mContext);
                    movieList.add(current);
                }
            }
        }
        return movieList;
    }
}
