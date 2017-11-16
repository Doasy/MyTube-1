package XMLDatabase;

import org.w3c.dom.*;
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
import java.util.List;

public class XMLCreator {
    private String filename = "./server01/Contents.xml";
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    private Document document;
    private Element classElement;

    public XMLCreator() throws ParserConfigurationException {
        File file = new File(filename);

        try {
            if (file.exists()) {

                this.document = dBuilder.parse(file);
                classElement = document.getDocumentElement();
            } else {

                this.document = dBuilder.newDocument();
                this.classElement = document.createElement("Contents");
                document.appendChild(classElement);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File(this.filename));
                transformer.transform(source, result);
            }
        }catch( IOException | SAXException | TransformerException ex){
            ex.printStackTrace();
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

    public String deleteElement(String user, String id) throws TransformerException {
        Node content = document.getFirstChild();
        NodeList contentList = content.getChildNodes();
        XMLParser xmlParser = new XMLParser();

        for (int temp = 0; temp < contentList.getLength(); temp++) {
            Node childNode = contentList.item(temp);
            if(isEquals(id, childNode) && xmlParser.userIsUploader(user, id)){
                content.removeChild(childNode);
            }
        }

        writeXML();

        return "Element Deleted Correctly";
    }

    private boolean isEquals(String id, Node childNode) {
        return childNode.getAttributes().item(0).getNodeValue().equals(id);
    }

    private void writeXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(this.filename));
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
