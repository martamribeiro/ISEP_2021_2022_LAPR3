package lapr.project.controller;

import lapr.project.domain.dataStructures.PositionsBST;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.model.ShipSortMmsi;
import lapr.project.domain.store.ShipStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SearchShipControllerTest {
    ShipStore shipStore;
    private Company comp;
    private int mmsi1,mmsi2, mmsi3, mmsi4;
    private String vesselName;
    private String imo;
    private String callSign;
    private Ship s1, s2, s3, s4;

    double [] lats = {-30.033056, -42.033006, -55.022056, 23.008721};
    double [] lons = {-51.230000, -47.223056, -46.233056, 24.092123};
    double [] sogs = {25.4, 25.8, 31.7, 10.2};
    double [] cogs = {341.2, 330.3, 328.5, 320.9};
    int [] headings = {300, 302, 315, 300};
    String transcieverClass = "AIS";
    Date[] d1 = {new SimpleDateFormat("dd/MM/yyyy").parse("04/05/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("07/04/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("10/03/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("13/02/2021")} ;


    SearchShipControllerTest() throws ParseException {
    }

    @BeforeEach
    public void setUp() throws Exception {
        comp = new Company("Shipping company");
        shipStore = comp.getShipStore();
        PositionsBST positions = new PositionsBST();
        mmsi1 = 123456789;
        mmsi2 = 123456700;
        for(int i=0; i<4;i++){
            positions.insert(new ShipPosition(mmsi1, d1[i], lats[i], lons[i], sogs[i], cogs[i], headings[i], transcieverClass));
        }
        mmsi3 = 123456788;
        mmsi4 = 123456790;
        vesselName = "VARAMO";
        imo = "IMO9395044";
        callSign = "C4SQ2";
        PositionsBST positions2 = new PositionsBST();
        positions2.insert(new ShipPosition(mmsi1, d1[3], lats[3], lons[3], sogs[3], cogs[3], headings[3], transcieverClass));
        s1 = new ShipSortMmsi(positions, mmsi1, vesselName, "IMO9395045", "C4SG2", 70, 294,32,13.6,"79");
        s2 = new ShipSortMmsi(positions2, mmsi2, vesselName, "IMO9395044", "C4GQ2", 70, 294,32,13.6,"79");
        s3 = new ShipSortMmsi(positions2, mmsi3, vesselName, "IMO9395042", "CGSQ2", 70, 294,32,13.6,"79");
        s4 = new ShipSortMmsi(positions2, mmsi4, vesselName, "IMO9395043", "G4SQ2", 70, 294,32,13.6,"79");

        shipStore.saveShip(s1);
        shipStore.saveShip(s2);
        shipStore.saveShip(s3);
        shipStore.saveShip(s4);
    }

    @Test
    public void checkSearchShipWithAnyCodeImo(){
        SearchShipController controller = new SearchShipController(comp);
        String expected = controller.shipDetailsToString(s2);
        assertEquals(expected, controller.getShipInfoByAnyCode(("IMO9395044")));
    }

    @Test
    public void checkSearchShipWithAnyCodeMmsi(){
        SearchShipController controller = new SearchShipController(comp);
        String expected = controller.shipDetailsToString(s1);
        assertEquals(expected, controller.getShipInfoByAnyCode(Integer.toString(mmsi1)));
    }

    @Test
    public void checkSearchShipWithAnyCodeCallSign(){
        SearchShipController controller = new SearchShipController(comp);
        String expected = controller.shipDetailsToString(s3);
        assertEquals(expected, controller.getShipInfoByAnyCode(("CGSQ2")));
    }
}