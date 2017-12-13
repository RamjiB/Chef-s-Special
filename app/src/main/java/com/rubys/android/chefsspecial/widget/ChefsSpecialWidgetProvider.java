package com.rubys.android.chefsspecial.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.rubys.android.chefsspecial.R;
import com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes;

import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class ChefsSpecialWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "ChefsSpecialWidgetProvi";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        String recipeName, String ingredients, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.chefs_special_widget_provider);

        //Create an Intent to launch main activity when clicked
        Intent intent = new Intent(context, ListOfRecipes.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        Log.i(TAG,"recipeName: "+ recipeName);
        Log.i(TAG,"ingredients: "+ ingredients);

        if (recipeName.isEmpty() && ingredients.isEmpty()){

            views.setTextViewText(R.id.appwidget_recipeName,"No desired recipes");

        }else{
            //update the recipe name and ingredients
            String[] recipe = ingredients.split("< ");
            String ingredientList = "";
            Log.i(TAG,"recipe: " + Arrays.toString(recipe));
            Log.i(TAG,"recipe length: "+ recipe.length);
            for (int i = 0; i < recipe.length; i++){
                int position = i + 1;
                ingredientList =  ingredientList + position + ". " + recipe[i] + "\n";
            }
            views.setTextViewText(R.id.appwidget_recipeName,recipeName);
            views.setTextViewText(R.id.appwidget_ingredients,ingredientList);
        }

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.appwidget,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RecipeUpdatedWidget.startActionUpdateRecipeWidgets(context);

    }

    public static void updateRecipeWidgets(Context context,AppWidgetManager appWidgetManager,
                                           String recipeName,String ingredients,int[] appWidgetIds){

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,recipeName,ingredients, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

