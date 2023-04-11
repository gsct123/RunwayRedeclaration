package Model.Helper;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

public class XMLParserWriter {
    public static void writeToFile(ObservableList<Airport> airports, String fileName) throws ParserConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(toXML(airports));
        StreamResult streamResult = new StreamResult(new File(fileName));
        transformer.transform(domSource, streamResult);
    }

    public static String printString(ObservableList<Airport> airports) throws TransformerException, ParserConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(toXML(airports));
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(domSource, result);
        return writer.toString();
    }

    public static Document toXML(ObservableList<Airport> airports) throws ParserConfigurationException {
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
        return document;
    }
}
