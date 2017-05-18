package server.remote_business_enitities;

import shared.MessageHeaders;
import shared.business_entities.Member;
import shared.remote_business_interfaces.RemoteMemberInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by lenovo on 4/12/2017.
 */
public class RMember implements RemoteMemberInterface {
    private String email;
    private String name;

    public RMember(String email, String name) throws RemoteException{
        this.email = email;
        this.name = name;
        UnicastRemoteObject.exportObject(this,0);
    }

    public RMember(Member member) throws RemoteException{
        this.email = member.getEmail();
        this.name = member.getName();
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String getEmail() throws RemoteException {
        return email;
    }

    @Override
    public void setEmail(String email) throws RemoteException {
        this.email = email;
    }
}
