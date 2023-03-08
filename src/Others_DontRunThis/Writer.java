package Others_DontRunThis;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;
import java.util.List;

public class Writer {
    public static void main(String[] args) throws Exception {
        Writer tmp = new Writer();

        ObservableList<LogicalRunway> logRunways = FXCollections.observableArrayList();
        logRunways.add(new LogicalRunway("09L", 3902, 3902, 3902, 3595));
        logRunways.add(new LogicalRunway("27R", 3884, 3962, 3884, 3884));
        logRunways.add(new LogicalRunway("09R", 3660, 3660, 3660, 3353));
        logRunways.add(new LogicalRunway("27L", 3660, 3660, 3660, 3660));

        ObservableList<PhysicalRunway> physRunways = FXCollections.observableArrayList();
        physRunways.add(new PhysicalRunway("09L/27R", logRunways));

        Airport airport = new Airport("Heathrow", physRunways);
        List<Airport> airports = new ArrayList<>();
        airports.add(airport);

        tmp.helperWriter(airports, "src/Data/airports.xml");
    }

    public void helperWriter(List<Airport> airports, String file) throws Exception {
        // Create a DocumentBuilder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Create the root element
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("airports");
        doc.appendChild(rootElement);

        // Create an XML element for each airport in the list
        for (Airport airport : airports) {
            Element airportElement = doc.createElement("airport");
            rootElement.appendChild(airportElement);

            // Create an XML element for the airport name
            Element nameElement = doc.createElement("name");
            nameElement.appendChild(doc.createTextNode(airport.getName()));
            airportElement.appendChild(nameElement);

            // Create an XML element for the physical runways
            Element physRunwayElements = doc.createElement("physicalRunways");
            airportElement.appendChild(physRunwayElements);

            // Iterate through the list of physical runways and create an XML element for each one
            for (PhysicalRunway physicalRunway : airport.getPhysicalRunways()) {
                Element physRunwayElement = doc.createElement("physicalRunway");
                physRunwayElement.setAttribute("name", physicalRunway.getName());
                physRunwayElements.appendChild(physRunwayElement);

                // Create an XML element for the logical runways
                Element logRunwayElements = doc.createElement("logicalRunways");
                physRunwayElement.appendChild(logRunwayElements);

                // Iterate through the list of logical runways and create an XML element for each one
                for (LogicalRunway logicalRunway : physicalRunway.getLogicalRunways()) {
                    Element logRunwayElement = doc.createElement("logicalRunway");
                    logRunwayElement.setAttribute("designator", logicalRunway.getDesignator());
                    logRunwayElement.setAttribute("tora", Double.toString(logicalRunway.getTora()));
                    logRunwayElement.setAttribute("toda", Double.toString(logicalRunway.getToda()));
                    logRunwayElement.setAttribute("asda", Double.toString(logicalRunway.getAsda()));
                    logRunwayElement.setAttribute("lda", Double.toString(logicalRunway.getLda()));

                    logRunwayElements.appendChild(logRunwayElement);
                }

                physRunwayElements.appendChild(physRunwayElement);
            }
            physRunwayElements.setTextContent(airport.getPhysicalRunways().toString());
            airportElement.appendChild(nameElement);
            airportElement.appendChild(physRunwayElements);
            rootElement.appendChild(airportElement);
        }

        // Write the XML document to a file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
}



