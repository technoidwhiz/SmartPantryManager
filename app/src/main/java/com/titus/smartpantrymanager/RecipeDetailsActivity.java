package com.titus.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        TextView textRecipeName = findViewById(R.id.textRecipeName);
        TextView textRecipeIngredients = findViewById(R.id.textRecipeIngredients);
        TextView textRecipeMethod = findViewById(R.id.textRecipeMethod);
        Button buttonBackToRecipes = findViewById(R.id.buttonBackToRecipes);

        String recipeName = getIntent().getStringExtra("recipeName");
        String recipeIngredients = getIntent().getStringExtra("recipeIngredients");
        String recipeMethod = getIntent().getStringExtra("recipeMethod");

        if (recipeName != null) {
            textRecipeName.setText(recipeName);
        }

        if (recipeIngredients != null) {
            textRecipeIngredients.setText(recipeIngredients);
        }

        if (recipeMethod != null) {
            textRecipeMethod.setText(recipeMethod);
        }

        buttonBackToRecipes.setOnClickListener(view -> {
            Intent intent = new Intent(
                    RecipeDetailsActivity.this,
                    SuggestedRecipesActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            view.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });
    }
}