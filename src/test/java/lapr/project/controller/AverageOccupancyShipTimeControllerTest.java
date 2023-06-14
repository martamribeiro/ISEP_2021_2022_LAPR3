package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.dataControllers.AverageOccupancyShipTimeController;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;

public class AverageOccupancyShipTimeControllerTest {

    private Company comp;
    private AverageOccupancyShipTimeController ctrl;
    private DatabaseConnection databaseConnection;
    private AverageOccupancyShipTimeController averageOccupancyShipTimeController;

    @BeforeEach
    public void SetUp(){
        comp = new Company("Company");
        this.ctrl=new AverageOccupancyShipTimeController(comp);
        databaseConnection = mock(DatabaseConnection.class);
        averageOccupancyShipTimeController = mock(AverageOccupancyShipTimeController.class);
    }

    /*
    insert into ship (mmsi, vesseltypeid, imo, callsign, shipname, currentcapacity, draft, length, width)
    values (636092948, 79, 'IMO9225789', 'D5LK9', 'MSC ILANA', '30', 11.8, 299, 40);

    insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date)
    values (81901, 81999, 636092948, 10136, 16485, 19851, 19852, '06/02/2021', '08/02/2021', '26/02/2021', '30/04/2021');

    25/02/2021: +0 -0 = 0
    26/02/2021: +0 -0 = 0
    27/02/2021: +2 -0 = 2 -> foi adicionado no dia 26, mas no inicio do dia 26
                            ainda não tinha sido adicionado, só no inicio do dia 27

    CONSIDERO O INICIO DO DIA, por isso a minha função vai até ao dia seguinte ao dia final

     */

    @Disabled
    @Test
    void testAverageIsCorrect() throws SQLException {
        int shipID=636092948;
        int day1=25;
        int month1=2;
        int year1=2021;
        int day2=27;
        int month2=2;
        int year2=2021;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=((2.0*100.0/30.0)/3.0);
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhenInvalidShipID() throws SQLException {
        int shipID=636092946;
        int day1=5;
        int month1=2;
        int year1=2021;
        int day2=9;
        int month2=2;
        int year2=2021;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhenMonthMinor1() throws SQLException {
        int shipID=636092948;
        int day1=23;
        int month1=0;
        int year1=2020;
        int day2=23;
        int month2=7;
        int year2=2020;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhenMonthBigger12() throws SQLException {
        int shipID=636092948;
        int day1=5;
        int month1=13;
        int year1=2020;
        int day2=1;
        int month2=2;
        int year2=2021;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhen31dMonthInvalidDays() throws SQLException {
        int shipID=636092948;
        int day1=32;
        int month1=1;
        int year1=2020;
        int day2=5;
        int month2=5;
        int year2=2020;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhen30dMonthInvalidDays() throws SQLException {
        int shipID=636092948;
        int day1=31;
        int month1=4;
        int year1=2020;
        int day2=6;
        int month2=5;
        int year2=2020;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhen29dMonthInvalidDays() throws SQLException {
        int shipID=636092948;
        int day1=30;
        int month1=2;
        int year1=2020;
        int day2=5;
        int month2=5;
        int year2=2020;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhen28dMonthInvalidDays() throws SQLException {
        int shipID=636092948;
        int day1=29;
        int month1=2;
        int year1=2021;
        int day2=5;
        int month2=3;
        int year2=2021;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhenPeriodIsNotCompleted() throws SQLException {
        int shipID=636092948;
        int day1=4;
        int month1=4;
        int year1=2020;
        int day2=4;
        int month2=4;
        int year2=2025;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoAverageWhenBeginningDateComesAfterEndingDate() throws SQLException {
        int shipID=636092948;
        int day1=4;
        int month1=4;
        int year1=2021;
        int day2=4;
        int month2=4;
        int year2=2020;
        double actual=ctrl.getAverageOccupancyRateByShipIDandDates(shipID,month1,year1,day1,month2,year2,day2);
        double expected=-1.0;
        Assertions.assertEquals(expected,actual);
    }

}