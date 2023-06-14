package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.WarehouseStoreDB;
import lapr.project.domain.model.Company;
import java.sql.SQLException;

/**
 * Controller class to coordinate the calculation of ship's occupancy rate
 *
 * @author Marta Ribeiro (1201592)
 */
public class CheckOccupancyRatesAndEstimationsWarehouseController {

    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public CheckOccupancyRatesAndEstimationsWarehouseController(){
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param companyy instance of company to be used.
     */
    public CheckOccupancyRatesAndEstimationsWarehouseController(Company companyy){
        this.company=companyy;
    }

    /**
     * Calculate occupancy rate with maxCapacity and currentCapacity.
     * @param maxCapacity warehouse max capacity.
     * @param currentCapacity current number of containers in the warehouse.
     * @return warehouse occupancy rate in percentage.
     */
    public int calculateOccupancyRate(int maxCapacity, int currentCapacity){
        if (currentCapacity>maxCapacity){
            return -1; //when invalid
        } else {
            return (currentCapacity*100/maxCapacity);
        }
    }

    /**
     * Get warehouse occupancy rate by warehouse id.
     * @param warehouseID warehouse id.
     * @return warehouse occupancy rate in percentage.
     */
    public int getOccupancyRateByWarehouseID(int warehouseID) throws SQLException {
        if (checkIfWarehouseExists(warehouseID)==0){
            return -1; //inv
        }
        WarehouseStoreDB warehouseStoreDB = this.company.getWarehouseStoreDB();
        int maxCapacity = warehouseStoreDB.getWarehouseMaxCapacity(warehouseID); //get with sql
        int currentCapacity = warehouseStoreDB.getCurrentContainersWarehouse(warehouseID); //get with sql
        return calculateOccupancyRate(maxCapacity,currentCapacity);
    }

    /**
     * Check if a warehouse exists in the data base.
     * @param warehouseID Warehouse's id.
     * @return 1 if the warehouse exists and 0 if it doesn't.
     */
    public int checkIfWarehouseExists(int warehouseID) {
        WarehouseStoreDB warehouseStoreDB = this.company.getWarehouseStoreDB();
        return warehouseStoreDB.checkIfWarehouseExists(warehouseID);
    }

    /**
     * Get an estimate of the containers leaving the warehouse in the next 30 days.
     * @param warehouseID warehouse id.
     * @return an estimate of the containers leaving the warehouse in the next 30 days.
     */
    public int getContainersOut30Days(int warehouseID) throws SQLException {
        if (checkIfWarehouseExists(warehouseID)==0){
            return -1; //inv
        }
        WarehouseStoreDB warehouseStoreDB = this.company.getWarehouseStoreDB();
        int numContainers = warehouseStoreDB.getNumContainersOutWarehouse(warehouseID);
        return numContainers;
    }

}