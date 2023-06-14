package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.dataControllers.ContainersLoadedController;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContainersLoadedControllerTest {
    private DatabaseConnection databaseConnection;
    private ContainersLoadedController controller;
    private Company comp;


    @BeforeEach
    void setUp() {
        /*comp = App.getInstance().getCompany();
        databaseConnection = App.getInstance().getConnection();
        controller = new ContainersLoadedController(comp);*/
        databaseConnection = mock(DatabaseConnection.class);
        controller = mock(ContainersLoadedController.class);
    }

    /**
     * test to ensure all the containers' ids are getting extracted from database
     */
    @Test
    void getOffloadedListTest() {
        List<Integer> expList = new LinkedList<>();
        expList.add(8529220);
        expList.add(6107524);
        expList.add(2755128);
        List<Integer> actList = new LinkedList<>();
        try {
            when(controller.getListLoadedContainers(256888000)).thenReturn(expList);
            actList = controller.getListLoadedContainers(256888000);
            Assertions.assertEquals(expList, actList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
