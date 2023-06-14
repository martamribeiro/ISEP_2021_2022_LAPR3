package lapr.project.controller;

import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Location;

import java.util.List;
import java.util.Map;

public class ClosenessPlacesController {
    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public ClosenessPlacesController() {
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public ClosenessPlacesController(Company company) {
        this.company=company;
    }

    public Map<String, List<Map.Entry<Location, Double>>> getClosenessPlacesByContinent(){
        FreightNetwork freightNetwork = this.company.getFreightNetwork();
        return freightNetwork.closenessPlacesByContinent();
    }
}
