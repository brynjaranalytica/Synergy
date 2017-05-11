package shared.business_entities;

import shared.remote_business_interfaces.RemoteCalendarInterface;
import shared.remote_business_interfaces.RemoteMemoInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by lenovo on 5/11/2017.
 */
public class Calendar implements BusinessEntity {
    private ArrayList<Memo> events;

    public Calendar() {
        this.events = new ArrayList<>();
    }

    public Calendar(RemoteCalendarInterface remoteCalendar){
        this.events = new ArrayList<>();
        try {
            ArrayList<RemoteMemoInterface> remoteMemos = remoteCalendar.getMemos();
            for(RemoteMemoInterface remoteMemo: remoteMemos)
                this.events.add(new Memo(remoteMemo));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addEvent(Memo memo){
        events.add(memo);
    }

    public ArrayList<Memo> getMemos(){
        return this.events;
    }
}
