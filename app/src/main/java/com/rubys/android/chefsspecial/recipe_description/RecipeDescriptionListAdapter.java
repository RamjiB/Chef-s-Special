package com.rubys.android.chefsspecial.recipe_description;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubys.android.chefsspecial.R;

public class RecipeDescriptionListAdapter extends
        RecyclerView.Adapter<RecipeDescriptionListAdapter.RecipeDescriptionViewHolder>{

    private static final String TAG = "RecipeDescripListAdap";

    private final ItemClickListener mItemClickListener;

    private String[] mRecipeDetails;

    /**
     * The interface that receives on click listener
     */
    interface ItemClickListener{
        void onItemClick(String[] recipeDetails,int adapterPosition);
    }

    RecipeDescriptionListAdapter(ItemClickListener clickListener){
        mItemClickListener = clickListener;
    }

    @Override
    public RecipeDescriptionListAdapter.RecipeDescriptionViewHolder
                                            onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.i(TAG,"onCreateViewHolder");
        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_recipe_description_rv,
                                                                                    parent,false);
        return new RecipeDescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeDescriptionListAdapter.
                                             RecipeDescriptionViewHolder holder, int position) {

        Log.i(TAG,"onBindViewHolder");

        if (mRecipeDetails != null) {

            String recipeDetails = mRecipeDetails[position];
            String[] recipeInfo = recipeDetails.split(">");

            int STEP_NAME = 0;
            String stepName = recipeInfo[STEP_NAME];


            holder.stepNumber.setText(position + 1 + ". ");
            holder.stepName.setText(stepName);
        }
    }

    @Override
    public int getItemCount() {

        Log.i(TAG,"getItemCount");
        if (mRecipeDetails == null) return 0;

        return mRecipeDetails.length;
    }

    //RecipeListAdapter View holder
    class RecipeDescriptionViewHolder extends RecyclerView.ViewHolder implements
                                                                            View.OnClickListener{


        final TextView stepNumber;
        final TextView stepName;

        public RecipeDescriptionViewHolder(View itemView) {
            super(itemView);

            Log.i(TAG,"RecipeDescriptionViewHolder");

            stepNumber = (TextView) itemView.findViewById(R.id.descriptionStepNumber);
            stepName = (TextView) itemView.findViewById(R.id.descriptionStepName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            if (mRecipeDetails != null){

                mItemClickListener.onItemClick(mRecipeDetails,adapterPosition);
            }
        }
    }

    void setRecipeDescriptionSteps(String[] recipeDetails){

        mRecipeDetails = recipeDetails;
        notifyDataSetChanged();

    }
}
