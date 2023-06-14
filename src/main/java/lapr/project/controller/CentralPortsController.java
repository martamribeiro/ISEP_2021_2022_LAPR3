package lapr.project.controller;

import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Location;

import java.util.List;
import java.util.Map;

public class CentralPortsController {

    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public CentralPortsController() {
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public CentralPortsController(Company company) {
        this.company=company;
    }

    public List<Map.Entry<Location, Integer>> getMostCentralPorts(){
        FreightNetwork freightNetwork = this.company.getFreightNetwork();
        return freightNetwork.getMostCentralPorts();
    }

}
