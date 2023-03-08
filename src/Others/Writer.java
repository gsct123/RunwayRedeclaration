package Others;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

public class Writer {
    public static void main(String[] args) {
        Writer tmp = new Writer();

        ObservableList<LogicalRunway> logRunways = FXCollections.observableArrayList();
        logRunways.add(new LogicalRunway("09L", 3902, 3902, 3902, 3595));
        logRunways.add(new LogicalRunway("27R", 3884, 3962, 3884, 3884));

        ObservableList<PhysicalRunway> physRunways = FXCollections.observableArrayList();
        physRunways.add(new PhysicalRunway("09L/27R", logRunways));


        Airport airport = new Airport("Heathrow", physRunways);

        tmp.helperWriter();
    }

    public void helperWriter(List<Airport> airports, String file) throws Exception {
        // Create a JAXB context for the Person class
        JAXBContext context = JAXBContext.newInstance(Airport.class);

        // Create a Marshaller from the JAXB context
        Marshaller marshaller = context.createMarshaller();

        // Set the Marshaller to output formatted XML
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Create a new root element for the XML document
        Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("people");

        // Iterate through the List of Person objects and create a new XML element for each one
        for (Airport airport : airports) {
            Element airportElement = root.getOwnerDocument().createElement("airport");
            Element nameElement = root.getOwnerDocument().createElement("name");
            nameElement.setTextContent(airport.getName());
            // Create an XML element for the physical runways
            Element physRunwayElements = root.getOwnerDocument().createElement("physicalRunways");

            // Iterate through the list of physical runways and create an XML element for each one
            for (PhysicalRunway physicalRunway : airport.getPhysicalRunways()) {
                Element physRunwayElement = root.getOwnerDocument().createElement("physicalRunway");

                // Add attributes to the runway element
                physRunwayElement.setAttribute("name", physicalRunway.getName());

                Element logRunwayElements = root.getOwnerDocument().createElement("logicalRunways");

                for(LogicalRunway logicalRunway : physicalRunway.getLogicalRunways()){
                    Element logRunwayElement = root.getOwnerDocument().createElement("logicalRunway");

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
            root.appendChild(airportElement);
        }

        // Write the XML document to a file
        marshaller.marshal(root, new File(file));
    }
}



