package com.example.android.movieshowapp1;

import android.app.Application;

import com.bumptech.glide.request.target.ViewTarget;

/**
 * Created by veeral on 12/05/2016.
 */
public class App extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        ViewTarget.setTagId(R.id.glide_tag);
    }


}
