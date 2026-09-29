# Smart Pantry Manager - Testing

## Final Manual Testing

The application was tested on an Android emulator using Android Studio.

### Pantry Management
- Add ingredient: Passed
- Edit ingredient: Passed
- Delete ingredient: Passed
- Pantry data persistence: Passed

### Recipe Matching
- Recipe suggested when all required ingredients and quantities are available: Passed
- Recipe not suggested when quantity is insufficient: Passed
- Banana Milkshake test with 1 banana and 250 ml milk: Passed
- Banana Milkshake removed when milk was reduced to 200 ml: Passed

### Recipe Details
- Recipe name displayed correctly: Passed
- Ingredients displayed correctly: Passed
- Preparation method displayed correctly: Passed
- Back navigation to Suggested Recipes: Passed

### Settings
- Settings screen opens correctly: Passed
- Default unit can be changed: Passed
- Setting remains saved after leaving and reopening the screen: Passed

### Navigation
- My Pantry to Suggested Recipes: Passed
- Suggested Recipes to Recipe Details: Passed
- Recipe Details back navigation: Passed
- My Pantry to Settings: Passed
- Settings back navigation: Passed

## Unit Testing

Unit tests were used to test the recipe-matching logic, including matching ingredients and checking required quantities.

## Final Result

All core application features passed the final manual test.