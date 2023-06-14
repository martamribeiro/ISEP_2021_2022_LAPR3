package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.dataControllers.CheckIfContainerExceedsWarehouseCapacityController;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;

public class CheckIfContainerExceedsWarehouseCapacityControllerTest {

    private Company comp;
    private CheckIfContainerExceedsWarehouseCapacityController ctrl;
    private DatabaseConnection databaseConnection;
    private CheckIfContainerExceedsWarehouseCapacityController containerExceedsWarehouseCapacityController;

    @BeforeEach
    public void SetUp(){
        comp = new Company("Company");
        this.ctrl=new CheckIfContainerExceedsWarehouseCapacityController(comp);
        databaseConnection = mock(DatabaseConnection.class);
        containerExceedsWarehouseCapacityController = mock(CheckIfContainerExceedsWarehouseCapacityController.class);
    }

    //alter session set nls_date_format = 'DD-MM-YYYY';

    //insert into trucktrip (trucktrip_id, route_id, truck_id, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values
    // (14112, 1402, 11111, 111, 14011, 19850, 19847, '28/04/2022', '29/04/2022', null, null);

    /*
    warehouse: 1401111
    trip1: 14111 ->> 0 para 2
     */

    @Disabled
    @Test
    void testcheckIfCargoManifestExceedsWarehouseCapacityValidValuesEnoughSpace() throws SQLException {
        //2+1=3 -> tem espaço
        ctrl.deleteTruckTrip(14112);
        int truckTripID = 14112;
        int routeID = 1402;
        int truckID = 11111;
        int depLocation = 111;
        int arriLocation = 14011;
        int loadCargID = 19850;
        int unloadCargID = 19847; //com espaço
        Date estDepDate = new Date(2022-1900,12-1,1);
        Date estArriDate = new Date(2022-1900,12-1,10);
        int result = ctrl.tryToCreateTruckTrip(truckTripID,routeID,truckID,depLocation,arriLocation,loadCargID,unloadCargID,estDepDate,estArriDate);
        int expResult = 1; //valor esperado: 1 -> tem espaço
        Assertions.assertEquals(expResult, result);
    }

    @Disabled
    @Test
    void testcheckIfCargoManifestExceedsWarehouseCapacityValidValuesNotEnoughSpace() throws SQLException {
        //2+2=4 -> nao tem espaço   max:3
        ctrl.deleteTruckTrip(14112);
        int truckTripID = 14112;
        int routeID = 1402;
        int truckID = 11111;
        int depLocation = 111;
        int arriLocation = 14011;
        int loadCargID = 19851;
        int unloadCargID =19852; //sem espaço
        Date estDepDate = new Date(2022-1900,12-1,1);
        Date estArriDate = new Date(2022-1900,12-1,10);
        int result = ctrl.tryToCreateTruckTrip(truckTripID,routeID,truckID,depLocation,arriLocation,loadCargID,unloadCargID,estDepDate,estArriDate);
        int expResult = 0; //valor esperado: 0 -> não tem espaço, logo ship trip nao é criada
        Assertions.assertEquals(expResult,result);
    }

    @Disabled
    @Test
    void testcheckIfCargoManifestExceedsWarehouseCapacityInvalidValueCargoManifestID() throws SQLException {
        ctrl.deleteTruckTrip(14112);
        int truckTripID = 14112;
        int routeID = 1402;
        int truckID = 11111;
        int depLocation = 111;
        int arriLocation = 14011;
        int loadCargID = 19850;
        int unloadCargID = 19849; //invalido
        Date estDepDate = new Date(2022-1900,12-1,1);
        Date estArriDate = new Date(2022-1900,12-1,10);
        int result = ctrl.tryToCreateTruckTrip(truckTripID,routeID,truckID,depLocation,arriLocation,loadCargID,unloadCargID,estDepDate,estArriDate);
        int expResult = 0; //valor esperado: 0 -> cargo manifest id invalido, logo ship trip nao é criada
        Assertions.assertEquals(expResult, result);
    }

    @Disabled
    @Test
    void testcheckIfCargoManifestExceedsWarehouseCapacityInvalidValueTruckID() throws SQLException {
        ctrl.deleteTruckTrip(14112);
        int truckTripID = 14112;
        int routeID = 1402;
        int truckID = 19849; //invalido
        int depLocation = 111;
        int arriLocation = 14011;
        int loadCargID = 19850;
        int unloadCargID = 19847; //com espaço
        Date estDepDate = new Date(2022-1900,12-1,1);
        Date estArriDate = new Date(2022-1900,12-1,10);
        int result = ctrl.tryToCreateTruckTrip(truckTripID,routeID,truckID,depLocation,arriLocation,loadCargID,unloadCargID,estDepDate,estArriDate);
        int expResult = 0; //valor esperado: 0 -> cargo manifest id invalido, logo ship trip nao é criada
        Assertions.assertEquals(expResult, result);
    }

}
