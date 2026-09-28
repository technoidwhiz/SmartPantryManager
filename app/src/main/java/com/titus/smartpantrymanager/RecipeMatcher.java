package com.titus.smartpantrymanager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class RecipeMatcher {

    private RecipeMatcher() {
        // This class only contains static matching methods.
    }

    public static List<Recipe> findMatches(
            List<Recipe> recipes, List<PantryItem> pantryItems) {

        Map<String, BigDecimal> available = new HashMap<>();

        // Combine pantry records for the same ingredient and unit type.
        for (PantryItem item : pantryItems) {
            String key = ingredientKey(item.getName(), item.getUnit());

            if (key != null && isValidQuantity(item.getQuantity())) {
                BigDecimal quantity = toBaseQuantity(
                        item.getQuantity(), item.getUnit());

                addQuantity(available, key, quantity);
            }
        }

        List<Recipe> matches = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (canMake(recipe, available)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    private static boolean canMake(
            Recipe recipe, Map<String, BigDecimal> available) {

        // An empty ingredient list is not a valid recipe.
        if (recipe.getIngredients().isEmpty()) {
            return false;
        }

        Map<String, BigDecimal> required = new HashMap<>();

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            String key = ingredientKey(
                    ingredient.getName(), ingredient.getUnit());

            if (key == null || !isValidQuantity(ingredient.getQuantity())) {
                return false;
            }

            BigDecimal quantity = toBaseQuantity(
                    ingredient.getQuantity(), ingredient.getUnit());

            // Also combine repeated ingredients within a recipe.
            addQuantity(required, key, quantity);
        }

        for (Map.Entry<String, BigDecimal> entry : required.entrySet()) {
            BigDecimal pantryQuantity = available.get(entry.getKey());

            if (pantryQuantity == null) {
                return false;
            }

            if (pantryQuantity.compareTo(entry.getValue()) < 0) {
                return false;
            }
        }

        return true;
    }

    private static void addQuantity(
            Map<String, BigDecimal> quantities,
            String key,
            BigDecimal quantity) {

        BigDecimal existing = quantities.get(key);

        if (existing == null) {
            quantities.put(key, quantity);
        } else {
            quantities.put(key, existing.add(quantity));
        }
    }

    private static boolean isValidQuantity(double quantity) {
        return !Double.isNaN(quantity)
                && !Double.isInfinite(quantity)
                && quantity > 0;
    }

    private static String ingredientKey(String name, String unit) {
        String normalName = normaliseName(name);
        String baseUnit = baseUnit(unit);

        if (normalName.isEmpty() || baseUnit == null) {
            return null;
        }

        // Keep weight, volume and individual-item counts separate.
        return normalName + "\n" + baseUnit;
    }

    private static String baseUnit(String unit) {
        switch (cleanText(unit)) {
            case "g":
            case "kg":
                return "g";

            case "ml":
            case "l":
                return "ml";

            case "pcs":
                return "pcs";

            default:
                return null;
        }
    }

    private static BigDecimal toBaseQuantity(
            double quantity, String unit) {

        BigDecimal result = BigDecimal.valueOf(quantity);
        String normalUnit = cleanText(unit);

        if (normalUnit.equals("kg") || normalUnit.equals("l")) {
            result = result.multiply(BigDecimal.valueOf(1000));
        }

        return result;
    }

    private static String normaliseName(String name) {
        String normalName = cleanText(name);

        // Explicit aliases avoid changing unrelated ingredient names.
        switch (normalName) {
            case "tomatoes":
                return "tomato";
            case "potatoes":
                return "potato";
            case "eggs":
                return "egg";
            case "bananas":
                return "banana";
            case "apples":
                return "apple";
            case "onions":
                return "onion";
            case "carrots":
                return "carrot";
            case "cucumbers":
                return "cucumber";
            case "cabbages":
                return "cabbage";
            case "plain yogurt":
                return "plain yoghurt";
            case "tinned tuna":
                return "canned tuna";
            case "tinned chickpeas":
                return "canned chickpeas";
            case "tinned sweetcorn":
                return "canned sweetcorn";
            default:
                return normalName;
        }
    }

    private static String cleanText(String text) {
        if (text == null) {
            return "";
        }

        return text.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }
}