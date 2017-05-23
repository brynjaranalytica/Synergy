package server.remote_business_enitities;

import shared.business_entities.Memo;
import shared.remote_business_interfaces.RemoteMemoInterface;

import java.rmi.Remote;
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

    public RMemo(Memo memo) throws RemoteException{
        this.date = memo.getDate();
        this.description = memo.getDescription();
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof RMemo))
            return false;
        try {
            return  ((RMemo) obj).getDate().equals(date);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
}
