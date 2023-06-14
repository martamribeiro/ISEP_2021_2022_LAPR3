package lapr.project.controller;

import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.model.Capital;
import lapr.project.domain.model.Company;

import java.util.Map;

/**
 * Controller class to coordinate the creation of ports
 *
 * @author Ana Albergaria <1201518@isep.ipp.pt>
 */
public class ColorMapController {
    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public ColorMapController() {
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public ColorMapController(Company company) {
        this.company=company;
    }

    /**
     * Method to which calls the method color the Map
     * @return a map containing the capitals and its colors
     */
    public Map<Capital, Integer> colorMap() {
        FreightNetwork freightNetwork = this.company.getFreightNetwork();
        Map<Capital, Integer> coloredCapitals = freightNetwork.colorMap();
        return coloredCapitals;
    }
}
