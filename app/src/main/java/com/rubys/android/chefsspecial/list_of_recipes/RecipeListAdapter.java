package com.rubys.android.chefsspecial.list_of_recipes;


import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubys.android.chefsspecial.R;
import com.rubys.android.chefsspecial.data.DesiredRecipeContract;

import java.util.Arrays;
import java.util.Objects;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>{

    private static final String TAG = "RecipeListAdapter";

    private final ItemClickListener mItemClickListener;
    private String[] mRecipeNames;

    private Cursor mCursor;

    /**
     * The interface that receives on click listener
     */
    interface ItemClickListener{
        void onItemClick(String recipeName);
        void heartClick(String recipeName,int heart);
    }

    RecipeListAdapter(ItemClickListener clickListener){
        mItemClickListener = clickListener;
    }

    @Override
    public RecipeListAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_rv,parent,false);
        Log.i(TAG,"onCreateViewHolder");
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeListAdapter.RecipeViewHolder holder, int position) {

        if (mRecipeNames != null ){

            String recipeNameAndImage = mRecipeNames[position];
            String[] info = recipeNameAndImage.split(">");
            Log.i(TAG,"info: "+ Arrays.toString(info));

            final String RECIPE_NAME = info[0];
            Log.i(TAG,"RECIPE_NAME: "+ RECIPE_NAME);
            holder.recipeName.setText(RECIPE_NAME);

            String RECIPE_IMAGE = info[1];
            Log.i(TAG,"RECIPE_IMAGE: "+ RECIPE_IMAGE);
            if (Objects.equals(RECIPE_IMAGE, " ")) {
                holder.recipeImage.setImageResource(ImageAsset.getRecipeImages().get(position));
            }

            if (mCursor != null){
                mCursor.moveToFirst();
                while(!mCursor.isAfterLast()){
                    String name = mCursor.getString(mCursor.getColumnIndex(
                            DesiredRecipeContract.DesiredRecipeEntry.COLUMN_RECIPENAME));
                    if (Objects.equals(name,RECIPE_NAME)){
                        holder.whiteHeart.setVisibility(View.INVISIBLE);
                        holder.redHeart.setVisibility(View.VISIBLE);
                        break;
                    }
                    mCursor.moveToNext();
                }

            }
            final int white = 0;
            holder.whiteHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.heartClick(RECIPE_NAME,white);
                    holder.whiteHeart.setVisibility(View.INVISIBLE);
                    holder.redHeart.setVisibility(View.VISIBLE);
                }
            });

            final int red = 1;
            holder.redHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.heartClick(RECIPE_NAME,red);
                    holder.redHeart.setVisibility(View.INVISIBLE);
                    holder.whiteHeart.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        if (mRecipeNames == null) return 0;

        return mRecipeNames.length;
    }

    //RecipeListAdapter View holder
    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView recipeImage;
        final TextView recipeName;
        final FloatingActionButton whiteHeart;
        final  FloatingActionButton redHeart;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            recipeImage = (ImageView) itemView.findViewById(R.id.recipeImage);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
            whiteHeart = (FloatingActionButton) itemView.findViewById(R.id.whiteHeart);
            redHeart = (FloatingActionButton) itemView.findViewById(R.id.redHeart);

            recipeImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG,"RV Clicked");

            mItemClickListener.onItemClick(recipeName.getText().toString());

        }
    }

    void setRecipeNames(String[] recipeNames,Cursor cursor){
        mRecipeNames = recipeNames;
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
