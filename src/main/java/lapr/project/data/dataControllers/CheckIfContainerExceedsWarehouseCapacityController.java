package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.TruckTripStoreDB;
import lapr.project.domain.model.Company;

import java.sql.Date;
import java.sql.SQLException;

public class CheckIfContainerExceedsWarehouseCapacityController {

    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public CheckIfContainerExceedsWarehouseCapacityController(){
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param companyy instance of company to be used.
     */
    public CheckIfContainerExceedsWarehouseCapacityController(Company companyy){
        this.company=companyy;
    }

    /**
     * Try to create truck trip to see if the trigger is fired.
     * @param truckTripID truck trip id.
     * @param routeID route id.
     * @param truckID truck id.
     * @param depLocation departure location.
     * @param arriLocation arrival location.
     * @param loadCargID loading cargo manifest id.
     * @param unloadCargID unloading cargo manifest id.
     * @param estDepDate estimated departure date.
     * @param estArriDate estimates arrival date.
     * @return -1 if the input information is wrong, otherwise it returns 1.
     */
    public int tryToCreateTruckTrip(int truckTripID,int routeID, int truckID, int depLocation, int arriLocation, int loadCargID, int unloadCargID, Date estDepDate, Date estArriDate) throws SQLException {
        TruckTripStoreDB truckTripStoreDB = this.company.getTruckTripStoreDB();
        truckTripStoreDB.triggerContainersWarehouse();
        int resultCreate = truckTripStoreDB.createTruckTripWithUnloading(truckTripID, routeID, truckID, depLocation, arriLocation, loadCargID, unloadCargID, estDepDate, estArriDate);
        return truckTripStoreDB.checkIfTruckTripExists(truckTripID);
    }

    /**
     * Delete truck trip.
     * @param truckTripID truck trip id.
     * @return -1 if the input information is wrong, otherwise it returns 1.
     */
    public int deleteTruckTrip(int truckTripID){
        TruckTripStoreDB truckTripStoreDB = this.company.getTruckTripStoreDB();
        return truckTripStoreDB.deleteTruckTrip(truckTripID);
    }

}
