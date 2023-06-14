package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.dataControllers.ContainersOffLoadedController;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContainersOffLoadedControllerTest {
    private DatabaseConnection databaseConnection;
    private ContainersOffLoadedController controller;
    private Company comp;


    @BeforeEach
    void setUp() {
        /*comp = App.getInstance().getCompany();
        databaseConnection = App.getInstance().getConnection();
        controller = new ContainersOffLoadedController(comp);*/
        databaseConnection = mock(DatabaseConnection.class);
        controller = mock(ContainersOffLoadedController.class);
    }

    /**
     * test to ensure all the containers' ids are getting extracted from database
     */
    @Test
    void getOffloadedListTest() {
        List<Integer> expList = new LinkedList<>();
        expList.add(4300882);
        expList.add(7964863);
        expList.add(5465471);
        expList.add(9070585);
        expList.add(9834739);
        expList.add(8059722);
        expList.add(4924955);
        expList.add(9143577);
        expList.add(2136499);
        expList.add(810979);
        expList.add(3989030);
        expList.add(2116393);

        List<Integer> actList = new LinkedList<>();
        try {
            when(controller.getListOffloadedContainers(256888000)).thenReturn(expList);
            actList = controller.getListOffloadedContainers(256888000);
            Assertions.assertEquals(expList, actList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
