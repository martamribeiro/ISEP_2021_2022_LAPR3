package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.dataControllers.CheckOccupancyRatesAndEstimationsWarehouseController;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;

public class CheckOccupancyRatesAndEstimationsWarehouseControllerTest {

    private Company comp;
    private CheckOccupancyRatesAndEstimationsWarehouseController ctrl;
    private DatabaseConnection databaseConnection;
    private CheckOccupancyRatesAndEstimationsWarehouseController checkOccupancyRatesAndEstimationsWarehouseController;

    @BeforeEach
    public void SetUp(){
        comp = new Company("Company");
        this.ctrl=new CheckOccupancyRatesAndEstimationsWarehouseController(comp);
        databaseConnection = mock(DatabaseConnection.class);
        checkOccupancyRatesAndEstimationsWarehouseController = mock(CheckOccupancyRatesAndEstimationsWarehouseController.class);
    }

    /*
    warehouse: 11111
        location: 111   |   max: 10
    warehouse: 22222
        location: 222   |   max: 20
    warehouse: 33333
        location: 333   |   max: 30
    warehouse: 44444
        location: 444   |   max: 40
    warehouse: 55555
        location: 555   |   max: 50

    truckTrip: 11111
        chegam a: 111   |   data: 9/12/2021     |   qtd:2   |   ficam: 2
    truckTrip: 11112
        saem de: 111    |   data: 18/01/2022    |   qtd:2   |   ficam: 0
     */

    @Disabled
    @Test
    void testOccupancyRatesWarehouseValidValues1(){
        //whid: 11111
        try {
            int warehouseID = 11111;
            int expResult = 10; //valor esperado
            int result = ctrl.getOccupancyRateByWarehouseID(warehouseID);
            Assertions.assertEquals(expResult, result);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Disabled
    @Test
    void testOccupancyRatesWarehouseValidValues2(){
        //whid: 22222
        try {
            int warehouseID = 22222;
            int expResult = 0; //valor esperado
            int result = ctrl.getOccupancyRateByWarehouseID(warehouseID);
            Assertions.assertEquals(expResult, result);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Disabled
    @Test
    void testOccupancyRatesNonExistentWarehouse(){
        //whid: 99999
        try {
            int warehouseID = 99999;
            int expResult = -1; //valor esperado
            int result = ctrl.getOccupancyRateByWarehouseID(warehouseID);
            Assertions.assertEquals(expResult, result);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Disabled
    @Test
    void testEstimationValidValues1(){
        //whid: 11111
        //truck trips em que sai: 11113 (CM: 19823 -> 2), 11112 (CM: 19825 -> 2), 14111 (CM: 19848 -> 2)
        try{
        int warehouseID = 11111;
        int expResult = 2; //valor esperado
        int result = ctrl.getContainersOut30Days(warehouseID);
        //vou ter de ir buscar com um cursor os loadings da warehouse para
            // um truck e somar o nr de contentores nos loadings
        Assertions.assertEquals(expResult, result);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Disabled
    @Test
    void testEstimationNonExistentWarehouse() {
        //whid: 99999
        try {
            int warehouseID = 99999;
            int expResult = -1; //valor esperado
            int result = ctrl.getContainersOut30Days(warehouseID);
            Assertions.assertEquals(expResult, result);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
