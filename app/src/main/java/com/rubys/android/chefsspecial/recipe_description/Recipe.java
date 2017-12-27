package com.rubys.android.chefsspecial.recipe_description;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.rubys.android.chefsspecial.R;
import com.rubys.android.chefsspecial.recipe_detail_steps.RecipeDetailStepsFragment;

import java.util.Arrays;

import static com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes.RECIPE_DETAILS;
import static com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes.RECIPE_NAMES;
import static com.rubys.android.chefsspecial.recipe_description.RecipeDescriptionFragment.mRecipeDetail;


public class Recipe extends AppCompatActivity {

    private static final String TAG = "Recipe";

    private String RECIPE_TITLE;
    public static Boolean tabletView;
    @SuppressLint("StaticFieldLeak")
    public static RecipeDetailStepsFragment recipeDetailStepsFragment;

    public static  FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Log.i(TAG,"onCreate");

        if(savedInstanceState == null) {

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


            if (findViewById(R.id.multiPane) != null) {

                tabletView = true;

                fragmentManager = getSupportFragmentManager();

                recipeDetailStepsFragment = new RecipeDetailStepsFragment();
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
