package lapr.project.controller;

import lapr.project.domain.dataStructures.PositionsBST;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;
import lapr.project.domain.store.ShipStore;
import lapr.project.dto.MovementsSummaryDto;

import java.util.Date;

public class MovementSummaryController {
    /**
     * The company associated to the Controller.
     */
    private final Company company;

    /**
     * empty constructor for the TopNController Class
     */
    public MovementSummaryController(){
        this.company= App.getInstance().getCompany();
    }

    /**
     * Constructor for TopNController Class, receiving "company" as attribute
     * @param company instance of company
     */
    public MovementSummaryController(Company company) {
        this.company = company;
    }

    public MovementsSummaryDto getShipMovementsSummary(String code){
        ShipStore shipStore = this.company.getShipStore();
        Ship currentShip = shipStore.getShipByAnyCode(code);
        PositionsBST shipMovements = currentShip.getPositionsBST();
        String name = currentShip.getVesselName();
        int mmsi = currentShip.getMmsi();
        Date startDate = shipMovements.getStartDate();
        Date endDate = shipMovements.getEndDate();
        double maxSog = shipMovements.getMaxSog();
        double meanSog = shipMovements.getMeanSog();
        double meanCog = shipMovements.getMeanCog();
        double departLat = shipMovements.getDepartLatitude();
        double departLon = shipMovements.getDepartLongitude();
        double arrivalLat = shipMovements.getArrivalLatitude();
        double arrivalLon = shipMovements.getArrivalLongitude();
        double travDist = shipMovements.getTotalDistance();
        double deltaDist = shipMovements.getDeltaDistance();

        return new MovementsSummaryDto(name, startDate, endDate, maxSog, meanSog,
                meanCog, departLat, departLon, arrivalLat, arrivalLon, travDist, deltaDist, mmsi);
    }
}
