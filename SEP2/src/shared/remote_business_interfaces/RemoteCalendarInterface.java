package shared.remote_business_interfaces;

import server.remote_business_enitities.RMemo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenovo on 5/11/2017.
 */
public interface RemoteCalendarInterface extends Remote {

    void addMemo(RemoteMemoInterface remoteMemo) throws RemoteException;

    ArrayList<RemoteMemoInterface> getMemos() throws RemoteException;

    RemoteMemoInterface getMemo(Date date) throws RemoteException;

    void removeMemo(Date date) throws RemoteException;

}
