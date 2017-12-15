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
    private Element classElement;

    public XMLParser(){
        try {
            File file = new File(filename);
            SAXBuilder saxBuilder = new SAXBuilder();

            Document document = saxBuilder.build(file);
            this.classElement = document.getRootElement();
        }catch (JDOMException | IOException ex){
            ex.printStackTrace();
        }
    }

    public void initFile(){
        try {
            File file = new File(filename);
            SAXBuilder saxBuilder = new SAXBuilder();

            Document document = saxBuilder.build(file);
            this.classElement = document.getRootElement();
        }catch (JDOMException | IOException ex){
            ex.printStackTrace();
        }
    }

    public List<String> XMLShowALL(){
        initFile();
        List<Element> contentList = classElement.getChildren();
        List<String> listOfTitles = new ArrayList<>();
        String contentText;

        for (Element content : contentList) {
            contentText = "ID: " + content.getAttributeValue("id") +
                    " Title: " + content.getChild("Title").getText() +
                    " Description: " + content.getChild("Description").getText() +
                    " Uploader: " + content.getChild("Uploader").getText();
            listOfTitles.add(contentText);
        }
        return listOfTitles;
    }

    public synchronized String newID(){
        return String.valueOf(Integer.parseInt(getLastId())+1);
    }

    public List<String> XMLFindByKeyWord(String keyWord){
        initFile();
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

    public List<String> XMLFindByUserName(String userName){
        initFile();
        List<Element> contentList = classElement.getChildren();
        List<String> listOfTitles = new ArrayList<>();
        String contentText;

        for (Element content : contentList) {
            String title = content.getChild("Uploader").getText();

            if(title.contains(userName)){
                contentText = "ID: " + content.getAttributeValue("id") +
                        " Title: " + content.getChild("Title").getText() +
                        " Description: " + content.getChild("Description").getText() +
                        " Uploader: " + content.getChild("Uploader").getText();
                listOfTitles.add(contentText);
            }
        }
        return listOfTitles;
    }

    public String XMLDownloaDistributedContent(String id, String title, String user){
        List<Element> contentList = classElement.getChildren();
        initFile();

        for (Element content : contentList) {
            String uploader = content.getChild("Uploader").getText().toLowerCase();
            String filename = content.getChild("Title").getText().toLowerCase();
            String fileId = content.getAttributeValue("id");

            if(user.toLowerCase().equals(uploader) &&
                    title.toLowerCase().equals(filename) && id.equals(fileId)){
                return "/" + id + "/"+ title;
            }
        }
        return "";
    }

    public int XMLFindIdByTitle(String title){
        List<Element> contentList = classElement.getChildren();
        initFile();

        for (Element content : contentList) {
            if(title.toLowerCase().equals(content.getChild("Title").getText().toLowerCase())){
                return Integer.parseInt(content.getAttributeValue("id"));
            }
        }
        return 0;
    }

    public String getNameById(String id){
        List<Element> contentList  = classElement.getChildren();
        initFile();

        for (Element content : contentList){
            if(isId(id, content)){
                return content.getChild("Title").getText();
            }
        }
        return "";
    }

    public boolean idExists(String id){
        initFile();
        String response = getNameById(id);
        return !response.equals("");
    }

    private String getLastId(){
        List<Element> contentList = classElement.getChildren();
        initFile();

        if(contentList.isEmpty()){
            return "0";
        }

        return contentList.get(contentList.size()-1).getAttributeValue("id");
    }

    public boolean userIsUploader(String user, String id){
        List<Element> contentList = classElement.getChildren();
        initFile();
        for (Element content : contentList) {
            if(isId(id, content) && isUploader(user, content)){
                return true;
            }
        }
        return false;
    }

    private boolean isUploader(String user, Element content) {
        initFile();
        return content.getChild("Uploader").getText().equals(user);
    }

    private boolean isId(String id, Element content) {
        initFile();
        return content.getAttributeValue("id").equals(id);
    }

}
