package com.titus.smartpantrymanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recipe {

    private final long id;
    private final String name;
    private final String method;
    private final List<RecipeIngredient> ingredients;

    public Recipe(
            long id,
            String name,
            String method,
            List<RecipeIngredient> ingredients) {

        this.id = id;
        this.name = name;
        this.method = method;

        // Keep a separate copy of the recipe's required ingredients.
        this.ingredients = Collections.unmodifiableList(
                new ArrayList<>(ingredients));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }
}