package Controller;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import View.AirportManager;
import View.Error;
import View.Login;
import View.Main;
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
import javafx.stage.FileChooser;
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

public class AirportManagerController implements Initializable {


    @FXML
    private Label identityLabel;
    @FXML
    private Button logoutButton;
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
        airportDetails.setText("No content to display, select an airport to view airport details");
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

        airportTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                airportDetails.setText(airportInfo(newSelection));
            }
        });

        System.out.println(MainController.airports + " airprot manager controller");
        airportTable.refresh();
    }

    public String airportInfo(Airport airport){
        StringBuilder message = new StringBuilder();
        message.append("Airport name: ").append(airport.getName()).append(" (").append(airport.getID()).append(")");
        message.append("\nPhysical runways: \n");
        ObservableList<PhysicalRunway> runways = airport.getPhysicalRunways();
        for(PhysicalRunway runway: runways){
            message.append(runway.getName());
            ObservableList<LogicalRunway> logRunways = runway.getLogicalRunways();
            for(LogicalRunway logRunway: logRunways){
                message.append("\n  ").append(logRunway.getDesignator());
                message.append("\n      ").append("TORA: ").append(logRunway.getTora());
                message.append("\n      ").append("ASDA: ").append(logRunway.getAsda());
                message.append("\n      ").append("TODA: ").append(logRunway.getToda());
                message.append("\n      ").append("LDA: ").append(logRunway.getLda());
            }
        }
        return message.toString();
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

    @FXML
    public void importAirport(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Airport XML File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(AirportManager.getStage());
        if(selectedFile != null) {
            addAirport(selectedFile);
            airportTable.setItems(MainController.airports);
            airportTable.refresh();
        }
    }

    public void addAirport(File selectedFile){
        try{
            // Create a DocumentBuilder
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Parse the XML file into a Document
            Document doc = docBuilder.parse(selectedFile);

            // Get a NodeList of all airport elements
            NodeList airportElements = doc.getElementsByTagName("airport");
            if(airportElements.getLength() == 0){
                throw new Exception();
            } else{
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
                        if(MainController.references.contains(reference)){
                            errorMessage.append("\nDuplicated airport reference (").append(reference);
                        } else if (MainController.airportNames.contains(airportName)) {
                            errorMessage.append("\nDuplicated airport name ").append(airportName);
                        }

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

                                        if(tora > asda | asda > toda || lda > tora){
                                            errorMessage.append("Error in parameter values for physical runway ").append(physRunwayName).append(": ");
                                            if(tora > asda){
                                                errorMessage.append("\nTORA value");
                                            }
                                        }
                                    }
                                }
                                // Create a new PhysicalRunway object with the logical runways and add it to the list of physical runways
                                PhysicalRunway physicalRunway = new PhysicalRunway(physRunwayName, logicalRunways);
                                physicalRunways.add(physicalRunway);
                            }
                        }

                        String manager = airportElement.getElementsByTagName("user").item(0).getTextContent();


                        Airport airport = new Airport(reference, airportName, physicalRunways, manager);


                        if(errorMessage.toString().length() > 0){
                            new Error().showError(errorMessage.toString());
                        }
                        MainController.airports.add(airport);
                    }
                }
                airports.sort(Comparator.comparing(Airport::getName));
            }
        }catch (Exception e){
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
            System.out.println("Exeption caught");
        }

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
