package Controller;


import Model.Airport;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    private ObservableList<String> items = FXCollections.observableArrayList();
    private Airport airportSelected = null;
    private PhysicalRunway physRunwaySelected = null;
    private LogicalRunway logRunwaySelected = null;
    private Obstacle obstacleSelected = null;
    private double distFromThreshold = 0;
    private double distFromCentreLine = 0;

    @FXML
    private MenuButton airportMenu;
    @FXML
    private MenuButton physicalRunwayMenu;
    @FXML
    private MenuButton logicalRunwayMenu;
    @FXML
    private MenuButton obstacleMenu;
    @FXML
    private Label obstacleHeightLabel;
    @FXML
    private Label obstacleWidthLabel;
    @FXML
    private TextField distanceThresholdTextField;
    @FXML
    private Button performCalculationButton;

    ObservableList<Airport> airports = FXCollections.observableArrayList();
    ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadAirports("src/Data/airports.xml");
            loadObstacles("src/Data/obstacles.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //this function read from a xml file and instantiate list of airports available
    public void loadAirports(String file) throws Exception {
        // Create a DocumentBuilder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Parse the XML file into a Document
        Document doc = docBuilder.parse(file);

        // Get a NodeList of all airport elements
        NodeList airportElements = doc.getElementsByTagName("airport");

        // Create a list to hold the airports
        ObservableList<Airport> airports = FXCollections.observableArrayList();

        // Loop over each airport element and create an Airport object
        for (int i = 0; i < airportElements.getLength(); i++) {
            Node airportNode = airportElements.item(i);

            if (airportNode.getNodeType() == Node.ELEMENT_NODE) {
                Element airportElement = (Element) airportNode;

                // Get the airport name
                String airportName = airportElement.getElementsByTagName("name").item(0).getTextContent();

                // Create a list to hold the physical runways
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
                        }

                        // Create a new PhysicalRunway object with the logical runways and add it to the list of physical runways
                        PhysicalRunway physicalRunway = new PhysicalRunway(physRunwayName, logicalRunways);
                        physicalRunways.add(physicalRunway);
                    }
                }

                // Create a new Airport object with the physical runways and add it to the list of airports
                Airport airport = new Airport(airportName, physicalRunways);
                airports.add(airport);
            }
        }
        airports.sort(new Comparator<Airport>() {
            @Override
            public int compare(Airport o1, Airport o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for(Airport airport: airports){
            MenuItem airportMenuItem = new MenuItem(airport.getName());
            airportMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 20px");
            airportMenuItem.setOnAction(e -> {
                airportSelected = airport;
                physicalRunwayMenu.getItems().clear();
                airportMenu.setText(airport.getName());
                physicalRunwayMenu.setDisable(false);
                for(PhysicalRunway runway: airport.getPhysicalRunways()){
                    MenuItem runwayMenuItem = new MenuItem(runway.getName());
                    runwayMenuItem.setOnAction(f -> {
                        physRunwaySelected = runway;
                        logicalRunwayMenu.getItems().clear();
                        physicalRunwayMenu.setText(runway.getName());
                        logicalRunwayMenu.setDisable(false);
                        for(LogicalRunway logicalRunway: runway.getLogicalRunways()){
                            MenuItem lRunwayMenuItem = new MenuItem(logicalRunway.getDesignator());
                            lRunwayMenuItem.setOnAction(g -> {
                                logRunwaySelected = logicalRunway;
                                logicalRunwayMenu.setText(logicalRunway.getDesignator());
                                obstacleMenu.setDisable(false);
                            });
                            lRunwayMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 20px");
                            logicalRunwayMenu.getItems().add(lRunwayMenuItem);
                        }
                    });
                    runwayMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 20px");
                    physicalRunwayMenu.getItems().add(runwayMenuItem);
                }
            });
            airportMenu.getItems().add(airportMenuItem);
        }
    }

    //functions to load obstacles from xml files and displayed in the menu selection
    public void loadObstacles(String file) throws Exception {
        // Create a DocumentBuilder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Parse the XML file into a Document
        Document doc = docBuilder.parse(file);

        // Get a NodeList of all obstacle elements
        NodeList obstacleElements = doc.getElementsByTagName("obstacle");

        // Create a list to hold the obstacles
        ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();

        // Loop over each obstacle element and create an Obstacle object
        for (int i = 0; i < obstacleElements.getLength(); i++) {
            Node obstacleNode = obstacleElements.item(i);

            if (obstacleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element obstacleElement = (Element) obstacleNode;

                // Get the obstacle name, height and width
                String obstacleName = obstacleElement.getElementsByTagName("name").item(0).getTextContent();
                double height = Double.parseDouble(obstacleElement.getElementsByTagName("height").item(0).getTextContent());
                double width = Double.parseDouble(obstacleElement.getElementsByTagName("width").item(0).getTextContent());


                // Create a new Obstacle object with the physical runways and add it to the list of airports
                Obstacle obstacle = new Obstacle(obstacleName, height, width, 0, 0);
                obstacles.add(obstacle);
            }
        }
        obstacles.sort(new Comparator<Obstacle>() {
            @Override
            public int compare(Obstacle o1, Obstacle o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for(Obstacle obstacle: obstacles){
            MenuItem obstacleMenuItem = new MenuItem(obstacle.getName());
            obstacleMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 20px");
            obstacleMenuItem.setOnAction(e -> {
                obstacleSelected = obstacle;
                obstacleMenu.setText(obstacle.getName());
                obstacleHeightLabel.setText("Obstacle Height: "+obstacle.getHeight());
                obstacleWidthLabel.setText("Obstacle Width: "+obstacle.getWidth());
                distanceThresholdTextField.setDisable(false);
                distanceThresholdTextField.setOnAction(actionEvent -> {
                    String disThreshold = distanceThresholdTextField.getText().trim();
                    while(true){
                        try{
                            if(disThreshold.startsWith("-")){
                                distFromThreshold = -1 * Double.parseDouble(disThreshold.substring(1));
                            } else{
                                distFromThreshold = Double.parseDouble(disThreshold);
                            }
                            break;
                        } catch (NumberFormatException exception) {
                            //display error message
                        }
                    }
                    performCalculationButton.setDisable(false);
                    performCalculationButton.setOnAction(actionEvent1 -> {
                        //perform calculation
                    });
                });
            });
            obstacleMenu.getItems().add(obstacleMenuItem);
        }
    }
}
