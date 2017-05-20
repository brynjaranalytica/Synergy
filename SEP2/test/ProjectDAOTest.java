import server.model.ProjectDAO;
import server.remote_business_enitities.RProjects;
import shared.remote_business_interfaces.RemoteProjectInterface;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Nicolai Onov on 5/20/2017.
 */
public class ProjectDAOTest {
    public static void main(String[] args) throws RemoteException, SQLException {
        RProjects remoteProjects = new RProjects();
        ArrayList<RemoteProjectInterface> listOfRemoteProjects = ProjectDAO.getInstance().readAllProjects();
        remoteProjects.setProjects(listOfRemoteProjects);
    }
}
