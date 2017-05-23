package shared.remote_business_interfaces;

import shared.UpdateMessage;
import shared.business_entities.BusinessEntity;
import shared.business_entities.Memo;
import utility.observer.RemoteSubject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lenovo on 4/12/2017.
 */
public interface RemoteProjectInterface extends RemoteSubject<UpdateMessage> {
    String getName() throws RemoteException;

    ArrayList<RemoteMemberInterface> getMembers() throws RemoteException;

    void setName(String name) throws RemoteException;

    void setMembers(ArrayList<RemoteMemberInterface> members) throws RemoteException;

    RemoteTaskListInterface getTaskList()throws RemoteException;

    void setTaskList(RemoteTaskListInterface taskList) throws RemoteException;

    void addTask(String task) throws RemoteException;

    void addMember(RemoteMemberInterface member) throws RemoteException;

    void addMember(String email) throws RemoteException;

    RemoteMemberInterface getMember(int index) throws RemoteException;

    RemoteMemberInterface getMember(String email) throws RemoteException;

    RemoteChatInterface getChat() throws RemoteException;

    RemoteCalendarInterface getCalendar() throws RemoteException;

    void setCalendar(RemoteCalendarInterface calendar) throws RemoteException;

    void setChat(RemoteChatInterface chat) throws RemoteException;

    void addMessage(String message) throws RemoteException;

    void removeMemo(Date date) throws RemoteException;

    void addMemo(Memo memo) throws RemoteException;

    ArrayList<RemoteMemoInterface> getMemos() throws RemoteException;

    void removeMember(String email) throws RemoteException;

    RemoteMemoInterface getMemo(Date date) throws RemoteException;

    void removeMember(int index) throws RemoteException;

    void notifyObservers(String messageHeader, BusinessEntity entity) throws RemoteException;
}
