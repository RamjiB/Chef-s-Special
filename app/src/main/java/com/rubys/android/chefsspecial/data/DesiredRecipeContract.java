package com.rubys.android.chefsspecial.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DesiredRecipeContract {

    //The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.rbuys.android.chefsspecial";

    //public static final Uri = "content://" + <authority>
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Path for the recipe directory
    public static final String PATH_TASKS = "DesiredRecipeTable";

    public static final class DesiredRecipeEntry implements BaseColumns{

        //TaskEntry content URI = base content uri + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        //Table Name
        public static final String TABLE_NAME = "desiredRecipes";

        //Table Columns
        public static final String COLUMN_RECIPENAME = "RecipeName";
        public static final String COLUMN_INGREDIENTS = "RecipeIngredients";
        public static final String COLUMN_CREATED_AT = "Created_at";
    }
}
