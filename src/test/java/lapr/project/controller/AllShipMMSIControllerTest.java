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

public class AllShipMMSIControllerTest {
    private final Company comp = new Company("Shipping company");
    private final ShipStore store = comp.getShipStore();
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

    public AllShipMMSIControllerTest() throws ParseException {
    }

    @BeforeEach
    public void setUp() {
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
    public void getSortedByTravelledDistance() {
        AllShipMMSIController controller = new AllShipMMSIController(comp);
        Set<Double> l1 = new LinkedHashSet<>();
        Ship s1 = store.getShipByAnyCode(String.valueOf(mmsiCodes[0]));
        l1.add(s1.getPositionsBST().getTotalDistance());
        l1.add(s1.getPositionsBST().getDeltaDistance());
        l1.add((double) s1.getPositionsBST().size());

        Set<Double> l2 = new LinkedHashSet<>();
        Ship s2 = store.getShipByAnyCode(String.valueOf(mmsiCodes[1]));
        l2.add(s2.getPositionsBST().getTotalDistance());
        l2.add(s2.getPositionsBST().getDeltaDistance());
        l2.add((double) s2.getPositionsBST().size());

        Set<Double> l3 = new LinkedHashSet<>();
        Ship s3 = store.getShipByAnyCode(String.valueOf(mmsiCodes[2]));
        l3.add(s3.getPositionsBST().getTotalDistance());
        l3.add(s3.getPositionsBST().getDeltaDistance());
        l3.add((double) s3.getPositionsBST().size());

        Set<Double> l4 = new LinkedHashSet<>();
        Ship s4 = store.getShipByAnyCode(String.valueOf(mmsiCodes[3]));
        l4.add(s4.getPositionsBST().getTotalDistance());
        l4.add(s4.getPositionsBST().getDeltaDistance());
        l4.add((double) s4.getPositionsBST().size());

        Map<Integer, Set<Double>> expectedMap = new LinkedHashMap<Integer, Set<Double>>(){
            {
                put(mmsiCodes[2], l3);
                put(mmsiCodes[3], l4);
                put(mmsiCodes[1], l2);
                put(mmsiCodes[0], l1);
            }
        };


        Map<Integer, Set<Double>> actualMap = controller.sortedByTravelledDistance();

        Assert.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void getSortedByTotalMovements() {
        AllShipMMSIController controller = new AllShipMMSIController(comp);
        Set<Double> l1 = new HashSet<>();
        Ship s1 = store.getShipByAnyCode(String.valueOf(mmsiCodes[0]));
        l1.add(s1.getPositionsBST().getTotalDistance());
        l1.add(s1.getPositionsBST().getDeltaDistance());
        l1.add((double) s1.getPositionsBST().size());

        Set<Double> l2 = new HashSet<>();
        Ship s2 = store.getShipByAnyCode(String.valueOf(mmsiCodes[1]));
        l2.add(s2.getPositionsBST().getTotalDistance());
        l2.add(s2.getPositionsBST().getDeltaDistance());
        l2.add((double) s2.getPositionsBST().size());

        Set<Double> l3 = new HashSet<>();
        Ship s3 = store.getShipByAnyCode(String.valueOf(mmsiCodes[2]));
        l3.add(s3.getPositionsBST().getTotalDistance());
        l3.add(s3.getPositionsBST().getDeltaDistance());
        l3.add((double) s3.getPositionsBST().size());

        Set<Double> l4 = new HashSet<>();
        Ship s4 = store.getShipByAnyCode(String.valueOf(mmsiCodes[3]));
        l4.add(s4.getPositionsBST().getTotalDistance());
        l4.add(s4.getPositionsBST().getDeltaDistance());
        l4.add((double) s4.getPositionsBST().size());

        Map<Integer, Set<Double>> expectedMap = new LinkedHashMap<Integer, Set<Double>>(){
            {
                put(mmsiCodes[1], l2);
                put(mmsiCodes[2], l3);
                put(mmsiCodes[3], l4);
                put(mmsiCodes[0], l1);
            }
        };

        Map<Integer, Set<Double>> actualMap = controller.sortedByTotalMovements();

        Assert.assertEquals(expectedMap, actualMap);
    }
}
