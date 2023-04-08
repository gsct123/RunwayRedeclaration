package Controller;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import View.Error;
import View.*;
import View.OtherPopUp.Confirmation;
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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
    public void backToMain(ActionEvent event) throws Exception {
        AirportManager.getStage().close();
        writeToFile(MainController.airports, "src/Data/airports.xml");
        new Main().start(new Stage());
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

    @FXML
    public void deleteAirport(ActionEvent event){
        Airport airport = airportTable.getSelectionModel().getSelectedItem();

        if(airport == null){
            new Error().errorPopUp("No airport selected. Hint: please select an airport to be deleted.");
        } else{
            boolean flag = new Confirmation().confirm("Are you sure you want to delete "+airport.getName()+" ("+airport.getID()+")?", "Warning: This action cannot be undone.\nSelected airport will be permanently deleted.");
            if(flag){
                MainController.airports.remove(airport);
                MainController.airportNames.remove(airport.getName());
                MainController.references.remove(airport.getID());
                MainController.managerMap.remove(airport.getManager());
                new Notification(AirportManager.getStage()).sucessNotification("Successful action", airport.getName()+" ("+airport.getID()+") has been deleted.");
            }
        }
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
                        if (checkAirport(airportName, reference).length() > 0) {
                            errorMessage.append(checkAirport(airportName, reference));
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
                                    }
                                    // Create a new PhysicalRunway object with the logical runways and add it to the list of physical runways

                                }
                                PhysicalRunway physicalRunway = new PhysicalRunway(physRunwayName, logicalRunways);
                                physicalRunways.add(physicalRunway);
                                errorMessage.append(checkRunway(physicalRunway));
                            }
                        }

                        String manager = airportElement.getElementsByTagName("user").item(0).getTextContent();
                        Airport airport = new Airport(reference, airportName, physicalRunways, manager);


                        if (errorMessage.toString().length() > 0) {
                            new Error().showError(errorMessage.toString());
                            new Notification(AirportManager.getStage()).failNotification("Failed action", "Fail to add airport.");
                        } else {
                            boolean result = new Confirmation().confirmAddAirport(airport, airportInfo(airport));
                            System.out.println(result);
                            if (result) {
                                MainController.airports.add(airport);
                                MainController.airportNames.add(airportName);
                                MainController.references.add(reference);
                                MainController.managerMap.put(manager, airport);
                                new Notification(AirportManager.getStage()).sucessNotification("Successful action", airportName + " successfully added to system.");
                            }
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
            new Notification(AirportManager.getStage()).failNotification("Failed action", "Fail to add airport, check XML and reimport.");
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

    private String checkAirport(String airportName, String airportReference){
        StringBuilder errorMessage = new StringBuilder();
        if(MainController.references.contains(airportReference)){
            errorMessage.append("\nNAME ERROR: Duplicated airport reference (").append(airportReference).append(")");
        }
        if(MainController.airportNames.contains(airportName)) {
            errorMessage.append("\nNAME ERROR: Duplicated airport name ").append(airportName);
        }
        if(errorMessage.length() > 0){
            return "AIRPORT ERROR: "+ errorMessage;
        }
        return "";
    }

    private String checkRunway(PhysicalRunway physicalRunway){
        StringBuilder res = new StringBuilder();
        //physical runways
        StringBuilder physError = new StringBuilder();
        String name = physicalRunway.getName();
        String[] split = name.split("/");
        if(split.length != 2){
            System.out.println("splitlength");
            return "\n\nPHYSICAL RUNWAY ERROR (RUNWAY " + (physicalRunway.getName()) + ")" + "\nFORMAT ERROR: Please provide a valid physical runway with format NL/NR or NR/NL with N representing a numerical value from 01 to 36";
        } else{
            String lower = split[0];
            String higher = split[1];
            if(lower.charAt(lower.length()-1) == 'L' && higher.charAt(higher.length()-1) == 'R' || lower.charAt(lower.length()-1) == 'R' && higher.charAt(higher.length()-1) == 'L'){
                try{
                    int lowerThreshold = Integer.parseInt(lower.substring(0, lower.length()-1));
                    int higherThreshold = Integer.parseInt(higher.substring(0, higher.length()-1));
                    if(lowerThreshold > higherThreshold){
                        physError.append("\nFORMAT ERROR: Please ensure the threshold with lower value is located at the left (Ex: 09R/27L instead of 27L/09R)");
                    }
                    if(Math.abs(higherThreshold - lowerThreshold) != 18){
                        physError.append("\nFORMAT ERROR: Please ensure runways are 180 degrees away from each other");
                    }
                    StringBuilder logError = new StringBuilder();
                    if(physicalRunway.getLogicalRunways().size() != 2){
                        logError.append("\nINVALID LOGICAL RUNWAYS: Please ensure you have provided information for both runways: ").append(lower).append(" and ").append(higher);
                    } else{
                        LogicalRunway left = physicalRunway.getLogicalRunways().get(0);
                        LogicalRunway right = physicalRunway.getLogicalRunways().get(1);
                        if(left.getDesignator().equals(lower) && right.getDesignator().equals(higher)){
                            if(left.getTora() != right.getTora()){
                                logError.append("\nINVALID PARAMETERS: Unequal TORA values for logical runways ").append(lower).append(" and ").append(higher);
                            }
                            logError.append(checkLogRunway(left));
                            logError.append(checkLogRunway(right));
                        } else{
                            logError.append("\nINVALID LOGICAL RUNWAYS: Please ensure you provided logical runway in correct sequence: ").append(lower).append(" followed by ").append(higher);
                        }

                    }
                    if(logError.length() > 0){
                        physError.append("\nLOGICAL RUNWAYS ERRORS: ");
                        physError.append(logError);
                    }
                }catch (NumberFormatException e){
                    physError.append("\nFORMAT ERROR: Please provide a valid physical runway with format NL/NR or NR/NL with N representing a numerical value from 01 to 36");
                }
            } else{
                physError.append("\nFORMAT ERROR: Please provide a valid physical runway with format NL/NR or NR/NL with N representing a numerical value from 01 to 36");
            }

            if(physError.length() > 0){
                res.append("\n\nPHYSICAL RUNWAY ERROR (RUNWAY ").append(physicalRunway.getName()).append("):");
                res.append(physError);
            }

        }
        return res.toString();
    }

    public String checkLogRunway(LogicalRunway logicalRunway){
        StringBuilder res = new StringBuilder();
        if(logicalRunway.getLda() > logicalRunway.getTora()){
            res.append("\n-- LDA value greater than TORA value (Invalid displaced threshold value)");
        }
        if(logicalRunway.getTora() > logicalRunway.getAsda()){
            res.append("\n-- TORA value greater than ASDA value (Invalid stopway value)");
        }
        if(logicalRunway.getAsda() > logicalRunway.getToda()){
            res.append("\n-- ASDA value greater than TODA value (Invalid clearway value");
        }
        if(res.length() > 0){
            return "\nPARAMETER ERROR (Logical Runway "+logicalRunway.getDesignator()+"): "+res;
        } else{
            return res.toString();
        }
    }

    public static void writeToFile(ObservableList<Airport> airports, String fileName) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.setXmlStandalone(true);

        Element root = document.createElement("airports");
        document.appendChild(root);

        for (Airport airport : airports) {
            Element airportElement = document.createElement("airport");
            root.appendChild(airportElement);

            Element idElement = document.createElement("ID");
            idElement.appendChild(document.createTextNode(airport.getID()));
            airportElement.appendChild(idElement);

            Element nameElement = document.createElement("name");
            nameElement.appendChild(document.createTextNode(airport.getName()));
            airportElement.appendChild(nameElement);

            Element physicalRunwaysElement = document.createElement("physicalRunways");
            airportElement.appendChild(physicalRunwaysElement);

            ObservableList<PhysicalRunway> physicalRunways = airport.getPhysicalRunways();
            for (PhysicalRunway physicalRunway : physicalRunways) {
                Element physicalRunwayElement = document.createElement("physicalRunway");
                physicalRunwayElement.setAttribute("name", physicalRunway.getName());
                physicalRunwaysElement.appendChild(physicalRunwayElement);

                ObservableList<LogicalRunway> logicalRunways = physicalRunway.getLogicalRunways();
                for (LogicalRunway logicalRunway : logicalRunways) {
                    Element logicalRunwayElement = document.createElement("logicalRunway");
                    logicalRunwayElement.setAttribute("designator", logicalRunway.getDesignator());
                    logicalRunwayElement.setAttribute("tora", String.valueOf(logicalRunway.getTora()));
                    logicalRunwayElement.setAttribute("toda", String.valueOf(logicalRunway.getToda()));
                    logicalRunwayElement.setAttribute("asda", String.valueOf(logicalRunway.getAsda()));
                    logicalRunwayElement.setAttribute("lda", String.valueOf(logicalRunway.getLda()));
                    physicalRunwayElement.appendChild(logicalRunwayElement);
                }
            }

            Element userElement = document.createElement("user");
            userElement.appendChild(document.createTextNode(airport.getManager()));
            airportElement.appendChild(userElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(fileName));
        transformer.transform(domSource, streamResult);
    }

}
