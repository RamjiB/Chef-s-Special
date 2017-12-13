package com.rubys.android.chefsspecial.recipe_description;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.rubys.android.chefsspecial.R;

import static com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes.RECIPE_NAMES;


public class Recipe extends AppCompatActivity {

    private static final String TAG = "Recipe";

    private String RECIPE_TITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Log.i(TAG,"onCreate");
        //get recipe name from previous activity
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra(RECIPE_NAMES)){
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

        Log.i(TAG,"set recipe title: "+ RECIPE_TITLE);

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
