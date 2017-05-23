package server.remote_business_enitities;

import shared.business_entities.Calendar;
import shared.business_entities.Memo;
import shared.remote_business_interfaces.RemoteCalendarInterface;
import shared.remote_business_interfaces.RemoteMemoInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenovo on 5/11/2017.
 */
public class RCalendar implements RemoteCalendarInterface {
    private ArrayList<RemoteMemoInterface> events;

    public RCalendar() throws RemoteException{
        this.events = new ArrayList<>();
        UnicastRemoteObject.exportObject(this, 0);
    }

    public RCalendar(Calendar calendar) throws RemoteException{
        this.events = new ArrayList<>();
        ArrayList<Memo> memos = calendar.getMemos();
        for(Memo memo: memos){
            this.events.add(new RMemo(memo));
        }
        UnicastRemoteObject.exportObject(this, 0);
    }



    @Override
    public void addMemo(RemoteMemoInterface remoteMemo) throws RemoteException {
        this.events.add(remoteMemo);
    }

    @Override
    public ArrayList<RemoteMemoInterface> getMemos() throws RemoteException {
        return this.events;
    }

    @Override
    public RemoteMemoInterface getMemo(Date date) throws RemoteException {
        for(RemoteMemoInterface memo:events){
            if (memo.getDate().equals(date))
                return memo;
        }

        return null;    }

    @Override
    public void setMemo(RemoteMemoInterface updatedRemoteMemo) throws RemoteException {
        for(int i = 0; i < events.size(); i++){
            if(events.get(i).getDate().equals(updatedRemoteMemo.getDate())){
                events.set(i, updatedRemoteMemo);
                return;
            }
        }
    }

    @Override
    public void removeMemo(Date date) throws RemoteException {
        for(RemoteMemoInterface memo: events) {
            if (memo.getDate().equals(date)) {
                events.remove(memo);
                break;
            }
        }
    }
}
