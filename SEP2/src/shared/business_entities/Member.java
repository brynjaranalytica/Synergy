package shared.business_entities;


import shared.remote_business_interfaces.RemoteMemberInterface;

import java.rmi.RemoteException;

/**
 * Created by lenovo on 4/11/2017.
 */
public class Member implements BusinessEntity{
    private String email;
    private String name;

    public Member(RemoteMemberInterface remoteMember) throws RemoteException{
        this.email = remoteMember.getEmail();
        this.name = remoteMember.getName();
    }

    public Member(String name, String email) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
