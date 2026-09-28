package com.titus.smartpantrymanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public final class RecipeSeeder {

    private RecipeSeeder() {
        // This class only contains static helper methods.
    }

    public static void seed(SQLiteDatabase db) {

        addRecipe(db, "Banana Milkshake",
                "1. Peel and slice the banana.\n"
                        + "2. Blend with the milk until smooth.\n"
                        + "3. Pour into a glass and serve.",
                ingredient("banana", 1, "pcs"),
                ingredient("milk", 250, "ml"));

        addRecipe(db, "Banana Yoghurt Bowl",
                "1. Spoon the yoghurt into a bowl.\n"
                        + "2. Peel and slice the banana.\n"
                        + "3. Add the banana and stir gently.",
                ingredient("banana", 1, "pcs"),
                ingredient("plain yoghurt", 150, "g"));

        addRecipe(db, "Apple Yoghurt Bowl",
                "1. Wash, core and dice the apple.\n"
                        + "2. Spoon the yoghurt into a bowl.\n"
                        + "3. Stir in the apple pieces.",
                ingredient("apple", 1, "pcs"),
                ingredient("plain yoghurt", 150, "g"));

        addRecipe(db, "Overnight Oats",
                "1. Mix the oats and milk in a covered container.\n"
                        + "2. Refrigerate overnight.\n"
                        + "3. Stir before serving.",
                ingredient("oats", 50, "g"),
                ingredient("milk", 150, "ml"));

        addRecipe(db, "Banana Overnight Oats",
                "1. Mash the peeled banana.\n"
                        + "2. Mix with the oats and milk.\n"
                        + "3. Cover and refrigerate overnight.",
                ingredient("banana", 1, "pcs"),
                ingredient("oats", 50, "g"),
                ingredient("milk", 150, "ml"));

        addRecipe(db, "Peanut Butter Banana Toast",
                "1. Toast the bread.\n"
                        + "2. Spread the peanut butter over the toast.\n"
                        + "3. Top with sliced banana.",
                ingredient("bread", 60, "g"),
                ingredient("peanut butter", 20, "g"),
                ingredient("banana", 1, "pcs"));

        addRecipe(db, "Cheese Toast",
                "1. Place the cheese on the bread.\n"
                        + "2. Grill until the cheese melts and bubbles.\n"
                        + "3. Serve warm.",
                ingredient("bread", 60, "g"),
                ingredient("cheese", 40, "g"));

        addRecipe(db, "Tomato Cheese Toast",
                "1. Slice the tomato and place it on the bread.\n"
                        + "2. Cover with cheese.\n"
                        + "3. Grill until the cheese melts.",
                ingredient("bread", 60, "g"),
                ingredient("tomato", 1, "pcs"),
                ingredient("cheese", 40, "g"));

        addRecipe(db, "Cucumber Cheese Sandwich",
                "1. Wash and thinly slice the cucumber.\n"
                        + "2. Place the cucumber and cheese between the bread.\n"
                        + "3. Cut and serve.",
                ingredient("bread", 60, "g"),
                ingredient("cucumber", 80, "g"),
                ingredient("cheese", 40, "g"));

        addRecipe(db, "Tuna Sandwich",
                "1. Mix the drained tuna with the mayonnaise.\n"
                        + "2. Spread the mixture over the bread.\n"
                        + "3. Close the sandwich and serve.",
                ingredient("bread", 60, "g"),
                ingredient("canned tuna", 100, "g"),
                ingredient("mayonnaise", 20, "g"));

        addRecipe(db, "Tomato Cucumber Salad",
                "1. Wash and chop the tomato and cucumber.\n"
                        + "2. Place in a bowl.\n"
                        + "3. Toss with the olive oil.",
                ingredient("tomato", 1, "pcs"),
                ingredient("cucumber", 100, "g"),
                ingredient("olive oil", 10, "ml"));

        addRecipe(db, "Chickpea Tomato Salad",
                "1. Chop the tomato and onion.\n"
                        + "2. Combine with the drained canned chickpeas.\n"
                        + "3. Toss with olive oil.",
                ingredient("canned chickpeas", 150, "g"),
                ingredient("tomato", 1, "pcs"),
                ingredient("onion", 30, "g"),
                ingredient("olive oil", 10, "ml"));

        addRecipe(db, "Tuna Sweetcorn Salad",
                "1. Drain the canned tuna and sweetcorn.\n"
                        + "2. Combine them in a bowl.\n"
                        + "3. Stir in the mayonnaise.",
                ingredient("canned tuna", 100, "g"),
                ingredient("canned sweetcorn", 100, "g"),
                ingredient("mayonnaise", 20, "g"));

        addRecipe(db, "Carrot Cabbage Slaw",
                "1. Wash and grate the carrot.\n"
                        + "2. Wash and finely slice the cabbage.\n"
                        + "3. Mix both with the mayonnaise.",
                ingredient("carrot", 100, "g"),
                ingredient("cabbage", 100, "g"),
                ingredient("mayonnaise", 30, "g"));

        addRecipe(db, "Scrambled Eggs",
                "1. Beat the eggs with the milk.\n"
                        + "2. Melt the butter in a frying pan over gentle heat.\n"
                        + "3. Add the eggs and stir until fully set.",
                ingredient("egg", 2, "pcs"),
                ingredient("milk", 30, "ml"),
                ingredient("butter", 10, "g"));

        addRecipe(db, "Cheese Omelette",
                "1. Beat the eggs and grate the cheese.\n"
                        + "2. Melt the butter in a frying pan.\n"
                        + "3. Add the eggs and cook gently.\n"
                        + "4. Add the cheese, fold and cook until the eggs are set.",
                ingredient("egg", 2, "pcs"),
                ingredient("cheese", 30, "g"),
                ingredient("butter", 10, "g"));

        addRecipe(db, "Tomato Omelette",
                "1. Chop the tomato and beat the eggs.\n"
                        + "2. Heat the olive oil and soften the tomato.\n"
                        + "3. Add the eggs and cook until fully set.",
                ingredient("egg", 2, "pcs"),
                ingredient("tomato", 1, "pcs"),
                ingredient("olive oil", 10, "ml"));

        addRecipe(db, "French Toast",
                "1. Beat the egg and milk in a shallow bowl.\n"
                        + "2. Dip the bread into the mixture.\n"
                        + "3. Melt the butter in a frying pan.\n"
                        + "4. Fry both sides until golden and cooked through.",
                ingredient("bread", 60, "g"),
                ingredient("egg", 1, "pcs"),
                ingredient("milk", 60, "ml"),
                ingredient("butter", 10, "g"));

        addRecipe(db, "Banana Pancakes",
                "1. Mash the peeled banana.\n"
                        + "2. Mix in the egg and flour.\n"
                        + "3. Heat the oil in a frying pan.\n"
                        + "4. Cook small pancakes on both sides until set.",
                ingredient("banana", 1, "pcs"),
                ingredient("egg", 1, "pcs"),
                ingredient("flour", 30, "g"),
                ingredient("olive oil", 5, "ml"));

        addRecipe(db, "Garlic Butter Toast",
                "1. Peel and finely chop the garlic.\n"
                        + "2. Mix it with softened butter.\n"
                        + "3. Spread over the bread.\n"
                        + "4. Grill until golden.",
                ingredient("bread", 60, "g"),
                ingredient("garlic", 5, "g"),
                ingredient("butter", 15, "g"));
    }

    private static RecipeIngredient ingredient(
            String name, double quantity, String unit) {

        return new RecipeIngredient(name, quantity, unit);
    }

    private static void addRecipe(
            SQLiteDatabase db,
            String name,
            String method,
            RecipeIngredient... ingredients) {

        ContentValues recipeValues = new ContentValues();
        recipeValues.put("name", name);
        recipeValues.put("method", method);

        long recipeId = db.insertOrThrow(
                "recipes", null, recipeValues);

        for (RecipeIngredient ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();

            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("name", ingredient.getName());
            ingredientValues.put("quantity", ingredient.getQuantity());
            ingredientValues.put("unit", ingredient.getUnit());

            db.insertOrThrow(
                    "recipe_ingredients", null, ingredientValues);
        }
    }
}