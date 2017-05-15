package shared.business_entities;

import shared.remote_business_interfaces.RemoteCalendarInterface;
import shared.remote_business_interfaces.RemoteMemoInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

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

    public void addMemo(Memo memo){
        events.add(memo);
    }

    public void removeMemo(Date date) {
        for(Memo memo: events) {
            if (memo.getDate().equals(date))
                events.remove(memo);
        }
    }

    public ArrayList<Memo> getMemos(){
        return this.events;
    }

    public Memo getMemo(Date date) {
        for(Memo memo:events){
            if (memo.getDate().equals(date))
                return memo;
        }

        return null;
    }
}
