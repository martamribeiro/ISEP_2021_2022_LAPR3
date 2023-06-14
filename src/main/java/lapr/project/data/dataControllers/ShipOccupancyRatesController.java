package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.CargoManifestStoreDB;
import lapr.project.data.ShipStoreDB;
import lapr.project.data.ShipTripStoreDB;
import lapr.project.domain.model.Company;

import java.sql.Date;
import java.sql.SQLException;

/**
 * Controller class to coordinate the calculation of ship's occupancy rate
 *
 * @author Marta Ribeiro (1201592)
 */
public class ShipOccupancyRatesController {

    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public ShipOccupancyRatesController(){
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param companyy instance of company to be used.
     */
    public ShipOccupancyRatesController(Company companyy){
        this.company=companyy;
    }

    /**
     * Calculate occupancy rate with maxCapacity, initialNumContainers, addedContainerNum and removedContainersNum.
     * @param maxCapacity ship cargo.
     * @param initialNumContainers ship trip initial num containers.
     * @param alreadyAddedRemovedContainersTripNum containers added and removed in loading and unloading cargo manifest.
     * @return ship occupancy rate in percentage.
     */
    public int calculateOccupancyRate(int maxCapacity, int initialNumContainers, int alreadyAddedRemovedContainersTripNum){
        int current = initialNumContainers+alreadyAddedRemovedContainersTripNum;
        if (current>maxCapacity){
            return -1; //when invalid
        } else {
            return (current*100/maxCapacity);
        }
    }

    /**
     * Get ship occupancy rate by cargo manifest id.
     * @param cargoManifestID cargo manifest id.
     * @return ship occupancy rate in percentage.
     */
    public int getShipOccupancyRateByCargoManifestID(int cargoManifestID) throws SQLException {
        int numCM = checkIfCargoManifestExists(cargoManifestID);
        if (numCM==0){
            throw new IllegalArgumentException("There is no cargo manifest in the data base with the given ID.");
        }
        //o cargo nao pode ser NA
        int maxCapacity=0, initialNumContainers=0, alreadyAddedRemovedContainersTripNum=0;
        ShipStoreDB shipStoreDB = this.company.getShipStoreDB();
        maxCapacity=shipStoreDB.getShipCargo(cargoManifestID);
        ShipTripStoreDB shipTripStoreDB = this.company.getShipTripStoreDB();
        java.sql.Date estDepDate = shipTripStoreDB.getEstDepartureDateFromShipTrip(cargoManifestID);
        int mmsi = getMmsiByCargoManifest(cargoManifestID);
        initialNumContainers=shipTripStoreDB.getInitialNumContainersPerShipTrip(cargoManifestID,estDepDate,mmsi);
        alreadyAddedRemovedContainersTripNum=shipTripStoreDB.getAddedRemovedContainersShipTripMoment(cargoManifestID);
        int result= calculateOccupancyRate(maxCapacity, initialNumContainers, alreadyAddedRemovedContainersTripNum);
        return result;
    }

    /**
     * Get cargo manifest id by mmsi and date.
     * @param mmsi ship mmsi.
     * @param date date to analyse.
     * @return cargo manifest id.
     */
    public int getCargoManifestIDByMmsiAndDate(int mmsi, Date date){
        CargoManifestStoreDB cargoManifestStoreDB = this.company.getCargoManifestStoreDB();
        int cargoManifestID = cargoManifestStoreDB.getCargoManifestByMmsiAndDate(mmsi,date);
        return cargoManifestID;
    }

    /**
     * Check if a ship exists in the data base.
     * @param mmsi Ship's mmsi.
     * @return 1 if the ship exists and 0 if it doesn't.
     */
    public int checkIfShipExists(int mmsi){
        ShipStoreDB shipStoreDB = this.company.getShipStoreDB();
        int num = shipStoreDB.checkIfShipExists(mmsi);
        return num;
    }

    /**
     * Check if a cargo manifest exists in the data base.
     * @param cargoManifestID Cargo manifest ID.
     * @return 1 if the cargo manifest exists and 0 if it doesn't.
     */
    public int checkIfCargoManifestExists(int cargoManifestID){
        CargoManifestStoreDB cargoManifestStoreDB = this.company.getCargoManifestStoreDB();
        int num = cargoManifestStoreDB.checkIfCargoManifestExists(cargoManifestID);
        return num;
    }

    /**
     * Get ship occupancy rate by mmsi and date.
     * @param mmsi ship mmsi.
     * @param date date to analyse.
     * @return ship occupancy rate in percentage.
     */
    public int getShipOccupancyRateByMmsiAndDate(int mmsi, java.sql.Date date) throws SQLException, IllegalArgumentException{
        int numShip = checkIfShipExists(mmsi);
        if (numShip==0){
            throw new IllegalArgumentException("There is no ship in the data base with the given MMSI.");
        }
        int cargoManifestID = getCargoManifestIDByMmsiAndDate(mmsi,date);
        if (cargoManifestID==-1){ //ship had no ship trips for the moment
            return 0;
        }
        int result= getShipOccupancyRateByCargoManifestID(cargoManifestID);
        return result;
    }

    /**
     * Get MMSI by cargo manifest ID.
     * @param cargoManifestID cargo manifest ID.
     * @return ship MMSI.
     */
    public int getMmsiByCargoManifest(int cargoManifestID) {
        int numCM = checkIfCargoManifestExists(cargoManifestID);
        if (numCM==0){
            throw new IllegalArgumentException("There is no cargo manifest in the data base with the given ID.");
        }
        ShipTripStoreDB shipTripStoreDB = this.company.getShipTripStoreDB();
        int result= shipTripStoreDB.getMmsiByCargoManifestID(cargoManifestID);
        return result;
    }
}