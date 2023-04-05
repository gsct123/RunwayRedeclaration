package Controller;

import Model.Airport;
import View.Main;
import View.MainWithLogin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AirportManagerController implements Initializable {


    @FXML
    private Label identityLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button backButton;
    @FXML
    private TableView<Airport> airportTable;
    @FXML
    private Button importAirportButton;
    @FXML
    private Button deleteAirportButton;
    @FXML
    private TextArea airportDetails;
    @FXML
    private Button editAirportButton;
    @FXML
    private Button exportAirportButton;
    @FXML
    private TextField searchField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        identityLabel.setText("Logged in as "+Main.getUsername());

        airportTable = new TableView<>();
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                airportTable.setItems(filterList(MainController.airports, newValue))
        );
//        TableColumn<User, String> nameCol = new TableColumn<>("NAME");
//        TableColumn<User, String> usernameCol = new TableColumn<>("USERNAME");
//        TableColumn<User, String> emailCol = new TableColumn<>("EMAIL");
//        TableColumn<User, String> typeCol = new TableColumn<>("TYPE");
//        TableColumn<User, Date> hiredDateCol = new TableColumn<>("HIRED DATE");
//        airportTable.setEditable(true);
//        airportTable.getColumns().addAll(nameCol, usernameCol,emailCol,typeCol, hiredDateCol);
//
//        nameCol.setCellValueFactory(
//                new PropertyValueFactory<>("name")
//        );
//        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        usernameCol.setCellValueFactory(
//                new PropertyValueFactory<>("username")
//        );
//        emailCol.setCellValueFactory(
//                new PropertyValueFactory<>("email")
//        );
//        typeCol.setCellValueFactory(
//                new PropertyValueFactory<>("type")
//        );
//
//        hiredDateCol.setCellValueFactory(
//                new PropertyValueFactory<>("hiredDate"));
    }


    @FXML
    public void handleLogout(ActionEvent event) throws Exception {
        Main.getStage().close();
        new MainWithLogin().start(new Stage());
    }

    @FXML
    public void loadAboutProject(ActionEvent event){
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SEG-Group-1-2023/ProjectRelatedInformation/blob/main/runwayprojectdefinition.pdf"));
        } catch (IOException | URISyntaxException ignored) {}
    }

    private ObservableList<Airport> filterList(List<Airport> list, String searchText){
        List<Airport> filteredList = new ArrayList<>();
        for (Airport airport : list){
            if(searchFindAirport(airport, searchText)) filteredList.add(airport);
        }
        airportTable.refresh();
        return FXCollections.observableList(filteredList);
    }

    private boolean searchFindAirport(Airport airport, String searchText){
        return (airport.getName().toLowerCase().contains(searchText.toLowerCase()));
    }


}
