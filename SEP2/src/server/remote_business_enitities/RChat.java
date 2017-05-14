package server.remote_business_enitities;

import shared.remote_business_interfaces.RemoteChatInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Nicolai Onov on 5/10/2017.
 */
public class RChat implements RemoteChatInterface {
    private ArrayList<String> messages;

    public RChat() throws RemoteException{
        this.messages = new ArrayList<>();
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void addMessage(String message) throws RemoteException {
        messages.add(message);
    }

    @Override
    public ArrayList<String> getMessages() throws RemoteException {
        return messages;
    }
}
