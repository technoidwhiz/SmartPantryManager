package com.titus.smartpantrymanager;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "pantry_item_id";

    private DatabaseHelper databaseHelper;
    private EditText editIngredientName;
    private EditText editQuantity;
    private Spinner spinnerUnit;

    // An ID of -1 means we are adding a new ingredient.
    private long itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);

        // Keep the form clear of system bars and the keyboard.
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    Insets keyboard = insets.getInsets(
                            WindowInsetsCompat.Type.ime());

                    view.setPadding(
                            bars.left,
                            bars.top,
                            bars.right,
                            Math.max(bars.bottom, keyboard.bottom));

                    return insets;
                });

        databaseHelper = new DatabaseHelper(this);

        editIngredientName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        TextView textFormTitle = findViewById(R.id.textFormTitle);

        ArrayAdapter<CharSequence> unitAdapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.pantry_units,
                        android.R.layout.simple_spinner_item);

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerUnit.setAdapter(unitAdapter);

        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);

        if (itemId != -1) {
            textFormTitle.setText(R.string.edit_ingredient);

            // Do not overwrite restored form values after rotation.
            if (savedInstanceState == null) {
                PantryItem item = databaseHelper.getPantryItem(itemId);

                if (item == null) {
                    Toast.makeText(
                            this,
                            R.string.ingredient_not_found,
                            Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                editIngredientName.setText(item.getName());
                editQuantity.setText(String.valueOf(item.getQuantity()));

                int position = unitAdapter.getPosition(item.getUnit());
                if (position >= 0) {
                    spinnerUnit.setSelection(position);
                }
            }
        }

        findViewById(R.id.buttonSaveIngredient)
                .setOnClickListener(view -> saveIngredient());

        findViewById(R.id.buttonCancel)
                .setOnClickListener(view -> finish());
    }

    private void saveIngredient() {
        String name = editIngredientName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();

        editIngredientName.setError(null);
        editQuantity.setError(null);

        if (name.isEmpty()) {
            editIngredientName.setError(
                    getString(R.string.error_name_required));
            editIngredientName.requestFocus();
            return;
        }

        double quantity;

        try {
            // Accept a decimal point or decimal comma.
            quantity = Double.parseDouble(quantityText.replace(',', '.'));
        } catch (NumberFormatException exception) {
            showQuantityError();
            return;
        }

        if (Double.isNaN(quantity)
                || Double.isInfinite(quantity)
                || quantity <= 0) {
            showQuantityError();
            return;
        }

        String unit = spinnerUnit.getSelectedItem().toString();

        try {
            if (itemId == -1) {
                databaseHelper.addPantryItem(name, quantity, unit);
            } else {
                boolean updated = databaseHelper.updatePantryItem(
                        itemId, name, quantity, unit);

                if (!updated) {
                    Toast.makeText(
                            this,
                            R.string.ingredient_not_found,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(
                    this,
                    R.string.ingredient_saved,
                    Toast.LENGTH_SHORT).show();

            setResult(RESULT_OK);
            finish();

        } catch (SQLiteException exception) {
            Toast.makeText(
                    this,
                    R.string.error_save_failed,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showQuantityError() {
        editQuantity.setError(
                getString(R.string.error_quantity_invalid));
        editQuantity.requestFocus();
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}