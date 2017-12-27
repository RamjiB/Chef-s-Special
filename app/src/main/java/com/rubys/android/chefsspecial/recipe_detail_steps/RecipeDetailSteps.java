package com.rubys.android.chefsspecial.recipe_detail_steps;


import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import com.rubys.android.chefsspecial.R;

import java.util.Arrays;

import static android.provider.Contacts.SettingsColumns.KEY;
import static com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes.RECIPE_NAMES;
import static com.rubys.android.chefsspecial.recipe_description.Recipe.fragmentManager;
import static com.rubys.android.chefsspecial.recipe_detail_steps.RecipeDetailStepsFragment.KEY_ADAPTER_POSITION;
import static com.rubys.android.chefsspecial.recipe_detail_steps.RecipeDetailStepsFragment.KEY_RECIPE_DETAILS;
import static com.rubys.android.chefsspecial.recipe_detail_steps.RecipeDetailStepsFragment.KEY_RECYCLER_STATE;


public class RecipeDetailSteps extends AppCompatActivity {

    private static final String TAG = "RecipeDetailSteps";

    private String recipeName;
    private RecipeDetailStepsFragment fragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_steps);
        Log.i(TAG,"onCreate");

        //get recipe name from previous activity
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(RECIPE_NAMES)) {
                recipeName = intent.getStringExtra(RECIPE_NAMES);
                Log.i(TAG, "recipeDetails: " + recipeName);
            }
        }

        //Title Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.titleToolbar);
        toolbar.setTitle(recipeName);
        setSupportActionBar(toolbar);

        //Get a support ActionBAr corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        //Enable the up button
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Log.i(TAG, "Title setted");


        if (savedInstanceState == null){

            //Create a recipe detail steps fragment
            fragment = new RecipeDetailStepsFragment();

            mFragmentManager = getSupportFragmentManager();

            mFragmentManager.beginTransaction().add(R.id.container, fragment).commit();
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
