package Controller;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditAirportController implements Initializable {
    private static Airport airportSelected;

    @FXML
    private TextArea textArea;

    public static void setAirportSelected(Airport airportSelected) {
        EditAirportController.airportSelected = airportSelected;
    }

    public EditAirportController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textArea.setText(airportInfo(airportSelected));
    }

    public String airportInfo(Airport airport){
        StringBuilder message = new StringBuilder();
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
        return message.toString();
    }

    public static Airport parseAirport(String content) throws FileNotFoundException {
        String[] lines = content.split("\n");
        String[] nameReference = lines[0].replace("Airport name: ", "").split("(");
        String airportName = nameReference[0].trim();
        String airportReference = nameReference[1].trim().substring(0, nameReference[1].trim().length()-1);
        String airportManager = lines[1].replace("Airport manager: ", "");
        ObservableList<PhysicalRunway> physicalRunways = FXCollections.observableArrayList();
        int i = 3;
        while (i < lines.length) {
            String[] runwayInfo = lines[i].split("/");
            String runwayName = runwayInfo[0].trim();
            ObservableList<LogicalRunway> logicalRunways = FXCollections.observableArrayList();
            i++;
            while (i < lines.length && lines[i].startsWith(" ")) {
                String[] logicalRunwayInfo = lines[i].trim().split("\s+");
                String logicalRunwayDesignator = logicalRunwayInfo[0];
                double tora = Double.parseDouble(logicalRunwayInfo[1].replace("TORA:", ""));
                double toda = Double.parseDouble(logicalRunwayInfo[2].replace("TODA:", ""));
                double asda = Double.parseDouble(logicalRunwayInfo[3].replace("ASDA:", ""));
                double lda = Double.parseDouble(logicalRunwayInfo[4].replace("LDA:", ""));
                LogicalRunway logicalRunway = new LogicalRunway(logicalRunwayDesignator, tora, toda, asda, lda);
                logicalRunways.add(logicalRunway);
                i++;
            }
            PhysicalRunway physicalRunway = new PhysicalRunway(runwayName, logicalRunways);
            physicalRunways.add(physicalRunway);
        }
        Airport airport = new Airport(airportReference, airportName, physicalRunways, airportManager);
        return airport;
    }
}
