package Client;

import java.rmi.RemoteException;

public interface ClientInterface {

    void search(String keyWord);

    void listAll();

    void download();

    String upload(String contentPath, String description);

    void deleteContent(String id);

    void modifyContent() throws RemoteException;

    void exit();
}
