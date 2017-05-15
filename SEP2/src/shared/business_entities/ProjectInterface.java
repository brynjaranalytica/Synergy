package shared.business_entities;

import shared.remote_business_interfaces.RemoteMemoInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nicolai Onov on 5/10/2017.
 */
public interface ProjectInterface extends BusinessEntity {
    String getName();

    ArrayList<Member> getMembers();

    void setName(String name);

    void setMembers(ArrayList<Member> members);

    TaskList getTaskList();

    void setTaskList(TaskList taskList);

    void addTask(String task);

    void addMember(Member member);

    String toString();

    Member getMember(int index);

    Chat getChat();

    Calendar getCalendar();

    void setChat(Chat chat);

    void setCalendar(Calendar calendar);

    void addMessage(String message);

    void addMemo(Memo memo);

    void removeMemo(Date date);

    ArrayList<Memo> getMemos();

    Memo getMemo(Date date);

}
