package com.example.android.movieshowapp1.AdapterClasses;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Context;
import butterknife.ButterKnife;
import butterknife.Bind;
import java.util.ArrayList;
import com.bumptech.glide.Glide;
import android.util.TypedValue;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android.movieshowapp1.R;
import com.example.android.movieshowapp1.BaseClasses.MovieData;
import com.example.android.movieshowapp1.UI.DisplayArea;

/**
 * Created by veeral on 23/04/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<MovieData> movieList;
    private  Context mContext;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;

    public interface CallbackFromAdapter{
        void onItemSelected(MovieData movie);
    }
    public MovieAdapter(Context context,ArrayList<MovieData> mMovie){
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground,mTypedValue,true);
        mBackground = mTypedValue.resourceId;
        movieList = mMovie;
        mContext= context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_data_for_grid, parent, false);
        view.setBackgroundResource(mBackground);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position){
         final MovieData mData = movieList.get(position);
         final  Context context = holder.myView.getContext();
         holder.mMovie = mData;
         holder.mTitle.setText(mData.getMovieTitle());
         String posterUrl = mData.getPosterURL(context);

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        DisplayArea displayArea = new DisplayArea();
        displayArea.setUIParam(metrics);



        int col = displayArea.getWidth()/displayArea.getDensityDpi();
        holder.mPoster.getLayoutParams().height = (int)((displayArea.getWidth()/col)*1.5f);

        if (displayArea.getWidth()/displayArea.getDensity() > 550 && displayArea.getHeight()/displayArea.getDensity() > 550) {
            displayArea.setHeight((int) Math.round(displayArea.getHeight() * 0.66));
            displayArea.setWidth((int) Math.round(displayArea.getWidth() * 0.66));
            displayArea.setDensityDpi(displayArea.getDensityDpi()* 2);
            //holder.mPoster.getLayoutParams().height = displayArea.getHeight();
            //holder.mPoster.getLayoutParams().width = displayArea.getWidth();
        }

        holder.progressBar.setVisibility(View.VISIBLE);

        Glide.with(holder.mPoster.getContext())
                .load(movieList.get(position).getPosterURL(context))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.mPoster);
            // for event hadling
        holder.myView.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View view){
                holder.mPoster.setTag(mData.getMovieId());
                ((CallbackFromAdapter) mContext).onItemSelected(mData);
            }
        });
   }
    public int getItemCount(){
        if (movieList != null) {
            return movieList.size();
        } else {
            return 0;
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public View myView;
        @Bind(R.id.poster)
        ImageView mPoster;
        @Bind(R.id.title)
        TextView mTitle;
        @Bind(R.id.progress)
        ProgressBar progressBar;

        public MovieData mMovie;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            myView = view;
        }
        @Override
        public String toString(){
            return super.toString() + " '"+mTitle.getText();
        }
    }
}
