package com.rubys.android.chefsspecial.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.rubys.android.chefsspecial.data.DesiredRecipeContract;

public class RecipeUpdatedWidget extends IntentService {

    private static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.rubys.android.chefsspecial.update_recipe_widgets";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RecipeUpdatedWidget() {
        super("RecipeUpdatedWidget");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        assert intent != null;
        final String action = intent.getAction();
        if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)){
            handleActionUpdateRecipeWidgets();
        }

    }

    private void handleActionUpdateRecipeWidgets() {

        //Query for the desired recipe name and ingredients
        @SuppressWarnings("UnusedAssignment") String recipeName = "";
        @SuppressWarnings("UnusedAssignment") String ingredients = "";
        Cursor cursor = getContentResolver().query(
                DesiredRecipeContract.DesiredRecipeEntry.CONTENT_URI,null,null,null,
                DesiredRecipeContract.DesiredRecipeEntry.COLUMN_CREATED_AT);

        if (cursor != null && cursor.getCount() >0) {
            cursor.moveToFirst();
            recipeName = cursor.getString(cursor.getColumnIndex(
                    DesiredRecipeContract.DesiredRecipeEntry.COLUMN_RECIPENAME));
            ingredients = cursor.getString(cursor.getColumnIndex(
                    DesiredRecipeContract.DesiredRecipeEntry.COLUMN_INGREDIENTS));
            cursor.close();


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(this, ChefsSpecialWidgetProvider.class));

            //Now update all widgets
            ChefsSpecialWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipeName,
                    ingredients, appWidgetIds);
        }
    }

    public static void startActionUpdateRecipeWidgets(Context context){
        Intent intent = new Intent(context,RecipeUpdatedWidget.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }
}
