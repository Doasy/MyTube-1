package Client;

import Content.ContentInterface;

import java.util.List;

public interface ClientInterface {
    List<ContentInterface> search(String keyWord);

    List<String> listAll();

    ContentInterface download(String contentName);

    String upload(ContentInterface content);

    void exit();
}
