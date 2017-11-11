package RemoteInterface;

import java.rmi.Remote;

public interface RemoteInterface extends Remote{

    void upload();
    void download();
    String getTitles();
}
