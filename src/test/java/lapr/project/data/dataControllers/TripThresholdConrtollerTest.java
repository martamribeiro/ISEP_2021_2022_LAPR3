package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.DatabaseConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TripThresholdConrtollerTest {
    private DatabaseConnection databaseConnection;
    private TripThresholdController ctrl;

    @BeforeEach
    void setUp() {
        databaseConnection = mock(DatabaseConnection.class);
        ctrl = mock(TripThresholdController.class);
    }

    @Test
    void getVoyagesList(){
        List<Integer> expList = new LinkedList<>(Arrays.asList(73929, 89070, 33710, 29960, 98507, 29440, 36929, 90333, 1334, 77575, 25803, 5230, 47090, 53526, 13398, 22209, 57425, 35642, 13631, 16347, 54675, 46988, 41359, 56510, 68138, 29045, 74590, 34710, 47538, 7282, 79138, 68973, 18507, 17423, 31374, 52877, 98427, 95119, 81342, 67132, 68139, 81347, 81901, 81903, 81904, 81905));

        List<Integer> actList = new LinkedList<>();
        try {
            when(ctrl.getVoyagesBelowThreshold()).thenReturn(expList);
            actList = ctrl.getVoyagesBelowThreshold();
            Assertions.assertEquals(expList, actList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
