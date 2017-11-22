package ServerRemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MyTubeInterface extends Remote {

    String getContentFromKey(int key) throws RemoteException;

    String getContentFromTitle(String title) throws RemoteException;

    byte[] downloadSpecificContent(String id, String title, String user) throws RemoteException;

    byte[] downloadDistributedContent(String id, String title, String user) throws RemoteException;

    String getTitleFromKey(int key) throws RemoteException;

    List<String> searchFromKeyword(String keyword) throws RemoteException;

    List<String> searchAll() throws RemoteException;

    String uploadContent(String title, String description, byte[] fileData, String userName) throws RemoteException;

    byte[] downloadContent(int id) throws RemoteException;

    String modifyContent(String id, String title, String description) throws RemoteException;

    String deleteContent(String id, String userName) throws RemoteException;

    List<String> showOwnFiles(String userName) throws RemoteException;

    void addCallback(MyTubeCallbackInterface callbackObject) throws RemoteException;

    void removeCallback(MyTubeCallbackInterface callbackObject) throws RemoteException;

    List<String> showAllDistributedContent() throws RemoteException;

    List<String> searchDistributedFromKeyword(String keyword) throws RemoteException;

    boolean isValidID(int ID) throws RemoteException;
}
