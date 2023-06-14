package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.dataControllers.ShipOccupancyRatesController;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

/**
 * @author Marta Ribeiro (1201592)
 */
public class ShipOccupancyRatesControllerTest {

    private Company comp;
    private ShipOccupancyRatesController ctrl;
    private DatabaseConnection databaseConnection;
    private ShipOccupancyRatesController shipOccupancyRatesController;

    @BeforeEach
    public void SetUp(){
        comp = new Company("Company");
        this.ctrl=new ShipOccupancyRatesController(comp);
        databaseConnection = mock(DatabaseConnection.class);
        shipOccupancyRatesController = mock(ShipOccupancyRatesController.class);
    }

    /**
     * Test to ensure the occupancy rate is correctly calculated when the values are valid.
     */
    @Test
    void testOccupancyRatesCalculationWhenValid(){
        System.out.println("Test1: testOccupancyRatesCalculationWhenValid()");
        int maxCapacity1=79;
        int initialNumContainers1=14;
        int alreadyAddedRemovedContainersTripNum1=7;
        int expected=26;
        int actual=ctrl.calculateOccupancyRate(maxCapacity1,initialNumContainers1,alreadyAddedRemovedContainersTripNum1);
        System.out.println("exp: " + expected);
        System.out.println("real: " + actual);
        Assertions.assertEquals(expected,actual);
    }

    /**
     * Test to ensure the occupancy rate is -1 when the values are invalid.
     */
    @Test
    void testOccupancyRatesCalculationWhenInvalid(){
        System.out.println("Test2: testOccupancyRatesCalculationWhenInvalid()");
        int maxCapacity1=20;
        int initialNumContainers1=14;
        int alreadyAddedRemovedContainersTripNum1=7;
        int expected=-1;
        int actual=ctrl.calculateOccupancyRate(maxCapacity1,initialNumContainers1,alreadyAddedRemovedContainersTripNum1);
        System.out.println("exp: " + expected);
        System.out.println("real: " + actual);
        Assertions.assertEquals(expected,actual);
    }

    /*
    mmsi:636092933  max:30
    no ship trips
    mmsi:212351001  max:79
    19822 - loading - de 06/02/2021 a 8/02/2021 -  0+10=10
            unloading: 724 -  10-1=9
    39824 - loading - de 11/02/2021 a 20/02/2021 -  9+8=17
            unloading: 45807 -  17-3=14
    77329 - loading - de 24/02/2021 a 26/02/2021 -  14+7=21
            unloading: 87508 -  21-6=15
    */

    /*
    @Test
    void testOccupancyRatesCargoManifestValidValues(){
        //cmid: 77329
        System.out.println("Test3: testOccupancyRatesCargoManifestValidValues()");
        try {
            int cargoManifestID = 77329;
            int mmsi = ctrl.getMmsiByCargoManifest(cargoManifestID);
            int expResult = 26; //valor esperado
            int result = ctrl.getShipOccupancyRateByCargoManifestID(cargoManifestID);
            System.out.println("OCCUPANCY RATE");
            System.out.println("> For the Ship with MMSI [" + mmsi + "], the occupancy rate is " + result + "%, at the moment of the Cargo Manifest with ID [" + cargoManifestID + "].");
            Assert.assertEquals(expResult, result);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void testOccupancyRatesMomentValidValues(){
        //mmsi: 212351001 and date: 25/02/2021 -> cmid: 77329
        System.out.println("Test4: testOccupancyRatesMomentValidValues()");
        try {
            int mmsi=212351001;
            String date = "2021-02-25";
            int expResult = 26; //valor esperado
            int result = ctrl.getShipOccupancyRateByMmsiAndDate(mmsi, java.sql.Date.valueOf(date));
            System.out.println("OCCUPANCY RATE");
            System.out.println("> For the Ship with MMSI [" + mmsi + "], the occupancy rate is " + result + "%, at " + date + ".");
            Assert.assertEquals(expResult, result);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    void testOccupancyRatesCargoManifestNonexistentCargoManifest(){
        System.out.println("Test5: testOccupancyRatesCargoManifestInvalidValues()");
        try {
            int cargoManifestID = 77330;
            int mmsi = ctrl.getMmsiByCargoManifest(cargoManifestID);
            int result = ctrl.getShipOccupancyRateByCargoManifestID(cargoManifestID);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }

    @Test
    void testOccupancyRatesMomentNonexistentShip(){
        System.out.println("Test6: testOccupancyRatesMomentNonexistentShip()");
        try {
            int mmsi=636092934;
            String date = "2021-02-25";
            int result = ctrl.getShipOccupancyRateByMmsiAndDate(mmsi, java.sql.Date.valueOf(date));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }

    @Test
    void testOccupancyRatesMomentShipNoShipTripsAtTime(){
        System.out.println("Test7: testOccupancyRatesMomentShipNoShipTripsAtTime()");
        try {
            int mmsi=636092933;
            String date = "2021-02-25";
            int expResult = 0; //valor esperado
            int result = ctrl.getShipOccupancyRateByMmsiAndDate(mmsi, java.sql.Date.valueOf(date));
            System.out.println("OCCUPANCY RATE");
            System.out.println("> For the Ship with MMSI [" + mmsi + "], the occupancy rate is " + result + "%, at " + date + ".");
            Assert.assertEquals(expResult, result);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }*/

}
