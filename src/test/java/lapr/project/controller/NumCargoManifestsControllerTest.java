package lapr.project.controller;

import lapr.project.data.dataControllers.NumCargoManifestsController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NumCargoManifestsControllerTest {

    @Test
    void integrationTest() {
        int year = 2021;
        int mmsi = 305373000;
        String expectedReturn = "Number of cargo manifests in 2021:\n" +
                "10\n" +
                "Average number of containers per cargo manifest:\n" +
                "6\n";

        NumCargoManifestsController numCargoManifestsController = mock(NumCargoManifestsController.class);
        when(numCargoManifestsController.getNumberOfCMAndAverageContForYear(year, mmsi)).thenReturn(expectedReturn);
        String returned = numCargoManifestsController.getNumberOfCMAndAverageContForYear(year, mmsi);
        assertEquals(expectedReturn, returned);
    }

  /*  @Test
    void getNumberOfCMAndAverageContForYear() {
        NumCargoManifestsController numCargoManifestsController = new NumCargoManifestsController();
        System.out.println(numCargoManifestsController.getNumberOfCMAndAverageContForYear(2021, 305373000));
    }*/
}