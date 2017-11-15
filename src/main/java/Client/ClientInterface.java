package Client;

import java.util.List;

public interface ClientInterface {
    void search(String keyWord);

    void listAll();

    void download();

    String upload(String contentName);

    void exit();
}
