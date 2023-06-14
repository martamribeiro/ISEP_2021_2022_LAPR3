package lapr.project.controller;

import lapr.project.domain.dataStructures.PositionsBST;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.model.ShipSortMmsi;
import lapr.project.domain.store.ShipStore;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TopNControllerTest {
    App app = App.getInstance();
    private  Company comp;
    private  ShipStore store;
    private PositionsBST posBST;
    private PositionsBST posBST1;
    private PositionsBST posBST2;
    private PositionsBST posBST3;

    Date[] d1 = {new SimpleDateFormat("dd/MM/yyyy").parse("04/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("07/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("10/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("13/01/2021")} ;
    int [] mmsiCodes = {333333333, 111111111, 222222222, 123456789};
    String [] vesselNames = {"VARAMO", "SAITA", "VARAMO", "HYUNDAI SINGAPURE"};
    String [] imoCodes = {"IMO9395044", "IMO9395022", "IMO9395066", "IMO9395088"};
    String [] callSigns = {"C4SQ2", "5BBA4", "C4SQ2", "5BZP3"};
    double [] lats = {-30.033056, -42.033006, -55.022056, 23.008721};
    double [] lons = {-51.230000, -47.223056, -46.233056, 24.092123};
    double [] sogs = {25.4, 25.8, 31.7, 10.2};
    double [] cogs = {341.2, 330.3, 328.5, 320.9};
    int [] headings = {300, 302, 315, 300};
    String transcieverClass = "AIS";

    public TopNControllerTest() throws ParseException {
    }

    @BeforeEach
    public void setUp() {
        comp = app.getCompany();
        store = comp.getShipStore();
        posBST = new PositionsBST();
        posBST1 = new PositionsBST();
        posBST2 = new PositionsBST();
        posBST3 = new PositionsBST();

        posBST.insert(new ShipPosition(mmsiCodes[0], d1[0], lats[0], lons[0], sogs[0], cogs[0], headings[0], transcieverClass));
        posBST.insert(new ShipPosition(mmsiCodes[1], d1[1], lats[1], lons[1], sogs[1], cogs[1], headings[1], transcieverClass));
        posBST1.insert(new ShipPosition(mmsiCodes[1], d1[1], lats[1], lons[1], sogs[1], cogs[1], headings[1], transcieverClass));
        posBST1.insert(new ShipPosition(mmsiCodes[2], d1[2], lats[2], lons[2], sogs[2], cogs[2], headings[2], transcieverClass));
        posBST2.insert(new ShipPosition(mmsiCodes[2], d1[2], lats[2], lons[2], sogs[2], cogs[2], headings[2], transcieverClass));
        posBST2.insert(new ShipPosition(mmsiCodes[3], d1[3], lats[3], lons[3], sogs[3], cogs[3], headings[3], transcieverClass));
        posBST3.insert(new ShipPosition(mmsiCodes[3], d1[3], lats[3], lons[3], sogs[3], cogs[3], headings[3], transcieverClass));
        posBST3.insert(new ShipPosition(mmsiCodes[1], d1[1], lats[1], lons[1], sogs[1], cogs[1], headings[1], transcieverClass));

        //System.out.println(new Ship(vesselType, positionsBST, mmsiCodes[i], vesselNames[i], imoCodes[i], callSigns[i]));
        store.saveShip(new ShipSortMmsi(posBST, mmsiCodes[0], vesselNames[0], imoCodes[0], callSigns[0], 70, 294, 32, 13.6, "79"));
        store.saveShip(new ShipSortMmsi(posBST1, mmsiCodes[1], vesselNames[1], imoCodes[1], callSigns[1], 70, 294, 32, 13.6, "79"));
        store.saveShip(new ShipSortMmsi(posBST2, mmsiCodes[2], vesselNames[2], imoCodes[2], callSigns[2], 70, 294, 32, 13.6, "79"));
        store.saveShip(new ShipSortMmsi(posBST3, mmsiCodes[3], vesselNames[3], imoCodes[3], callSigns[3], 70, 294, 32, 13.6, "79"));


    }

    @Test
    public void getListByDateController() throws ParseException {
        TopNController controller = new TopNController(comp);

        List<Ship> testList = controller.getShipsByDate(new SimpleDateFormat("dd/MM/yyyy").parse("06/01/2021"), new SimpleDateFormat("dd/MM/yyyy").parse("11/01/2021"));
        List<Ship> expectedList = new ArrayList<>();
        expectedList.add(new ShipSortMmsi(posBST, mmsiCodes[2], vesselNames[2], imoCodes[2], callSigns[2], 70, 294,32,13.6,"79"));
        expectedList.add(new ShipSortMmsi(posBST, mmsiCodes[1], vesselNames[1], imoCodes[1], callSigns[1], 70, 294,32,13.6,"79"));

        Assert.assertEquals(expectedList, testList);
    }

    @Test
    public void getShipWithMeanControllerTest() {
        TopNController controller = new TopNController();

        Ship ship1 = new ShipSortMmsi(posBST, mmsiCodes[0], vesselNames[0], imoCodes[0], callSigns[0], 70, 294,32,13.6,"79");
        Ship ship2 = new ShipSortMmsi(posBST1, mmsiCodes[1], vesselNames[1], imoCodes[1], callSigns[1], 80, 250,25,15.2,"67");
        Map<Integer, Map<Ship, Set<Double>>> testMap = new HashMap<Integer, Map<Ship, Set<Double>>>();
        List<Ship> expectedList = new LinkedList<>();
        expectedList.add(ship1);
        expectedList.add(ship2);
        testMap = controller.getShipWithMean(expectedList, 2);

        Set<Double> shipInfo1 = new HashSet<>();
        shipInfo1.add(ship1.getPositionsBST().getTotalDistance());
        shipInfo1.add(ship1.getPositionsBST().getMeanSog());
        Set<Double> shipInfo2 = new HashSet<>();
        shipInfo2.add(ship2.getPositionsBST().getTotalDistance());
        shipInfo2.add(ship2.getPositionsBST().getMeanSog());

        Map<Ship, Set<Double>> insideMap1 = new HashMap<Ship, Set<Double>>(){ {
            put(ship1, shipInfo1);

        }
        };
        Map<Ship, Set<Double>> insideMap2 = new HashMap<Ship, Set<Double>>(){ {
            put(ship2, shipInfo2);

        }
        };

        Map<Integer, Map<Ship, Set<Double>>> expectedMap = new HashMap<Integer, Map<Ship, Set<Double>>>() {
            {
                put(ship1.getVesselTypeID(), insideMap1);
                put(ship2.getVesselTypeID(), insideMap2);
            }
        };

        Assert.assertEquals(testMap, expectedMap);
    }
}
