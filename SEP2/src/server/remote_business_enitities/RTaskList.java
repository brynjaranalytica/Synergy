package server.remote_business_enitities;

import shared.business_entities.TaskList;
import shared.remote_business_interfaces.RemoteTaskListInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by lenovo on 4/12/2017.
 */
public class RTaskList implements RemoteTaskListInterface {
    private ArrayList<String> tasks;

    public RTaskList(TaskList taskList) throws RemoteException{
        this.tasks = new ArrayList<>();
        ArrayList<String> tasks = taskList.getTasks();
        for(String task: tasks){
            this.tasks.add(task);
        }
        UnicastRemoteObject.exportObject(this,0);
    }

    public RTaskList() throws RemoteException{
        this.tasks = new ArrayList<>();
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public String getTask(int index) throws RemoteException {
        return this.tasks.get(index);
    }

    @Override
    public void addTask(String task) throws RemoteException {
        this.tasks.add(task);
    }

    @Override
    public ArrayList<String> getTasks() throws RemoteException {
        return this.tasks;
    }
}
