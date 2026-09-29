package com.titus.smartpantrymanager;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private PantryAdapter pantryAdapter;
    private TextView textEmptyPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttonSuggestedRecipes)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class);
                    startActivity(intent);
                });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            bars.left, bars.top, bars.right, bars.bottom);

                    return insets;
                });

        databaseHelper = new DatabaseHelper(this);
        pantryAdapter = new PantryAdapter(this);

        ListView listPantry = findViewById(R.id.listPantry);
        textEmptyPantry = findViewById(R.id.textEmptyPantry);

        listPantry.setAdapter(pantryAdapter);
        listPantry.setEmptyView(textEmptyPantry);

        findViewById(R.id.buttonAddIngredient)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(
                            MainActivity.this,
                            AddEditIngredientActivity.class);
                    startActivity(intent);
                });
        findViewById(R.id.buttonSettings)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(
                            MainActivity.this,
                            SettingsActivity.class);
                    startActivity(intent);
                });
        listPantry.setOnItemClickListener(
                (parent, view, position, id) -> {
                    PantryItem item = pantryAdapter.getItem(position);
                    showItemActions(item);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload after returning from the add/edit screen.
        refreshPantry();
    }

    private void refreshPantry() {
        try {
            pantryAdapter.setItems(databaseHelper.getAllPantryItems());
            textEmptyPantry.setText(R.string.empty_pantry);
        } catch (SQLiteException exception) {
            textEmptyPantry.setText(R.string.error_load_pantry);

            Toast.makeText(
                    this,
                    R.string.error_load_pantry,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showItemActions(PantryItem item) {
        String[] actions = {
                getString(R.string.action_edit),
                getString(R.string.action_delete)
        };

        new AlertDialog.Builder(this)
                .setTitle(item.getName())
                .setItems(actions, (dialog, choice) -> {
                    if (choice == 0) {
                        Intent intent = new Intent(
                                MainActivity.this,
                                AddEditIngredientActivity.class);

                        intent.putExtra(
                                AddEditIngredientActivity.EXTRA_ITEM_ID,
                                item.getId());

                        startActivity(intent);
                    } else {
                        confirmDelete(item);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void confirmDelete(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_ingredient_title)
                .setMessage(getString(
                        R.string.delete_ingredient_message,
                        item.getName()))
                .setPositiveButton(
                        R.string.action_delete,
                        (dialog, which) -> deleteIngredient(item))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteIngredient(PantryItem item) {
        try {
            boolean deleted = databaseHelper.deletePantryItem(item.getId());

            int message = deleted
                    ? R.string.ingredient_deleted
                    : R.string.ingredient_not_found;

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            refreshPantry();

        } catch (SQLiteException exception) {
            Toast.makeText(
                    this,
                    R.string.error_delete_failed,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}