package XMLDatabase;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLCreator {
    private static final String FILENAME = "server01/Contents.xml";
    private Document document;
    private Element classElement;

    public XMLCreator() throws ParserConfigurationException, IOException, SAXException {
        File file = new File(FILENAME);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        if(file.exists()){
            this.document = dBuilder.parse(file);
            classElement = document.getDocumentElement();
        }else {
            this.document = dBuilder.newDocument();
            this.classElement = document.createElement("Contents");
            document.appendChild(classElement);
        }
    }

    public void addElement(String id, String contentTitle, String contentDescription, String uploaderName){
        try{
            Element content = addContent(id);

            addTitleAttr(contentTitle, content);
            addDescriptionAttr(contentDescription, content);
            addUploaderAttr(uploaderName, content);

            writeXML();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Element addContent(String id) {
        Element content = document.createElement("Content");
        this.classElement.appendChild(content);
        Attr attr = document.createAttribute("id");
        attr.setValue(id);
        content.setAttributeNode(attr);
        return content;
    }

    private void writeXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(FILENAME));
        transformer.transform(source, result);
    }

    private void addUploaderAttr(String uploaderName, Element content) {
        Element uploader = document.createElement("Uploader");
        uploader.appendChild(document.createTextNode(uploaderName));
        content.appendChild(uploader);
    }

    private void addTitleAttr(String ContentTitle, Element content) {
        Element title = document.createElement("Title");
        title.appendChild(document.createTextNode(ContentTitle));
        content.appendChild(title);
    }

    private void addDescriptionAttr(String ContentDescription, Element content){
        Element description = document.createElement("Description");
        description.appendChild(document.createTextNode(ContentDescription));
        content.appendChild(description);
    }
}
