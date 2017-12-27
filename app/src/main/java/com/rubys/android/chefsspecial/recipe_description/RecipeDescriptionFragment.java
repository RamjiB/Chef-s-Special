package com.rubys.android.chefsspecial.recipe_description;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rubys.android.chefsspecial.R;
import com.rubys.android.chefsspecial.recipe_detail_steps.RecipeDetailSteps;

import java.util.Arrays;

import static com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes.RECIPE_DETAILS;
import static com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes.RECIPE_NAMES;
import static com.rubys.android.chefsspecial.recipe_description.Recipe.fragmentManager;
import static com.rubys.android.chefsspecial.recipe_description.Recipe.recipeDetailStepsFragment;
import static com.rubys.android.chefsspecial.recipe_description.Recipe.tabletView;

public class RecipeDescriptionFragment extends Fragment implements RecipeDescriptionListAdapter.ItemClickListener  {

    private static final String TAG = "RecipeFragment";

//    public static final String RECIPE_DETAILS = "Recipe Details";
    public static final String ADAPTER_POSITION = "Adapter Position";

    private RecyclerView recipeDescriptionRV;

    private String recipeName;

    //saved instance constance
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Parcelable listState;
    public static String[] mRecipeDetail;
    public static int mAdapterPosition;

    //Mandatory empty constructor
    public RecipeDescriptionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG,"onCreateView");

        final View view = inflater.inflate(R.layout.fragment_recipe_master,container,false);


        recipeName = getActivity().getIntent().getStringExtra(RECIPE_NAMES);
        Log.i(TAG, "recipeName: " + recipeName);

        String[] recipeDetails = getActivity().getIntent().getStringArrayExtra(RECIPE_DETAILS);
        Log.i(TAG, "recipeDetails: " + Arrays.toString(recipeDetails));

        mRecipeDetail = recipeDetails;


        recipeDescriptionRV = (RecyclerView) view.findViewById(R.id.recipeDescriptionRV);
        
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recipeDescriptionRV.setLayoutManager(linearLayoutManager);
        recipeDescriptionRV.setHasFixedSize(true);

        RecipeDescriptionListAdapter recipeDescriptionListAdapter = new RecipeDescriptionListAdapter(this);
        recipeDescriptionRV.setAdapter(recipeDescriptionListAdapter);

        if (listState != null){

            recipeDescriptionRV.getLayoutManager().onRestoreInstanceState(listState);
            recipeDescriptionListAdapter.setRecipeDescriptionSteps(recipeDetails);
        }else {

            recipeDescriptionListAdapter.setRecipeDescriptionSteps(recipeDetails);
        }

        Log.i(TAG,"3");
        return view;

    }

    @Override
    public void onItemClick(String[] recipeDetails, int adapterPosition) {

        Log.i(TAG,"item clicked");

        if (tabletView){

            Log.i(TAG,"tabletView: "+ true);
            mAdapterPosition = adapterPosition;
            recipeDetailStepsFragment.initialiseView();
            recipeDetailStepsFragment.setUpViews(mAdapterPosition,mRecipeDetail,null);
            try {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, recipeDetailStepsFragment).commitAllowingStateLoss();
            }catch(Exception e){
                e.printStackTrace();
            }

        }else {

            Intent intent = new Intent(getContext(), RecipeDetailSteps.class);
            intent.putExtra(ADAPTER_POSITION, adapterPosition);
            intent.putExtra(RECIPE_DETAILS, recipeDetails);
            intent.putExtra(RECIPE_NAMES, recipeName);
            startActivity(intent);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RECYCLER_STATE,recipeDescriptionRV.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null){
            listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
        }
    }
}
