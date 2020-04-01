package recipesearch;

import se.chalmers.ait.dat215.lab2.*;

import java.util.List;

public class RecipeBackendController {
    private String cuisine;
    private String mainIngredient;
    private String difficulty;
    private int maxPrice;
    private int maxTime;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = db.search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
        return recipes;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setMaxPrice(int maxPrice)  {
        this.maxPrice = maxPrice;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }
}
