package com.example.android.movieshowapp1.AdapterClasses;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android.movieshowapp1.Database.MovieContract;
import com.example.android.movieshowapp1.R;
import com.example.android.movieshowapp1.UI.DisplayArea;

import butterknife.ButterKnife;
import butterknife.Bind;
/**
 * Created by veeral on 09/05/2016.
 */
public class FavoriteAdapter extends CursorAdapter {

    private final Context mContext;

    public FavoriteAdapter(Context context,Cursor c,int flags){
        super(context,c,flags);
        mContext=context;
    }
    public static class ViewHolder{
        @Bind(R.id.poster)
        ImageView posterImage;
        @Bind(R.id.progress)
        ProgressBar progress;
        @Bind(R.id.favoriteMovie)
        ImageView favorite;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }


    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_data_for_grid,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;

    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(final View view, Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        int versionIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        final String versionName = cursor.getString(versionIndex);
        viewHolder.favorite.setImageResource(R.drawable.ic_favorite);

        int imageIndx = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH);
        String image = cursor.getString(imageIndx);

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        DisplayArea displayArea = new DisplayArea();
        displayArea.setUIParam(metrics);


      //  viewHolder.posterImage.getLayoutParams().height = (int)((displayArea.getWidth()/displayArea.getWidth()/displayArea.getDensityDpi())*1.5f);
        if(image!=null){
            Glide.with(mContext)
                    .load(image)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            viewHolder.progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(viewHolder.posterImage);
        }

    }

}
