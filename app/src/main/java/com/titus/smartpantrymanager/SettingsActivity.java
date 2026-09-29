package com.titus.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "smart_pantry_settings";
    public static final String KEY_DEFAULT_UNIT = "default_unit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {

                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            bars.left,
                            bars.top,
                            bars.right,
                            bars.bottom);

                    return insets;
                });

        Spinner spinnerDefaultUnit =
                findViewById(R.id.spinnerDefaultUnit);

        Button buttonSaveSettings =
                findViewById(R.id.buttonSaveSettings);

        Button buttonBackToPantry =
                findViewById(R.id.buttonBackToPantry);

        String[] units = {"g", "ml", "pcs"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerDefaultUnit.setAdapter(adapter);

        SharedPreferences preferences =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String savedUnit =
                preferences.getString(KEY_DEFAULT_UNIT, "g");

        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(savedUnit)) {
                spinnerDefaultUnit.setSelection(i);
                break;
            }
        }

        buttonSaveSettings.setOnClickListener(view -> {

            String selectedUnit =
                    spinnerDefaultUnit.getSelectedItem().toString();

            preferences.edit()
                    .putString(KEY_DEFAULT_UNIT, selectedUnit)
                    .apply();

            Toast.makeText(
                    this,
                    "Settings saved",
                    Toast.LENGTH_SHORT
            ).show();
        });

        buttonBackToPantry.setOnClickListener(
                view -> finish());
    }
}