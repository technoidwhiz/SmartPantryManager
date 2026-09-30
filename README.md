# Smart Pantry Manager

Smart Pantry Manager is an Android application developed in Java that helps users manage pantry ingredients and discover recipes that can be made using the ingredients and quantities available.

## Features

- Add pantry ingredients
- Edit existing ingredients
- Delete ingredients with confirmation
- Store ingredient quantities and measurement units
- SQLite local database storage
- Suggested recipes based on pantry contents
- Quantity-aware recipe matching
- Recipe details with ingredients and preparation method
- Persistent application settings
- Simple and user-friendly interface

## Technologies Used

- Java
- Android Studio
- XML
- SQLite
- SharedPreferences
- Git
- GitHub
- JUnit

## Main Screens

- My Pantry
- Add/Edit Ingredient
- Suggested Recipes
- Recipe Details
- Settings

## Recipe Matching

A recipe is suggested only when all required ingredients exist in the pantry and the available quantities are sufficient.

For example, Banana Milkshake requires:

- 1 banana
- 250 ml milk

If less than 250 ml of milk is available, the recipe is not displayed.

## Testing

The application was tested for:

- Adding ingredients
- Editing ingredients
- Deleting ingredients
- Data persistence
- Recipe matching
- Quantity validation
- Recipe details
- Navigation
- Settings persistence

## Running the Application

1. Open the project in Android Studio.
2. Allow Gradle to sync.
3. Start an Android emulator or connect an Android device.
4. Select the `app` run configuration.
5. Click Run.

## GitHub Repository

https://github.com/technoidwhiz/SmartPantryManager