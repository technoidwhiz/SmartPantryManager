package com.titus.smartpantrymanager;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RecipeMatcherTest {

    private Recipe recipe(RecipeIngredient... ingredients) {
        return new Recipe(
                1,
                "Test recipe",
                "Test preparation steps",
                Arrays.asList(ingredients));
    }

    private List<Recipe> match(
            Recipe recipe, PantryItem... pantryItems) {

        return RecipeMatcher.findMatches(
                Collections.singletonList(recipe),
                Arrays.asList(pantryItems));
    }

    @Test
    public void includesRecipeWhenEveryQuantityIsExactlyEnough() {
        Recipe recipe = recipe(
                new RecipeIngredient("banana", 1, "pcs"),
                new RecipeIngredient("milk", 250, "ml"));

        List<Recipe> matches = match(
                recipe,
                new PantryItem(1, "banana", 1, "pcs"),
                new PantryItem(2, "milk", 250, "ml"));

        assertEquals(1, matches.size());
        assertEquals(recipe.getId(), matches.get(0).getId());
    }

    @Test
    public void excludesRecipeWhenOneIngredientIsMissing() {
        Recipe recipe = recipe(
                new RecipeIngredient("banana", 1, "pcs"),
                new RecipeIngredient("milk", 250, "ml"));

        assertTrue(match(
                recipe,
                new PantryItem(1, "banana", 1, "pcs")).isEmpty());
    }

    @Test
    public void excludesRecipeWhenQuantityIsTooSmall() {
        Recipe recipe = recipe(
                new RecipeIngredient("milk", 250, "ml"));

        assertTrue(match(
                recipe,
                new PantryItem(1, "milk", 249, "ml")).isEmpty());
    }

    @Test
    public void recognisesPluralNamesAndCapitalLetters() {
        Recipe recipe = recipe(
                new RecipeIngredient("tomato", 2, "pcs"));

        assertEquals(1, match(
                recipe,
                new PantryItem(1, "  TOMATOES  ", 2, "pcs")).size());
    }

    @Test
    public void convertsKilogramsToGrams() {
        Recipe recipe = recipe(
                new RecipeIngredient("flour", 200, "g"));

        assertEquals(1, match(
                recipe,
                new PantryItem(1, "flour", 0.2, "kg")).size());
    }

    @Test
    public void convertsLitresToMillilitres() {
        Recipe recipe = recipe(
                new RecipeIngredient("milk", 250, "ml"));

        assertEquals(1, match(
                recipe,
                new PantryItem(1, "milk", 0.25, "l")).size());
    }

    @Test
    public void excludesInsufficientQuantityAfterConversion() {
        Recipe recipe = recipe(
                new RecipeIngredient("milk", 250, "ml"));

        assertTrue(match(
                recipe,
                new PantryItem(1, "milk", 0.2, "l")).isEmpty());
    }

    @Test
    public void combinesSeparatePantryRecords() {
        Recipe recipe = recipe(
                new RecipeIngredient("flour", 250, "g"));

        assertEquals(1, match(
                recipe,
                new PantryItem(1, "flour", 100, "g"),
                new PantryItem(2, "flour", 0.15, "kg")).size());
    }

    @Test
    public void doesNotTreatWeightAsVolume() {
        Recipe recipe = recipe(
                new RecipeIngredient("milk", 250, "ml"));

        assertTrue(match(
                recipe,
                new PantryItem(1, "milk", 500, "g")).isEmpty());
    }

    @Test
    public void addsRepeatedRecipeRequirementsTogether() {
        Recipe recipe = recipe(
                new RecipeIngredient("flour", 100, "g"),
                new RecipeIngredient("flour", 150, "g"));

        assertTrue(match(
                recipe,
                new PantryItem(1, "flour", 200, "g")).isEmpty());

        assertEquals(1, match(
                recipe,
                new PantryItem(1, "flour", 250, "g")).size());
    }

    @Test
    public void emptyPantryProducesNoMatches() {
        Recipe recipe = recipe(
                new RecipeIngredient("egg", 2, "pcs"));

        assertTrue(match(recipe).isEmpty());
    }

    @Test
    public void recipeWithoutIngredientsIsExcluded() {
        Recipe recipe = recipe();

        assertTrue(match(
                recipe,
                new PantryItem(1, "egg", 2, "pcs")).isEmpty());
    }
}