package com.rubys.android.chefsspecial.utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class RecipeJsonUtils {

    private static final String TAG = "RecipeJsonUtils";

    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_QUANTITY = "quantity";
    private static final String RECIPE_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";

    /**
     * get recipe names from json
     * @param recipeJson
     * @return
     * @throws JSONException
     */
    @SuppressWarnings("JavaDoc")
    public static String[] getRecipeName(String recipeJson) throws JSONException{

        String[] recipeName;

        JSONArray recipeJsonArray = new JSONArray(recipeJson);
        recipeName = new String[recipeJsonArray.length()];


        //get name and store it in an array
        for (int i = 0; i < recipeJsonArray.length(); i++ ){

            JSONObject recipeDetails = recipeJsonArray.getJSONObject(i);

            //get recipe name
            String name = recipeDetails.getString(RECIPE_NAME);

            //get image url
            String RECIPE_IMAGE = "image";
            String image = recipeDetails.getString(RECIPE_IMAGE);

            recipeName[i] = name + "> " + image;

        }
        return recipeName;
    }

    public static String getRecipeIngredients(String recipeJson,String recipeName) throws JSONException{

        String getIngredients = "";
        JSONArray recipeJsonArray = new JSONArray(recipeJson);

        //get name and store it in an array
        for (int i = 0; i < recipeJsonArray.length(); i++ ) {

            JSONObject recipeDetails = recipeJsonArray.getJSONObject(i);

            //get recipe name
            String name = recipeDetails.getString(RECIPE_NAME);

            if (name.equals(recipeName)) {

                JSONArray ingredientsArray = recipeDetails.getJSONArray(RECIPE_INGREDIENTS);
                Log.i(TAG,"ingredients: "+ingredientsArray);

                //get recipe details
                for (int j = 0; j < ingredientsArray.length(); j++ ){

                    JSONObject ingredientDetails = ingredientsArray.getJSONObject(j);

                    //get Quantity
                    String quantity = ingredientDetails.getString(RECIPE_QUANTITY);

                    //get measure
                    String measure = ingredientDetails.getString(RECIPE_MEASURE);

                    //get ingredient
                    String ingredient = ingredientDetails.getString(RECIPE_INGREDIENT);

                    getIngredients = getIngredients + ingredient + ": " + quantity + " " + measure + "< ";
                }

                Log.i(TAG,"Ingredients: "+ getIngredients);
                return getIngredients;
            }
        }

        return null;
    }

    /**
     * get recipe details from json
     * @param recipeJson
     * @return
     * @throws JSONException
     */

    @SuppressWarnings("JavaDoc")
    public static String[] getRecipeDetails(String recipeJson, String recipeName) throws JSONException{

        String[] getRecipeDetails;

        JSONArray recipeJsonArray = new JSONArray(recipeJson);

        //get name and store it in an array
        for (int i = 0; i < recipeJsonArray.length(); i++ ){

            JSONObject recipeDetails = recipeJsonArray.getJSONObject(i);

            //get recipe name
            String name = recipeDetails.getString(RECIPE_NAME);

            if (name.equals(recipeName)){

                String RECIPE_STEPS = "steps";
                JSONArray steps = recipeDetails.getJSONArray(RECIPE_STEPS);
                Log.i(TAG,"steps: "+ steps);

                int stepLength = steps.length()+1;
                Log.i(TAG,"stepLength: "+ stepLength);

                getRecipeDetails = new String[stepLength];

                //get recipe details
                for (int j = 0; j < stepLength; j++ ){

                    if (j == 0){

                        JSONArray ingredientsArray = recipeDetails.getJSONArray(RECIPE_INGREDIENTS);
                        Log.i(TAG,"ingredients: "+ingredientsArray);

                        int ingredientsLength = ingredientsArray.length();

                        String ingredients = "";

                        for (int k = 0 ; k < ingredientsLength ; k++){
                            JSONObject ingredientDetails = ingredientsArray.getJSONObject(k);

                            //get Quantity
                            String quantity = ingredientDetails.getString(RECIPE_QUANTITY);

                            //get measure
                            String measure = ingredientDetails.getString(RECIPE_MEASURE);

                            //get ingredient
                            String ingredient = ingredientDetails.getString(RECIPE_INGREDIENT);

                            ingredients = ingredients + ": " + quantity + "< "
                                                        + measure + "< " + ingredient;
                        }
                        getRecipeDetails[j] = "Ingredients" +"> " + ingredients +"> " + " ";


                    }else{

                        JSONObject recipeSteps = steps.getJSONObject(j-1);

                        //get short description
                        String RECIPE_SHORT_DESCRIPTION = "shortDescription";
                        String shortDescription = recipeSteps.getString(RECIPE_SHORT_DESCRIPTION);

                        //get description
                        String RECIPE_DESCRIPTION = "description";
                        String description = recipeSteps.getString(RECIPE_DESCRIPTION);

                        //get videoUrl
                        String RECIPE_VIDEO_URL = "videoURL";
                        String videoURL = recipeSteps.getString(RECIPE_VIDEO_URL);

                        if (Objects.equals(videoURL, "")){

                            String RECIPE_THUMBNAIL_URL = "thumbnailURL";
                            videoURL = recipeSteps.getString(RECIPE_THUMBNAIL_URL);
                        }

                        getRecipeDetails[j] = shortDescription + "> " + description + "> "+
                                                            videoURL;
                    }
                }

                return getRecipeDetails;

            }

        }
        return null;
    }
}
