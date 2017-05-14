package shared.business_entities;

import shared.remote_business_interfaces.RemoteMemoInterface;

import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by lenovo on 5/11/2017.
 */
public class Memo implements BusinessEntity {
    private Date date;
    private String description;

    public Memo(Date date, String description) {
        this.date = date;
        this.description = description;
    }

    public Memo(RemoteMemoInterface remoteMemo){
        try {
            this.date = remoteMemo.getDate();
            this.description = remoteMemo.getDescription();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getDescription(){
        return this.description;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
