package server.remote_business_enitities;

import shared.remote_business_interfaces.RemoteCalendarInterface;
import shared.remote_business_interfaces.RemoteMemoInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by lenovo on 5/11/2017.
 */
public class RCalendar implements RemoteCalendarInterface {
    private ArrayList<RemoteMemoInterface> events;

    public RCalendar() throws RemoteException{
        this.events = new ArrayList<>();
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void addEvent(RemoteMemoInterface remoteMemo) throws RemoteException {
        this.events.add(remoteMemo);
    }

    @Override
    public ArrayList<RemoteMemoInterface> getMemos() throws RemoteException {
        return this.events;
    }
}
