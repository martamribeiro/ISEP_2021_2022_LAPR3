package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.dataControllers.CheckIfContainerExceedsShipCapacityController;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class CheckIfContainerExceedsShipCapacityControllerTest {

    private Company comp;
    private CheckIfContainerExceedsShipCapacityController ctrl;
    private DatabaseConnection databaseConnection;
    private CheckIfContainerExceedsShipCapacityController shipOccupancyRatesController;

    @BeforeEach
    public void SetUp(){
        comp = new Company("Company");
        this.ctrl=new CheckIfContainerExceedsShipCapacityController(comp);
        databaseConnection = mock(DatabaseConnection.class);
        shipOccupancyRatesController = mock(CheckIfContainerExceedsShipCapacityController.class);
    }

    //mmsi:212351004  max:30
    //atual:0 (no passado fez +2 e -2)
    //0+34=34 -> excede     82846
    //0+5=5 -> tem espaço   82847
    //cmid invalido: 77330
    //mmsi invalido: 636092934

    //int shipTripID, int mmsi, int depLocation, int arriLocation, int loadCargID, java.sql.Date estDepDate, java.sql.Date estArriDate

    /*alter session set nls_date_format = 'DD-MM-YYYY';
    insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81348, 212351004, 224858, 16485, 82846, NULL, '10-12-2021', '12-12-2022', NULL, NULL);
    insert into shiptrip (shiptrip_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (81348, 212351004, 224858, 16485, 82847, NULL, '10-12-2021', '12-12-2022', NULL, NULL);
    delete from shipTrip where shiptrip_id = 81348;*/

    @Disabled
    @Test
    void testcheckIfCargoManifestExceedsShipCapacityValidValuesEnoughSpace() throws SQLException {
        //0+5=5 -> tem espaço   82847
        ctrl.deleteShipTrip(81348);
        int shipTripID = 81348;
        int routeID = 1020;
        int mmsi = 212351004;
        int depLocation = 224858;
        int arriLocation = 16485;
        int loadCargID = 82847;
        Date estDepDate = new Date(Calendar.getInstance().getTime().getTime());
        Date estArriDate = new Date(2022,12,1);
        int result = ctrl.tryToCreateShipTrip(shipTripID,routeID,mmsi,depLocation,arriLocation,loadCargID,estDepDate,estArriDate);
        int expResult = 1; //valor esperado: 1 -> tem espaço
        Assertions.assertEquals(expResult, result);
    }

    @Disabled
    @Test
    void testcheckIfCargoManifestExceedsShipCapacityValidValuesNotEnoughSpace() throws SQLException {
        //0+34=34 -> excede     82846
        ctrl.deleteShipTrip(81348);
        int shipTripID = 81348;
        int routeID = 1020;
        int mmsi = 212351004;
        int depLocation = 16485;
        int arriLocation = 224858;
        int loadCargID = 82846;
        Date estDepDate = new Date(Calendar.getInstance().getTime().getTime());
        Date estArriDate = new Date(2022-1900,12-1,1);
        int result = ctrl.tryToCreateShipTrip(shipTripID,routeID,mmsi,depLocation,arriLocation,loadCargID,estDepDate,estArriDate);
        int expResult = 0; //valor esperado: 0 -> não tem espaço, logo ship trip nao é criada
        Assertions.assertEquals(expResult, result);
    }

    @Disabled
    @Test
    void testcheckIfCargoManifestExceedsShipCapacityInvalidValueCargoManifestID() throws SQLException {
        ctrl.deleteShipTrip(81348);
        int shipTripID = 81348;
        int routeID = 1020;
        int mmsi = 212351004;
        int depLocation = 224858;
        int arriLocation = 16485;
        int loadCargID = 77330;
        Date estDepDate = new Date(Calendar.getInstance().getTime().getTime());
        Date estArriDate = new Date(2022,12,1);
        int result = ctrl.tryToCreateShipTrip(shipTripID,routeID,mmsi,depLocation,arriLocation,loadCargID,estDepDate,estArriDate);
        int expResult = 0; //valor esperado: 0 -> cargo manifest id invalido, logo ship trip nao é criada
        Assertions.assertEquals(expResult, result);
    }

    @Disabled
    @Test
    void testcheckIfCargoManifestExceedsShipCapacityInvalidValueMMSI() throws SQLException {
        ctrl.deleteShipTrip(81348);
        int shipTripID = 81348;
        int routeID = 1020;
        int mmsi = 636092934;
        int depLocation = 224858;
        int arriLocation = 16485;
        int loadCargID = 82847;
        Date estDepDate = new Date(Calendar.getInstance().getTime().getTime());
        Date estArriDate = new Date(2022,12,1);
        int result = ctrl.tryToCreateShipTrip(shipTripID,routeID,mmsi,depLocation,arriLocation,loadCargID,estDepDate,estArriDate);
        int expResult = 0; //valor esperado: 0 -> cargo manifest id invalido, logo ship trip nao é criada
        Assertions.assertEquals(expResult, result);
    }

    @Disabled
    @Test
    void addShipTripInUSedDateTest() throws SQLException {
        System.out.println("Test5: addShipTripInUSedDateTest()");
        ctrl.deleteShipTrip(101001);
        int shipTripID = 101001;
        int routeID = 1020;
        int mmsi = 228339600;
        int depLocation = 13390;
        int arriLocation = 20512;
        int loadCargID = 34491;
        Date estDepDate = new Date(2021, 2, 18);
        Date estArriDate = new Date(2021,3,21);
        int result = ctrl.tryToCreateShipTrip(shipTripID,routeID,mmsi,depLocation,arriLocation,loadCargID,estDepDate,estArriDate);
        int expResult = 1;
        Assertions.assertEquals(expResult, result, "First it should run fine");
        System.out.println("passed  here");
        int shipTripIdNew = 101011;
        int expResult2 = 0;
        Exception thrown = assertThrows(Exception.class, () -> ctrl.tryToCreateShipTrip(shipTripIdNew,routeID,mmsi,depLocation,arriLocation,loadCargID,estDepDate,estArriDate));
        assertEquals("ORA-20010 - Currently, the ship is not available in this date.", thrown.getMessage());
    }

}
