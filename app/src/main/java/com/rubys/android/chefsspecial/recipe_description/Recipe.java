package com.rubys.android.chefsspecial.recipe_description;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.rubys.android.chefsspecial.R;
import com.rubys.android.chefsspecial.recipe_detail_steps.RecipeDetailSteps;
import com.rubys.android.chefsspecial.recipe_detail_steps.RecipeDetailStepsFragment;

import java.util.Arrays;

import static com.rubys.android.chefsspecial.R.id.recipeName;
import static com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes.RECIPE_DETAILS;
import static com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes.RECIPE_NAMES;
import static com.rubys.android.chefsspecial.recipe_description.RecipeDescriptionFragment.ADAPTER_POSITION;



public class Recipe extends AppCompatActivity implements RecipeDescriptionListAdapter.ItemClickListener{

    private static final String TAG = "Recipe";

    private String RECIPE_TITLE;
    public static Boolean tabletView;

    public static int mAdapterPosition = 0;
    public static String[] mRecipeDetail;

    private RecipeDescriptionFragment recipeDescriptionFragment;

    public static  FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Log.i(TAG,"onCreate");

        //get recipe name from previous activity
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(RECIPE_NAMES) && intent.hasExtra(RECIPE_DETAILS)) {
                RECIPE_TITLE = intent.getStringExtra(RECIPE_NAMES);
            }
        }

        //Title Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.titleToolbar);
        toolbar.setTitle(RECIPE_TITLE);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        //Enable the up button
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Log.i(TAG, "set recipe title: " + RECIPE_TITLE);

        if(savedInstanceState == null) {

            mRecipeDetail = intent.getStringArrayExtra(RECIPE_DETAILS);

            if (findViewById(R.id.multiPane) != null) {

                tabletView = true;

                fragmentManager = getSupportFragmentManager();

                RecipeDetailStepsFragment recipeDetailStepsFragment = new RecipeDetailStepsFragment();
                fragmentManager.beginTransaction().add(R.id.container, recipeDetailStepsFragment).commit();

            } else {

                tabletView = false;

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                mAdapterPosition = 0;
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(String[] recipeDetails, int adapterPosition) {

        Log.i(TAG,"item clicked");

        if (tabletView){

            Log.i(TAG,"tabletView: "+ true);
            mAdapterPosition = adapterPosition;

            RecipeDetailStepsFragment newFragment = new RecipeDetailStepsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newFragment).commit();

        }else {

            Intent intent = new Intent(this, RecipeDetailSteps.class);
            intent.putExtra(ADAPTER_POSITION, adapterPosition);
            intent.putExtra(RECIPE_DETAILS, recipeDetails);
            intent.putExtra(RECIPE_NAMES, recipeName);
            startActivity(intent);
        }

    }
}
