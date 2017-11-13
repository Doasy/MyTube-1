package Client;

import Content.ContentInterface;

import java.util.List;

public interface ClientInterface {
    List<ContentInterface> search(String keyWord);

    List<ContentInterface> listAll();

    ContentInterface download();

    String upload(ContentInterface content);

    void exit();
}
