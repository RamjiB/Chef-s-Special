package com.rubys.android.chefsspecial.recipe_detail_steps;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rubys.android.chefsspecial.R;

import java.util.Arrays;
import java.util.Objects;

import static android.view.View.GONE;
import static com.rubys.android.chefsspecial.recipe_description.RecipeDescriptionFragment.ADAPTER_POSITION;
import static com.rubys.android.chefsspecial.recipe_description.RecipeDescriptionFragment.RECIPE_DETAILS;


public class RecipeDetailStepsFragment extends Fragment {

    private static final String TAG = "RecipeDetailStepsFragme";

    private View rootView;

    private String[] recipeDetails;
    private int adapterPosition;
    // --Commented out by Inspection (12/13/2017 10:02 PM):boolean tab,// --Commented out by Inspection (12/13/2017 10:02 PM):landscape;

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private Dialog mFullScreenDialog;
    private Boolean mExoPlayerFullscreen = false;


    private FloatingActionButton playVideo;
    private ImageView noVideoImage;
    private ImageView mFullScreenIcon;
    private TextView instruction;
    private TextView description;
    private FloatingActionButton nextStep;
    private FloatingActionButton previousStep;

    private RecyclerView ingredientRV;

    //saved instance constance
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private final String KEY_RECIPE_DETAILS = "recipe_details";
    private final String KEY_ADAPTER_POSITION = "adapter_position";
    private final String KEY_LANDSCAPE = "landscape";
    private Parcelable listState;
    private String[] savedRecipe;
    private int savedPosition;

    private String[] recipeInfo;


    public RecipeDetailStepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");
        if (savedInstanceState != null){
            mExoPlayerFullscreen = savedInstanceState.getBoolean(KEY_LANDSCAPE);
            savedRecipe = savedInstanceState.getStringArray(KEY_RECIPE_DETAILS);
            savedPosition = savedInstanceState.getInt(KEY_ADAPTER_POSITION);
            listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);

            Log.i(TAG,"onCreateView recipeDetails: "+ Arrays.toString(savedRecipe));
            Log.i(TAG,"onCreateView adapterPosition: "+savedPosition);
            Log.i(TAG,"onCreateView landscape: "+mExoPlayerFullscreen);
            Log.i(TAG,"onCreateView listState: "+listState);
        }

        if (mExoPlayerFullscreen) {

            Log.i(TAG,"ORIENTATION_LANDSCAPE");

            rootView = inflater.inflate(R.layout.fragment_landscape_video, container, false);

            Log.i(TAG,"savedRecipe: "+ Arrays.toString(savedRecipe));
            Log.i(TAG,"savedPosition: "+savedPosition);

            if (savedRecipe != null && savedPosition != 0) {

                Log.i(TAG,"inside if: ");

                initialiseView();

                setUpViews(savedPosition, savedRecipe,listState);

                initFullScreenDialog();
                openFullscreenDialog();

                return rootView;
            }

        } else {

            Log.i(TAG,"ORIENTATION_PORTRAIT");
            //Inflate recipe detail steps fragment
            rootView = inflater.inflate(R.layout.fragment_activity_recipe_detail_steps, container, false);

            if (savedInstanceState != null){

                initialiseView();

                setUpViews(savedPosition, savedRecipe, listState);

                setNextFloatingButton();

                setPreviousFloatingButton();

                return rootView;

            }else {

                //get recipe name from previous activity
                Intent intent = getActivity().getIntent();
                if (intent != null) {

                    if (intent.hasExtra(RECIPE_DETAILS) &&
                            intent.hasExtra(ADAPTER_POSITION)) {
                        recipeDetails = intent.getStringArrayExtra(RECIPE_DETAILS);
                        Log.i(TAG, "recipeDetails: " + Arrays.toString(recipeDetails));

                        adapterPosition = intent.getIntExtra(ADAPTER_POSITION, 0);
                        Log.i(TAG, "adapterPosition: " + adapterPosition);

                        initialiseView();

                        setUpViews(adapterPosition, recipeDetails, listState);

                        setNextFloatingButton();

                        setPreviousFloatingButton();

                        return rootView;
                    }
                }
            }

        }

        return null;

    }




    private void initialiseView(){

        Log.i(TAG,"initialiseView");

        //initialize the variables
        nextStep = (FloatingActionButton) rootView.findViewById(R.id.nextStep);
        previousStep = (FloatingActionButton) rootView.findViewById(R.id.previousStep);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.recipeVideo);
        playVideo = (FloatingActionButton) rootView.findViewById(R.id.playVideo);
        noVideoImage = (ImageView) rootView.findViewById(R.id.noVideoImage);
        ingredientRV = (RecyclerView) rootView.findViewById(R.id.ingredientsListRV);
        instruction = (TextView) rootView.findViewById(R.id.instructionTV);
        description = (TextView) rootView.findViewById(R.id.description);

    }

    private void setUpViews(int adapterPosition, String[] recipeDetails, Parcelable listState) {

        Log.i(TAG,"setUpViews");

        //get Recipe Step Title
        String mRecipeDetails = recipeDetails[adapterPosition];

        recipeInfo = mRecipeDetails.split(">");

        int VIDEO_URL = 2;
        String videoURL = recipeInfo[VIDEO_URL];
        Log.i(TAG,"videoURL: "+ videoURL);

        if (adapterPosition == 0){

            setIngredientList(listState);

            setVideoPlayer(videoURL);

            ingredientView();


        }else {

            otherViews();
            if (rootView.findViewById(R.id.ingredientsListRV) != null) {

                if (adapterPosition == recipeDetails.length - 1) {
                    nextStep.setVisibility(View.INVISIBLE);
                }

                setVideoPlayer(videoURL);

                //Instruction text
                int DESCRIPTION = 1;
                instruction.setText(recipeInfo[DESCRIPTION]);
            }else{
                setVideoPlayer(videoURL);
            }

        }

    }

    private void setIngredientList(Parcelable listState){

        Log.i(TAG,"setIngredientList");

        if (rootView.findViewById(R.id.ingredientsListRV) != null) {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            ingredientRV.setLayoutManager(linearLayoutManager);
            ingredientRV.setHasFixedSize(true);

            Log.i(TAG, "set ingredient rv");

            IngredientsListAdapter ingredientsListAdapter = new IngredientsListAdapter(getContext());
            ingredientRV.setAdapter(ingredientsListAdapter);

            Log.i(TAG, "set ingredient rv");

            if (listState != null) {
                ingredientRV.getLayoutManager().onRestoreInstanceState(listState);
                ingredientsListAdapter.setIngredients(recipeInfo[1]);
            } else {

                ingredientsListAdapter.setIngredients(recipeInfo[1]);
            }
        }
    }

    private void setVideoPlayer(String videoURL){
        //Create default track selector
        TrackSelector trackSelector = new DefaultTrackSelector();

        //create loader
        LoadControl loadControl = new DefaultLoadControl();

        //Create the player
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

        try {

            //Prepare the Media source
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                    new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "Chef")),
                    new DefaultExtractorsFactory(), null, null);

            LoopingMediaSource loopingMediaSource = new LoopingMediaSource(mediaSource);
            mExoPlayer.prepare(loopingMediaSource);
        }catch(Exception e){
            e.printStackTrace();
        }
        mPlayerView.setResizeMode(3);
        Log.i(TAG,"set video player");
        mPlayerView.setPlayer(mExoPlayer);
        if (rootView.findViewById(R.id.ingredientsListRV) != null) {
            mPlayerView.setUseController(false);
        }else{
            mPlayerView.setUseController(true);
        }

        mExoPlayer.seekTo(200);

        initFullScreenButton();


        //play video once play button clicked
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo.setVisibility(GONE);
                mPlayerView.setUseController(true);
                mExoPlayer.seekTo(0);
                mExoPlayer.setPlayWhenReady(true);
            }
        });

        if (Objects.equals(videoURL, " ")) {

            mPlayerView.setVisibility(View.INVISIBLE);
            playVideo.setVisibility(View.INVISIBLE);

            noVideoImage.setImageResource(R.drawable.no_video_image);
            noVideoImage.setVisibility(View.VISIBLE);
        }
    }

    private void ingredientView(){

        ingredientRV.setVisibility(View.VISIBLE);
        previousStep.setVisibility(View.INVISIBLE);
        mPlayerView.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        instruction.setVisibility(View.INVISIBLE);
        playVideo.setVisibility(View.INVISIBLE);
        noVideoImage.setVisibility(View.INVISIBLE);

    }

    private void otherViews(){

        if (rootView.findViewById(R.id.ingredientsListRV) != null) {
            ingredientRV.setVisibility(View.INVISIBLE);
            nextStep.setVisibility(View.VISIBLE);
            previousStep.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            instruction.setVisibility(View.VISIBLE);
            playVideo.setVisibility(View.VISIBLE);
            noVideoImage.setVisibility(View.INVISIBLE);
        }else{
            mPlayerView.setVisibility(View.VISIBLE);
            playVideo.setVisibility(View.VISIBLE);
        }
    }

    private void setNextFloatingButton(){

        //set next step floating button

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterPosition = adapterPosition + 1;
                Log.i(TAG, "adapterPosition: " + adapterPosition);
                Log.i(TAG, "recipeDetails.length: " + recipeDetails.length);

                if (adapterPosition < recipeDetails.length) {
                    noVideoImage.setVisibility(View.INVISIBLE);
                    mPlayerView.setVisibility(View.VISIBLE);
                    mExoPlayer.setPlayWhenReady(false);
                    mPlayerView.hideController();
                    playVideo.setVisibility(View.VISIBLE);
                    setUpViews(adapterPosition, recipeDetails,null);
                }
            }
        });

    }

    private void setPreviousFloatingButton(){

        //set previous step floating button

        previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterPosition = adapterPosition - 1;
                Log.i(TAG, "adapterPosition: " + adapterPosition);

                if (adapterPosition > -1) {
                    noVideoImage.setVisibility(View.INVISIBLE);
                    mPlayerView.setVisibility(View.VISIBLE);
                    mExoPlayer.setPlayWhenReady(false);
                    mPlayerView.hideController();
                    playVideo.setVisibility(View.VISIBLE);
                    setUpViews(adapterPosition, recipeDetails,null);
                }
            }
        });

    }

    private void initFullScreenDialog(){
        mFullScreenDialog = new Dialog(getContext(),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen){
            public void onBackPressed(){
                if (mExoPlayerFullscreen){
                    mExoPlayerFullscreen = false;
                    mFullScreenDialog.dismiss();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    super.onBackPressed();
                }
            }
        };
    }

    private void openFullscreenDialog(){

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.ic_shrink));
        mFullScreenDialog.show();

    }

    private void initFullScreenButton(){

        mFullScreenIcon = (ImageView) rootView.findViewById(R.id.exo_fullscreen_icon);
        FrameLayout mFullScreenButton = (FrameLayout) rootView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen) {
                    mExoPlayerFullscreen = true;
                    Log.i(TAG,"mExoPlayerFullscreen: "+ true);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else {
                    mExoPlayerFullscreen = false;
                    Log.i(TAG,"mExoPlayerFullscreen: "+ false);
                    Log.i(TAG,"adapterPosition:"+ adapterPosition);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapterPosition != 0) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG,"onSaveInstanceState");

        if (adapterPosition == 0) {
            Log.i(TAG,"adapterPosition:"+ adapterPosition);
            outState.putParcelable(KEY_RECYCLER_STATE, ingredientRV.getLayoutManager().onSaveInstanceState());
            outState.putStringArray(KEY_RECIPE_DETAILS,recipeDetails);
            outState.putInt(KEY_ADAPTER_POSITION,adapterPosition);
        }else{
            outState.putStringArray(KEY_RECIPE_DETAILS,recipeDetails);
            outState.putInt(KEY_ADAPTER_POSITION,adapterPosition);
            outState.putBoolean(KEY_LANDSCAPE,mExoPlayerFullscreen);

            Log.i(TAG,"recipeDetails: "+ Arrays.toString(recipeDetails));
            Log.i(TAG,"adapterPosition: "+adapterPosition);
            Log.i(TAG,"landscape: "+mExoPlayerFullscreen);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Log.i(TAG, "onViewStateRestored");

        if (savedInstanceState != null) {

            if (savedPosition != 0) {
                adapterPosition = savedInstanceState.getInt(KEY_ADAPTER_POSITION);
                recipeDetails = savedInstanceState.getStringArray(KEY_RECIPE_DETAILS);
                mExoPlayerFullscreen = savedInstanceState.getBoolean(KEY_LANDSCAPE);

                Log.i(TAG,"onViewStateRestored recipeDetails: "+ Arrays.toString(recipeDetails));
                Log.i(TAG,"onViewStateRestored adapterPosition: "+adapterPosition);
                Log.i(TAG,"onViewStateRestored landscape: "+mExoPlayerFullscreen);
            }else{
                listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
                adapterPosition = savedInstanceState.getInt(KEY_ADAPTER_POSITION);
                recipeDetails = savedInstanceState.getStringArray(KEY_RECIPE_DETAILS);
            }

        }
    }
}