package Controller;

import Controller.Helper.AddNewUserController;
import Model.Airport;
import Model.Helper.XMLParserWriter;
import Model.User;
import View.Error;
import View.*;
import View.OtherPopUp.Confirmation;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.*;

public class UserManagerController implements Initializable {
    private static final int INACTIVITY_TIMEOUT = 3 * 60 * 1000; // 3 seconds in milliseconds

    private Timer inactivityTimer;
    @FXML
    private Label identityLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private TableView<User> userTable;
    @FXML
    private Button infoButton;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<User, String> usernameCol;
    @FXML
    private TableColumn<User, String> nameCol;
    @FXML
    private TableColumn<User, String> airportCol;
    @FXML
    private TableColumn<User, String> airportIDCol;
    @FXML
    private Button addUserButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button addManagerButton;
    @FXML
    private MenuItem airportManager;
    @FXML
    private MenuItem logoutItem;
    @FXML
    private MenuItem backToMain;
    @FXML
    private MenuItem aboutProject;

    public static Stage helperStage;

    private ObservableList<User> userData = FXCollections.observableArrayList();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetInactivityTimer();
        identityLabel.setText("Logged in as "+ Main.getUsername());

        infoButton.setOnMouseEntered(event -> {
            resetInactivityTimer();
            infoLabel.setVisible(true);
        });
        infoButton.setOnMouseExited(event -> {
            resetInactivityTimer();
            infoLabel.setVisible(false);
        });

        if(Main.getRole() == 1){
            userData = FXCollections.observableArrayList(MainController.managers.values().stream().toList());
            addManagerButton.setVisible(true);
            addUserButton.setVisible(false);
            deleteUserButton.setVisible(false);
            airportManager.setVisible(true);
            //adduser
            //edit username and name
            addManagerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    inactivityTimer.cancel();
                    helperStage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddNewUser.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheet.css")).toExternalForm());

                    helperStage.setTitle("Set Up Manager Account");
                    helperStage.setScene(scene);
                    helperStage.setResizable(false);
                    helperStage.initModality(Modality.APPLICATION_MODAL);
                    helperStage.showAndWait();


                    if(AddNewUserController.newUser != null){
                        User newUser = AddNewUserController.newUser;
                        LoginController.users.put(newUser.getUsername(), newUser);
                        MainController.managerMap.put(newUser.getUsername(), MainController.airportMap.get(newUser.getAirportID()));
                        MainController.managers.put(newUser.getUsername(), newUser);
                        userData = FXCollections.observableArrayList(MainController.managers.values().stream().toList());
                        userTable.setItems(userData);
                        userTable.refresh();

                        try {
                            XMLParserWriter.updateUserXML(FXCollections.observableArrayList(LoginController.users.values()), "src/Data/users.xml");
                        } catch (ParserConfigurationException | TransformerException e) {
                            e.printStackTrace();
                        }
                        new Notification(UserManager.getStage()).sucessNotification("Successful Action", "Manager account has been set up.");
                    }
                    resetInactivityTimer();
                }
            });
        } else if (Main.getRole() == 2) {
            userData = FXCollections.observableArrayList(MainController.users.getOrDefault(MainController.managerMap.get(Main.getUsername()), new ArrayList<>()));
            addManagerButton.setVisible(false);
            addUserButton.setVisible(true);
            deleteUserButton.setVisible(true);
            airportManager.setVisible(false);
            addUserButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    inactivityTimer.cancel();
                    helperStage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddNewUser.fxml"));

                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheet.css")).toExternalForm());

                    helperStage.setTitle("Set Up Manager Account");
                    helperStage.setScene(scene);
                    helperStage.setResizable(false);
                    helperStage.initModality(Modality.APPLICATION_MODAL);
                    helperStage.showAndWait();

                    if(AddNewUserController.newUser != null){
                        User newUser = AddNewUserController.newUser;
                        LoginController.users.put(newUser.getUsername(), newUser);
                        ArrayList<User> original = MainController.users.getOrDefault(MainController.airportMap.get(newUser.getAirportID()), new ArrayList<>());
                        original.add(newUser);
                        MainController.users.put(MainController.airportMap.get(newUser.getAirportID()), original);
                        userData = FXCollections.observableArrayList(MainController.users.get(MainController.managerMap.get(Main.getUsername())));
                        userTable.setItems(userData);
                        userTable.refresh();
                        try {
                            XMLParserWriter.updateUserXML(FXCollections.observableArrayList(LoginController.users.values()), "src/Data/users.xml");
                        } catch (ParserConfigurationException | TransformerException e) {
                            e.printStackTrace();
                        }
                        new Notification(UserManager.getStage()).sucessNotification("Successful Action", "User added to system.");
                    }
                    resetInactivityTimer();
                }
            });

            deleteUserButton.setOnAction(actionEvent -> {
                inactivityTimer.cancel();
                User user = userTable.getSelectionModel().getSelectedItem();
                if(user == null){
                    new Error().errorPopUp("Please select a user to be deleted");
                } else{
                    boolean res = new Confirmation().confirm("Are you sure you want to delete the selected user?", "This action cannot be undone.");
                    if(res){
                        LoginController.users.remove(user.getUsername());
                        Airport airport = MainController.airportMap.get(user.getAirportID());
                        ArrayList<User> original = MainController.users.get(airport);
                        original.remove(user);
                        MainController.users.put(airport, original);
                        userData = FXCollections.observableArrayList(MainController.users.get(MainController.managerMap.get(Main.getUsername())));
                        userTable.setItems(userData);
                        userTable.refresh();
                        try {
                            XMLParserWriter.updateUserXML(FXCollections.observableArrayList(LoginController.users.values()), "src/Data/users.xml");
                        } catch (ParserConfigurationException | TransformerException e) {
                            e.printStackTrace();
                        }
                        new Notification(UserManager.getStage()).sucessNotification("Successful Action", "User has been deleted from system.");
                    }
                }
                resetInactivityTimer();
            });
        }
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            resetInactivityTimer();
            //refer list of users
            userTable.setItems(filterList(userData, newValue));
        });

        userTable.setEditable(false);

        usernameCol.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        airportCol.setCellValueFactory(cellData -> {
            String airportID = cellData.getValue().getAirportID();
            Airport airport = MainController.airportMap.get(airportID);
            if(airport == null){
                return new SimpleStringProperty("NULL");
            } else{
                return new SimpleStringProperty(MainController.airportMap.get(airportID).getName());
            }
        });
        userTable.setItems(userData);
        userTable.refresh();
        initShortcut();
    }


    @FXML
    public void goAirportManager(ActionEvent event) throws Exception {
        inactivityTimer.cancel();
        try {
            UserManager.getStage().close();
            new AirportManager(AirportManager.getUsername()).start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToMain(ActionEvent event) throws Exception {
        inactivityTimer.cancel();
        UserManager.getStage().close();
        Main.getStage().show();
    }

    @FXML
    public void handleLogout(ActionEvent event) throws Exception {
        inactivityTimer.cancel();
        UserManager.getStage().close();
        new Login().start(new Stage());
    }

    @FXML
    public void loadAboutProject(ActionEvent event){
        resetInactivityTimer();
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SEG-Group-1-2023/ProjectRelatedInformation/blob/main/runwayprojectdefinition.pdf"));
        } catch (IOException | URISyntaxException ignored) {}
    }



    private ObservableList<User> filterList(List<User> list, String searchText){
        List<User> filteredList = new ArrayList<>();
        for (User user : list){
            if(searchFindAirport(user, searchText)) filteredList.add(user);
        }
        userTable.refresh();
        return FXCollections.observableList(filteredList);
    }

    private boolean searchFindAirport(User user, String searchText){
        return (user.getUsername().toLowerCase().contains(searchText.toLowerCase()) ||
                user.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                MainController.airportMap.get(user.getAirportID()).getName().toLowerCase().contains(searchText.toLowerCase()));
    }

    public void startInactivityTimer() {
        // Schedule the timer to prompt for logout after 3 minutes of inactivity
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Prompt for logout
                    boolean flag = new Confirmation().confirm("You have been inactive for "+INACTIVITY_TIMEOUT/60000+" minutes.", "Do you want to continue using or logout");
                    if(flag){
                        UserManager.getStage().close();
                        try {
                            new Login().start(new Stage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        resetInactivityTimer();
                    }
                    // TODO: Add code to handle user input
                });

            }
        }, INACTIVITY_TIMEOUT);
    }

    public void resetInactivityTimer() {
        // Cancel the current timer and start a new one
        if(inactivityTimer != null){
            inactivityTimer.cancel();
        }
        inactivityTimer = new Timer();
        startInactivityTimer();
    }

    private void initShortcut(){
        KeyCombination AIRPORT_MANAGER_KEY = new KeyCodeCombination(KeyCode.A,KeyCombination.SHORTCUT_DOWN);
        KeyCombination USER_MANAGER_KEY = new KeyCodeCombination(KeyCode.U,KeyCombination.SHORTCUT_DOWN);
        KeyCombination LOGOUT_KEY = new KeyCodeCombination(KeyCode.L,KeyCombination.SHORTCUT_DOWN,KeyCombination.SHIFT_DOWN);
        KeyCombination ABOUT_PROJECT_KEY = new KeyCodeCombination(KeyCode.I,KeyCombination.SHORTCUT_DOWN);

        airportManager.setAccelerator(AIRPORT_MANAGER_KEY);
        logoutItem.setAccelerator(LOGOUT_KEY);
        aboutProject.setAccelerator(ABOUT_PROJECT_KEY);
        backToMain.setAccelerator(USER_MANAGER_KEY);
    }
}
