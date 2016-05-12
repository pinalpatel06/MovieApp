package com.example.android.movieshowapp1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.movieshowapp1.AdapterClasses.MovieAdapter;
import com.example.android.movieshowapp1.BaseClasses.ConstantData;
import com.example.android.movieshowapp1.BaseClasses.MovieData;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements MovieAdapter.CallbackFromAdapter, FavoriteActivityFragment.CallbackFromFavorite {
    private String LOG_TAG = MainActivity.class.getSimpleName();

    //default sort criteria
    private String sort = ConstantData.MOST_POPULAR;

    private MainActivityFragment mainActivityFragment;
    private DetailActivityFragment detailActivityFragment;

    //
    private final String DETAILFRAGMENT_TAG = "DFTAG";
    private final String PREFERE_TAG = "PFTAG";
    private final String FAVORITE_TAG = "FAVTAG";

    private boolean twoPane;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            mainActivityFragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, mainActivityFragment, PREFERE_TAG).commit();
        } else {
            mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(PREFERE_TAG);
        }

        //for tav view
        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                detailActivityFragment = new DetailActivityFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.movie_detail_container, detailActivityFragment, DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            twoPane = false;
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
      //  client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onItemSelected(MovieData movieData) {
        if (twoPane) {
            Bundle args = new Bundle();
            args.putParcelable("movie", movieData);
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("movie", movieData);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        switch (sort) {
            case ConstantData.MOST_POPULAR:
                menu.findItem(R.id.sort_popular).setChecked(true);
                break;
            case ConstantData.TOP_RATED:
                menu.findItem(R.id.sort_rating).setChecked(true);
                break;
            case ConstantData.FAVORITES:
                menu.findItem(R.id.action_favorite).setChecked(true);
                break;
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            sort = ConstantData.FAVORITES;
            item.setChecked(true);
            if (twoPane) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FavoriteActivityFragment(), FAVORITE_TAG)
                      //  .replace(R.id.container,FavoriteActivity.class,FAVORITE_TAG);
                        .commit();
            } else {
                startActivity(new Intent(this, FavoriteActivity.class));
            }
        } else if (id == R.id.sort_popular) {
            sort = ConstantData.MOST_POPULAR;
            item.setChecked(true);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            sharedPreferences.edit().putString(getString(R.string.pref_sort_key), "popularity.desc").apply();
            MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(PREFERE_TAG);
            if (mainActivityFragment != null) {
                mainActivityFragment.updateMovieList();
            }

        } else if (id == R.id.sort_rating) {
            sort = ConstantData.TOP_RATED;
            item.setChecked(true);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            sharedPreferences.edit().putString(getString(R.string.pref_sort_key), "top_rated.desc").apply();
            MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(PREFERE_TAG);
            if (mainActivityFragment != null) {
                mainActivityFragment.updateMovieList();
            }
        }

        //   return true;

        return super.onOptionsItemSelected(item);
    }

     /*   @Override
    public void onBackPressed(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(PREFERE_TAG)!=null){
            fm.beginTransaction()
                    .replace(R.id.container, new MainActivityFragment(),PREFERE_TAG)
                    .commit();
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }else{
            super.onBackPressed();
        }
    }*/
}
