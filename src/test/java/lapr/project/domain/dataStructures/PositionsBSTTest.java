package lapr.project.domain.dataStructures;

import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.model.ShipSortMmsi;
import lapr.project.utils.DistanceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PositionsBSTTest {

    PositionsBST instance;
    PositionsBST longerInstance;
    Date [] d1 = {new SimpleDateFormat("dd/MM/yyyy").parse("04/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("07/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("10/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("13/01/2021")} ;

    Date [] d2 = {new SimpleDateFormat("dd/MM/yyyy").parse("04/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("07/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("10/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("02/04/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("15/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2020"),
            new SimpleDateFormat("dd/MM/yyyy").parse("03/01/2021")};

    int [] mmsiCodes = {333333333, 111111111, 222222222, 123456789};
    int mmsiCode = 111111111;

    double [] lats = {-30.033056, -42.033006, -55.022056, 23.008721};
    double [] lats2 = {-30.033056, -42.033006, -55.022056, 23.008721, 62.97875,
                        72.96912, -22.033006, -70.022056, -29.00006,  60.008721};

    double [] lons = {-51.230000, -47.223056, -46.233056, 24.092123};
    double [] lons2 = {-51.230000, -47.223056, -46.233056, 24.092123, 50.000000,
                        60.000000, -30.000000, 20.000000, 10.000000, 12.123456};

    double [] sogs = {25.4, 25.8, 31.7, 10.2};
    double [] sogs2 = {25.4, 25.8, 31.7, 10.2, 12.2, 14.2, 16.0, 18.0, 20.0, 22.0};

    double [] cogs = {341.2, 330.3, 328.5, 320.9};
    double [] cogs2 = {341.2, 330.3, 328.5, 320.9, 300.0, 322.0, 330.0, 340.0, 350.0, 355.0};

    int [] headings = {300, 302, 315, 300};
    int [] headings2 = {300, 302, 315, 300, 299, 300, 301, 302, 303, 304};

    String transcieverClass = "AIS";

    /* for US7 */
    String [] vesselNames = {"VARAMO", "SAITA", "VARAMO", "HYUNDAI SINGAPURE"};
    String [] imoCodes = {"IMO9395044", "IMO9395022", "IMO9395066", "IMO9395088"};
    String [] callSigns = {"C4SQ2", "5BBA4", "C4SQ2", "5BZP3"};
    private ShipTreeMmsi shipsBST2;
    List<PositionsBST> positionsList;
    private PositionsBST positionsBST1;
    private PositionsBST positionsBST2;
    private PositionsBST positionsBST3;
    private PositionsBST positionsBST4;
    double [] lats4 = {62.97875, 72.96912, -22.033006, -70.022056};
    double [] lats3 = {-29.00006,  60.008721, 50.00003, 34.345321};
    double [] lons4 = {50.000000, 60.000000, -30.000000, 20.000000};
    double [] lons3 = {-29.00006,  60.008721, 50.00003, 34.345321};
    ShipPosition p1;

    public PositionsBSTTest() throws ParseException {
    }

    @BeforeEach
    public void setUp() throws ParseException {
        instance = new PositionsBST();
        longerInstance = new PositionsBST();
        for(int i=0; i<3;i++){
            instance.insert(new ShipPosition(mmsiCodes[i], d1[i], lats[i], lons[i], sogs[i], cogs[i], headings[i], transcieverClass));
        }
        p1 = new ShipPosition(mmsiCodes[0], d1[0], lats[0], lons[0], sogs[0], cogs[0], headings[0], transcieverClass);
        ShipPosition p2 = new ShipPosition(mmsiCodes[1], d1[1], lats[1], lons[1], sogs[1], cogs[1], headings[1], transcieverClass);
        ShipPosition p3 = new ShipPosition(mmsiCodes[2], d1[2], lats[2], lons[2], sogs[2], cogs[2], headings[2], transcieverClass);

        for(int i=0; i<10;i++){
            ShipPosition sp = new ShipPosition(mmsiCode, d2[i], lats2[i], lons2[i], sogs2[i], cogs2[i], headings2[i], transcieverClass);
            longerInstance.insert(sp);
        }

        /* for US7 */

        shipsBST2 = new ShipTreeMmsi();
        positionsList = new ArrayList<>();
        positionsBST1 = new PositionsBST();
        positionsBST2 = new PositionsBST();
        positionsBST3 = new PositionsBST();
        positionsBST4 = new PositionsBST();

        for(int i=0; i<4;i++){
            positionsBST1.insert(new ShipPosition(mmsiCodes[0], d1[i], lats[i], lons[i], sogs[i], cogs[i], headings[i], transcieverClass));
            positionsBST2.insert(new ShipPosition(mmsiCodes[1], d1[i], lats4[i], lons4[i], sogs[i], cogs[i], headings[i], transcieverClass));
            positionsBST3.insert(new ShipPosition(mmsiCodes[2], d1[i], lats3[i], lons3[i], sogs[i], cogs[i], headings[i], transcieverClass));
        }

        positionsBST4.insert(new ShipPosition(mmsiCodes[3], d1[3], lats[3], lons[3], sogs[3], cogs[3], headings[3], transcieverClass));

        positionsList.add(positionsBST1);
        positionsList.add(positionsBST2);
        positionsList.add(positionsBST3);
        positionsList.add(positionsBST4);

        for(int i=0; i<4;i++){
            shipsBST2.insert(new ShipSortMmsi(positionsList.get(i), mmsiCodes[i], vesselNames[i], imoCodes[i], callSigns[i], 70, 294,32,13.6,"79"));
        }

        /* end for US7 */
    }

    @Test
    public void testStartDate(){
        Date expected = d1[0];
        System.out.println("start date");
        assertEquals(expected, instance.getStartDate(), "Date should be 04/01/2021");
        instance.remove(instance.smallestElement());
        expected = d1[1];
        assertEquals(expected, instance.getStartDate(), "Date should be 07/01/2021");
    }

    @Test
    public void testDatesWithEmptyList(){
        PositionsBST p2 = new PositionsBST();
        IllegalArgumentException thrownStart = assertThrows( IllegalArgumentException.class, p2::getStartDate);
        assertEquals("List is empty", thrownStart.getMessage());

        IllegalArgumentException thrownEnd = assertThrows( IllegalArgumentException.class, p2::getEndDate);
        assertEquals("List is empty", thrownEnd.getMessage());
    }

    @Test
    public void testSpeedAndCourseWithEmptyList(){
        PositionsBST p3 = new PositionsBST();
        IllegalArgumentException thrownMaxSog = assertThrows( IllegalArgumentException.class, () -> {
            p3.getMaxSog();
        });
        assertEquals("List is empty", thrownMaxSog.getMessage());

        IllegalArgumentException thrownMeanSog = assertThrows( IllegalArgumentException.class, () -> {
            p3.getMeanSog();
        });
        assertEquals("List is empty", thrownMeanSog.getMessage());

        IllegalArgumentException thrownMeanCog = assertThrows( IllegalArgumentException.class, () -> {
            p3.getMeanCog();
        });
        assertEquals("List is empty", thrownMeanCog.getMessage());

    }

    @Test
    public void testCoordinatesWithEmptyList(){
        PositionsBST p2 = new PositionsBST();
        IllegalArgumentException thrownDepartLat = assertThrows( IllegalArgumentException.class, () -> {
            p2.getDepartLatitude();
        });
        assertEquals("List is empty", thrownDepartLat.getMessage());

        IllegalArgumentException thrownDepartLon = assertThrows( IllegalArgumentException.class, () -> {
            p2.getDepartLongitude();
        });
        assertEquals("List is empty", thrownDepartLon.getMessage());

        IllegalArgumentException thrownArrivalLat = assertThrows( IllegalArgumentException.class, () -> {
            p2.getArrivalLatitude();
        });
        assertEquals("List is empty", thrownArrivalLat.getMessage());

        IllegalArgumentException thrownArrivalLon = assertThrows( IllegalArgumentException.class, () -> {
            p2.getArrivalLongitude();
        });
        assertEquals("List is empty", thrownArrivalLon.getMessage());
    }

    @Test
    public void testDistancesWithEmptyList(){
        PositionsBST p2 = new PositionsBST();
        IllegalArgumentException thrownTotalDistance = assertThrows( IllegalArgumentException.class, () -> {
            p2.getTotalDistance();
        });
        assertEquals("List is empty", thrownTotalDistance.getMessage());

        IllegalArgumentException thrownDeltaDistance = assertThrows( IllegalArgumentException.class, () -> {
            p2.getDeltaDistance();
        });
        assertEquals("List is empty", thrownDeltaDistance.getMessage());

    }

    @Test
    public void testDistanceCalculationWithEqualValues(){
        double lat1 = 33.3;
        double lon1 = 44.4;
        double expected = 0.0;
        assertEquals(expected, DistanceUtils.distanceBetweenInKm(lat1, lat1, lon1, lon1), "equal positions should result in "+expected);
    }

    @Test
    public void testEndDate(){
        Date expected = d1[2];
        System.out.println("end date");
        assertEquals(expected, instance.getEndDate(), "Date should be 10/01/2021");
    }

    @Test
    public void testMaxSog(){
        Double expected = sogs[2];
        System.out.println("max sog");
        System.out.println(instance.getMaxSog());
        assertEquals(expected, instance.getMaxSog(), "Max Sog should be "+expected);
    }
    @Test
    public void testMeanSog(){
        Double expected;
        double sum=0.0;
        for(int i=0;i<3;i++){
            sum += sogs[i];
        }
        expected = sum/3.0;
        System.out.println("mean sog");
        System.out.println(instance.getMeanSog());
        System.out.println(expected);

        assertEquals(expected, instance.getMeanSog(), "Mean sog should be "+expected);
    }

    @Test
    public void testMeanCog(){
        Double expected;
        double sum=0.0;
        for(int i=0;i<3;i++){
            sum += cogs[i];
        }
        expected = sum/3.0;
        System.out.println("mean cog");
        System.out.println(instance.getMeanCog());
        System.out.println(expected);

        assertEquals(expected, instance.getMeanCog(), "Mean cog should be "+expected);
    }
    

    @Test
    public void testDepartLatitude(){
        Double expected = lats[0];
        System.out.println("depart latitude");
        assertEquals(expected, instance.getDepartLatitude(), "depart latitude "+expected);
    }

    @Test
    public void testDepartLongitude(){
        Double expected = lons[0];
        System.out.println("depart longitude");
        assertEquals(expected, instance.getDepartLongitude(), "depart longitude "+expected);
    }

    @Test
    public void testArrivalLatitude(){
        Double expected = lats[2];
        System.out.println("Arrival latitude");
        assertEquals(expected, instance.getArrivalLatitude(), "Arrival latitude "+expected);
    }

    @Test
    public void testArrivalLongitude(){
        Double expected = lons[2];
        System.out.println("Arrival longitude");
        assertEquals(expected, instance.getArrivalLongitude(), "Arrival longitude "+expected);
    }

    @Test
    public void testTravelDistance(){
        //1382+1446+10920
        double expected = 1382.0+1446.0; //got from distance calculator in: http://www.movable-type.co.uk/scripts/latlong.html
        System.out.println("delta distance");
        System.out.println(instance.getTotalDistance());
        assertEquals(expected, instance.getTotalDistance(), 2, "total distance should be "+expected);
        instance.insert(new ShipPosition(mmsiCodes[3], d1[3], lats[3], lons[3], sogs[3], cogs[3], headings[3], transcieverClass));
        expected = 1382.0+1446.0+10920.0;
        assertEquals(expected, instance.getTotalDistance(), 2, "total distance now should be "+expected);
    }

    @Test
    public void testDeltaDistance(){
        double expected = 2807.0; //got from distance calculator in: http://www.movable-type.co.uk/scripts/latlong.html
        System.out.println("delta distance");
        System.out.println(instance.getDeltaDistance());
        assertEquals(expected, instance.getDeltaDistance(), 2, "delta distance should be "+expected);
        instance.insert(new ShipPosition(mmsiCodes[3], d1[3], lats[3], lons[3], sogs[3], cogs[3], headings[3], transcieverClass));
        expected = 9968.0;
        assertEquals(expected, instance.getDeltaDistance(), 2, "delta distance now should be "+expected);
    }

    /**
     * US103 - Test to ensure getPositionalMessages() is functioning properly
     * Situation 1: the user wishes to see ship positions in a period
     */
    @Test
    public void getPositionalMessagesInAPeriod() throws ParseException {
        Date initialDate = new SimpleDateFormat("dd/MM/yyyy").parse("02/12/2020");
        Date finalDate = new SimpleDateFormat("dd/MM/yyyy").parse("10/01/2021");
        List<String> expList = new ArrayList<>();

        ShipPosition sp1 = new ShipPosition(mmsiCode, d2[8], lats2[8], lons2[8], sogs2[8], cogs2[8], headings2[8], transcieverClass);
        ShipPosition sp2 = new ShipPosition(mmsiCode, d2[4], lats2[4], lons2[4], sogs2[4], cogs2[4], headings2[4], transcieverClass);
        ShipPosition sp3 = new ShipPosition(mmsiCode, d2[9], lats2[9], lons2[9], sogs2[9], cogs2[9], headings2[9], transcieverClass);
        ShipPosition sp4 = new ShipPosition(mmsiCode, d2[0], lats2[0], lons2[0], sogs2[0], cogs2[0], headings2[0], transcieverClass);
        ShipPosition sp5 = new ShipPosition(mmsiCode, d2[1], lats2[1], lons2[1], sogs2[1], cogs2[1], headings2[1], transcieverClass);
        ShipPosition sp6 = new ShipPosition(mmsiCode, d2[2], lats2[2], lons2[2], sogs2[2], cogs2[2], headings2[2], transcieverClass);

        expList.add(sp1.toString());
        expList.add(sp2.toString());
        expList.add(sp3.toString());
        expList.add(sp4.toString());
        expList.add(sp5.toString());
        expList.add(sp6.toString());

        List<String> list = longerInstance.getPositionalMessages(initialDate, finalDate);

        assertEquals(expList, list);
    }

    /**
     * US103 - Test to ensure getPositionalMessages() is functioning properly
     * Situation 2: the user wishes to see ship positions in a date
     */
    @Test
    public void getPositionalMessagesInADate() throws ParseException {
        Date initialDate = new SimpleDateFormat("dd/MM/yyyy").parse("02/04/2021");

        List<String> expList = new ArrayList<>();
        ShipPosition sp1 = new ShipPosition(mmsiCode, d2[3], lats2[3], lons2[3], sogs2[3], cogs2[3], headings2[3], transcieverClass);
        expList.add(sp1.toString());

        List<String> list = longerInstance.getPositionalMessages(initialDate, initialDate);

        assertEquals(expList, list);
    }

    /**
     * US103 - Test to ensure getPositionalMessages() is functioning properly
     * Situation 3: there aren't any positional messages found
     */
    @Test
    public void getPositionalMessagesNotFoundInAPeriod() throws ParseException {
        Date initialDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020");
        Date finalDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/05/2020");

        List<String> expList = new ArrayList<>();

        List<String> list = longerInstance.getPositionalMessages(initialDate, finalDate);

        assertEquals(expList, list);
    }

    /**
     * US7 - Test to ensure getArrivalDistance() works properly.
     */
    @Test
    void getArrivalDistance() {
        Double expRes = 10350.0;

        Ship s1 = new ShipSortMmsi(positionsList.get(0), mmsiCodes[0], vesselNames[0], imoCodes[0], callSigns[0], 70, 294,32,13.6,"79");
        Ship s2 = new ShipSortMmsi(positionsList.get(1), mmsiCodes[1], vesselNames[1], imoCodes[1], callSigns[1], 70, 294,32,13.6,"79");

        PositionsBST positionsBST = s1.getPositionsBST();
        PositionsBST positionsBST2 = s2.getPositionsBST();

        Double res = (double) Math.round(positionsBST.getArrivalDistance(positionsBST2));

        assertEquals(expRes, res);
    }

    /**
     * US7 - Test to ensure getDepartureDistance() works properly.
     */
    @Test
    void getDepartureDistance() {
        Double expRes = 13510.0;

        Ship s1 = new ShipSortMmsi(positionsList.get(0), mmsiCodes[0], vesselNames[0], imoCodes[0], callSigns[0], 70, 294,32,13.6,"79");
        Ship s2 = new ShipSortMmsi(positionsList.get(1), mmsiCodes[1], vesselNames[1], imoCodes[1], callSigns[1], 70, 294,32,13.6,"79");

        PositionsBST positionsBST = s1.getPositionsBST();
        PositionsBST positionsBST2 = s2.getPositionsBST();

        Double res = (double) Math.round(positionsBST.getDepartureDistance(positionsBST2));

        assertEquals(expRes, res);
    }

    /**
     * Ensure getPosInDateTime() works correctly
     */
    @Test
    public void getPosInDateTime() {
        List<Double> exp = new LinkedList<>();
        exp.add(p1.getLat());
        exp.add(p1.getLon());

        List<Double> actual = positionsBST1.getPosInDateTime(d1[0]);

        assertEquals(exp, actual);
    }

}