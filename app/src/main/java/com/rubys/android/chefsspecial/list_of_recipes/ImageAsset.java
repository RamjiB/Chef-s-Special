package com.rubys.android.chefsspecial.list_of_recipes;



import com.rubys.android.chefsspecial.R;

import java.util.ArrayList;
import java.util.List;

class ImageAsset {

    private static final List<Integer> recipeImages = new ArrayList<Integer>(){{
        add(R.drawable.nutella_pie);
        add(R.drawable.brownies);
        add(R.drawable.yellow_cake);
        add(R.drawable.cheesecake);
    }};

    public static List<Integer> getRecipeImages() {
        return recipeImages;
    }
}
