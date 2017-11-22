package Client;

import java.rmi.RemoteException;

public interface ClientInterface {

    //Server Connected Methods
    void search(String keyWord) throws RemoteException;
    void listAll();
    void download() throws RemoteException, InterruptedException;
    void deleteContent();
    void modifyContent() throws RemoteException;
    void exit();
    void upload(String contentPath, String description);

    //Distributed Methods
    void showAllDistrubutedContent() throws RemoteException;
    void searchDistributedFromKeyword(String keyword) throws RemoteException;
    void downloadDistributedContent(String id, String title, String user) throws RemoteException;

}
