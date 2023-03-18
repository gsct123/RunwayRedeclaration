package Controller;


import Model.Airport;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import View.Handlers.PerformCalculationHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private String flightMethod = "";

    //fxml elements
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
    @FXML
    private TextField clDistTextField;
//    @FXML
//    private MenuButton flightMethodMenu;
    @FXML
    private Label originalToraLabel;
    @FXML
    private Label originalTodaLabel;
    @FXML
    private Label originalAsdaLabel;
    @FXML
    private Label originalLdaLabel;
    @FXML
    private Label newToraLabel;
    @FXML
    private Label newTodaLabel;
    @FXML
    private Label newAsdaLabel;
    @FXML
    private Label newLdaLabel;
    @FXML
    private Label revisedRunwayTitle;
    @FXML
    private Label editToBeginLabel;
    @FXML
    private Label noCalcPerformedLabel;
    @FXML
    private Label breakdownLabel;
    @FXML
    private MenuItem aboutProject;
    @FXML
    private Button oldToraInfo;
    @FXML
    private Button oldTodaInfo;
    @FXML
    private Button oldAsdaInfo;
    @FXML
    private Button oldLdaInfo;
    @FXML
    private Label oldToraInfoLabel;
    @FXML
    private Label oldTodaInfoLabel;
    @FXML
    private Label oldAsdaInfoLabel;
    @FXML
    private Label oldLdaInfoLabel;

    //list of airports and obstacles from files
    ObservableList<Airport> airports = FXCollections.observableArrayList();
    ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadInfos();
        try {
            loadAirports("src/Data/airports.xml");
            addAirportEvent();
            loadObstacles("src/Data/obstacles.xml");
            addObstacleEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //getters
    public ObservableList<Obstacle> getObstacles(){return this.obstacles;}
    public ObservableList<Airport> getAirports(){return this.airports;}
    public ObservableList<String> getItems() {return items;}
    public Airport getAirportSelected() {return airportSelected;}
    public PhysicalRunway getPhysRunwaySelected() {return physRunwaySelected;}
    public LogicalRunway getLogRunwaySelected() {return logRunwaySelected;}
    public Obstacle getObstacleSelected() {return obstacleSelected;}
    public double getDistFromThreshold() {return distFromThreshold;}
    public double getDistFromCentreLine() {return distFromCentreLine;}
    public String getFlightMethod() {return flightMethod;}
    public MenuButton getAirportMenu() {return airportMenu;}
    public MenuButton getPhysicalRunwayMenu() {return physicalRunwayMenu;}
    public MenuButton getLogicalRunwayMenu() {return logicalRunwayMenu;}
    public MenuButton getObstacleMenu() {return obstacleMenu;}
    public Label getObstacleHeightLabel() {return obstacleHeightLabel;}
    public Label getObstacleWidthLabel() {return obstacleWidthLabel;}
    public TextField getDistanceThresholdTextField() {return distanceThresholdTextField;}
    public Button getPerformCalculationButton() {return performCalculationButton;}
    public TextField getClDistTextField() {return clDistTextField;}
    public Label getOriginalToraLabel() {return originalToraLabel;}
    public Label getOriginalTodaLabel() {return originalTodaLabel;}
    public Label getOriginalAsdaLabel() {return originalAsdaLabel;}
    public Label getOriginalLdaLabel() {return originalLdaLabel;}
    public Label getNewToraLabel() {return newToraLabel;}
    public Label getNewTodaLabel() {return newTodaLabel;}
    public Label getNewAsdaLabel() {return newAsdaLabel;}
    public Label getNewLdaLabel() {return newLdaLabel;}
    public Label getRevisedRunwayTitle() {return revisedRunwayTitle;}
    public Label getEditToBeginLabel() {return editToBeginLabel;}
    public Label getNoCalcPerformedLabel() {return noCalcPerformedLabel;}
    public Label getBreakdownLabel() {return breakdownLabel;}
    public MenuItem getAboutProject() {return aboutProject;}
    public Button getOldToraInfo() {return oldToraInfo;}
    public Button getOldTodaInfo() {return oldTodaInfo;}
    public Button getOldAsdaInfo() {return oldAsdaInfo;}
    public Button getOldLdaInfo() {return oldLdaInfo;}
    public Label getOldToraInfoLabel() {return oldToraInfoLabel;}
    public Label getOldTodaInfoLabel() {return oldTodaInfoLabel;}
    public Label getOldAsdaInfoLabel() {return oldAsdaInfoLabel;}
    public Label getOldLdaInfoLabel() {return oldLdaInfoLabel;}

    //method to load information such as documentation and desription of parameters
    public void loadInfos() {
        aboutProject.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/SEG-Group-1-2023/ProjectRelatedInformation/blob/main/runwayprojectdefinition.pdf"));
            } catch (IOException | URISyntaxException ignored) {}
        });
        oldToraInfo.setOnMouseEntered(mouseEvent -> oldToraInfoLabel.setVisible(true));
        oldToraInfo.setOnMouseExited(mouseEvent -> oldToraInfoLabel.setVisible(false));
        oldTodaInfo.setOnMouseEntered(mouseEvent -> oldTodaInfoLabel.setVisible(true));
        oldTodaInfo.setOnMouseExited(mouseEvent -> oldTodaInfoLabel.setVisible(false));
        oldAsdaInfo.setOnMouseEntered(mouseEvent -> oldAsdaInfoLabel.setVisible(true));
        oldAsdaInfo.setOnMouseExited(mouseEvent -> oldAsdaInfoLabel.setVisible(false));
        oldLdaInfo.setOnMouseEntered(mouseEvent -> oldLdaInfoLabel.setVisible(true));
        oldLdaInfo.setOnMouseExited(mouseEvent -> oldLdaInfoLabel.setVisible(false));
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
                getAirports().add(airport);
            }
        }
        airports.sort(Comparator.comparing(Airport::getName));
    }

    public void addAirportEvent() {
        for(Airport airport: getAirports()){
            MenuItem airportMenuItem = new MenuItem(airport.getName());
            airportMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
            airportMenuItem.setOnAction(e -> {
                airportSelected = airport;
                physicalRunwayMenu.getItems().clear();
                airportMenu.setText(airport.getName());
                physicalRunwayMenu.setText("Select Physical Runway");
                physicalRunwayMenu.setDisable(false);
                physRunwaySelected = null;
                logicalRunwayMenu.setText("Select Logical Runway");
                logRunwaySelected = null;
                performCalculationButton.setDisable(true);
                for(PhysicalRunway runway: airport.getPhysicalRunways()){
                    MenuItem runwayMenuItem = new MenuItem(runway.getName());
                    runwayMenuItem.setOnAction(f -> {
                        physRunwaySelected = runway;
                        logicalRunwayMenu.getItems().clear();
                        physicalRunwayMenu.setText(runway.getName());
                        logicalRunwayMenu.setText("Select Logical Runway");
                        logRunwaySelected = null;
                        performCalculationButton.setDisable(true);
                        logicalRunwayMenu.setDisable(false);
                        for(LogicalRunway logicalRunway: runway.getLogicalRunways()){
                            MenuItem lRunwayMenuItem = new MenuItem(logicalRunway.getDesignator());
                            lRunwayMenuItem.setOnAction(g -> {
                                logRunwaySelected = logicalRunway;
                                logicalRunwayMenu.setText(logicalRunway.getDesignator());
                                obstacleMenu.setDisable(false);
                                originalToraLabel.setText("TORA  =  "+logRunwaySelected.getTora());
                                originalTodaLabel.setText("TODA  =  "+logRunwaySelected.getToda());
                                originalAsdaLabel.setText("ASDA  =  "+logRunwaySelected.getAsda());
                                originalLdaLabel.setText("LDA     =  "+logRunwaySelected.getLda());
                            });
                            lRunwayMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
                            logicalRunwayMenu.getItems().add(lRunwayMenuItem);
                        }
                    });
                    runwayMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
                    physicalRunwayMenu.getItems().add(runwayMenuItem);
                }
            });
            airportMenu.getItems().add(airportMenuItem);
        }
    }

    //functions to load obstacles from xml files and displayed in the menu selection
    public void loadObstacles(String file) throws IOException, ParserConfigurationException, SAXException {
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
                getObstacles().add(obstacle);
            }
        }
        obstacles.sort(Comparator.comparing(Obstacle::getName));
    }

    public void addObstacleEvent(){
        for(Obstacle obstacle: getObstacles()){
            MenuItem obstacleMenuItem = new MenuItem(obstacle.getName());
            obstacleMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
            obstacleMenuItem.setOnAction(e -> {
                performCalculationButton.setDisable(false);
                obstacleSelected = obstacle;
                obstacleMenu.setText(obstacle.getName());
                obstacleHeightLabel.setText("Obstacle Height: "+obstacle.getHeight()+" m");
                obstacleWidthLabel.setText("Obstacle Width: "+obstacle.getWidth()+" m");
                performCalculationButton.setOnAction(actionEvent -> new PerformCalculationHandler().handlingCalcPerformation(getClass()));
            });
            obstacleMenu.getItems().add(obstacleMenuItem);
        }
    }

//    //load flight menu
//    public void loadFlightMenu(){
//        flightMethodMenu.getItems().clear();
//        flightMethodMenu.setText("Select Flight Method");
//        ObservableList<MenuItem> methods = FXCollections.observableArrayList();
//        if(distFromThreshold <= logRunwaySelected.getTora()/2){
//            MenuItem takeOffAway = new MenuItem("Take Off Away");
//            MenuItem landOver = new MenuItem("Landing Over");
//            flightMethodMenu.getItems().add(takeOffAway);
//            flightMethodMenu.getItems().add(landOver);
//            methods.add(takeOffAway);
//            methods.add(landOver);
//        }
//        if(distFromThreshold >= logRunwaySelected.getTora()/2){
//            MenuItem takeOffTowards = new MenuItem("Take Off Towards");
//            MenuItem landTowards = new MenuItem("Landing Towards");
//            flightMethodMenu.getItems().add(takeOffTowards);
//            flightMethodMenu.getItems().add(landTowards);
//            methods.add(takeOffTowards);
//            methods.add(landTowards);
//        }
//
//        for(MenuItem method: methods){
//            method.setOnAction(actionEvent -> {
//                flightMethodMenu.setText(method.getText());
//                flightMethod = method.getText();
//                performCalculationButton.setDisable(false);
//                performCalculationButton.setOnAction(actionEvent1 -> {
//                    if(Calculator.needRedeclare(obstacleSelected, logRunwaySelected)){
//                        performCalculation();
//                        newToraLabel.setText("TORA  =  "+logRunwaySelected.getNewTora());
//                        newTodaLabel.setText("TODA  =  "+logRunwaySelected.getNewToda());
//                        newAsdaLabel.setText("ASDA  =  "+logRunwaySelected.getNewAsda());
//                        newLdaLabel.setText("LDA     =  "+logRunwaySelected.getNewLda());
//                        editToBeginLabel.setVisible(false);
//                        noCalcPerformedLabel.setVisible(false);
//                        //start breaking down
//                        breakdownLabel.setText(Calculator.getCalculationBreakdownT(obstacleSelected, logRunwaySelected));
//                        breakdownLabel.setVisible(true);
//                        //edit this to show the correct breakdown message
//                    } else{
//                        breakdownLabel.setVisible(false);
//                        //check needRedeclare function (clarify)
//                        editToBeginLabel.setVisible(true);
//                        noCalcPerformedLabel.setVisible(true);
//                        editToBeginLabel.setText("No runway redeclation needed");
//                        noCalcPerformedLabel.setText("Original runway parameters can be used");
//
//                        newToraLabel.setText("TORA  =  "+logRunwaySelected.getTora());
//                        newTodaLabel.setText("TODA  =  "+logRunwaySelected.getToda());
//                        newAsdaLabel.setText("ASDA  =  "+logRunwaySelected.getAsda());
//                        newLdaLabel.setText("LDA     =  "+logRunwaySelected.getLda());
//
//                        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
//                        infoAlert.setTitle("Information");
//                        infoAlert.setHeaderText("INFO");
//                        infoAlert.setContentText("No runway redeclaration needed\nOriginal runway parameters can be used");
//                        infoAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 5px");
//                        Optional<ButtonType> result = infoAlert.showAndWait();
//
//                        if(result.isPresent() && result.get() == ButtonType.OK){
//                            infoAlert.close();
//                        }
//
//                        Button okButton = (Button) infoAlert.getDialogPane().lookupButton(ButtonType.OK);
//                        okButton.setOnAction(event -> {
//                            infoAlert.close();
//                        });
//                    }
//                });
//            });
//        }
//    }
}
