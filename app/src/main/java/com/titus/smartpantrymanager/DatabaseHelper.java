package com.titus.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_PANTRY = "pantry_items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(),
                DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL " +
                        "CHECK(length(trim(name)) > 0), " +
                        COLUMN_QUANTITY + " REAL NOT NULL CHECK(quantity > 0), " +
                        COLUMN_UNIT + " TEXT NOT NULL " +
                        "CHECK(length(trim(unit)) > 0))";

        db.execSQL(createPantryTable);
        createRecipeTables(db);
        RecipeSeeder.seed(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {

        // Upgrade existing installations without removing pantry records.
        if (oldVersion < 2) {
            createRecipeTables(db);
            RecipeSeeder.seed(db);
        }
    }

    private void createRecipeTables(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL CHECK(length(trim(name)) > 0), " +
                        "method TEXT NOT NULL CHECK(length(trim(method)) > 0))");

        db.execSQL(
                "CREATE TABLE recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "name TEXT NOT NULL CHECK(length(trim(name)) > 0), " +
                        "quantity REAL NOT NULL CHECK(quantity > 0), " +
                        "unit TEXT NOT NULL CHECK(unit IN " +
                        "('g', 'kg', 'ml', 'l', 'pcs')), " +
                        "FOREIGN KEY(recipe_id) REFERENCES recipes(id) " +
                        "ON DELETE CASCADE)");

        db.execSQL(
                "CREATE INDEX index_recipe_ingredients_recipe_id " +
                        "ON recipe_ingredients(recipe_id)");
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                "recipes",
                new String[]{"id", "name", "method"},
                null, null, null, null,
                "name COLLATE NOCASE ASC")) {

            while (cursor.moveToNext()) {
                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow("id"));

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));

                String method = cursor.getString(
                        cursor.getColumnIndexOrThrow("method"));

                recipes.add(new Recipe(
                        id, name, method, getRecipeIngredients(db, id)));
            }
        }

        return recipes;
    }

    public Recipe getRecipe(long recipeId) {
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                "recipes",
                new String[]{"id", "name", "method"},
                "id = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));

                String method = cursor.getString(
                        cursor.getColumnIndexOrThrow("method"));

                return new Recipe(
                        recipeId,
                        name,
                        method,
                        getRecipeIngredients(db, recipeId));
            }
        }

        return null;
    }

    private List<RecipeIngredient> getRecipeIngredients(
            SQLiteDatabase db, long recipeId) {

        List<RecipeIngredient> ingredients = new ArrayList<>();

        try (Cursor cursor = db.query(
                "recipe_ingredients",
                new String[]{"name", "quantity", "unit"},
                "recipe_id = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, "id ASC")) {

            while (cursor.moveToNext()) {
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("quantity"));

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow("unit"));

                ingredients.add(
                        new RecipeIngredient(name, quantity, unit));
            }
        }

        return ingredients;
    }

    // CREATE: save a new ingredient and return its database ID.
    public long addPantryItem(String name, double quantity, String unit) {
        ContentValues values = createValues(name, quantity, unit);
        SQLiteDatabase db = getWritableDatabase();

        return db.insertOrThrow(TABLE_PANTRY, null, values);
    }

    // READ: return all ingredients in alphabetical order.
    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " COLLATE NOCASE ASC, " + COLUMN_ID + " ASC")) {

            while (cursor.moveToNext()) {
                items.add(readPantryItem(cursor));
            }
        }

        return items;
    }

    // READ: find one ingredient for the edit screen.
    public PantryItem getPantryItem(long id) {
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null)) {

            if (cursor.moveToFirst()) {
                return readPantryItem(cursor);
            }
        }

        return null;
    }

    // UPDATE: change only the ingredient with the supplied ID.
    public boolean updatePantryItem(
            long id, String name, double quantity, String unit) {

        ContentValues values = createValues(name, quantity, unit);
        SQLiteDatabase db = getWritableDatabase();

        int updatedRows = db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        return updatedRows == 1;
    }

    // DELETE: remove only the ingredient with the supplied ID.
    public boolean deletePantryItem(long id) {
        SQLiteDatabase db = getWritableDatabase();

        int deletedRows = db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        return deletedRows == 1;
    }

    // Check values before they reach the database.
    private ContentValues createValues(
            String name, double quantity, String unit) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient name is required.");
        }

        if (Double.isNaN(quantity)
                || Double.isInfinite(quantity)
                || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be a number greater than zero.");
        }

        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("A unit is required.");
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name.trim());
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit.trim());

        return values;
    }

    // Convert one database row into a Java object.
    private PantryItem readPantryItem(Cursor cursor) {
        long id = cursor.getLong(
                cursor.getColumnIndexOrThrow(COLUMN_ID));

        String name = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_NAME));

        double quantity = cursor.getDouble(
                cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));

        String unit = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_UNIT));

        return new PantryItem(id, name, quantity, unit);
    }
}