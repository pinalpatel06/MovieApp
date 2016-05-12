package com.example.android.movieshowapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.movieshowapp1.BaseClasses.MovieData;

public class FavoriteActivity extends AppCompatActivity implements FavoriteActivityFragment.CallbackFromFavorite{

    FavoriteActivityFragment favoriteActivityFragment;
    private final String FAVACT_TAG = "PFTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState==null){
            favoriteActivityFragment = new FavoriteActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container,favoriteActivityFragment,FAVACT_TAG).commit();
        }
        else{
            favoriteActivityFragment = (FavoriteActivityFragment)getSupportFragmentManager().findFragmentByTag(FAVACT_TAG);
        }


    /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onItemSelected(MovieData data){
        Intent intent = new Intent(this, DetailActivity.class)
                .putExtra("movie",data);
        startActivity(intent);
    }
}
