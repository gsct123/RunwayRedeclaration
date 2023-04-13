package Controller;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import Model.User;
import View.Error;
import View.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
            //adduser
            //edit username and name
        } else if (Main.getRole() == 2) {
            userData = FXCollections.observableArrayList(MainController.users.get(MainController.managerMap.get(Main.getUsername())));
            addManagerButton.setVisible(false);
            addUserButton.setVisible(true);
            deleteUserButton.setVisible(true);
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
            Airport airport = MainController.airports.get(airportID);
            if(airport == null){
                return new SimpleStringProperty("NULL");
            } else{
                return new SimpleStringProperty(MainController.airports.get(airportID).getName());
            }
        });


        userTable.setItems(userData);

        userTable.refresh();


    }



    @FXML
    public void backToMain(ActionEvent event) throws Exception {
        AirportManager.getStage().close();
        Main.getStage().show();
    }

    @FXML
    public void handleLogout(ActionEvent event) throws Exception {
        AirportManager.getStage().close();
        new Login().start(new Stage());
    }

    @FXML
    public void loadAboutProject(ActionEvent event){
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SEG-Group-1-2023/ProjectRelatedInformation/blob/main/runwayprojectdefinition.pdf"));
        } catch (IOException | URISyntaxException ignored) {}
    }


    public void addAirport(File selectedFile){
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Parse the XML file into a Document
            Document doc = docBuilder.parse(selectedFile);

            // Get a NodeList of all airport elements
            NodeList airportElements = doc.getElementsByTagName("airport");
            if (airportElements.getLength() == 0) {
                throw new Exception();
            } else {
                // Create a list to hold the airports
                ObservableList<Airport> airports = FXCollections.observableArrayList();

                // Loop over each airport element and create an Airport object
                for (int i = 0; i < airportElements.getLength(); i++) {
                    Node airportNode = airportElements.item(i);

                    if (airportNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element airportElement = (Element) airportNode;
                        String reference = airportElement.getElementsByTagName("ID").item(0).getTextContent();
                        // Get the airport name
                        String airportName = airportElement.getElementsByTagName("name").item(0).getTextContent();
                        // Create a list to hold the physical runways

                        StringBuilder errorMessage = new StringBuilder();
//                        if (checkAirport(airportName, reference).length() > 0) {
//                            errorMessage.append(checkAirport(airportName, reference));
//                        }

                        ObservableList<PhysicalRunway> physicalRunways = FXCollections.observableArrayList();
                        // Get a NodeList of all physical runway elements for the current airport
                        NodeList physRunwayElements = airportElement.getElementsByTagName("physicalRunway");
                        // Loop over each physical runway element and create a PhysicalRunway object
                        for (int j = 0; j < physRunwayElements.getLength(); j++) {
                            Node physRunwayNode = physRunwayElements.item(j);

                            if (physRunwayNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element physRunwayElement = (Element) physRunwayNode;
                                // Get the physical runway name
                                String physRunwayName = physRunwayElement.getAttribute("name");
                                // Create a list to hold the logical runways
                                ObservableList<LogicalRunway> logicalRunways = FXCollections.observableArrayList();
                                // Get a NodeList of all logical runway elements for the current physical runway
                                NodeList logRunwayElements = physRunwayElement.getElementsByTagName("logicalRunway");
                                // Loop over each logical runway element and create a LogicalRunway object
                                for (int k = 0; k < logRunwayElements.getLength(); k++) {
                                    Node logRunwayNode = logRunwayElements.item(k);
                                    if (logRunwayNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element logRunwayElement = (Element) logRunwayNode;
                                        // Get the logical runway designator and dimensions
                                        String designator = logRunwayElement.getAttribute("designator");

                                        double tora = Double.parseDouble(logRunwayElement.getAttribute("tora"));
                                        double toda = Double.parseDouble(logRunwayElement.getAttribute("toda"));
                                        double asda = Double.parseDouble(logRunwayElement.getAttribute("asda"));
                                        double lda = Double.parseDouble(logRunwayElement.getAttribute("lda"));
                                        // Create a new LogicalRunway object and add it to the list of logical runways
                                        LogicalRunway logicalRunway = new LogicalRunway(designator, tora, toda, asda, lda);
                                        logicalRunways.add(logicalRunway);
                                    }
                                    // Create a new PhysicalRunway object with the logical runways and add it to the list of physical runways

                                }
                                PhysicalRunway physicalRunway = new PhysicalRunway(physRunwayName, logicalRunways);
                                physicalRunways.add(physicalRunway);
//                                errorMessage.append(checkRunway(physicalRunway));
                            }
                        }

                        String manager = airportElement.getElementsByTagName("user").item(0).getTextContent();
                        if(MainController.managerMap.containsKey(manager)){
                            errorMessage.append("\nDuplicated username for airport manager, already in use");
                        }
                        Airport airport = new Airport(reference, airportName, physicalRunways, manager);

                        if (errorMessage.toString().length() > 0) {
                            new Error().showError(errorMessage.toString());
                            new Notification(AirportManager.getStage()).failNotification("Failed action", "Fail to add airport.");
                        } else {

                        }
                    }
                }
                airports.sort(Comparator.comparing(Airport::getName));
            }
        } catch (Exception e){
            e.printStackTrace();
            new Error().showError("""
                        Invalid XML File. Please ensure the xml inputs matches the required format.
    
                        Example:\s
                        <?xml version="1.0" encoding="UTF-8"?>
                        <airports>
                         <airport>
                          <ID>LHR</ID>
                          <name>Heathrow Airport</name>
                          <physicalRunways>
                           <physicalRunway name="09L/27R">
                            <logicalRunway designator="09L" tora="3902.0" toda="3902.0" asda="3902.0" lda="3595.0" />
                            <logicalRunway designator="27R" tora="3884.0" toda="3962.0" asda="3884.0" lda="3884.0" />
                           </physicalRunway>
                           <physicalRunway name="09R/27L">
                            <logicalRunway designator="09R" tora="3660.0" toda="3660.0" asda="3660.0" lda="3353.0" />
                            <logicalRunway designator="27L" tora="3660.0" toda="3660.0" asda="3660.0" lda="3660.0" />
                           </physicalRunway>
                           <physicalRunway name="06/24">
                            <logicalRunway designator="06" tora="2734.0" toda="2734.0" asda="2734.0" lda="2734.0" />
                            <logicalRunway designator="24" tora="2734.0" toda="2900.0" asda="2800.0" lda="2500.0" />
                           </physicalRunway>
                          </physicalRunways>
                          <user>manager</user>
                         </airport>
                        </airports>
    
    
                        """);
            new Notification(AirportManager.getStage()).failNotification("Failed action", "Fail to add airport.");
        }
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
                user.getAirportID().toLowerCase().contains(searchText.toLowerCase()));
    }

//    private void editColumn(TableColumn<Airport, String> tableColumn){
//        tableColumn.setResizable(false);
//        tableColumn.setCellFactory(column -> new TableCell<>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (!empty) {
//                    this.setStyle("-fx-background-color: rgb(244,244,244); -fx-alignment: CENTER;-fx-font-family: Verdana; -fx-padding: 7");
//                    // Set the text of the cell to the item
//                    setText(item);
//                } else {
//                    setText(null);
//                }
//            }
//        });
//    }


}
