package server.remote_business_enitities;

import shared.remote_business_interfaces.RemoteMemoInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Created by lenovo on 5/11/2017.
 */
public class RMemo implements RemoteMemoInterface {
    private Date date;
    private String description;

    public RMemo(Date date, String description) throws RemoteException {
        this.date = date;
        this.description = description;
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public String getDescription() throws RemoteException {
        return this.description;
    }

    @Override
    public Date getDate() throws RemoteException {
        return this.date;
    }

    @Override
    public void setDescription(String description) throws RemoteException {
        this.description = description;
    }

    @Override
    public void setDate(Date date) throws RemoteException {
        this.date = date;
    }
}
