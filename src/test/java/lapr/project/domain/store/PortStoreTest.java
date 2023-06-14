package lapr.project.domain.store;

import lapr.project.domain.dataStructures.Ports2DTree;
import lapr.project.domain.dataStructures.PositionsBST;
import lapr.project.domain.model.Port;
import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.model.ShipSortMmsi;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PortStoreTest {
    private final PortStore store = new PortStore();
    private PositionsBST posBST = new PositionsBST();
    private Ship ship;
    private Date dateR1;
    private Port port1, port2;
    private Ports2DTree portTree = new Ports2DTree();
    private ShipPosition shipPosition;


    @BeforeEach
    public void setUp() throws ParseException {
        dateR1 = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2020");
        shipPosition = new ShipPosition(123456789, dateR1, 36.39094, -122.71335, 19.7, 145.5, 147, "B");
        posBST.insert(shipPosition);
        ship = new ShipSortMmsi(posBST, 123456789, "VARAMO", "IMO9395044", "C4SQ2", 70, 294,32,13.6,"79");

        port1 = new Port( 123456789, "Porto Leixões", "Europa", "Portugal", 35.22, -120.71);
        store.savePort(port1);
        port2 = new Port(987654321, "Porto Algarve", "Europa", "Portugal", 20.54, -100.87);
        store.savePort(port2);
        store.balancePorts2DTree();
    }

    @Test
    public void getNearestPort() {
        Port exp = port1;


        List<Double> coordinates = new LinkedList<>();
        coordinates.add(shipPosition.getLat());
        coordinates.add(shipPosition.getLon());

        Port actual = store.findClosestPort(coordinates);

        assertEquals(exp, actual);
    }

    @Test
    public void validatePortFalse(){
        assertFalse(store.validatePort(null));
    }

    @Test
    public void importPorts(){
        List<Port> existentPorts = new ArrayList<>();
        existentPorts.add(port1);
        existentPorts.add(port2);

        store.importPorts(existentPorts);
        assertEquals(2, store.getPortsList().size(), "List size should be 2");

        existentPorts.add(null);
        store.importPorts(existentPorts);
        assertEquals(2, store.getPortsList().size(), "List size should remain 2, not add repeated ports");
    }

    @Test
    public void getPortsTest(){
        List<Port> existentPorts = new ArrayList<>();
        existentPorts.add(port1);
        existentPorts.add(port2);

        store.importPorts(existentPorts);
        List<Port> getPorts = store.getPortsList();

        for(int i = 0; i<existentPorts.size(); i++){
            assertEquals(existentPorts.get(i), getPorts.get(i));
        }
    }

    @Test
    public void getPortsByCountryTest(){
        List<Port> existentPorts = new ArrayList<>();
        existentPorts.add(port1);
        existentPorts.add(port2);

        store.importPorts(existentPorts);
        List<Port> getPorts = store.getPortsByCountry("Portugal");

        for(int i = 0; i<existentPorts.size(); i++){
            assertEquals(existentPorts.get(i), getPorts.get(i));
        }

        List<Port> emptyPorts = store.getPortsByCountry("Germany");

        assertTrue(emptyPorts.isEmpty());
    }

    @Test
    public void getPortByNameTest(){
        List<Port> existentPorts = new ArrayList<>();
        existentPorts.add(port1);
        existentPorts.add(port2);

        store.importPorts(existentPorts);

        assertEquals(port1, store.getPortById(123456789));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> store.getPortById(6767676));
        assertEquals("Could not find a port with the given id: 6767676",  thrown.getMessage(), "Not found port should throw an exception");

    }


}
