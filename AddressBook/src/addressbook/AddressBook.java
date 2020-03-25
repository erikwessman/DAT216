
package addressbook;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import se.chalmers.cse.dat215.lab1.Model;

public class AddressBook extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("addressbook/resources/AddressBook");
        
        Parent root = FXMLLoader.load(getClass().getResource("address_book.fxml"), bundle);
        
        Scene scene = new Scene(root, 600, 400);
        
        stage.setTitle(bundle.getString("application.name"));
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.getIcons().add(new Image("addressbook/resources/frameIcon32.gif"));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Model.getInstance().shutDown();
            }
        }));
    }
    
}
