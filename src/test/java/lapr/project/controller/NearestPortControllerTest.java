package lapr.project.controller;

import lapr.project.domain.dataStructures.Ports2DTree;
import lapr.project.domain.dataStructures.PositionsBST;
import lapr.project.domain.model.*;
import lapr.project.domain.store.PortStore;
import lapr.project.domain.store.ShipStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NearestPortControllerTest {
    private final Company comp = new Company("Shipping company");
    private NearestPortController controller = new NearestPortController(comp);
    private final PortStore store = comp.getPortStore();
    private final ShipStore shipStore = comp.getShipStore();
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
        ship = new ShipSortCallSign(posBST, 123456789, "VARAMO", "IMO9395044", "C4SQ2", 70, 294,32,13.6,"79");
        shipStore.saveShip(ship);

        port1 = new Port( 123456789, "Porto Leixões", "Europa", "Portugal", 35.22, -120.71);
        store.savePort(port1);
        port2 = new Port(987654321, "Porto Algarve", "Europa", "Portugal", 20.54, -100.87);
        store.savePort(port2);
        store.balancePorts2DTree();
    }

    /**
     * Ensure the correct Ship is accessed with getShipByCallSigh()
     */
    @Test
    public void getShipByCallSignTest() {
        Ship actual = controller.getShipByCallSign("C4SQ2");

        Assertions.assertEquals(ship, actual);
    }

    /**
     * Ensure the correct coordinates are accessed with getShipCoordinates()
     */
    @Test
    public void getShipCoordinatesTest() {
        List<Double> exp = new LinkedList<>();
        exp.add(shipPosition.getLat());
        exp.add(shipPosition.getLon());

        List<Double> actual = controller.getShipCoordinates(ship, dateR1);

        Assertions.assertEquals(exp, actual);
    }

    /**
     * Ensure the correct Port is accessed with findClosestPort()
     */
    @Test
    public void findClosestPortTest() {
        Port exp = port1;

        Port actual = controller.findClosestPort("C4SQ2", dateR1);

        Assertions.assertEquals(exp, actual);
    }

}
