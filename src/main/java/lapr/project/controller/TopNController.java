package lapr.project.controller;


import lapr.project.domain.dataStructures.ShipTreeMmsi;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TopNController {
    /**
     * The company associated to the Controller.
     */
    private final Company company;

    /**
     * The binary search Tree with the ships imported from the file
     */
    private final ShipTreeMmsi shipBST;

    /**
     * empty constructor for the TopNController Class
     */
    public TopNController(){
        this.company= App.getInstance().getCompany();
        this.shipBST = company.getShipStore().getShipsBstMmsi();
    }

    /**
     * Constructor for TopNController Class, receiving "company" as attribute
     * @param company instance of company
     */
    public TopNController(Company company) {
        this.company = company;
        this.shipBST = company.getShipStore().getShipsBstMmsi();
    }

    /**
     * Method to get all the ships within the Base Date Time gap
     * @param initialDate initial Base Date Time
     * @param finalDate final Base Date Time
     * @return list with all the ships who belong in the time gap
     */
    public List<Ship> getShipsByDate(Date initialDate, Date finalDate){
        return this.shipBST.getShipsByDate(initialDate, finalDate);
    }

    /**
     * method to get the map with the ships associated by VesselType and sorted
     * @param sortedShips list with the sorted ships by most km travelled
     * @return map with the ships associated by VesselType and sorted
     */
    public Map<Integer,  Map<Ship, Set<Double>>> getShipWithMean(List<Ship> sortedShips, int number) {
        return this.shipBST.getShipWithMean(sortedShips, number);
    }
}
