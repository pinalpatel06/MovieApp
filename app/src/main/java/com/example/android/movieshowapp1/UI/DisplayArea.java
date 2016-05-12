package com.example.android.movieshowapp1.UI;

import android.util.DisplayMetrics;

/**
 * Created by veeral on 28/04/2016.
 */
public class DisplayArea {
    private int width;
    private int height;
    private int densityDpi;
    private float density;


    public DisplayArea(){
        width=0;
        height=0;
        density=0.0f;
        densityDpi=0;
    }

    public DisplayArea(int width, int height, int densityDpi, float density) {
        this.width = width;
        this.height = height;
        this.densityDpi = densityDpi;
        this.density = density;
    }
    public void setUIParam(DisplayMetrics displayMetrics){
        setWidth(displayMetrics.widthPixels);
        setHeight(displayMetrics.heightPixels);
        setDensity(displayMetrics.density);
        setDensityDpi(displayMetrics.densityDpi);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public void setDensityDpi(int densityDpi) {
        this.densityDpi = densityDpi;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int calColumn() {
        int column=width/densityDpi;
        if (width / density > 550 && height / density > 550) {
            column = (int) Math.round(column * 0.33);
            return column;
        }
        return column;
    }
}
