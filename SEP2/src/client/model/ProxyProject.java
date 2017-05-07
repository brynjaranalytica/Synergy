package client.model;

        import client.controller.ClientController;
        import shared.Project;

/**
 * Created by lenovo on 4/17/2017.
 */
public class ProxyProject extends Project {
    private Project realProject;

    public ProxyProject(String name) {
        super(name);
    }

    @Override
    public String getName() {
        if(realProject == null)
            realProject = ClientController.getProjectFromServer(super.getName());
        return realProject.getName();
    }
}
