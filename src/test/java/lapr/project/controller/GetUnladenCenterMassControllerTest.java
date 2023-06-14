package lapr.project.controller;

import lapr.project.domain.dataStructures.PositionsBST;
import lapr.project.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetUnladenCenterMassControllerTest {
    private Company company;
    private GetUnladenCenterMassController ctrl;
    private ShipPosition shipPosition;
    private PositionsBST positionsBST;
    private Date date;
    private Mass m1, m2, m3, m4;
    private Ship containerShip, lakeFreighter, bulkCarrier;
    private List<Mass> masses1, masses2, masses3;

    @BeforeEach
    void setUp() throws ParseException {
        company = new Company("Shipping Company");
        ctrl = new GetUnladenCenterMassController(company);
        positionsBST = new PositionsBST();
        date = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2020");
        shipPosition = new ShipPosition(211331640, date, 36.39094, -122.71335, 19.7, 145.5, 147, "B");
        positionsBST.insert(shipPosition);
        //Container Ship
        m1 = new Mass(147.0, 294.0, 32.0);
        m2 = new Mass(277.5, 33.0, 32.0);
        m3 = new Mass(71.5, 25.0, 9.0);
        masses1 = new ArrayList<>();
        masses1.add(m1);
        masses1.add(m2);
        masses1.add(m3);
        containerShip = new ShipSortMmsi(positionsBST, 123456789, "Panamax", "IMO9395044", "C4SQ2", 70, 294, 32, "NA", masses1, 155000000);

        //Lake Freighter - Chinamax
        m1 = new Mass(180.0, 360.0, 65.0);
        m2 = new Mass(46.0, 92.0, 65.0);
        m3 = new Mass(329.5, 61.0, 65.0);
        m4 = new Mass(325.5, 39.0, 27.9);
        masses2 = new ArrayList<>();
        masses2.add(m1);
        masses2.add(m2);
        masses2.add(m3);
        masses2.add(m4);
        lakeFreighter = new ShipSortMmsi(positionsBST, 123456788, "Chinamax", "IMO9395045", "C4SQ1", 70, 360, 65, "NA", masses2, 900000000);

        //Bulk Carrier Ship (Bridge in the stern) - Chinamax
        m1 = new Mass(180.0, 360.0, 65.0);
        m2 = new Mass(15.5, 31.0, 46.0);
        m3 = new Mass(341.0, 38.0, 65.0);
        masses3 = new ArrayList<>();
        masses3.add(m1);
        masses3.add(m2);
        masses3.add(m3);
        bulkCarrier = new ShipSortMmsi(positionsBST, 123456787, "Chinamax", "IMO9395043", "C4SQ3", 70, 360, 65, "NA", masses3, 900000000);
    }

    @Test
    void getUnladenCenterOfMass() {
        //Container Ship
        Point2D.Double expCenterOfMass = new Point2D.Double(158.303, 16.0);
        double expX = expCenterOfMass.x;
        double expY = expCenterOfMass.y;

        double x = ctrl.getUnladenCenterOfMass(containerShip).x;
        double y = ctrl.getUnladenCenterOfMass(containerShip).y;
        assertEquals(expX, x, 0.001);
        assertEquals(expY, y, 0.3);

        //Lake Freighter
        expCenterOfMass = new Point2D.Double(178.541, 32.5);
        expX = expCenterOfMass.x;
        expY = expCenterOfMass.y;

        x = lakeFreighter.getCenterOfMass().x;
        y = lakeFreighter.getCenterOfMass().y;
        assertEquals(expX, x, 0.001);
        assertEquals(expY, y, 0.6);

        //Bulk Carrier
        expCenterOfMass = new Point2D.Double(185.974, 32.5);
        expX = expCenterOfMass.x;
        expY = expCenterOfMass.y;

        x = bulkCarrier.getCenterOfMass().x;
        y = bulkCarrier.getCenterOfMass().y;
        assertEquals(expX, x, 0.001);
        assertEquals(expY, y, 0.5);
    }

}