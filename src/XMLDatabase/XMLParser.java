package XMLDatabase;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    private static final String filename = "server01/Contents.xml";
    private Document document;
    private Element classElement;

    public XMLParser() throws JDOMException, IOException {
        File file = new File(filename);
        SAXBuilder saxBuilder = new SAXBuilder();
        this.document = saxBuilder.build(file);
        this.classElement = (Element) document.getRootElement();
    }

    public List<String> XMLShowALL(){
        List<Element> contentList = classElement.getChildren();
        List<String> listOfTitles = new ArrayList<>();

        for (Element content : contentList) {
            listOfTitles.add(content.getChild("Title").getText());
        }
        return listOfTitles;
    }

    public synchronized String newID(){
        return String.valueOf(Integer.parseInt(getLastId())+1);
    }



    public List<String> XMLFindByKeyWord(String keyWord){
        List<Element> contentList = classElement.getChildren();
        List<String> listOfTitles = new ArrayList<>();
        String contentText;
        keyWord = keyWord.toLowerCase();

        for (Element content : contentList) {
            String title = content.getChild("Title").getText().toLowerCase();
            String description = content.getChild("Description").getText().toLowerCase();

            if(title.contains(keyWord) || description.contains(keyWord)){
                contentText = "ID: " + content.getAttributeValue("id") +
                        " Title: " + content.getChild("Title").getText() +
                        " Description: " + content.getChild("Description").getText() +
                        " Uploader: " + content.getChild("Uploader").getText();
                listOfTitles.add(contentText);
            }
        }
        return listOfTitles;
    }

    public int XMLFindIdByTitle(String title){
        List<Element> contentList = classElement.getChildren();

        for (Element content : contentList) {
            if(title.toLowerCase().equals(content.getChild("Title").getText().toLowerCase())){
                return Integer.parseInt(content.getAttributeValue("id"));
            }
        }
        return 0;
    }

    public String getLastId(){
        List<Element> contentList = classElement.getChildren();

        return contentList.get(-1).getAttributeValue("id");
    }
}
