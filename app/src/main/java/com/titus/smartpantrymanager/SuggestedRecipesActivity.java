package com.titus.smartpantrymanager;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> recipeAdapter;
    private TextView textRecipeCount;
    private TextView textEmptyRecipes;

    private final List<Recipe> matchingRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });

        databaseHelper = new DatabaseHelper(this);

        textRecipeCount = findViewById(R.id.textRecipeCount);
        textEmptyRecipes = findViewById(R.id.textEmptyRecipes);
        ListView listRecipes = findViewById(R.id.listRecipes);

        recipeAdapter = new ArrayAdapter<>(
                this,
                R.layout.item_recipe,
                new ArrayList<String>());

        listRecipes.setAdapter(recipeAdapter);
        listRecipes.setEmptyView(textEmptyRecipes);

        listRecipes.setOnItemClickListener((parent, view, position, id) -> {

            Recipe selectedRecipe = matchingRecipes.get(position);

            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    RecipeDetailsActivity.class);

            intent.putExtra("recipeName", selectedRecipe.getName());
            intent.putExtra(
                    "recipeIngredients",
                    buildIngredientText(selectedRecipe));

            intent.putExtra(
                    "recipeMethod",
                    selectedRecipe.getMethod());

            startActivity(intent);
        });

        findViewById(R.id.buttonBackToPantry)
                .setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMatchingRecipes();
    }

    private void loadMatchingRecipes() {
        try {

            List<Recipe> allRecipes =
                    databaseHelper.getAllRecipes();

            List<PantryItem> pantryItems =
                    databaseHelper.getAllPantryItems();

            List<Recipe> foundRecipes =
                    RecipeMatcher.findMatches(
                            allRecipes,
                            pantryItems);

            matchingRecipes.clear();
            matchingRecipes.addAll(foundRecipes);

            List<String> recipeNames = new ArrayList<>();

            for (Recipe recipe : matchingRecipes) {
                recipeNames.add(recipe.getName());
            }

            textEmptyRecipes.setText(
                    R.string.empty_recipe_matches);

            recipeAdapter.clear();
            recipeAdapter.addAll(recipeNames);

            textRecipeCount.setText(getString(
                    R.string.recipe_match_count,
                    matchingRecipes.size(),
                    allRecipes.size()));

        } catch (SQLiteException exception) {

            matchingRecipes.clear();
            recipeAdapter.clear();

            textRecipeCount.setText("");
            textEmptyRecipes.setText(
                    R.string.error_load_recipes);
        }
    }

    private String buildIngredientText(Recipe recipe) {

        StringBuilder ingredientsText =
                new StringBuilder();

        for (RecipeIngredient ingredient
                : recipe.getIngredients()) {

            if (ingredientsText.length() > 0) {
                ingredientsText.append("\n");
            }

            ingredientsText.append("- ")
                    .append(ingredient.getName())
                    .append(": ")
                    .append(formatQuantity(
                            ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit());
        }

        return ingredientsText.toString();
    }

    private String formatQuantity(double quantity) {

        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}