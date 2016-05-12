package com.example.android.movieshowapp1.BaseClasses;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by veeral on 05/05/2016.
 */
public class MovieDbURL {

    public static URL getTrailerURL(String movieId){
        String TrailerUrl = ConstantData.BASE_URL+ "movie/" + movieId + "/videos";
        //http://api.themoviedb.org/3/movie//videos?api_key=1f0f081607bc4bce8419e47eeb04fa99
        final  String API_KEY= "api_key";
        Uri buitlUri = Uri.parse(TrailerUrl).buildUpon()
                .appendQueryParameter(API_KEY, ConstantData.API_KEY)
                .build();

        URL url = null;
        try{
            url = new URL(buitlUri.toString());
        }catch (MalformedURLException e){
            Log.e("LOG", "erorr"+ e);
        }
        return url;
    }
    public static URL getReviewURL(String movieId){
        String TrailerUrl = ConstantData.BASE_URL+ "movie/" + movieId + "/reviews";
        final  String API_KEY= "api_key";
        Uri buitlUri = Uri.parse(TrailerUrl).buildUpon()
                .appendQueryParameter(API_KEY, ConstantData.API_KEY)
                .build();

        URL url = null;
        try{
            url = new URL(buitlUri.toString());
        }catch (MalformedURLException e){
            Log.e("LOG", "erorr"+ e);
        }
        return url;
    }
    public static URL getMovies(String sort){
        final String FORECAST_BASE_URL = ConstantData.BASE_URL + "discover/movie?";
        final String QUERY_PARAM = "sort_by";
        final String PAGE_QUERY = "page";
        String PAGE = Integer.toString(1);
        String API_KEY="api_key";
        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, sort)
                        // .appendQueryParameter(PAGE_QUERY, "1")
                .appendQueryParameter(API_KEY, ConstantData.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            Log.e("URL", "error " + e);
        }
        return url;
    }
}
