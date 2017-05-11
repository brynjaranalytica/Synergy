package shared.remote_business_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Nicolai Onov on 5/10/2017.
 */
public interface RemoteChatInterface extends Remote {
    void addMessage(String message) throws RemoteException;

    ArrayList<String> getMessages() throws RemoteException;
}
