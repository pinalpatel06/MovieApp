package com.example.android.movieshowapp1.FetchTask;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.movieshowapp1.BaseClasses.MovieData;
import com.example.android.movieshowapp1.Database.MovieContract;

/**
 * Created by veeral on 09/05/2016.
 */
public class FetchFromDB extends AsyncTask<Object,Void,Void> {

    private final String LOG_CAT = FetchFromDB.class.getSimpleName();
    private final Context mContext;

    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    public FetchFromDB(Context context){
        mContext=context;
    }

    public ContentValues makeContentValues(MovieData data){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                data.getMovieId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                data.getMovieTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                data.getMoviePoster());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                data.getMovieOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                data.getMovieRatings());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                data.getMovieReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                data.getMovieBackdropURL());
        return contentValues;
        }

    public  boolean isFavorite(MovieData data){
        Cursor cursor  = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID+ "=" + data.getMovieId(),null,null);

        if(cursor!=null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }else{
            return false;
        }

    }
    public static MovieData fetchMovieFromDB(Cursor cursor){
        MovieData movieData = new MovieData();

        int id = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        int title = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        int posterPath = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH);
        int overView = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
        int rating = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE);
        int releaseDate = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        int backdropPath = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH);

        movieData.setMovieId(cursor.getLong(id));
        movieData.setMovieTitle(cursor.getString(title));
        movieData.setMoviePoster(cursor.getString(posterPath));
        movieData.setMovieOverview(cursor.getString(overView));
        movieData.setMovieRatings(cursor.getDouble(rating));
        movieData.setMovieReleaseDate(cursor.getString(releaseDate));
        movieData.setMovieBackdropPath(cursor.getString(backdropPath));

        return movieData;
    }
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
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
    protected Void doInBackground(Object... params) {
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues values;

        switch((Integer) params[0]){
            case INSERT:
                values = (ContentValues) params[1];
                resolver.insert(MovieContract.MovieEntry.CONTENT_URI,values);
                break;
            case DELETE:
                values=(ContentValues) params[1];
                int id = values.getAsInteger(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                resolver.delete(MovieContract.MovieEntry.CONTENT_URI,MovieContract.MovieEntry.COLUMN_MOVIE_ID+" = "+
                        Integer.toString(id),null);
                break;
            case UPDATE:

                break;
            default:
                break;
        }
        return null;
    }

}
