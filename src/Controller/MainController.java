package Controller;

import Model.*;
import Model.Helper.Utility;
import Model.Helper.XMLParserWriter;
import View.AirportManager;
import View.Error;
import View.Main;
import View.OtherPopUp.Confirmation;
import View.OtherPopUp.NoRedeclarationNeeded;
import View.UserManager;
import com.gluonhq.charm.glisten.control.ToggleButtonGroup;
import javafx.animation.FadeTransition;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController implements Initializable {
    private static boolean needRedeclare = true;
    @FXML
    private Button notificationButton;
    @FXML
    private Button resetViewButton;
    //notification
    @FXML
    private Pane notiPane;
    @FXML
    private ScrollPane notiScrollPane;
    @FXML
    private VBox notiVBox;
    @FXML
    private Button extendButton;

    //fxml elements
    @FXML
    private MenuButton airportMenu;
    @FXML
    private MenuButton physicalRunwayMenu;
    @FXML
    private MenuButton obstacleMenu;
    @FXML
    private MenuItem logoutItem;
    @FXML
    private MenuItem airportManager;
    @FXML
    private Label obstacleHeightLabel;
    @FXML
    private Label obstacleWidthLabel;
    @FXML
    private TextField obstacleHeightField;
    @FXML
    private TextField obstacleWidthField;
    @FXML
    private TextField distanceThresholdTextField;
    @FXML
    private Button performCalculationButton;
    @FXML
    private TextField clDistTextField;
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
    @FXML
    private TextField stripEndTextField;
    @FXML
    private TextField blastProtectionField;
    @FXML
    private TextField resaTextField;
    @FXML
    private Label stripEndInfoLabel;
    @FXML
    private Label blastProtectionInfoLabel;
    @FXML
    private Label resaInfoLabel;
    @FXML
    private Button stripEndInfo;
    @FXML
    private Button blastProtectionInfo;
    @FXML
    private Button resaInfo;
    @FXML
    private Button calculationBreakdown;
    @FXML
    public Tab topViewTab;
    @FXML
    private Tab sideViewTab;
    @FXML
    private Tab simultaneousViewTab;
    @FXML
    private ToggleButtonGroup lrButtonGroup;
    @FXML
    private RadioButton leftDirButton;
    @FXML
    private Label notificationLabel;

    //table
    @FXML
    private TableView<Parameter> leftTableView;
    @FXML
    private TableView<Parameter> rightTableView;
    @FXML
    private TableColumn<Parameter, String> parColumn1;
    @FXML
    private TableColumn<Parameter, String> originalCol1;
    @FXML
    private TableColumn<Parameter, String> revisedCol1;
    @FXML
    private TableColumn<Parameter, String> parColumn2;
    @FXML
    private TableColumn<Parameter, String> originalCol2;
    @FXML
    private TableColumn<Parameter, String> revisedCol2;
    @FXML
    private Button logoutButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label identityLabel;

    //property to be used in Visualisation classes
    public static ObjectProperty<PhysicalRunway> physRunwayItem = new SimpleObjectProperty<>();
    public static ObjectProperty<Airport> airportItem = new SimpleObjectProperty();
    public static ObjectProperty<Obstacle> obstacleProperty = new SimpleObjectProperty<>();
    public static DoubleProperty disFromThreshold = new SimpleDoubleProperty();
    public static DoubleProperty disFromCentre = new SimpleDoubleProperty();
    public static StringProperty dirFromCentre = new SimpleStringProperty();
    public static DoubleProperty valueChanged = new SimpleDoubleProperty();
    public static DoubleProperty obstacleHeight = new SimpleDoubleProperty();
    public static DoubleProperty obstacleWidth = new SimpleDoubleProperty();

    //list of airports and obstacles from files
    public static HashMap<String, Airport> airportMap = new HashMap<>();
    public static ObservableList<Airport> airports = FXCollections.observableArrayList();
//    public static MapProperty<String, Airport> airports = new SimpleMapProperty<>(FXCollections.observableMap(map));
    public static ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();
    public static ObservableList<String> airportNames = FXCollections.observableArrayList();
    public static HashMap<String, Airport> managerMap = new HashMap<>();
    public static HashMap<Airport, ArrayList<User>> users = new HashMap<>();
    public static HashMap<User, Airport> managers = new HashMap<>();

    //Controllers
    private TopViewController topViewController;
    private SideViewController sideViewController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obstacleProperty = new SimpleObjectProperty<>();
        DropShadow shadow = new DropShadow(2, Color.valueOf("#212f45"));
        leftTableView.setEffect(shadow);
        rightTableView.setEffect(shadow);

        // or if you only want to disable horizontal scrolling
        leftTableView.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
        rightTableView.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        loadInfos();
        try {
            //create instances of the controller so that we can direct access their field when needed
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TopView.fxml"));
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/FXML/SideView.fxml"));
            Parent root = loader.load();
            Parent root1 = loader1.load();
            topViewController = loader.getController();
            sideViewController = loader1.getController();
            topViewTab.setContent(root);
            sideViewTab.setContent(root1);
            simultaneousViewTab.setContent(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/FXML/SimultaneousView.fxml"))));
            loadAirports("src/Data/airports.xml");
            addAirportEvent();
            loadObstacles("src/Data/obstacles.xml");
            addObstacleEvent();
            loadUsers();

            identityLabel.setText("Logged in as "+ Main.getUsername());
            logoutItem.setOnAction(actionEvent -> {
                try {
                    Utility.handleLogout(new ActionEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            logoutButton.setOnMouseClicked(mouseEvent -> {
                try {
                    Utility.handleLogout(new ActionEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            airportManager.setOnAction(actionEvent -> {
                try {
                    Main.getStage().close();
                    new AirportManager(AirportManager.getUsername()).start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if(Main.isReset()){
                setNotificationLabel("Status: Options Reset\t " + getDateTimeNow());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeNotification(getNotiPane(),getNotiScrollPane(),getNotiVBox());
        topViewController.initializeCompass();

        airports.addListener(listener);

    }

    public ListChangeListener<Airport> listener = new ListChangeListener<Airport>() {
        @Override
        public void onChanged(Change<? extends Airport> c) {
            Airport temp = airportItem.get();
            try {
                XMLParserWriter.writeToFile(FXCollections.observableArrayList(MainController.airports.stream().toList()), "src/Data/airports.xml");
                loadAirports("src/Data/airports.xml");
            } catch (Exception e) {
                e.printStackTrace();
            }
            addAirportEvent();
            System.out.println("Listener detected airport list changes");
            if(temp != null){
                boolean flag = false;
                while (c.next()) {
                    if (c.wasRemoved() && c.getRemoved().contains(temp)) {
                        flag = true;
                        airportMenu.setText("Select an Airport");
                        physicalRunwayMenu.setText("Select Physical Runway");
                        //create instances of the controller so that we can direct access their field when needed
                        try{
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TopView.fxml"));
                            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/FXML/SideView.fxml"));
                            Parent root = loader.load();
                            Parent root1 = loader1.load();
                            topViewController = loader.getController();
                            sideViewController = loader1.getController();
                            topViewTab.setContent(root);
                            sideViewTab.setContent(root1);
                            simultaneousViewTab.setContent(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/FXML/SimultaneousView.fxml"))));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                if(!flag){
                    airportMenu.setText(temp.getName());
                    PhysicalRunway runway = physRunwayItem.get();
                    if(runway != null){
                        physicalRunwayMenu.setText(runway.getName());
                    }
                }
            }
            airports.addListener(listener);
        }
    };

    //getters
    public ObservableList<Obstacle> getObstacles(){return obstacles;}
    public static ObservableList<Airport> getAirports(){return airports;}
    public static PhysicalRunway getPhysRunwaySelected() {return physRunwayItem.get();}
    public static boolean needRedeclare(){return needRedeclare;}
    public static Obstacle getObstacleSelected() {return obstacleProperty.get();}
    public MenuButton getAirportMenu() {return this.airportMenu;}
    public TopViewController getTopViewController() { return topViewController;}
    public SideViewController getSideViewController() { return sideViewController;}
    public VBox getNotiVBox() {return notiVBox;}
    public Pane getNotiPane() {return notiPane;}
    public ScrollPane getNotiScrollPane() {return notiScrollPane;}
    public Label getNotificationLabel() {return notificationLabel;}

    //event handlers
    @FXML
    public void loadAboutProject(ActionEvent event){
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SEG-Group-1-2023/ProjectRelatedInformation/blob/main/runwayprojectdefinition.pdf"));
        } catch (IOException | URISyntaxException ignored) {}
    }

    @FXML
    public void goUserManager(ActionEvent event) throws Exception {
        Main.getStage().close();
        new UserManager(Main.getUsername()).start(new Stage());
    }

    @FXML
    public void handleReset(ActionEvent event) throws IOException {
        boolean flag = new Confirmation().confirm("Are you sure you want to reset the system?", "Warning: This action cannot be undone.\nAll inputs and selections will be cleared.");
        Main.setReset(true);
        if(flag) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = Main.getStage();
            stage.setTitle("SEG Runway Project");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void checkDistFromThreshold(ActionEvent event){
        try {
            double distFromThreshold = Double.parseDouble(distanceThresholdTextField.getText().trim());
            getObstacleSelected().setDistFThreshold(distFromThreshold);
            disFromThreshold.set(distFromThreshold);
        } catch (NumberFormatException exception) {
            //display error message
            new Error().showError(distanceThresholdTextField, "Invalid input for distance from threshold\nHint: please input a numerical value", ""+getObstacleSelected().getDistFThreshold());
        }
    }

    @FXML
    public void checkDistFromCentreLine(ActionEvent event){
        try{
            double distFromCentreLine = Double.parseDouble(clDistTextField.getText().trim());
            if(distFromCentreLine < 0){throw new NumberFormatException();}
            getObstacleSelected().setDistFCent(distFromCentreLine);
            disFromCentre.set(distFromCentreLine);
        } catch (NumberFormatException exception){
            new Error().showError(clDistTextField, "Invalid input for distance from centre line\nHint: please input a numerical value greater or equal to 0", ""+getObstacleSelected().getDirFromCentre());
        }
    }

    @FXML
    public void checkObstacleHeight(ActionEvent event){
        try{
            double height = Double.parseDouble(obstacleHeightField.getText().trim());
            if(height <= 0){throw new NumberFormatException();}
            getObstacleSelected().setHeight(height);
            obstacleHeight.set(height);
        } catch (NumberFormatException e){
            new Error().showError(obstacleHeightField, "Invalid obstacle height, please input a numerical value greater than 0", ""+getObstacleSelected().getHeight());
        }
    }

    @FXML
    public void checkObstacleWidth(ActionEvent event){
        try{
            double width = Double.parseDouble(obstacleWidthField.getText().trim());
            if(width <= 0){throw new NumberFormatException();}
            getObstacleSelected().setWidth(width);
            obstacleWidth.set(width);
        } catch (NumberFormatException e){
            new Error().showError(obstacleWidthField, "Invalid obstacle width, please input a numerical value greater than 0", ""+getObstacleSelected().getWidth());
        }
    }

    @FXML
    public void performCalculation(ActionEvent event){
        checkDistFromThreshold(new ActionEvent());
        checkDistFromCentreLine(new ActionEvent());
        checkObstacleHeight(new ActionEvent());
        checkObstacleWidth(new ActionEvent());
        setRESA(new ActionEvent());
        setStripEnd(new ActionEvent());
        setBlastProtection(new ActionEvent());
        if(Calculator.needRedeclare(getObstacleSelected(), getPhysRunwaySelected().getLogicalRunways().get(0))){
            needRedeclare = true;
            Calculator.performCalc(getObstacleSelected(), getPhysRunwaySelected());
            showTable(false);
        } else{
            needRedeclare = false;
            showTable(false);
            new NoRedeclarationNeeded().showNoRedeclarationNeeded();
        }
        calculationBreakdown.setDisable(false);
        valueChanged.set(valueChanged.doubleValue() == 1? 0: 1);
        setNotificationLabel( "Status: Calculation performed\t " + getDateTimeNow());
    }

    public String getDateTimeNow(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return  dtf.format(now);
    }

    @FXML
    public void setStripEnd(ActionEvent event){
        try{
            double stripEnd = Double.parseDouble(stripEndTextField.getText().trim());
            if(stripEnd < 0 || stripEnd > 100){throw new NumberFormatException();}
            PhysicalRunway.setStripEnd(stripEnd);
        } catch (NumberFormatException e){
            new Error().showError(stripEndTextField, "Invalid input for strip end \nHint: please input a numerical value within this range 0-100", "60");
        }
    }

    @FXML
    public void setBlastProtection(ActionEvent event){
        try{
            double blastProtection = Double.parseDouble(blastProtectionField.getText().trim());
            if(blastProtection < 300 || blastProtection > 500){
                throw new NumberFormatException();
            }
            PhysicalRunway.setBlastProtection(blastProtection);
        } catch (NumberFormatException e){
            new Error().showError(blastProtectionField, "Invalid input for blast protection\nHint: please input a numerical value within this range: 300-500 (for safety purpose)", "300");
        }
    }

    @FXML
    public void setRESA(ActionEvent event){
        try{
            double resa = Double.parseDouble(resaTextField.getText().trim());
            if(resa < 240 || resa > 500){
                throw new NumberFormatException();
            }
            PhysicalRunway.setResa(resa);
        } catch (NumberFormatException e){
            new Error().showError(resaTextField, "Invalid input for RESA\nHint: please input a numerical value within this range 240-500 (for safety purpose)", "240");
        }
    }

    @FXML
    public void showCalculationBreakdown(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CalculationBreakdown.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheet.css")).toExternalForm());

        stage.setTitle("Calculation Breakdown");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    public void setLeftRightDirection(ActionEvent event){
        if(leftDirButton.isSelected()){
            getObstacleSelected().setDirFromCentre("L");
            dirFromCentre.set("L");
        } else{
            getObstacleSelected().setDirFromCentre("R");
            dirFromCentre.set("R");
        }
    }

    //method to load information such as documentation and description of parameters
    public void loadInfos() {
        oldToraInfo.setOnMouseEntered(mouseEvent -> oldToraInfoLabel.setVisible(true));
        oldToraInfo.setOnMouseExited(mouseEvent -> oldToraInfoLabel.setVisible(false));
        oldTodaInfo.setOnMouseEntered(mouseEvent -> oldTodaInfoLabel.setVisible(true));
        oldTodaInfo.setOnMouseExited(mouseEvent -> oldTodaInfoLabel.setVisible(false));
        oldAsdaInfo.setOnMouseEntered(mouseEvent -> oldAsdaInfoLabel.setVisible(true));
        oldAsdaInfo.setOnMouseExited(mouseEvent -> oldAsdaInfoLabel.setVisible(false));
        oldLdaInfo.setOnMouseEntered(mouseEvent -> oldLdaInfoLabel.setVisible(true));
        oldLdaInfo.setOnMouseExited(mouseEvent -> oldLdaInfoLabel.setVisible(false));
        stripEndInfo.setOnMouseEntered(mouseEvent -> stripEndInfoLabel.setVisible(true));
        stripEndInfo.setOnMouseExited(mouseEvent -> stripEndInfoLabel.setVisible(false));
        blastProtectionInfo.setOnMouseEntered(mouseEvent -> blastProtectionInfoLabel.setVisible(true));
        blastProtectionInfo.setOnMouseExited(mouseEvent -> blastProtectionInfoLabel.setVisible(false));
        resaInfo.setOnMouseEntered(mouseEvent -> resaInfoLabel.setVisible(true));
        resaInfo.setOnMouseExited(mouseEvent -> resaInfoLabel.setVisible(false));
    }

    public void loadUsers() {
        for(User user: LoginController.users.values()){
            if(user.getRole() == 2){
                managers.put(user, MainController.airportMap.get(user.getAirportID()));
            } else if(user.getRole() == 3){
                ArrayList<User> list = new ArrayList<>();
                if(users.containsKey(airportMap.get(user.getAirportID()))){
                    list = users.get(airportMap.get(user.getAirportID()));
                }
                list.add(user);
                users.put(MainController.airportMap.get(user.getAirportID()), list);
            }
        }
    }


    //this function read from a xml file and instantiate list of airports available
    public void loadAirports(String file) throws Exception {
        airports = FXCollections.observableArrayList();
        airportMap = new HashMap<>();
        airportNames = FXCollections.observableArrayList();
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
                String reference = airportElement.getElementsByTagName("ID").item(0).getTextContent();
                // Get the airport name
                String airportName = airportElement.getElementsByTagName("name").item(0).getTextContent();
                airportNames.add(airportName);
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

                String manager = airportElement.getElementsByTagName("user").item(0).getTextContent();


                Airport airport = new Airport(reference, airportName, physicalRunways, manager);
                managerMap.put(manager, airport);

                airportMap.put(airport.getID(), airport);
                getAirports().add(airport);
            }
        }
        airports.sort(Comparator.comparing(Airport::getName));
    }

    public void addAirportEvent() {
        airportMenu.getItems().clear();
        for(Airport airport: airports){
            MenuItem airportMenuItem = new MenuItem(airport.getName());
            airportMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
            airportMenuItem.setOnAction(e -> {
                airportItem.set(airport);
                physicalRunwayMenu.getItems().clear();
                getAirportMenu().setText(airport.getName());
                physicalRunwayMenu.setText("Select Physical Runway");
                physicalRunwayMenu.setDisable(false);
                performCalculationButton.setDisable(true);
                obstaclesEditing(true);
                for(PhysicalRunway runway: airport.getPhysicalRunways()){
                    MenuItem runwayMenuItem = new MenuItem(runway.getName());
                    runwayMenuItem.setOnAction(f -> {
                        oldToraInfo.setVisible(true);
                        oldTodaInfo.setVisible(true);
                        oldLdaInfo.setVisible(true);
                        oldAsdaInfo.setVisible(true);
                        physRunwayItem.set(runway);
                        physicalRunwayMenu.setText(runway.getName());
                        stripEndTextField.setText(String.valueOf(PhysicalRunway.getStripEnd()));
                        blastProtectionField.setText(String.valueOf(PhysicalRunway.getBlastProtection()));
                        resaTextField.setText(String.valueOf(PhysicalRunway.getResa()));
                        stripEndTextField.setDisable(false);
                        blastProtectionField.setDisable(false);
                        resaTextField.setDisable(false);
                        obstaclesEditing(false);
                        showTable(true);
                    });
                    runwayMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
                    physicalRunwayMenu.getItems().add(runwayMenuItem);
                }
            });
            getAirportMenu().getItems().add(airportMenuItem);
        }
    }

    public void showTable(boolean beforeCalc){
        ObservableList<Parameter> data1 = FXCollections.observableArrayList();
        LogicalRunway logRunway1 = getPhysRunwaySelected().getLogicalRunways().get(0);
        ObservableList<Parameter> data2 = FXCollections.observableArrayList();
        LogicalRunway logRunway2 = getPhysRunwaySelected().getLogicalRunways().get(1);
        //display original values
        // Define the columns of the table
        parColumn1.setCellValueFactory(new PropertyValueFactory<>("name"));
        parColumn1.setText(logRunway1.getDesignator());
        originalCol1.setCellValueFactory(new PropertyValueFactory<>("originalValue"));
        revisedCol1.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        editColumn(parColumn1);
        editColumn(originalCol1);
        editColumn(revisedCol1);

        parColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        parColumn2.setText(logRunway2.getDesignator());
        originalCol2.setCellValueFactory(new PropertyValueFactory<>("originalValue"));
        revisedCol2.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        editColumn(parColumn2);
        editColumn(originalCol2);
        editColumn(revisedCol2);

        if(beforeCalc){
            data1.add(new Parameter("TORA (m)", String.valueOf(logRunway1.getTora()), "-"));
            data1.add(new Parameter("TODA (m)", String.valueOf(logRunway1.getToda()), "-"));
            data1.add(new Parameter("ASDA (m)", String.valueOf(logRunway1.getAsda()), "-"));
            data1.add(new Parameter("LDA (m)", String.valueOf(logRunway1.getLda()), "-"));

            data2.add(new Parameter("TORA (m)", String.valueOf(logRunway2.getTora()), "-"));
            data2.add(new Parameter("TODA (m)", String.valueOf(logRunway2.getToda()), "-"));
            data2.add(new Parameter("ASDA (m)", String.valueOf(logRunway2.getAsda()), "-"));
            data2.add(new Parameter("LDA (m)", String.valueOf(logRunway2.getLda()), "-"));

            revisedCol1.setCellFactory(cell -> new BlackTableCell<>());
            revisedCol2.setCellFactory(cell -> new BlackTableCell<>());

        } else{
            if(needRedeclare){
                data1.add(new Parameter("TORA (m)", String.valueOf(logRunway1.getTora()), String.valueOf(logRunway1.getNewTora())));
                data1.add(new Parameter("TODA (m)", String.valueOf(logRunway1.getToda()), String.valueOf(logRunway1.getNewToda())));
                data1.add(new Parameter("ASDA (m)", String.valueOf(logRunway1.getAsda()), String.valueOf(logRunway1.getNewAsda())));
                data1.add(new Parameter("LDA (m)", String.valueOf(logRunway1.getLda()), String.valueOf(logRunway1.getNewLda())));
            } else{
                data1.add(new Parameter("TORA (m)", String.valueOf(logRunway1.getTora()), "-"));
                data1.add(new Parameter("TODA (m)", String.valueOf(logRunway1.getToda()), "-"));
                data1.add(new Parameter("ASDA (m)", String.valueOf(logRunway1.getAsda()), "-"));
                data1.add(new Parameter("LDA (m)", String.valueOf(logRunway1.getLda()), "-"));
            }

            if(needRedeclare){
                data2.add(new Parameter("TORA (m)", String.valueOf(logRunway2.getTora()), String.valueOf(logRunway2.getNewTora())));
                data2.add(new Parameter("TODA (m)", String.valueOf(logRunway2.getToda()), String.valueOf(logRunway2.getNewToda())));
                data2.add(new Parameter("ASDA (m)", String.valueOf(logRunway2.getAsda()), String.valueOf(logRunway2.getNewAsda())));
                data2.add(new Parameter("LDA (m)", String.valueOf(logRunway2.getLda()), String.valueOf(logRunway2.getNewLda())));
            } else{
                data2.add(new Parameter("TORA (m)", String.valueOf(logRunway2.getTora()), "-"));
                data2.add(new Parameter("TODA (m)", String.valueOf(logRunway2.getToda()), "-"));
                data2.add(new Parameter("ASDA (m)", String.valueOf(logRunway2.getAsda()), "-"));
                data2.add(new Parameter("LDA (m)", String.valueOf(logRunway2.getLda()), "-"));
            }

            if(needRedeclare){
                revisedCol1.setCellFactory(cell -> new RedTableCell<>());
                revisedCol2.setCellFactory(cell -> new RedTableCell<>());
            } else{
                revisedCol1.setCellFactory(cell -> new BlackTableCell<>());
                revisedCol2.setCellFactory(cell -> new BlackTableCell<>());
            }
        }

        leftTableView.setItems(data1);
        rightTableView.setItems(data2);
    }

    public static class RedTableCell<T> extends TableCell<T, String> {
        public RedTableCell() {
            setTextFill(Color.RED);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                this.setStyle("-fx-background-color: rgb(244,244,244); -fx-alignment: CENTER;-fx-font-family: Verdana; -fx-padding: 7 0 0 0");
                // Set the text of the cell to the item
                setText(item);
            } else {
                setText(null);
            }
        }
    }

    public static class BlackTableCell<T> extends TableCell<T, String> {
        public BlackTableCell() {
            setTextFill(Color.BLACK);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                this.setStyle("-fx-background-color: rgb(244,244,244); -fx-alignment: CENTER;-fx-font-family: Verdana; -fx-padding: 7 0 0 0");
                // Set the text of the cell to the item
                setText(item);
            } else {
                setText(null);
            }
        }
    }


    private void editColumn(TableColumn<Parameter, String> tableColumn){
        tableColumn.setResizable(false);
        tableColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    this.setStyle("-fx-background-color: rgb(244,244,244); -fx-alignment: CENTER;-fx-font-family: Verdana; -fx-padding: 7 0 0 0");
                    // Set the text of the cell to the item
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });
    }

    //functions to load obstacles from xml files and displayed in the menu selection
    public void loadObstacles(String file) throws IOException, ParserConfigurationException, SAXException {
        obstacles = FXCollections.observableArrayList();
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
                obstacleHeightField.setText(""+obstacle.getHeight());
                obstacleWidthField.setText(""+obstacle.getWidth());
                performCalculationButton.setDisable(false);
                distanceThresholdTextField.setDisable(false);
                lrButtonGroup.setDisable(false);
                clDistTextField.setDisable(false);
                obstacleProperty.set(obstacle);
                obstacleMenu.setText(obstacle.getName());
                if(obstacle.getName().equals("Customisable Obstacle")){
                    obstacleHeightField.setEditable(true);
                    obstacleWidthField.setEditable(true);
                } else{
                    obstacleHeightField.setEditable(false);
                    obstacleWidthField.setEditable(false);
                }
                obstacleProperty.set(obstacle);
                checkDistFromThreshold(new ActionEvent());
                checkDistFromCentreLine(new ActionEvent());
                checkObstacleWidth(new ActionEvent());
                checkObstacleHeight(new ActionEvent());
            });
            obstacleMenu.getItems().add(obstacleMenuItem);
        }
    }

    public void obstaclesEditing(boolean notAllowed){
        obstacleMenu.setDisable(notAllowed);
        if(MainController.getObstacleSelected() != null){
            lrButtonGroup.setDisable(notAllowed);
            distanceThresholdTextField.setDisable(notAllowed);
            clDistTextField.setDisable(notAllowed);
            performCalculationButton.setDisable(notAllowed);
        }
    }

    public void initializeNotification( Pane pane, ScrollPane scrollPane, VBox vBox) {
        pane.setMinHeight(15);
        scrollPane.setMinHeight(0);
        scrollPane.setMaxHeight(1080);
        //set the scroll pane to the latest notification
        vBox.heightProperty().addListener((observableValue, number, t1) -> {
            scrollPane.setVvalue(scrollPane.getVmax());
        } );
        pane.setOnMousePressed(mouseEvent -> {
            double y = mouseEvent.getY();
            pane.setOnMouseDragged(event -> {
                //-ve when up, +ve when down
                double extension = event.getY() - y;
                double newPaneH = pane.getPrefHeight() - extension;
                double newSPaneH = scrollPane.getPrefHeight() - extension;
                double newPaneY = pane.getLayoutY() + extension;
                //set new Height and Layout
                pane.setPrefHeight(newPaneH);
                pane.setLayoutY(newPaneY);
                scrollPane.setPrefHeight(newSPaneH);
                getNotiVBox().setPrefHeight(newSPaneH);
                event.consume();
            });
            mouseEvent.consume();
        });
        //reset to bottom if our of bound
        pane.setOnMouseReleased(releaseEvent -> {
            double oriPaneY = 759;
            if (pane.getLayoutY() >= oriPaneY  || pane.getLayoutY() < 0 ){
                resetNotificationBar(getNotiPane(),getNotiScrollPane());
            }
        });
        getNotificationLabel().setOnMouseEntered(mouseEvent -> {
            getNotificationLabel().setOpacity(1.0);
            mouseEvent.consume();
        });
        getNotificationLabel().setOnMouseExited(mouseEvent -> {
            fadeTransition(getNotificationLabel());
            mouseEvent.consume();
        });
    }

    //add notification into history and set text into notification label
    public void setNotificationLabel(String string) {
        Label label = new Label(string);
        //label.setPrefHeight(40);
        label.setTextFill(Color.RED);
        label.setPadding(new Insets(5));
        Font font = Font.font("Verdana", FontWeight.MEDIUM, FontPosture.REGULAR,12);
        label.setFont(font);
        getNotiVBox().getChildren().add(label);
        getNotificationLabel().setText(string);
        fadeTransition(getNotificationLabel());
    }

    public void fadeTransition(Label label){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), label);
        //set opacity
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    public void resetNotificationBar(Pane pane, ScrollPane scrollPane){
        double oriPaneY = 759;
        double oriPaneH = 25;
        pane.setLayoutY(oriPaneY);
        pane.setPrefHeight(oriPaneH);
        scrollPane.setPrefHeight(0);
    }

    public void resetView(AnchorPane pane, Pane dragPane){
        dragPane.setTranslateX(0);
        dragPane.setTranslateY(0);
        pane.setScaleX(1);
        pane.setScaleY(1);
        pane.setRotate(0);
    }

    public void handleResetView(ActionEvent actionEvent) {
        //Variables
        AnchorPane topViewAnchorPane = getTopViewController().getTopDownRunwayPane();
        AnchorPane sideViewAnchorPane = getSideViewController().getSideOnPane();
        Pane topViewDragPane  = getTopViewController().getDragPane();
        Pane sideViewDragPane = getSideViewController().getDragPane();
        try {
            resetView(topViewAnchorPane,topViewDragPane);
            resetView(sideViewAnchorPane,sideViewDragPane);
            getTopViewController().initializeCompass();
            topViewController.rotateRunway();
        }catch (Exception e){
            System.out.println(e);
        }
        setNotificationLabel("Status: View Reset\t " + getDateTimeNow());
    }

    int clickCount = 0;
    public void showNotibar(ActionEvent actionEvent) {
        if (clickCount%2 ==0){
            notiPane.setVisible(true);
            notificationLabel.setVisible(false);
            resetNotificationBar(getNotiPane(),getNotiScrollPane());
        }else {

            notiPane.setVisible(false);
            notificationLabel.setVisible(true);
        }
        clickCount++;
    }

    public void extendNotiBar(ActionEvent actionEvent) {
        resetNotificationBar(notiPane,notiScrollPane);
        notiPane.setVisible(false);
        notificationLabel.setVisible(true);
        clickCount++;
    }

}
