package lapr.project.domain.store;

import lapr.project.domain.dataStructures.PositionsBST;
import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.model.ShipSortMmsi;
import lapr.project.dto.PositionDTO;
import lapr.project.dto.ShipsFileDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ShipStoreTest {
    ShipsFileDTO s1, s2, s3, s4;
    ShipStore shipStore;
    private int mmsi1,mmsi2, mmsi3, mmsi4;
    private String vesselName;
    private String imo;
    private String callSign;
    private PositionDTO shipPosition;
    private Date dateR1;

    @BeforeEach
    public void setUp() throws ParseException {
        shipStore = new ShipStore();
        dateR1 = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2020");
        shipPosition = new PositionDTO(dateR1, 36.39094, -122.71335, 19.7, 145.5, 147, "B");
        mmsi1 = 123456789;
        mmsi2 = 123456700;
        mmsi3 = 123456788;
        mmsi4 = 123456790;
        vesselName = "VARAMO";
        imo = "IMO9395044";
        callSign = "C4SQ2";
        s1 = new ShipsFileDTO(shipPosition, mmsi1, vesselName, imo, callSign, 70, 294,32,13.6,"79");
        s2 = new ShipsFileDTO(shipPosition, mmsi2, vesselName, imo, callSign, 70, 294,32,13.6,"79");
        s3 = new ShipsFileDTO(shipPosition, mmsi3, vesselName, imo, callSign, 70, 294,32,13.6,"79");
        s4 = new ShipsFileDTO(shipPosition, mmsi4, vesselName, imo, callSign, 70, 294,32,13.6,"79");
    }
    @Test
    void createShip() {
        ShipPosition expPosition = new ShipPosition(mmsi1, dateR1, 36.39094, -122.71335, 19.7, 145.5, 147, "B");
        PositionsBST positionsBST = new PositionsBST();
        positionsBST.insert(expPosition);
        Ship expected = new ShipSortMmsi(positionsBST,mmsi1, vesselName, imo, callSign, 70, 294,32,13.6,"79");

        assertEquals(expected, shipStore.createShip(s1));
    }

    @Test
    void validateShip() {
        assertFalse(shipStore.validateShip(null));
    }

    @Test
    void getShipByAnyCode() {
        shipStore.saveShip(shipStore.createShip(s1));
        shipStore.saveShip(shipStore.createShip(s2));
        shipStore.saveShip(shipStore.createShip(s3));
        shipStore.saveShip(shipStore.createShip(s4));
        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> shipStore.getShipByAnyCode("lalaland"));
        assertEquals("Couldn't find a ship with given code", thrown.getMessage());
    }
}