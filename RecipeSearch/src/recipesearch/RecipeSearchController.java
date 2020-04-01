
package recipesearch;

import java.net.URL;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;

public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController backendController = new RecipeBackendController();

    @FXML private ComboBox mainIngredientController;
    @FXML private ComboBox cuisineController;

    @FXML private RadioButton difficultyController1;
    @FXML private RadioButton difficultyController2;
    @FXML private RadioButton difficultyController3;
    @FXML private RadioButton difficultyController4;
    @FXML private ToggleGroup difficultyControllers;

    @FXML private Spinner maxPriceController;

    @FXML private Slider maxTimeController;
    @FXML private Label maxTimeValue;

    @FXML private ImageView detailedRecipeImage;
    @FXML private Label detailedRecipeName;

    @FXML private SplitPane searchPane;
    @FXML private AnchorPane detailedPane;

    @FXML private FlowPane recipeSearchResults;

    List<Recipe> recipeList = new ArrayList<>();
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainIngredientController.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngredientController.getSelectionModel().select("Visa alla");

        cuisineController.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        cuisineController.getSelectionModel().select("Visa alla");

        mainIngredientController.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                backendController.setMainIngredient(newValue);
                updateRecipeList();
            }
        });

        cuisineController.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                backendController.setCuisine(newValue);
                updateRecipeList();
            }
        });

        difficultyController1.setSelected(true);

        difficultyControllers.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(difficultyControllers.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyControllers.getSelectedToggle();
                    backendController.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 250, 0, 10);
        maxPriceController.setValueFactory(valueFactory);

        maxPriceController.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                backendController.setMaxPrice(newValue);
                updateRecipeList();
            }
        });

        maxPriceController.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(!newValue){
                    Integer value = Integer.valueOf(maxPriceController.getEditor().getText());
                    backendController.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });

        maxTimeController.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                maxTimeValue.setText(newValue.intValue() + " minutes");
                if(newValue != null && !newValue.equals(oldValue) && !maxTimeController.isValueChanging()) {
                    backendController.setMaxTime(newValue.intValue());
                    updateRecipeList();
                }
            }
        });

        for(Recipe recipe : backendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        updateRecipeList();
    }

    private void updateRecipeList() {
        recipeSearchResults.getChildren().clear();

        recipeList = backendController.getRecipes();

        for(int i = 0; i < recipeList.size(); i++) {
            recipeSearchResults.getChildren().add(recipeListItemMap.get(recipeList.get(i).getName()));
        }
    }

    private void populateRecipeDetailView(Recipe recipe) {
        detailedRecipeName.setText(recipe.getName());
        detailedRecipeImage.setImage(recipe.getFXImage());
    }

    @FXML
    public void closeRecipeView() {
        searchPane.toFront();
    }

    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        detailedPane.toFront();
    }
}