package lapr.project.controller;

import lapr.project.domain.dataStructures.Ports2DTree;
import lapr.project.domain.store.PortStore;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Port;
import lapr.project.dto.PortFileDTO;

/**
 * Controller class to coordinate the creation of ports
 *
 * @author Ana Albergaria <1201518@isep.ipp.pt>
 */
public class ImportPortsController {
    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Ship to be created by the controller.
     */
    private Port port;
    /**
     * The 2D Tree containing all the imported Ports.
     */
    private Ports2DTree ports2DTree;

    /**
     * Constructor for the controller.
     */
    public ImportPortsController() {
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public ImportPortsController(Company company) {
        this.company=company;
        this.port = null;
        this.ports2DTree = new Ports2DTree();
    }

    /**
     * Method for creating a Port instance with all attributes.
     * @param portFileDTO portfile dto which contains all needed data
     * @return true if successfully created
     * and false if unsuccessfully created.
     */
    public boolean importPortFromFile(PortFileDTO portFileDTO) {
        PortStore portStore = this.company.getPortStore();
        try {
            this.port = portStore.createPort(portFileDTO);
        } catch (IllegalArgumentException e){
            System.out.println("NOT ADDED : " + e);
            return false;
        }
        return portStore.savePort(this.port);
    }

    /**
     * Method for balancing the Ports 2D Tree
     * after all the valid Port objects have been imported.
     *
     */
    public void balancePorts2DTree() {
        PortStore portStore = this.company.getPortStore();
        portStore.balancePorts2DTree();
    }

}
