package com.rubys.android.chefsspecial;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.rubys.android.chefsspecial.list_of_recipes.ListOfRecipes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ListOfRecipesActivityUITest {

    @Rule
    public ActivityTestRule<ListOfRecipes> activityTestRule = new ActivityTestRule<>(ListOfRecipes.class);


    @Test
    public void RecipeListDisplayed(){
        onView(withId(R.id.recipeListRV)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnRecyclerView(){

        onView(withId(R.id.recipeListRV)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));

        onView(withId(R.id.recipeDescriptionFragment)).check(matches(isDisplayed()));
    }

}
