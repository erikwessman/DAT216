
package recipesearch;

import java.net.URL;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;

public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController backendController = new RecipeBackendController();

    @FXML
    private ComboBox mainIngredientController;
    @FXML
    private ComboBox cuisineController;

    @FXML
    private RadioButton difficultyController1;
    @FXML
    private RadioButton difficultyController2;
    @FXML
    private RadioButton difficultyController3;
    @FXML
    private RadioButton difficultyController4;
    @FXML
    private ToggleGroup difficultyControllers;

    @FXML
    private Spinner maxPriceController;

    @FXML
    private Slider maxTimeController;
    @FXML
    private Label maxTimeValue;



    @FXML
    private ImageView detailedRecipeImage;
    @FXML
    private ImageView detailedRecipeDifficulty;
    @FXML
    private ImageView detailedRecipeMainIngredient;
    @FXML
    private ImageView detailedRecipeCuisine;

    @FXML
    private Label detailedRecipeName;
    @FXML
    private Label detailedRecipeTime;
    @FXML
    private Label detailedRecipePrice;

    @FXML
    private TextArea detailedRecipeDescription;
    @FXML
    private TextArea detailedRecipeIngredients;
    @FXML
    private TextArea detailedRecipeInstructions;



    @FXML
    private ImageView closeButtonImageView;

    @FXML
    private SplitPane searchPane;
    @FXML
    private StackPane homePane;
    @FXML
    private AnchorPane detailedPane;

    @FXML
    private FlowPane recipeSearchResults;

    List<Recipe> recipeList = new ArrayList<>();
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainIngredientController.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngredientController.getSelectionModel().select("Visa alla");

        cuisineController.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        cuisineController.getSelectionModel().select("Visa alla");

        populateMainIngredientComboBox();
        populateCuisineComboBox();

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
                if (difficultyControllers.getSelectedToggle() != null) {
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

                if (!newValue) {
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
                if (newValue != null && !newValue.equals(oldValue) && !maxTimeController.isValueChanging()) {
                    backendController.setMaxTime(newValue.intValue());
                    updateRecipeList();
                }
            }
        });

        for (Recipe recipe : backendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        updateRecipeList();

    }

    private void updateRecipeList() {
        recipeSearchResults.getChildren().clear();

        recipeList = backendController.getRecipes();

        for (int i = 0; i < recipeList.size(); i++) {
            recipeSearchResults.getChildren().add(recipeListItemMap.get(recipeList.get(i).getName()));
        }
    }

    private void populateRecipeDetailView(Recipe recipe) {
        detailedRecipeName.setText(recipe.getName());
        detailedRecipeTime.setText(Integer.toString(recipe.getTime()) + " minuter");
        detailedRecipePrice.setText(Integer.toString(recipe.getPrice()) + " kr");

        detailedRecipeCuisine.setImage(this.getCuisineImage(recipe.getCuisine()));
        detailedRecipeMainIngredient.setImage(this.getMainIngredientImage(recipe.getMainIngredient()));
        detailedRecipeDifficulty.setImage(this.getDifficultyImage(recipe.getDifficulty()));
        detailedRecipeImage.setImage(this.getSquareImage(recipe.getFXImage()));

        detailedRecipeDescription.setText(recipe.getDescription());
        detailedRecipeInstructions.setText(recipe.getInstruction());

        String ingredients = "";
        for(int i = 0; i < recipe.getIngredients().size(); i++) {
            ingredients += recipe.getIngredients().get(i) + "\n";
        }
        detailedRecipeIngredients.setText(ingredients);
    }

    @FXML
    public void closeRecipeView() {
        homePane.toFront();
    }

    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        detailedPane.toFront();
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredientController.setButtonCell(cellFactory.call(null));
        mainIngredientController.setCellFactory(cellFactory);
    }

    private void populateCuisineComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineController.setButtonCell(cellFactory.call(null));
        cuisineController.setCellFactory(cellFactory);
    }

    public Image getCuisineImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                return null;
        }
    }

    public Image getMainIngredientImage(String mainIngredient) {
        String iconPath;
        switch (mainIngredient) {
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetarisk":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                return null;
        }
    }

    public Image getDifficultyImage(String difficulty) {
        String iconPath;
        switch (difficulty) {
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                return null;
        }
    }

    public Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }

    @FXML
    public void closeButtonMouseEntered(){
        closeButtonImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        //samma princip som ovan, ta rätt bild
        closeButtonImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        //samma princip som ovan, ta rätt bild. Denna metod ska återställa bilden
        //ifall användaren tar bort musen.
        closeButtonImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }
}