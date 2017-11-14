package RemoteInterface;

import Content.ContentInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MyTubeInterface extends Remote {

    /**
     * Get a Content from key or null if not exist
     *
     * @param key key of the Content
     * @return Content with specified key
     * @throws RemoteException if can't make the petition to the Server
     */
    public ContentInterface getContentFromKey(int key) throws RemoteException;


    public ContentInterface getContentFromTitle(String title) throws RemoteException;


    public List<String> searchFromKeyword(String keyword)
            throws RemoteException;

    public List<String> searchAll() throws RemoteException;

    public String uploadContent(String title, String description, byte[] fileData)
            throws RemoteException;

    public ContentInterface downloadContent()
            throws RemoteException;

    /**
     * Add a MyTubeCallback object into the callback objects lists
     *
     * @param callbackObject callbackObject to be added on callback list
     * @throws RemoteException if can't make the petition to the Server
     */
   /* public void addCallback(MyTubeCallback callbackObject)
            throws RemoteException;
*/
    /**
     * Removes a MyTubeCallback object from the callback objects list
     *
     * @param callbackObject callbackObject to remove from callback list
     * @throws RemoteException if can't make the petition to the Server
     */
   /* public void removeCallback(MyTubeCallback callbackObject)
            throws RemoteException;*/


    byte[] downloadContent(String contentName) throws RemoteException;

    void exit() throws RemoteException;
}
