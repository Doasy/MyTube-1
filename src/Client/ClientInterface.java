package Client;

import java.util.List;

public interface ClientInterface {
    List<String> search(String keyWord);

    List<String> listAll();

    void download(String contentName);

    String upload(String contentName);

    void exit();
}
