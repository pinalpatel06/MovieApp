package com.example.android.movieshowapp1;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import android.net.Uri;
import com.example.android.movieshowapp1.AdapterClasses.MovieAdapter;
import com.example.android.movieshowapp1.BaseClasses.ConstantData;
import com.example.android.movieshowapp1.BaseClasses.MovieData;

import java.util.ArrayList;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import com.example.android.movieshowapp1.UI.DisplayArea;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private ArrayList<MovieData> movieList;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private GridLayoutManager gridLayoutManager;
    private String msort=ConstantData.MOST_POPULAR;
    private  int currentPosition;

    private BroadcastReceiver networkStatus;
    private boolean networkStart;
    public MainActivityFragment() {
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
       // updateMovieList();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       // fetchMovies(msort);
       // updateMovieList();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycleViewList);


        if(savedInstanceState==null || !savedInstanceState.containsKey("movie")){
            updateMovieList();
        }else{
            movieList = savedInstanceState.getParcelableArrayList("movie");
        }

        //calculate no of column according to screen size
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        DisplayArea displayArea = new DisplayArea();
        displayArea.setUIParam(metrics);

        int col = displayArea.calColumn();
        gridLayoutManager = new GridLayoutManager(recyclerView.getContext(),col);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(getActivity(),movieList);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.smoothScrollToPosition(currentPosition);


        networkStatus = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo!=null && movieList==null){
                    networkStart = true;
                    updateMovieList();
                }
            }
        };
        startListening();
        return  rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(networkStatus);
    }
    private void startListening(){
        IntentFilter filter= new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkStatus, filter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movie",(ArrayList<MovieData>) movieList);
    }
    public boolean isOnline(Context context) {

        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        return false;
    }
    public void updateMovieList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort = prefs.getString(getContext().getString(R.string.pref_sort_key),
                getContext().getString(R.string.pref_sort_default));

        if(isOnline(getContext())) {
            if(movieList==null || movieList.isEmpty()){
                fetchMovies(msort);
            }
            else if(!msort.equals(sort) ) {
                msort = sort;
                fetchMovies(msort);
            }
            else if(networkStart){
                fetchMovies(msort);
            }

        }
    }
    private void fetchMovies(String sort){
        FetchMovieData fetchMovieData  = new FetchMovieData(getContext());
        fetchMovieData.execute(sort);
    }
    public class FetchMovieData extends AsyncTask<String,Void,ArrayList <MovieData>>{
        private Context mContext;
        public FetchMovieData(Context context){
            mContext = context;

        }
       @Override
        protected ArrayList<MovieData> doInBackground(String... params) {

           //fetching data on web site
           //final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
           final String FORECAST_BASE_URL = ConstantData.BASE_URL + "discover/movie?";
           final String QUERY_PARAM = "sort_by";
           final String PAGE_QUERY = "page";
           String PAGE = Integer.toString(1);
           String API_KEY="api_key";
           Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                   .appendQueryParameter(QUERY_PARAM, params[0])
                  // .appendQueryParameter(PAGE_QUERY, "1")
                   .appendQueryParameter(API_KEY, ConstantData.API_KEY)
                   .build();

           URL url = null;
           try {
               url = new URL(builtUri.toString());
           } catch (MalformedURLException e) {
               Log.e("URL", "error " + e);
           }

           OkHttpClient okHttpClient = new OkHttpClient();
           String jSonString=null;
           try {
               Request request = new Request.Builder()
                       .url(url)
                       .build();
               Response response = okHttpClient.newCall(request).execute();
               jSonString = response.body().string();
               response.body().close();

           }catch (IOException e){

               Log.e(LOG_TAG,"Error",e);
           }catch (NullPointerException e){
               Log.e(LOG_TAG,"Error",e);
           }
           //convert json string to string
           JsonParser parser = new JsonParser();
           JsonElement element = parser.parse(jSonString);
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
                       current.makeCompletePath(getContext());
                      // current.getMovieBackdropPath(getContext());
                       //current.setTrailerPath(getTrailersURL(current.getId()).toString());
                       movieList.add(current);
                   }
               }
           }
            return movieList;
        }
        @Override
        public void onPostExecute(ArrayList<MovieData> movieDatas){
            movieAdapter = new MovieAdapter(getActivity(),movieDatas);
            if(recyclerView!=null){
                recyclerView.swapAdapter(movieAdapter,false);
            }
        }
    }
}
