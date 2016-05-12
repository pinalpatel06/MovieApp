package com.example.android.movieshowapp1.BaseClasses;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.example.android.movieshowapp1.R;
/**
 * Created by veeral on 23/04/2016.
 */
public class MovieData implements Parcelable {
    @SerializedName("id")
    private long movieId;
    @SerializedName("original_title")
    private String movieTitle;
    @SerializedName("poster_path")
    private String moviePoster;
    @SerializedName("overview")
    private String movieOverview;
    @SerializedName("vote_average")
    private double movieRatings;
    @SerializedName("release_date")
    private String movieReleaseDate;
    @SerializedName("backdrop_path")
    private String movieBackdropPath;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.movieId);
        dest.writeString(this.movieTitle);
        dest.writeString(this.moviePoster);
        dest.writeString(this.movieOverview);
        dest.writeDouble(this.movieRatings);
        dest.writeString(this.movieReleaseDate);
        dest.writeString(this.movieBackdropPath);
    }

    public MovieData() {
    }

    protected MovieData(Parcel in) {
        this.movieId = in.readLong();
        this.movieTitle = in.readString();
        this.moviePoster = in.readString();
        this.movieOverview = in.readString();
        this.movieRatings = in.readDouble();
        this.movieReleaseDate = in.readString();
        this.movieBackdropPath = in.readString();
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }


    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public double getMovieRatings() {
        return movieRatings;
    }

    public void setMovieRatings(double movieRatings) {
        this.movieRatings = movieRatings;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieBackdropPath(Context context) {

        if(movieBackdropPath!=null){
            this.movieBackdropPath = context.getResources().getString(R.string.url_for_movie_poster)+"w500"+ movieBackdropPath;
            return movieBackdropPath;
        }

        return null;
    }
    public String getMovieBackdropURL(){
        return this.movieBackdropPath;
    }

    public void setMovieBackdropPath(String movieBackdropPath) {
        this.movieBackdropPath = movieBackdropPath;
    }
    public String getPosterURL(Context context) {
        if (moviePoster != null) {
            return context.getResources().getString(R.string.url_for_movie_poster) + moviePoster;
        }
        return null;
    }
    public void makeCompletePath(Context context){
        this.moviePoster = "http://image.tmdb.org/t/p/" + "w342"  + this.moviePoster;
        this.movieBackdropPath = "http://image.tmdb.org/t/p/"+ "w500"+ this.movieBackdropPath;
    }
}
