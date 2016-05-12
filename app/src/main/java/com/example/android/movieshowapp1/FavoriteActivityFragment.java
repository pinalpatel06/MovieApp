package com.example.android.movieshowapp1;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;

import com.example.android.movieshowapp1.AdapterClasses.FavoriteAdapter;
import com.example.android.movieshowapp1.AdapterClasses.MovieAdapter;
import com.example.android.movieshowapp1.BaseClasses.MovieData;
import com.example.android.movieshowapp1.Database.MovieContract;
import com.example.android.movieshowapp1.FetchTask.FetchFromDB;
import com.example.android.movieshowapp1.UI.DisplayArea;

import butterknife.ButterKnife;

import butterknife.Bind;
import butterknife.OnItemClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoriteActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private FavoriteAdapter favoriteAdapter;
    private static final int CURSON_LOADER_ID=0;

    @Bind(R.id.favorite_movie_grid)
    GridView favoriteGridView;

    private static final int CURSOR_LOADER_ID = 0;
    public FavoriteActivityFragment() {
    }

    public interface CallbackFromFavorite{
        void onItemSelected(MovieData data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, rootView);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        DisplayArea displayArea = new DisplayArea();
        displayArea.setUIParam(metrics);

        int col, posterheight;
        if (displayArea.getWidth()/displayArea.getDensity() > 550 && displayArea.getHeight()/displayArea.getDensity() > 550) {
            displayArea.setHeight((int) Math.round(displayArea.getHeight() * 0.66));
            displayArea.setWidth((int) Math.round(displayArea.getWidth() * 0.66));
            displayArea.setDensityDpi(displayArea.getDensityDpi()* 2);
            //holder.mPoster.getLayoutParams().height = displayArea.getHeight();
            //holder.mPoster.getLayoutParams().width = displayArea.getWidth();
        }
            col = displayArea.calColumn();
            posterheight = (int)((displayArea.getWidth()/col)*1.5f);

        favoriteGridView.setNumColumns(col);
        favoriteGridView.setColumnWidth(posterheight);

        favoriteAdapter = new FavoriteAdapter(getActivity(),null,0);

        favoriteGridView.setAdapter(favoriteAdapter);
         return rootView;

    }
    @OnItemClick(R.id.favorite_movie_grid)
    void onItemClick(int position){
        Cursor cursor = (Cursor) favoriteGridView.getItemAtPosition(position);
        MovieData movie = FetchFromDB.fetchMovieFromDB(cursor);
        ((CallbackFromFavorite) getContext()).onItemSelected(movie);
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSON_LOADER_ID,null,this);
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p/>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p/>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoriteAdapter.swapCursor(data);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoriteAdapter.swapCursor(null);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
