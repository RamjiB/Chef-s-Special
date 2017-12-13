package com.rubys.android.chefsspecial.recipe_detail_steps;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubys.android.chefsspecial.R;

public class IngredientsListAdapter extends
                            RecyclerView.Adapter<IngredientsListAdapter.IngredientViewHolder>{

    private static final String TAG = "IngredientsListAdapter";

    private Context mContext;
    private String[] mIngredients;


    IngredientsListAdapter(Context context){
        mContext = context;
    }

    @Override
    public IngredientsListAdapter.IngredientViewHolder
                                    onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.i(TAG,"onCreateViewHolder");
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredients_list_rv,
                parent,false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsListAdapter.
                                             IngredientViewHolder holder, int position) {

        if (mIngredients != null) {

            String recipeDetails = mIngredients[position+1];
            String[] recipeInfo = recipeDetails.split("<");

            int QUANTITY = 0;
            String quantity = recipeInfo[QUANTITY];

            int MEASURE = 1;
            String measure = recipeInfo[MEASURE];

            int INGREDIENT = 2;
            String ingredient = recipeInfo[INGREDIENT];

            Log.i(TAG,ingredient + " - " + quantity + " " + measure);


            holder.stepNumber.setText(position + 1 + ". ");
            holder.stepName.setText(ingredient + " - " + quantity + " " + measure);
        }
    }

    @Override
    public int getItemCount() {

        if (mIngredients == null) return 0;

        return mIngredients.length-1;
    }

    //RecipeListAdapter View holder
    class IngredientViewHolder extends RecyclerView.ViewHolder {

        final TextView stepNumber;
        final TextView stepName;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            stepNumber = (TextView) itemView.findViewById(R.id.stepNumber);
            stepName = (TextView) itemView.findViewById(R.id.ingredient);
        }
    }

    void setIngredients(String ingredients){

        mIngredients = ingredients.split(":");
        notifyDataSetChanged();

    }
}
