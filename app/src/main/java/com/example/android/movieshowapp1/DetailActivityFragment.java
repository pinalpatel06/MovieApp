package com.example.android.movieshowapp1;


import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android.movieshowapp1.BaseClasses.ConstantData;
import com.example.android.movieshowapp1.BaseClasses.MovieData;
import com.example.android.movieshowapp1.BaseClasses.MovieDbURL;
import com.example.android.movieshowapp1.BaseClasses.TrailerData;
import com.example.android.movieshowapp1.FetchTask.Fetch;
import com.example.android.movieshowapp1.FetchTask.FetchReview;
import com.example.android.movieshowapp1.FetchTask.FetchTrailer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;


import com.example.android.movieshowapp1.UI.DisplayArea;
import com.example.android.movieshowapp1.FetchTask.FetchFromDB;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements FetchTrailer.TrailerListener,YouTubePlayer.OnInitializedListener{

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    MovieData movieData;
    ImageView tempBackdrop;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    ArrayList<TrailerData> trailerList;
    YouTubePlayer youTubePlayer;
    private  int currentVideo;
    private int currentVideoTimeInMillSec;
    private final String VIDEO_TAG = "youtube";
    private final String VIDEO_NUM = "video_num";

    private FetchFromDB fetchFromDB ;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private String mMovie;

    private ShareActionProvider mShareActionProvider;

    @Bind(R.id.moviePoster)ImageView moviePoster;
    @Bind(R.id.movie_progress) ProgressBar movieProgressBar;
    @Bind(R.id.movie_favorite) ImageView favorite;
    //@Bind(R.id.movie_title) TextView movieTitle;
    @Bind(R.id.detail_rating_textview) TextView movieRating;
    @Bind(R.id.detail_releasedate_textview) TextView movieReleaseDate;
    @Bind(R.id.detail_description_textview) TextView movieOverview;
    @Bind({R.id.firstStar,R.id.secondStar,R.id.thirdStar,R.id.fourthStar,R.id.fifthStar})
    List<ImageView> ratingStars;
    @Bind(R.id.detail_reviews_textview) TextView reviews;
    List<String> trailerKeyList=new ArrayList<String>();
    ArrayList<String> list = null;
    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);

        if(savedIntanceState!=null && savedIntanceState.containsKey(VIDEO_TAG)){
            currentVideoTimeInMillSec = savedIntanceState.getInt(VIDEO_TAG);
            currentVideo=savedIntanceState.getInt(VIDEO_NUM);
        }

        Intent intent;

        Bundle args = getArguments();
        if(args !=null){
            movieData = args.getParcelable("movie");
        }else{
            intent = this.getActivity().getIntent();
            movieData = intent.getParcelableExtra("movie");
            Log.d("DetailFragment", "(mMovieObject != null):" + (movieData != null));
            if(movieData==null){
                try {
                    Fetch fetch = new Fetch(getContext());
                    ArrayList<MovieData> movieList = null;
                    movieList = fetch.execute(ConstantData.MOST_POPULAR).get();
                    if (movieList != null) {
                        movieData = movieList.get(0);
                    }
                }catch (ExecutionException e) {
                    Log.e(LOG_TAG,"error"+e);
                }
                catch (InterruptedException e){
                    Log.e(LOG_TAG,"error"+e);
                }
            }
        }
        //Fetch Trailer Data
        try{
            FetchTrailer fetchTrailer = new FetchTrailer(this);
            fetchTrailer.execute((Double.toString(movieData.getMovieId())));
        }catch(Exception e){
            Log.e(LOG_TAG,"error " + e);
        }

        //fetch review

        //TextView reviews = (TextView)rootView.findViewById(R.id.detail_reviews_textview);
        try{
            FetchReview fetchReview = new FetchReview();
            list = fetchReview.execute(Long.toString(movieData.getMovieId())).get();

        }catch(Exception e){
            Log.e(LOG_TAG,"error " + e);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(movieIntent());
        } else {
            Log.e(LOG_TAG, "fail to set a share intent");
        }
    }

    private Intent movieIntent(){
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, mMovie + "\n#Pop Movie App");

        return sendIntent;
    }

    @OnClick(R.id.movie_favorite)
    public void onClick(View view){
        ContentValues contentValues;
       if(!fetchFromDB.isFavorite(movieData)){
            contentValues = fetchFromDB.makeContentValues(movieData);

            fetchFromDB.execute(FetchFromDB.INSERT, contentValues);
            Toast.makeText(getContext(), "Clicked Second Image",
                    Toast.LENGTH_SHORT).show();
            favorite.setImageResource(R.drawable.ic_favorite);
        }else{
           contentValues = fetchFromDB.makeContentValues(movieData);
           fetchFromDB.execute(FetchFromDB.DELETE,contentValues);
           favorite.setImageResource(R.drawable.ic_favorite_blank);
       }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        youTubePlayerFragment = new YouTubePlayerSupportFragment();
        youTubePlayerFragment.initialize("AIzaSyBME_Xcjs_GvDGDJ-I61AfaWXz7K9041mI",this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.youtube_fragment,youTubePlayerFragment).commit();
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,YouTubePlayer player,boolean isRestored){
        youTubePlayer=player;
        DisplayArea displayArea = new DisplayArea();
        TypedValue typedValue = new TypedValue();
        if(getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize,typedValue,true) && (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data,getResources().getDisplayMetrics());

            if(getView()!=null){
                FrameLayout frameLayout = (FrameLayout) getView().findViewById(R.id.youtube_fragment);
                ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
                DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
                displayArea.setUIParam(metrics);
                layoutParams.height = displayArea.getHeight()-(3*actionBarHeight);
                layoutParams.width = displayArea.getWidth();
                frameLayout.setLayoutParams(layoutParams);
            }
        }
        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
            }

            @Override
            public void onLoaded(String s) {
                currentVideo = trailerKeyList.indexOf(s);
            }

            @Override
            public void onAdStarted() {
            }

            @Override
            public void onVideoStarted() {
            }

            @Override
            public void onVideoEnded() {
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        });
        if(!isRestored){
            if(trailerList!=null){
                youTubePlayer.cueVideos(trailerKeyList, currentVideo, currentVideoTimeInMillSec);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(getActivity(),RECOVERY_DIALOG_REQUEST).show();
        }else{
            Toast.makeText(getActivity(),"error",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize("AIzaSyBME_Xcjs_GvDGDJ-I61AfaWXz7K9041mI", this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerSupportFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        if(youTubePlayer!=null){
            saveInstanceState.putInt(VIDEO_TAG,youTubePlayer.getCurrentTimeMillis());
            saveInstanceState.putInt(VIDEO_NUM,currentVideo);
        }
    }
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,rootView);
        fetchFromDB = new FetchFromDB(getContext());
        if(rootView.findViewById(R.id.backdrop)==null){
            tempBackdrop = (ImageView) getActivity().findViewById(R.id.backdrop);
        }else {
            tempBackdrop = (ImageView)rootView.findViewById(R.id.backdrop);
        }
        //fill information on detail fragment UI
        if(movieData!=null){
            fillMovieData();
        }

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(movieData.getMovieTitle());
        }
        return rootView;
    }

    @Override
    public void fetchTrailerFinished(ArrayList<TrailerData> trailerList){
        this.trailerList = trailerList;
        int s = trailerList.size();
        for(int i=0;i<s;i++) {
            String ss = trailerList.get(i).getmKey().toString();
            trailerKeyList.add(ss);
        }
    }

    public void fillMovieData(){
      //  movieTitle.setText(movieData.getMovieTitle());
        movieOverview.setText(movieData.getMovieOverview());
        movieRating.setText(movieData.getMovieRatings() + "/10");
        fillColorInStar(movieData.getMovieRatings());
        movieReleaseDate.setText(movieData.getMovieReleaseDate());

        if(fetchFromDB.isFavorite(movieData)){
            favorite.setImageResource(R.drawable.ic_favorite);
        }else{
            favorite.setImageResource(R.drawable.ic_favorite_blank);
        }
        Glide.with(getContext())
                .load(movieData.getPosterURL(getContext()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        movieProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(moviePoster);
        Glide.with(getContext())
                .load(movieData.getMovieBackdropPath(getContext()))
                .into(tempBackdrop);

        if(list!=null){
            reviews.setText("\n"+list);
        }else{
            reviews.setText("Review not available");
        }


        mMovie = movieData.getMovieTitle() +
                "\n" + movieData.getMovieReleaseDate() +
                "\n" + movieData.getMovieRatings() +
                "\n" + movieData.getMovieOverview();

    }

    //how many stars to be display full & howmany half
    private void fillColorInStar(double ratings){

        double halfstar = ratings/2;
        int fullStar = (int) halfstar;

        for(int i=0;i<fullStar;i++){
            ratingStars.get(i).setImageResource(R.drawable.ic_rating_fill_star_small);
        }
        if(halfstar>fullStar){
            ratingStars.get(fullStar).setImageResource(R.drawable.ic_rating_halfstar);
        }
    }
}
