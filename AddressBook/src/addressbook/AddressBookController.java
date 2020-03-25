
package addressbook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.chalmers.cse.dat215.lab1.*;

public class AddressBookController implements Initializable {
    
    @FXML private MenuBar menuBar;

    @FXML private Button newButton;
    @FXML private Button deleteButton;

    @FXML private ListView listView;

    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField phone;
    @FXML private TextField email;
    @FXML private TextField address;
    @FXML private TextField postCode;
    @FXML private TextField city;

    Presenter presenter;

    private class TextFieldListener implements ChangeListener<Boolean>{

        private TextField textField;

        public TextFieldListener(TextField textField){
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                presenter.textFieldFocusGained(textField);

            }
            else{
                presenter.textFieldFocusLost(textField);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        presenter = new Presenter(
            listView,
            firstName,
            lastName,
            phone,
            email,
            address,
            postCode,
            city);
        presenter.init();


        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                presenter.contactsListChanged();
            }
        });

        firstName.focusedProperty().addListener(new TextFieldListener(firstName));
        lastName.focusedProperty().addListener(new TextFieldListener(lastName));
        phone.focusedProperty().addListener(new TextFieldListener(phone));
        email.focusedProperty().addListener(new TextFieldListener(email));
        address.focusedProperty().addListener(new TextFieldListener(address));
        postCode.focusedProperty().addListener(new TextFieldListener(postCode));
        city.focusedProperty().addListener(new TextFieldListener(city));
    }

    @FXML
    protected void newButtonActionPerformed (ActionEvent event){
        presenter.newContact();
    }

    @FXML
    protected void deleteButtonActionPerformed (ActionEvent event){
        presenter.removeCurrentContact();
    }

    @FXML
    protected void textFieldActionPerformed (ActionEvent event){
        presenter.textFieldActionPerformed(event);
    }

    @FXML 
    protected void openAboutActionPerformed(ActionEvent event) throws IOException{
    
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("addressbook/resources/AddressBook");
        Parent root = FXMLLoader.load(getClass().getResource("address_book_about.fxml"), bundle);
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(root));
        aboutStage.setTitle(bundle.getString("about.title.text"));
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.setResizable(false);
        aboutStage.showAndWait();
    }
    
    @FXML 
    protected void closeApplicationActionPerformed(ActionEvent event) throws IOException{
        
        Stage addressBookStage = (Stage) menuBar.getScene().getWindow();
        addressBookStage.hide();
    }    
}


