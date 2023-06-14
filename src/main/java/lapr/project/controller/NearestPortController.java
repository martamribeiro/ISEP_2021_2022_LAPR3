package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.domain.model.Port;
import lapr.project.domain.model.Ship;
import lapr.project.domain.store.PortStore;
import lapr.project.domain.store.ShipStore;

import java.util.Date;
import java.util.List;

public class NearestPortController {
    /**
     * The company associated to the Controller.
     */
    private final Company company;

    /**
     * empty constructor for the  Class
     */
    public NearestPortController() {
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor with company parameter
     */
    public NearestPortController(Company company) {
        this.company = company;
    }

    /**
     * Method to get the Ship associated with the callSign
     * @param callSign Ship callSign
     * @return Ship
     */
    public Ship getShipByCallSign(String callSign) {
        ShipStore shipStore = company.getShipStore();
        return shipStore.getShipByAnyCode(callSign);
    }

    /**
     * Method to get the coordinates of a Ship in a certain Time
     * @param ship Ship
     * @param dateTime time to get coordinates
     * @return list with Latitude and Longitude
     */
    public List<Double> getShipCoordinates(Ship ship, Date dateTime) {
        return ship.getPositionsBST().getPosInDateTime(dateTime);
    }

    /**
     * Method to get the closest Port to a certain Ship in a certain time
     * @param callSign Ship's callSign
     * @param dateTime time to be searched
     * @return closest Port
     */
    public Port findClosestPort(String callSign, Date dateTime) {
        Ship ship = getShipByCallSign(callSign);
        List<Double> coordinates = getShipCoordinates(ship, dateTime);
        PortStore portStore = this.company.getPortStore();
        return portStore.findClosestPort(coordinates);
    }
}
