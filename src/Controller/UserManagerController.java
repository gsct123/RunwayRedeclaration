package Controller;

import Model.Airport;
import Model.Helper.XMLParserWriter;
import Model.User;
import View.Error;
import View.*;
import View.OtherPopUp.Confirmation;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserManagerController implements Initializable {
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

    public static Stage helperStage;

    private ObservableList<User> userData = FXCollections.observableArrayList();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        identityLabel.setText("Logged in as "+ Main.getUsername());

        infoButton.setOnMouseEntered(event -> infoLabel.setVisible(true));
        infoButton.setOnMouseExited(event -> infoLabel.setVisible(false));

        if(Main.getRole() == 1){
            userData = FXCollections.observableArrayList(MainController.managers.keySet().stream().toList());
            addManagerButton.setVisible(true);
            addUserButton.setVisible(false);
            deleteUserButton.setVisible(false);
            airportManager.setVisible(true);
            //adduser
            //edit username and name
            addManagerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
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
                        userData = FXCollections.observableArrayList(MainController.managers.keySet().stream().toList());
                        userTable.setItems(userData);
                        userTable.refresh();
                    }
                }
            });
        } else if (Main.getRole() == 2) {
            userData = FXCollections.observableArrayList(MainController.users.get(MainController.managerMap.get(Main.getUsername())));
            addManagerButton.setVisible(false);
            addUserButton.setVisible(true);
            deleteUserButton.setVisible(true);
            airportManager.setVisible(false);
            addUserButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
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
                        ArrayList<User> original = MainController.users.get(MainController.airportMap.get(newUser.getAirportID()));
                        original.add(newUser);
                        MainController.users.put(MainController.airportMap.get(newUser.getAirportID()), original);
                        userData = FXCollections.observableArrayList(MainController.users.get(MainController.managerMap.get(Main.getUsername())));
                        userTable.setItems(userData);
                        userTable.refresh();
                        try {
                            XMLParserWriter.updateUserXML(FXCollections.observableArrayList(LoginController.users.values()), "src/Data/users.xml");
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            deleteUserButton.setOnAction(actionEvent -> {
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
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                //refer list of users
                userTable.setItems(filterList(userData, newValue))
        );

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
    }


    @FXML
    public void goAirportManager(ActionEvent event) throws Exception {
        try {
            UserManager.getStage().close();
            new AirportManager(AirportManager.getUsername()).start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToMain(ActionEvent event) throws Exception {
        UserManager.getStage().close();
        Main.getStage().show();
    }

    @FXML
    public void handleLogout(ActionEvent event) throws Exception {
        UserManager.getStage().close();
        new Login().start(new Stage());
    }

    @FXML
    public void loadAboutProject(ActionEvent event){
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
}
