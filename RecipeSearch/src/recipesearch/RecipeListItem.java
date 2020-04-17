package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.*;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private Label listItemName;
    @FXML private Label listItemTime;
    @FXML private Label listItemPrice;
    @FXML private Label listItemDescription;
    @FXML private ImageView listItemImage;
    @FXML private ImageView listItemMainIngredient;
    @FXML private ImageView listItemDifficulty;
    @FXML private ImageView listItemCuisine;

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;
        this.listItemName.setText(recipe.getName());
        this.listItemTime.setText(Integer.toString(recipe.getTime()) + " minuter");
        this.listItemPrice.setText(Integer.toString(recipe.getPrice()) + " kr");
        this.listItemDescription.setText(recipe.getDescription());
        this.listItemImage.setImage(parentController.getSquareImage(recipe.getFXImage()));
        this.listItemCuisine.setImage(parentController.getCuisineImage(recipe.getCuisine()));
        this.listItemMainIngredient.setImage(parentController.getMainIngredientImage(recipe.getMainIngredient()));
        this.listItemDifficulty.setImage(parentController.getDifficultyImage(recipe.getDifficulty()));
    }

    @FXML
    protected void onClick(Event event) {
        parentController.openRecipeView(recipe);
    }
}
