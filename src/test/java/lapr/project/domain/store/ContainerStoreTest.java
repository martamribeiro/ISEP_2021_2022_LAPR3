package lapr.project.domain.store;

import lapr.project.domain.model.Container;
import lapr.project.domain.model.ContainerLayer;
import lapr.project.dto.PositionDTO;
import lapr.project.dto.ShipsFileDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerStoreTest {

    ContainerStore containerStore;
    Container c1;
    Container c2;
    Container c3;
    Container c4;
    int[] ids = {12345,1234,123456,123457};
    List<ContainerLayer> layersMinus5 = new ArrayList<>();
    List<ContainerLayer> layerSeven = new ArrayList<>();

    @BeforeEach
    public void setUp() throws ParseException {
        containerStore = new ContainerStore();
        double[] payloads = {12.3, 14.5, 12.1, 10.0};
        double[] tare = {10.3, 11.5, 2.1, 1.0};
        double[] gross = {22.6, 26.0, 14.2, 11.0};
        String[] iso = {"ISO1", "ISO2", "ISO3", "ISO4"};

        ContainerLayer iron = new ContainerLayer(0.003, 55);
        ContainerLayer rockWool = new ContainerLayer(0.09, 0.045);
        ContainerLayer wood = new ContainerLayer(0.007, 0.13);
        ContainerLayer phenolicFoam = new ContainerLayer(0.09, 0.023);
        ContainerLayer cork = new ContainerLayer(0.007, 0.038);

        layersMinus5.add(iron);
        layersMinus5.add(phenolicFoam);
        layersMinus5.add(cork);

        layerSeven.add(iron);
        layerSeven.add(rockWool);
        layerSeven.add(wood);

        c1 = new Container(ids[0], payloads[0], tare[0], gross[0], iso[0], layersMinus5, -5);
        c2 = new Container(ids[1], payloads[1], tare[1], gross[1], iso[1], layerSeven, 7);
        c3 = new Container(ids[2], payloads[2], tare[2], gross[2], iso[2], layersMinus5, -5);
        c4 = new Container(ids[3], payloads[3], tare[3], gross[3], iso[3], layerSeven, 7);

        containerStore.saveContainer(c1);
        containerStore.saveContainer(c2);
        containerStore.saveContainer(c3);
        containerStore.saveContainer(c4);
    }

    @Test
    void saveContainer() {
        boolean result = containerStore.saveContainer(new Container(1, 8.0, 8.0, 16.0, "iso123", layersMinus5, -5));
        assertTrue(result);
        boolean resultFalse = containerStore.saveContainer(c1);
        assertFalse(resultFalse);
    }


    @Test
    void getContainerById() {
        Container result = containerStore.getContainerById(ids[0]);
        assertEquals(c1, result);

        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> containerStore.getContainerById(22222222));
        assertEquals("Could not find container with given id", thrown.getMessage());
    }

    @Test
    void getContainersList() {
        List<Container> exp = new ArrayList<>();
        exp.add(c1);
        exp.add(c2);
        exp.add(c3);
        exp.add(c4);

        assertEquals(exp, containerStore.getContainersList());
    }
}