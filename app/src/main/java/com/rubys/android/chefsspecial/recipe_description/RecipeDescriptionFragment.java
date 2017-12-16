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

        Log.i(TAG,"1");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recipeDescriptionRV.setLayoutManager(linearLayoutManager);
        recipeDescriptionRV.setHasFixedSize(true);
        Log.i(TAG,"2");

//        //fetching details from Recipe json
//        new FetchRecipeDescription().execute(jsonUrl, recipeName);

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

//    /**
//     * Fetching recipe short description details
//     */
//    private class FetchRecipeDescription extends AsyncTask<String,Void,String[]> {
//
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            Log.i(TAG,"params: "+ Arrays.toString(params));
//
//            try {
//
//                URL url = new URL(params[0]);
//                Log.i(TAG,"url: " + url);
//
//                String recipeJson = NetworkUtils.getResponseFromHttpUrl(url);
//                Log.i(TAG,"json: " + recipeJson);
//
//                String[] recipeDetails = RecipeJsonUtils.
//                                                getRecipeDetails(recipeJson,params[1]);
//
//                Log.i(TAG,"recipeDetails: "+ Arrays.toString(recipeDetails));
//
//                return recipeDetails;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String[] recipeDetails) {
//
//            if (recipeDetails != null){
//
//                Log.i(TAG,"mRecipeDetail: "+ Arrays.toString(mRecipeDetail));
//
//                if (listState != null){
//                    recipeDescriptionRV.getLayoutManager().onRestoreInstanceState(listState);
//                    recipeDescriptionListAdapter.setRecipeDescriptionSteps(recipeDetails);
//                }else {
//
//                    recipeDescriptionListAdapter.setRecipeDescriptionSteps(recipeDetails);
//                }
//            }
//
//        }
//    }


    @Override
    public void onItemClick(String[] recipeDetails, int adapterPosition) {

        Log.i(TAG,"item clicked");

        if (tabletView){

            Log.i(TAG,"tabletView: "+ true);

            recipeDetailStepsFragment.initialiseView();
            recipeDetailStepsFragment.setUpViews(adapterPosition,recipeDetails,null);
            fragmentManager.beginTransaction()
                    .replace(R.id.container,recipeDetailStepsFragment).commit();

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
