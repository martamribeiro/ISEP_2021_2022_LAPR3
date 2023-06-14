package lapr.project.controller;

import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MostEfficientCircuitController {
    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public MostEfficientCircuitController(){this(App.getInstance().getCompany());}

    /**
     * Constructor receiving the company as an argument.
     * @param company instance of company to be used.
     */
    public MostEfficientCircuitController(Company company) {
        this.company = company;
    }

    /**
     * Method to get the biggest circuit possible starting from all vertices and the smaller distance
     * @return Shorter distance that goes through more Locations
     */
    public Map<List<Location>, Double> getMostEfficientCircuit(){
        FreightNetwork freightNetwork = company.getFreightNetwork();

        return freightNetwork.getTotalDistanceMinorCost();
    }
}
