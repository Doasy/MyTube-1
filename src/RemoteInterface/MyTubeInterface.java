package RemoteInterface;

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
    String getContentFromKey(int key) throws RemoteException;

    String getContentFromTitle(String title) throws RemoteException;

    List<String> searchFromKeyword(String keyword) throws RemoteException;

    List<String> searchAll() throws RemoteException;

    String uploadContent(String title, String description, byte[] fileData, String userName) throws RemoteException;

    byte[] downloadContent(int id) throws RemoteException;

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
     * @throws RemoteException if can't make the petition to the Server
     */
   /* public void removeCallback(MyTubeCallback callbackObject)
            throws RemoteException;*/

    void exit() throws RemoteException;
}
