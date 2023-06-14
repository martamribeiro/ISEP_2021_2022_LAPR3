package lapr.project.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PortTest {

    private final int[] identifications = {123456789, 987654321, 369258147, 741852963};

    private final String [] names = {"Port1", "Port2", "Port3", "Port4"};

    private String continent1, continent2;

    private String country1, country2, country3;

    private final double [] lats = {-30.033056, -42.033006, -55.022056, 23.008721};
    private final double [] lons = {-51.230000, -47.223056, -46.233056, 24.092123};

    private Port port1, port2, port3, port4;

    @BeforeEach
    public void setUp() {
        continent1 = "Asia";
        continent2 = "Europe";

        country1 = "China";
        country2 = "Portugal";
        country3 = "India";

        port1 = new Port(identifications[0], names[0], continent1, country1, lats[0], lons[0]);
        port2 = new Port(identifications[0], names[1], continent1, country3, lats[1], lons[1]);
        port3 = new Port(identifications[2], names[2], continent2, country2, lats[2], lons[2]);
        port4 = new Port(identifications[3], names[3], continent1, country1, lats[3], lons[3]);

    }

    /**
     * Test to ensure identification can't be negative
     */
    @Test
    public void ensureNegativeIdentificationNotAllowed() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Port(-1, names[0], continent1, country1, lats[0], lons[0]));
        assertEquals("Port identification as to be above 0.", thrown.getMessage());
    }

    /**
     * Test to ensure identification can't be null
     */
    @Test
    public void ensureNullIdentificationNotAllowed() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Port(0, names[0], continent1, country1, lats[0], lons[0]));
        assertEquals("Port identification as to be above 0.", thrown.getMessage());
    }

    /**
     * Test to ensure Port Name cannot be null.
     */
    @Test
    public void ensureNullPortNameNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], null, continent1, country1, lats[0], lons[0]));
        assertEquals("Port name cannot be null.", thrown.getMessage());

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], null, continent1, country1, lats[0], lons[0], new HashMap<>()));
        assertEquals("Port name cannot be null.", thrown2.getMessage());
    }

    /**
     * Test to ensure Port's Continent Name cannot be null.
     */
    @Test
    public void ensureNullContinentNameNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], names[0], null, country1, lats[0], lons[0]));
        assertEquals("Continent name cannot be null.", thrown.getMessage());

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], names[0], null, country1, lats[0], lons[0], new HashMap<>()));
        assertEquals("Continent name cannot be null.", thrown2.getMessage());

    }

    /**
     * Test to ensure Port's Country Name cannot be null.
     */
    @Test
    public void ensureNullCountryNameNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], names[0], continent1, null, lats[0], lons[0]));
        assertEquals("Country name cannot be null.", thrown.getMessage());

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], names[0], continent1, null, lats[0], lons[0], new HashMap<>()));
        assertEquals("Country name cannot be null.", thrown2.getMessage());
    }

    /**
     * Test to ensure Latitude cannot be under -90.
     */
    @Test
    public void createPortWithLatitudeUnderMinus90() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new  Port(identifications[0], names[0], continent1, country1, -120.32, lons[0]));
        assertEquals("Latitude must be between -90 and 90. It might also be 91 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * Test to ensure latitude cannot de over 91.
     */
    @Test
    public void createPortWithLatitudeOver91() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], names[0], continent1, country1, 120.32, lons[0]));
        assertEquals("Latitude must be between -90 and 90. It might also be 91 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * Test to ensure latitude can have value 90.
     */
    @Test
    public void checkCreatePortWithLatitude90() {
        new Port(identifications[0], names[0], continent1, country1, 90, lons[0]);
    }

    /**
     * Test to ensure Latitude can have value -90.
     */
    @Test
    public void checkCreatePortWithLatitudeMinus90() {
        new Port(identifications[0], names[0], continent1, country1, -90, lons[0]);
    }

    /**
     * Test to ensure Longitude cannot be under -180.
     */
    @Test
    public void createPortWithLongitudeUnderMinus180() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], names[0], continent1, country1, lats[0], -181));
        assertEquals("Longitude must be between -180 and 180. It might also be 181 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * Test to ensure longitude cannot be over 181.
     */
    @Test
    public void createPortWithLongitudeOver181() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Port(identifications[0], names[0], continent1, country1, lats[0], 182));
        assertEquals("Longitude must be between -180 and 180. It might also be 181 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * test to ensure longitude can be 180
     */
    @Test
    public void createPortWithLongitude180() {
        new Port(identifications[0], names[0], continent1, country1, lats[0], 180);
    }

    /**
     * test to ensure longitude can be -180
     */
    @Test
    public void createPortWithLongitudeMinus180() {
        new Port(identifications[0], names[0], continent1, country1, lats[0], -180);
    }

    /**
     * ensure getIdentification() works correctly by returning the correct identification of a Port
     */
    @Test
    public void testGetIdentification() {
        Assertions.assertEquals(port1.getIdentification(), 123456789);
    }

    /**
     * ensure getName() works correctly by returning the correct name of a Port
     */
    @Test
    public void testGetName() {
        Assertions.assertEquals(port1.getName(), "Port1");
    }

    /**
     * ensure getContinent() works correctly by returning the correct continent of a Port
     */
    @Test
    public void testGetContinent() {
        Assertions.assertEquals(port1.getContinent(), "Asia");
    }

    /**
     * ensure getCountry() works correctly by returning the correct country of a Port
     */
    @Test
    public void testGetCountry() {
        Assertions.assertEquals(port1.getCountry(), "China");
    }

    /**
     * ensure getLatitude() works correctly by returning the correct latitude of a Port
     */
    @Test
    public void testGetLatitude() {
        Assertions.assertEquals(port1.getLatitude(), -30.033056);
    }

    /**
     * ensure getLongitude() works correctly by returning the correct longitude of a Port
     */
    @Test
    public void testGetLongitude() {
        Assertions.assertEquals(port1.getLongitude(), -51.230000);
    }

    /**
     * Test to ensure compareTo() is functioning correctly.
     *      Situation 1: the identification codes are equal
     *
     * For demonstration purposes the Arrange/Act/Assert syntax is used:
     * Arranje: two ports (port1 and port2) with the same identification (done in @Before)
     * Act: port1 is compared to port2 using compareTo()
     * Assert: the result should be zero.
     */
    @Test
    public void compareToEqualIdentification() {
        int expRes = 0;
        int res = port1.compareTo(port2);
        assertEquals(expRes, res);
    }

    /**
     * Test to ensure compareTo() is functioning correctly.
     *      Situation 2: identification 1 > identification 2
     *
     * For demonstration purposes the Arrange/Act/Assert syntax is used:
     * Arranje: one port (port1) with a identification greater than other (port2) (done in @Before)
     * Act: port1 is compared to port2 using compareTo()
     * Assert: the result should be true.
     */
    @Test
    public void compareToIdentification1GreaterIdentification2() {
        boolean expRes = true;
        boolean res=false;
        if (port3.compareTo(port1) > 0) res=true;
        assertEquals(expRes, res);
    }

    /**
     * Test to ensure compareTo() is functioning correctly.
     *      Situation 2: identification 1 < identification 2
     *
     * For demonstration purposes the Arrange/Act/Assert syntax is used:
     * Arranje: one port (port1) with a identification smaller than other (port2) (done in @Before)
     * Act: port1 is compared to port2 using compareTo()
     * Assert: the result should be true.
     */
    @Test
    public void compareToIdentification1SmallerIdentification2() {
        boolean expRes = true;
        boolean res=false;
        if (port1.compareTo(port3) < 0) res=true;
        assertEquals(expRes, res);
    }

    /**
     * Test to ensure that the equals method works for different Objects.
     */
    @Test
    public void testNotEqualsObject(){
        String notShipObj =  "abc";
        assertNotEquals(port1, notShipObj);

    }

    /**
     * test to ensure Equals works for null objects
     */
    @Test
    public void testNotEqualsNull() {
        assertNotEquals(port1, null);
    }

    /**
     * test to ensure Equals works with different objects from the same class
     */
    @Test
    public void testNotEqualsSameClass() {
        assertNotEquals(port1, port3);
    }

    /**
     * test to ensure that the equals method works for same objects.
     */
    @Test
    public void testEqualsSameObject() {
        assertEquals(port1, port1);
    }

    /**
     * test to ensure that the equals method works for objects with same attributes
     */
    @Test
    public void testEqualsSameAtts() {
        Port port = new Port(identifications[0], names[0], continent1, country1, lats[0], lons[0]);
        assertEquals(port1, port);
    }

    @Test
    public void getToPortsDistanceTest(){
        Map<Integer, Double> dists = new HashMap<>();
        dists.put(1234, 123.0);
        Port port = new Port(identifications[0], names[0], continent1, country1, lats[0], lons[0], dists);
        assertEquals(dists, port.getToPortsDistance());
    }


}
