package shared.business_entities;


import shared.remote_business_interfaces.RemoteTaskListInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/12/2017.
 */
public class TaskList implements BusinessEntity{
    private ArrayList<String> tasks;

    public TaskList(RemoteTaskListInterface remoteTaskList) throws RemoteException{
        this.tasks = new ArrayList<>();
        ArrayList<String> remoteTasks = remoteTaskList.getTasks();
        for(String task : remoteTasks){
            this.tasks.add(task);
        }
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(String task){
        tasks.add(task);
    }

    public String getTask(int index){
        return tasks.get(index);
    }

    public ArrayList<String> getTasks(){
        return this.tasks;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String message : tasks)
            builder.append(message).append("\n");
        return builder.toString();
    }
}
