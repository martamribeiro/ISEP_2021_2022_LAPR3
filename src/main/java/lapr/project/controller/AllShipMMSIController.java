package lapr.project.controller;

import lapr.project.domain.dataStructures.ShipTreeMmsi;
import lapr.project.domain.model.Company;

import java.util.Map;
import java.util.Set;

public class AllShipMMSIController {
    /**
     * The company associated to the Controller.
     */
    private final Company company;

    /**
     * The binary search Tree with the ships imported from the file
     */
    private final ShipTreeMmsi shipBST;

    /**
     * empty constructor for the AllShipMMSIController Class
     */
    public AllShipMMSIController() {
        this.company= App.getInstance().getCompany();
        this.shipBST = company.getShipStore().getShipsBstMmsi();
    }

    /**
     * Constructor for AllShipMMSIController Class, receiving "company" as attribute
     * @param company instance of company
     */
    public AllShipMMSIController(Company company) {
        this.company = company;
        this.shipBST = company.getShipStore().getShipsBstMmsi();
    }

    /**
     * method to get all the Ships in the BST
     * @return all the ships by MMSI sorted by Travelled Distance
     */
    public Map<Integer, Set<Double>> sortedByTravelledDistance() {
        return shipBST.sortedByTravelledDistance();
    }

    /**
     * method to get all the Ships in the BST
     * @return all the ships by MMSI sorted by Travelled Distance
     */
    public Map<Integer, Set<Double>> sortedByTotalMovements() {
        return shipBST.sortedByTotalMovements();
    }

}
