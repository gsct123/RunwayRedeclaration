package Controller;

import Model.Airport;
import Model.Helper.Utility;
import Model.User;
import View.Error;
import View.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class AddNewUserController implements Initializable {
    @FXML
    public Label titleLabel;
    @FXML
    public MenuButton airportMenu;
    @FXML
    public TextField nameField;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label airportLabel;
    @FXML
    public Label airportNameLabel;

    public static User newUser = null;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final Airport[] airportSelected = new Airport[1];
        System.out.println(Main.getRole());
        if(Main.getRole() == 1){
            titleLabel.setText("Set Up Manager Account");
            airportMenu.setVisible(true);
            airportLabel.setVisible(false);
            airportNameLabel.setVisible(false);
            for(Airport airport: MainController.airports){
                if(!MainController.managerMap.containsValue(airport)){
                    MenuItem airportItem = new MenuItem(airport.getName());
                    airportItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            airportSelected[0] = airport;
                        }
                    });
                    airportMenu.getItems().add(airportItem);
                }
            }
        } else{
            titleLabel.setText("Add New User");
            airportMenu.setVisible(false);
            airportLabel.setVisible(true);
            airportNameLabel.setVisible(true);
            airportNameLabel.setText(MainController.managerMap.get(Main.getUsername()).getName());
            nameField.setDisable(false);
            airportSelected[0] = MainController.managerMap.get(Main.getUsername());
        }

        saveButton.setOnAction(actionEvent -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            if(name.length() == 0 || username.length() == 0 || password.length() == 0 || confirmPassword.length() == 0){
                new Error().errorPopUp("Please fill in all the fields.");
            } else{
                //check username
                if(LoginController.users.containsKey(username)){
                    new Error().errorPopUp("Username already exist, please try another username");
                    passwordField.clear();
                    confirmPasswordField.clear();
                } else if(password.length() < 6){
                    new Error().showError(passwordField, "Please ensure password length is at least 6 characters.", "");
                    confirmPasswordField.clear();
                } else if (!checkPassword(password)){
                    new Error().showError(passwordField, "Please ensure your password contains mix of symbols, lowercase and uppercase letters and numbers.", "");
                    confirmPasswordField.clear();
                } else if(!confirmPassword.equals(password)){
                    new Error().showError(confirmPasswordField, "Passwords do not match", "");
                } else{
                    try {
                        password = Utility.toHexString(Utility.getSHA(password));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    newUser = new User(username, name, password, airportSelected[0].getID(), 3);
                    UserManagerController.helperStage.close();
                }
            }
        });
        cancelButton.setOnAction(actionEvent -> {UserManagerController.helperStage.close();});
    }

    public static boolean checkPassword(String password) {
        boolean hasSymbol = false;
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasNumber = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                if (Character.isLowerCase(c)) {
                    hasLowercase = true;
                } else if (Character.isUpperCase(c)) {
                    hasUppercase = true;
                }
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else {
                hasSymbol = true;
            }
        }

        return hasSymbol && hasLowercase && hasUppercase && hasNumber;
    }

}
