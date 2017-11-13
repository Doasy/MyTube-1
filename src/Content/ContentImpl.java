package Content;


import java.io.File;

public class ContentImpl implements ContentInterface {
    private File file;
    private String title;
    private String description;

    public ContentImpl(String title, String description, File file){
        this.file=file;
        this.title=title;
        this.description=description;
    }

    public File getFile() {
        return file;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
