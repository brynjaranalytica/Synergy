package shared.business_entities;

import java.util.ArrayList;

/**
 * Created by Nicolai Onov on 5/10/2017.
 */
public interface ProjectInterface {
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

}
