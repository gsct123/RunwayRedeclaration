package Model.Helper;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import View.AirportManager;
import View.Login;
import View.Main;
import View.Notification;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Utility {

    public static void changeScene(Stage previous, Application newStage) throws Exception {
        previous.close();
        newStage.start(new Stage());
    }

    @FXML
    public static void handleLogout(ActionEvent event) throws Exception {
        Main.getStage().close();
        new Login().start(new Stage());
    }

    public static int getScaleRange(double input){
        if(input <= 500){
            return 90;
        } else if(input <= 1000){
            return 240;
        } else if(input <= 1500){
            return 390;
        } else if (input <= 2000){
            return 600;
        } else if (input <= 2500){
            return 750;
        } else if (input <= 3000){
            return 960;
        } else if (input <= 3500){
            return 1200;
        } else{
            return 1500;
        }
    }

    public static void exportAirport(Stage stage, ObservableList<Airport> airports) throws ParserConfigurationException, TransformerException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Airport Details");

// Set initial directory for the file chooser
        File userDirectory = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(userDirectory);

// Set extension filters based on the type of file to be saved
        FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().addAll(textFilter, csvFilter);

// Show the file chooser and get the selected file directory
        File selectedDirectory = fileChooser.showSaveDialog(AirportManager.getStage());

        if (selectedDirectory != null) {
            // Get the file extension
            String extension = "";
            int dotIndex = selectedDirectory.getName().lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < selectedDirectory.getName().length() - 1) {
                extension = selectedDirectory.getName().substring(dotIndex + 1).toLowerCase();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedDirectory));

            // Check the file extension
            if (extension.equals("txt")) {
                writer.write(Utility.airportInfo(airports));
            } else if (extension.equals("xml")) {
                ObservableList<Airport> temp = FXCollections.observableArrayList(airports);
                XMLParserWriter.writeToFile(temp, selectedDirectory.getAbsolutePath());
            }
            new Notification(stage).sucessNotification("Download successfull", "Saved to "+selectedDirectory);
            writer.close();
        }
    }

    public static void exportVisual(Node contentNode){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Airport Details");

// Set initial directory for the file chooser
        File userDirectory = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(userDirectory);

// Set extension filters based on the type of file to be saved
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Png Files (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(pngFilter);

// Show the file chooser and get the selected file directory
        File selectedDirectory = fileChooser.showSaveDialog(AirportManager.getStage());

        if (selectedDirectory != null) {
            try {
                SnapshotParameters params = new SnapshotParameters();
                params.setTransform(Transform.scale(2, 2)); // Set the device pixel ratio to 2x
                params.setFill(Color.TRANSPARENT);

                double width = contentNode.getBoundsInLocal().getWidth();
                double height = contentNode.getBoundsInLocal().getHeight();

                WritableImage snapshot = new WritableImage((int) (width * 2), (int) (height * 2));
                snapshot = contentNode.snapshot(params, snapshot);

                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
                ImageIO.write(bufferedImage, "png", selectedDirectory);


            } catch (IOException e) {
                e.printStackTrace();
            }
            new Notification(Main.getStage()).sucessNotification("Download Successful", "Image file saved to "+selectedDirectory);
        }
    }

    public static String airportInfo(ObservableList<Airport> airports){
        StringBuilder message = new StringBuilder();
        for(Airport airport: airports){
            message.append("Airport name: ").append(airport.getName()).append(" (").append(airport.getID()).append(")");
            message.append("\nAirport manager: ").append(airport.getManager());
            message.append("\n\nPhysical runways:");
            ObservableList<PhysicalRunway> runways = airport.getPhysicalRunways();
            for(PhysicalRunway runway: runways){
                message.append("\n").append(runway.getName());
                ObservableList<LogicalRunway> logRunways = runway.getLogicalRunways();
                for(LogicalRunway logRunway: logRunways){
                    message.append("\n  ").append(logRunway.getDesignator());
                    message.append("\n      ").append("TORA: ").append(logRunway.getTora());
                    message.append("\n      ").append("ASDA: ").append(logRunway.getAsda());
                    message.append("\n      ").append("TODA: ").append(logRunway.getToda());
                    message.append("\n      ").append("LDA: ").append(logRunway.getLda());
                }
            }
            message.append("\n\n\n");
        }

        return message.toString();
    }
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }


    //allow zoom in / out when hodl down ctrl
    public static void initializeZoom(AnchorPane pane){
        //constants
        double zoomSens = 0.005;
        double zoomInLimit = 0.75;
        pane.setOnScroll(scrollEvent -> {
            //conditions
            boolean zoomReachLimit = pane.getScaleX() >= zoomInLimit && pane.getScaleY() >= zoomInLimit;
            boolean zoomSmallerThanLimit = pane.getScaleX() < zoomInLimit && pane.getScaleY() < zoomInLimit;
            //scroll event
            if (scrollEvent.isShortcutDown()){
                double zoom = scrollEvent.getDeltaY();
                if (zoomReachLimit){
                    pane.setScaleX(pane.getScaleX() - zoom * -zoomSens);
                    pane.setScaleY(pane.getScaleY() - zoom * -zoomSens);
                }else if (zoomSmallerThanLimit){
                    pane.setScaleX(zoomInLimit);
                    pane.setScaleY(zoomInLimit);
                }
                scrollEvent.consume();
            }
        });
    }
    public static void initializeDrag(Pane dragPane){
        dragPane.setOnMousePressed(mouseEvent->{
            if (mouseEvent.isShortcutDown()){
                //mouse position before drag
                double mouseX = mouseEvent.getX();
                double mouseY = mouseEvent.getY();
                dragPane.setOnMouseDragged(dragEvent -> {
                    //the amount dragged minus original mouse position
                    double translationX = dragEvent.getX() - mouseX;
                    double translationY = dragEvent.getY() - mouseY;
                    //set translation
                    dragPane.setTranslateX(dragPane.getTranslateX() + translationX);
                    dragPane.setTranslateY(dragPane.getTranslateY() + translationY);
                    dragEvent.consume();
                });
            }
        });
        dragPane.setOnMouseReleased( releasedEvent -> {
            dragPane.setOnMouseDragged(null);
        });
    }


    //function from https://www.educative.io/answers/how-to-use-math-atan2double-y-double-x-in-java
    public static double getAngleBetween(double x1, double y1, double x2, double y2, double x3, double y3){
        double angle1To3 = Math.atan2(y1-y3,x1-x3);
        double angle2To3 = Math.atan2(y2-y3,x2-x3);
        double differenceInAngle = angle2To3 - angle1To3;
        //System.out.println(Math.toDegrees(differenceInAngle));
        //System.out.println(differenceInAngle);
        return Math.toDegrees(differenceInAngle);
    }

    public static void resetScale(AnchorPane pane){
        pane.setScaleX(1);
        pane.setScaleY(1);
        pane.setRotate(0);
        pane.setTranslateX(0);
        pane.setTranslateY(0);
        pane.setTranslateZ(0);
    }



}
