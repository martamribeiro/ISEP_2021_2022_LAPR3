package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.dataControllers.CreateMapContainersPortController;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CreateMapContainersPortControllerTest {

    private Company comp;
    private CreateMapContainersPortController ctrl;
    private DatabaseConnection databaseConnection;
    private CreateMapContainersPortController createMapContainersPortController;

    @BeforeEach
    public void SetUp(){
        comp = new Company("Company");
        this.ctrl=new CreateMapContainersPortController(comp);
        databaseConnection = mock(DatabaseConnection.class);
        createMapContainersPortController = mock(CreateMapContainersPortController.class);
    }

    /*
    (é considerada a quantidade existente no inicio do dia)
    maxCapacity: 50
    portID: 16485
    locationID: 1741
    --- OLD ONES: ---
    before 30/4/2021:
    --- NEW ONES: ---
    30/4/2021: +2
        1-current: 2
        occupancy rate: 4
    1/5/2021: +0
        2-current: 2
        occupancy rate: 4
    2/5/2021: +0
        3-current: 2
        occupancy rate: 4
    3/5/2021: +2
        4-current: 4
        occupancy rate: 8
    4/5/2021: +2
        5-current: 6
        occupancy rate: 12
    5/5/2021: +2
    5/5/2021: -2
        6-current: 6
        occupancy rate: 12
    6/5/2021: +0
        7-current: 6
        occupancy rate: 12
    7/5/2021: +0
        8-current: 6
        occupancy rate: 12
    --- OLD ONES: ---   (were already in the bootstrap)
    8/5/2021: +1
    8/5/2021: +2
        9-current: 9
        occupancy rate: 18
    --- NEW ONES: ---
    9/5/2021: +2
        10-current: 11
        occupancy rate: 22
    10/5/2021 a 30/5/2021: +0
        11 a 31-current: 11
        occupancy rate: 22
    */

    @Disabled
    @Test
    void testTheMapIsCorrect() throws SQLException {
        int portID=16485;
        int month=5;
        int year=2021;
        int[][] actual = ctrl.getOccupancyMap(portID,month,year);
        int[][] expected = new int[31][2];
        for (int i = 0; i < 31; i++) {
            expected[i][0]=i+1;
        }
        expected[0][1]=4;
        expected[1][1]=4;
        expected[2][1]=4;
        expected[3][1]=8;
        expected[4][1]=12;
        expected[5][1]=12;
        expected[6][1]=12;
        expected[7][1]=12;
        expected[8][1]=18;
        expected[9][1]=22;
        expected[10][1]=22;
        expected[11][1]=22;
        expected[12][1]=22;
        expected[13][1]=22;
        expected[14][1]=22;
        expected[15][1]=22;
        expected[16][1]=22;
        expected[17][1]=22;
        expected[18][1]=22;
        expected[19][1]=22;
        expected[20][1]=22;
        expected[21][1]=22;
        expected[22][1]=22;
        expected[23][1]=22;
        expected[24][1]=22;
        expected[25][1]=22;
        expected[26][1]=22;
        expected[27][1]=22;
        expected[28][1]=22;
        expected[29][1]=22;
        expected[30][1]=22;
        assertArrayEquals(expected,actual);
    }

    @Disabled
    @Test
    void testNoMapWhenInvalidPortID() throws SQLException {
        int portID=16486;
        int month=1;
        int year=2021;
        int[][] actual = ctrl.getOccupancyMap(portID,month,year);
        Assertions.assertNull(actual);
    }

    @Disabled
    @Test
    void testNoMapWhenMonthMinor1() throws SQLException {
        int portID=16485;
        int month=0;
        int year=2021;
        int[][] actual = ctrl.getOccupancyMap(portID,month,year);
        Assertions.assertNull(actual);
    }

    @Disabled
    @Test
    void testNoMapWhenMonthBigger12() throws SQLException {
        int portID=16485;
        int month=13;
        int year=2021;
        int[][] actual = ctrl.getOccupancyMap(portID,month,year);
        Assertions.assertNull(actual);
    }

    @Disabled
    @Test
    void testNoMapWhenMonthIsNotCompleted() throws SQLException {
        int portID=16485;
        int month=12;
        int year=2025;
        int[][] actual = ctrl.getOccupancyMap(portID,month,year);
        Assertions.assertNull(actual);
    }

}
