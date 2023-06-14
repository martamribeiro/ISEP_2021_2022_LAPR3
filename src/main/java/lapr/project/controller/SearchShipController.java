package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.store.ShipStore;

public class SearchShipController {

    /**
     * The company associated to the Controller.
     */
    private final Company company;

    /**
     * empty constructor for the  Class
     */
    public SearchShipController(){
        this.company= App.getInstance().getCompany();
    }
    /**
     * Constructor with company parameter
     */
    public SearchShipController(Company comp){
        this.company= comp;
    }


    public String getShipInfoByAnyCode(String code){
        ShipStore shipStore = company.getShipStore();
        return shipDetailsToString(shipStore.getShipByAnyCode(code));
    }

    public String shipDetailsToString(Ship ship){
        String details = String.format("Ship details:\n MMSI:%d\nCallSign:%s\nIMO:%s\nVesselType:%d\nVesselName:%s\nLenght:%d\nWidth:%d\nDraft:%f\n Cargo:%s\n",
                ship.getMmsi(), ship.getCallSign(), ship.getImo(), ship.getVesselTypeID(), ship.getVesselName(), ship.getLength(), ship.getWidth(), ship.getDraft(), ship.getCargo());
        details = details.concat("Ship Positions:\n");
        for(ShipPosition p : ship.getPositionsBST().inOrder()){
            details = details.concat(String.format("%s\n", p.toString()));
        }
        return details;
    }

}
