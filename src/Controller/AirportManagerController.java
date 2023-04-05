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
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private TableColumn<Airport, String> IDCol;
    @FXML
    private TableColumn<Airport, String> nameCol;
    @FXML
    private TableColumn<Airport, String> managerCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        identityLabel.setText("Logged in as "+ Main.getUsername());

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                airportTable.setItems(filterList(MainController.airports, newValue))
        );

        airportTable.setEditable(false);

        IDCol.setCellValueFactory(
                new PropertyValueFactory<>("ID")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        managerCol.setCellValueFactory(
                new PropertyValueFactory<>("manager")
        );
        editColumn(IDCol);
        editColumn(nameCol);
        editColumn(managerCol);


        airportTable.setItems(FXCollections.observableList(MainController.airports));
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

    private void editColumn(TableColumn<Airport, String> tableColumn){

        tableColumn.setResizable(false);
        tableColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    this.setStyle("-fx-background-color: rgb(244,244,244); -fx-alignment: CENTER;-fx-font-family: Verdana; -fx-padding: 7");
                    // Set the text of the cell to the item
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });
    }
}
