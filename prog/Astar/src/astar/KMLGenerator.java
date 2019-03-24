package astar;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KMLGenerator {
    private final DocumentBuilderFactory docFactory;
    private final DocumentBuilder docBuilder;
    private final Document doc;

    private final TransformerFactory transformerFactory;
    private final Transformer transformer;

    private final DOMSource source;
    private final StreamResult result;

    private Element Document;

    public KMLGenerator() throws ParserConfigurationException, TransformerConfigurationException {
        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.newDocument();

        transformerFactory = TransformerFactory.newInstance();
        transformer = transformerFactory.newTransformer();

        source = new DOMSource(doc);
        result = new StreamResult(new File("routes.kml"));
    }

    public void init() {
        Element kml = doc.createElement("kml");
        kml.setAttribute("xmlns", "http://earth.google.com/kml/2.1");
        doc.appendChild(kml);

        Document = doc.createElement("Document");
        kml.appendChild(Document);

        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode("Taxi Routes"));
        Document.appendChild(name);

        Document.appendChild(createStyleNode("green", "ff009900"));
        Document.appendChild(createStyleNode("red", "ff0000ff"));
    }

    public void append(String taxiName, String lineColor, String coordinates) {
        Document.appendChild(createPlacemarkNode(taxiName, lineColor, coordinates));
    }

    public void write() throws TransformerException {
        transformer.transform(source, result);
    }

    private Element createStyleNode(String colorDesc, String colorHex) {
        Element Style = doc.createElement("Style");
        Style.setAttribute("id", colorDesc);

        Element LineStyle = doc.createElement("LineStyle");
        Style.appendChild(LineStyle);

        Element color = doc.createElement("color");
        color.appendChild(doc.createTextNode(colorHex));
        Element width = doc.createElement("width");
        width.appendChild(doc.createTextNode("4"));
        LineStyle.appendChild(color);
        LineStyle.appendChild(width);
        Style.appendChild(LineStyle);

        return Style;
    }

    private Element createPlacemarkNode(String plName, String color, String coords) {
        Element Placemark = doc.createElement("Placemark");

        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(plName));

        Element styleUrl = doc.createElement("styleUrl");
        styleUrl.appendChild(doc.createTextNode("#" + color));

        Element LineString = doc.createElement("LineString");

        Element altitudeMode = doc.createElement("altitudeMode");

        Element coordinates = doc.createElement("coordinates");
        coordinates.appendChild(doc.createTextNode(coords));

        LineString.appendChild(altitudeMode);
        LineString.appendChild(coordinates);

        Placemark.appendChild(name);
        Placemark.appendChild(styleUrl);
        Placemark.appendChild(LineString);

        return Placemark;
    }
}
