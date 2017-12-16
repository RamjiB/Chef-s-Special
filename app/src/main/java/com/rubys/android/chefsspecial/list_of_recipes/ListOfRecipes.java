package com.rubys.android.chefsspecial.list_of_recipes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.rubys.android.chefsspecial.R;
import com.rubys.android.chefsspecial.widget.RecipeUpdatedWidget;
import com.rubys.android.chefsspecial.data.DesiredRecipeContract;
import com.rubys.android.chefsspecial.recipe_description.Recipe;
import com.rubys.android.chefsspecial.utils.NetworkUtils;
import com.rubys.android.chefsspecial.utils.RecipeJsonUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class ListOfRecipes extends AppCompatActivity implements RecipeListAdapter.ItemClickListener{

    private static final String TAG = "ListOfRecipes";

    public static final String RECIPE_NAMES = "recipe name";
    public static final String RECIPE_DETAILS = "Recipe Details";

    private static final String jsonUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017" +
            "/May/59121517_baking/baking.json";

    //saved instance constance
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Parcelable listState;

    private RecipeListAdapter recipeListAdapter;
    private RecyclerView recipeRecyclerView;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_recipes);
        Log.i(TAG,"onCreate");

        //Title Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.titleToolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        Log.i(TAG,"set title: "+ getString(R.string.app_name));

        if (isOnline()) {

            recipeRecyclerView = (RecyclerView) findViewById(R.id.recipeListRV);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recipeRecyclerView.setLayoutManager(linearLayoutManager);
            recipeRecyclerView.setHasFixedSize(true);

            //fetching details from Recipe json
            new FetchRecipeDetailsTask().execute(jsonUrl);

            recipeListAdapter = new RecipeListAdapter(this);
            recipeRecyclerView.setAdapter(recipeListAdapter);
        }else{
            try{
                new AlertDialog.Builder(this)
                    .setTitle("Internet Connection")
                    .setMessage("Please check your internet connection")
                    .setIcon(R.drawable.ic_internet)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    /**
     *fetch recipe name and image for a recycler view
     */

    private class FetchRecipeDetailsTask extends AsyncTask<String,Void,String[]>{


        @Override
        protected String[] doInBackground(String... params) {
            Log.i(TAG,"params: "+ params[0]);

            try {

                URL url = new URL(params[0]);
                Log.i(TAG,"url: " + url);

                String recipeJson = NetworkUtils.getResponseFromHttpUrl(url);
                Log.i(TAG,"json: " + recipeJson);

                String[] recipeNames = RecipeJsonUtils.getRecipeName(recipeJson);
                Log.i(TAG,"recipeNames: " + Arrays.toString(recipeNames));



                //get recipeDetails

                return recipeNames;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] recipeNames) {

            if (recipeNames != null){

                Cursor cursor = getContentResolver()
                        .query(DesiredRecipeContract.DesiredRecipeEntry.CONTENT_URI, null, null, null, null);
                Log.i(TAG,"cursor: "+ cursor);

                if (listState != null){

                    recipeRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
                    recipeListAdapter.setRecipeNames(recipeNames,cursor);

                }else {

                    recipeListAdapter.setRecipeNames(recipeNames,cursor);

                }
            }

        }
    }

    @Override
    public void onItemClick(String recipeName) {
        Log.i(TAG,"moving into Recipe activity");
        //fetching details from Recipe json
        new FetchRecipeDescription().execute(jsonUrl, recipeName);

        intent = new Intent(ListOfRecipes.this,Recipe.class);
        intent.putExtra(RECIPE_NAMES,recipeName);
    }

    /**
     * Fetching recipe short description details
     */
    private class FetchRecipeDescription extends AsyncTask<String,Void,String[]> {


        @Override
        protected String[] doInBackground(String... params) {
            Log.i(TAG,"params: "+ Arrays.toString(params));

            try {

                URL url = new URL(params[0]);
                Log.i(TAG,"url: " + url);

                String recipeJson = NetworkUtils.getResponseFromHttpUrl(url);
                Log.i(TAG,"json: " + recipeJson);

                String[] recipeDetails = RecipeJsonUtils.
                        getRecipeDetails(recipeJson,params[1]);

                Log.i(TAG,"recipeDetails: "+ Arrays.toString(recipeDetails));

                return recipeDetails;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] recipeDetails) {

            if (recipeDetails != null){
                intent.putExtra(RECIPE_DETAILS,recipeDetails);
                startActivity(intent);

            }

        }
    }

    @Override
    public void heartClick(String recipeName,int heart) {
        Log.i(TAG,"heartClicked");
        if (heart == 0) {
            new FetchRecipeIngredients().execute(jsonUrl, recipeName);
        }else {

            @SuppressLint("Recycle") Cursor cursor = getContentResolver()
                    .query(DesiredRecipeContract.DesiredRecipeEntry.CONTENT_URI, null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            long id = 0;
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(
                        DesiredRecipeContract.DesiredRecipeEntry.COLUMN_RECIPENAME));
                if (Objects.equals(name, recipeName)) {
                    id = cursor.getLong(cursor.getColumnIndex(
                            DesiredRecipeContract.DesiredRecipeEntry._ID));
                    break;
                }
                cursor.moveToNext();
            }

            //Build appropriate uri to delete using id
            Uri uri = DesiredRecipeContract.DesiredRecipeEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(String.valueOf(id)).build();

            getContentResolver().delete(uri,null,null);
            Toast.makeText(this, "Removed from desired recipe list", Toast.LENGTH_SHORT).show();

            RecipeUpdatedWidget.startActionUpdateRecipeWidgets(ListOfRecipes.this);
        }
    }

    //Fetch recipe ingredients

    private class FetchRecipeIngredients extends AsyncTask<String,Void,String[]>{

        @Override
        protected String[] doInBackground(String... params) {
            URL url;
            try {
                url = new URL(params[0]);
                Log.i(TAG,"url: " + url);

                String recipeJson = NetworkUtils.getResponseFromHttpUrl(url);
                Log.i(TAG,"json: " + recipeJson);

                String recipeIngredients = RecipeJsonUtils
                        .getRecipeIngredients(recipeJson,params[1]);
                Log.i(TAG,"recipeIngredients: " + recipeIngredients);

                //Insert values into the table
                ContentValues cv = new ContentValues();
                cv.put(DesiredRecipeContract.DesiredRecipeEntry.COLUMN_RECIPENAME,params[1]);
                cv.put(DesiredRecipeContract.DesiredRecipeEntry.COLUMN_INGREDIENTS,
                        recipeIngredients);
                cv.put(DesiredRecipeContract.DesiredRecipeEntry.COLUMN_CREATED_AT,
                        System.currentTimeMillis());

                Uri uri = getContentResolver()
                        .insert(DesiredRecipeContract.DesiredRecipeEntry.CONTENT_URI,cv);
                Log.i(TAG,"uri :"+ uri);
                if (uri != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ListOfRecipes.this, "Added to desired recipe list",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    RecipeUpdatedWidget.startActionUpdateRecipeWidgets(ListOfRecipes.this);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeRecyclerView != null) {

            outState.putParcelable(KEY_RECYCLER_STATE, recipeRecyclerView.getLayoutManager().onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
        }
    }

    /*
     * check for internet connection
     */

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh){
            finish();
            startActivity(getIntent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
