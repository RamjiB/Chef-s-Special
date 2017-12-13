package com.rubys.android.chefsspecial.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

class DesiredRecipeDbHelper extends SQLiteOpenHelper{


    //the database name
    private static final String DATABASE_NAME = "desiredRecipe.db";

    //If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    public DesiredRecipeDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create a table to hold desired recipe data
        final String SQL_CREATE_DESIRED_RECIPE_TABLE = "CREATE TABLE " +
                DesiredRecipeContract.DesiredRecipeEntry.TABLE_NAME + " ("+
                DesiredRecipeContract.DesiredRecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                DesiredRecipeContract.DesiredRecipeEntry.COLUMN_RECIPENAME + " TEXT NOT NULL, "+
                DesiredRecipeContract.DesiredRecipeEntry.COLUMN_INGREDIENTS+ " TEXT NOT NULL, "+
                DesiredRecipeContract.DesiredRecipeEntry.COLUMN_CREATED_AT+ " TEXT NOT NULL);";

        Log.d(TAG,"created table: "+ SQL_CREATE_DESIRED_RECIPE_TABLE);

        db.execSQL(SQL_CREATE_DESIRED_RECIPE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DesiredRecipeContract.DesiredRecipeEntry.TABLE_NAME);
        onCreate(db);
    }
}
