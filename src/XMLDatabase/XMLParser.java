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

    public List<String> XMLFindByKeyWord(String keyWord){
        List<Element> contentList = classElement.getChildren();
        List<String> listOfTitles = new ArrayList<>();
        keyWord = keyWord.toLowerCase();

        for (Element content : contentList) {
            String title = content.getChild("Title").getText().toLowerCase();
            String description = content.getChild("Description").getText().toLowerCase();

            if(title.contains(keyWord) || description.contains(keyWord)){
                listOfTitles.add(content.getChild("Title").getText());
            }
        }
        return listOfTitles;
    }


}
