package lapr.project.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ShipPositionTest {

    Company company;
    ShipPosition s1, s2, s3, s4;
    private Date dateR1, dateR3, dateR4;

    @BeforeEach
    public void setUp() throws ParseException {
        company = new Company("cargo shipping company");
        dateR1 = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2020");
        dateR3 = new SimpleDateFormat("dd/MM/yyyy").parse("29/12/2020");
        dateR4 = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2022");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 4);
        Date d1 = cal.getTime();
        int mmsi = 123456789;
        double lat = -30.033056;
        double lon = -51.230000;
        double sog = 25.4;
        double cog = 341.2;
        int heading = 300;
        String transcieverClass = "AIS";
        s1 = new ShipPosition(mmsi, d1, lat, lon, sog, cog, heading, transcieverClass);
        s2 = new ShipPosition(mmsi, d1, lat, lon, sog, cog, heading, transcieverClass);
        s3 = new ShipPosition(mmsi, dateR3, lat, lon, sog, cog, heading, transcieverClass);
        s4 = new ShipPosition(mmsi, dateR4, lat, lon, sog, cog, heading, transcieverClass);
    }

    /**
     * Tests of getters operations
     **/
    @Test
    public void getBaseDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 4);
        Date d1 = cal.getTime();
        assertEquals( d1.toString(), s1.getBaseDateTime().toString(), "date should be equal");
    }

    /**
     * Test to ensure that getLatitude works correctly.
     */
    @Test
    public void getLat() {
        double lat = -30.033056;
        assertEquals(lat, s1.getLat(), 2);
    }

    /**
     * Test to ensure that getLongitude works correctly.
     */
    @Test
    public void getLon() {
        double lon = -51.230000;
        assertEquals(lon, s1.getLon(), 2);
    }

    /**
     * Test to ensure that getSog works correctly.
     */
    @Test
    public void getSog() {
        double sog = 25.4;
        assertEquals(sog, s1.getSog(), 2);
    }

    /**
     * Test to ensure that getCog works correctly.
     */
    @Test
    public void getCog() {
        double cog = 341.2;
        assertEquals(cog, s1.getCog(), 2);
    }

    /**
     * Test to ensure Base Date Time cannot be null.
     */
    @Test
    public void ensureNullDateNotAllowed() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, null, 36.39094,
                -122.71335, 19.7, 145.5, 147, "B"));
        assertEquals("Base Date Time cannot be null.", thrown.getMessage());
    }

    /**
     * Test to ensure Transciever Class cannot be null.
     */
    @Test
    public void ensureNullTranscieverClassNotAllowed() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, 147, null));
        assertEquals("Transciever Class cannot be null.", thrown.getMessage());
    }

    /**
     * Test to ensure Transciever Class cannot be empty.
     */
    @Test
    public void ensureTranscieverClassNotEmpty() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, 147, ""));
        assertEquals("Transciever Class cannot be blank.", thrown.getMessage());
    }

    /**
     * Test to ensure Latitude cannot be under -90.
     */
    @Test
    public void createPositionWithLatitudeUnderMinus90() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, -91,
                -122.71335, 19.7, 145.5, 147, "B"));
        assertEquals("Latitude must be between -90 and 90. It might also be 91 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * Test to ensure latitude cannot de over 91.
     */
    @Test
    public void createPositionWithLatitudeOver91() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 92,
                -122.71335, 19.7, 145.5, 147, "B"));
        assertEquals("Latitude must be between -90 and 90. It might also be 91 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * Test to ensure latitude can have value 90.
     */
    @Test
    public void checkCreatePositionWithLatitude90() {
      new ShipPosition(211331640, dateR1, 90,
                -122.71335, 19.7, 145.5, 147, "B");
    }

    /**
     * Test to ensure Latitude can have value -90.
     */
    @Test
    public void checkCreatePositionWithLatitudeMinus90() {
       new ShipPosition(211331640, dateR1, -90,
                -122.71335, 19.7, 145.5, 147, "B");
    }

    /**
     * Test to ensure Longitude cannot be under -180.
     */
    @Test
    public void createPositionWithLongitudeUnderMinus180() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                -181, 19.7, 145.5, 147, "B"));
        assertEquals("Longitude must be between -180 and 180. It might also be 181 in case of being unavailable.", thrown.getMessage());

    }

    /**
     * Test to ensure longitude cannot be over 181.
     */
    @Test
    public void createPositionWithLongitudeOver181() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                182, 19.7, 145.5, 147, "B"));
        assertEquals("Longitude must be between -180 and 180. It might also be 181 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * Test to ensure longitude can have value 180.
     */
    @Test
    public void createPositionWithLongitude180() {
        new ShipPosition(211331640, dateR1, 36.39094,
                180, 19.7, 145.5, 147, "B");
    }

    /**
     * Test to ensure longitude can have value -180.
     */
    @Test
    public void createPositionWithLongitudeminus180() {
        new ShipPosition(211331640, dateR1, 36.39094,
                -180, 19.7, 145.5, 147, "B");
    }

    /**
     * Test to ensure sog cannot be under 0.
     */
    @Test
    public void createPositionWithSogUnder0() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, -2, 0, 147, "B"));
        assertEquals("SOG must be positive.", thrown.getMessage());
    }

    /**
     * Test to ensure SOG can have value 0.
     */
    @Test
    public void createPositionWithSog0() {
        new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 0, 0, 147, "B");
    }

    /**
     * Test to ensure cog cannot be under 0.
     */
    @Test
    public void createPositionWithCOGUnder0() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, -1, 147, "B"));
        assertEquals("COG must be between 0 and 359.", thrown.getMessage());
    }

    /**
     * Test to ensure Heading cannot be over 359.
     */
    @Test
    public void createPositionWithCOGOver359() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 360, 147, "B"));
        assertEquals("COG must be between 0 and 359.", thrown.getMessage());
    }

    /**
     * Test to ensure COG can have value 359.
     */
    @Test
    public void createPositionWithCOGOequals359() {
        new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 359, 147, "B");
    }

    /**
     * Test to ensure Heading cannot be under 0.
     */
    @Test
    public void createPositionWithHeadingUnder0() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, -1, "B"));
        assertEquals("Heading must be between 0 and 359. It might also be 511 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * Test to ensure Heading cannot be over 511.
     */
    @Test
    public void createPositionWithHeadingOver511() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, 512, "B"));
        assertEquals("Heading must be between 0 and 359. It might also be 511 in case of being unavailable.", thrown.getMessage());
    }

    /**
     * Test to ensure Heading can have value 0.
     */
    @Test
    public void createPositionWithHeadingequals0() {
         new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, 0, "B");
    }

    /**
     * Test to ensure heading can have value 359.
     */
    @Test
    public void createPositionWithHeadingequals359() {
        new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, 359, "B");
    }

    /**
     * Test to ensure MMSI has 9 digits.
     */
    @Test
    public void ensureMMSIHas9Digits() {
        ShipPosition shipPosition = new ShipPosition(211331640, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, 147, "B");
        assertEquals(9, String.valueOf(shipPosition.getMMSI()).length());
    }

    /**
     * Test to ensure MMSI cannot have more than 9 digits.
     */
    @Test
    public void checkMoreThen9DigitsMmsiException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(2113316402, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, 147, "B"));
        assertEquals("MMSI must hold 9 digits.", thrown.getMessage());
    }

    /**
     * Test to ensure MMSI cannot have less than 9 digits.
     */
    @Test
    public void checkLessThen9DigitsMmsiException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ShipPosition(2113316, dateR1, 36.39094,
                -122.71335, 19.7, 145.5, 147, "B"));
        assertEquals("MMSI must hold 9 digits.", thrown.getMessage());
    }

    /**
     * Test to ensure compareTo() is functioning correctly.
     *      Situation 1: the dates are equal
     *
     * For demonstration purposes the Arrange/Act/Assert syntax is used:
     * Arranje: two ships (s1 and s2) with the same date (done in @Before)
     * Act: s1 is compared to s2 using compareTo()
     * Assert: the result should be zero.
     */
   @Test
    public void compareToEqualDates() {
        int expRes = 0;
        int res = s1.compareTo(s2);
        assertEquals(expRes, res);
    }

    /**
     * Test to ensure compareTo() is functioning correctly.
     *      Situation 2: date 1 > date 2
     *
     * For demonstration purposes the Arrange/Act/Assert syntax is used:
     * Arranje: one ship (s1) with a date greater than other (s2) (done in @Before)
     * Act: s1 is compared to s2 using compareTo()
     * Assert: the result should be one.
     */
    @Test
    public void compareToD1GreaterD2() {
        int expRes = 1;
        int res = s1.compareTo(s3);
        assertEquals(expRes, res);
    }

    /**
     * Test to ensure compareTo() is functioning correctly.
     *      Situation 3: date 1 < date 2
     *
     * For demonstration purposes the Arrange/Act/Assert syntax is used:
     * Arranje: one ship (s1) with a mmsi less than other (s2) (done in @Before)
     * Act: s1 is compared to s2 using compareTo()
     * Assert: the result should be minus one.
     */
    @Test
    public void compareToD1LessD2() {
        int expRes = -1;
        int res = s1.compareTo(s4);
        assertEquals(expRes, res);
    }

    /**
     * Test to ensure that a shipPosition is equal to the same object.
     */
    @Test
    public void testEqualsSameObject(){
        assertEquals(s1, s1);
    }

    /**
     * Test to ensure that a shipPosition is equal to another one
     * with the same attributes.
     */
    @Test
    public void testEqualsSameAtts(){
        ShipPosition shipPos = new ShipPosition(s1.getMMSI(), s1.getBaseDateTime(), s1.getLat(), s1.getLon(), s1.getSog(), s1.getCog(), s1.getHeading(), s1.getTranscieverClass());
        assertEquals(s1, shipPos);
    }

    /**
     * Test to ensure that a shipPosition is not equal to another one,
     * refering to a different ship.
     */
    @Test
    public void testNotEqualsSameClass(){
        ShipPosition shipPos = new ShipPosition(123457779, s1.getBaseDateTime(), s1.getLat(), s1.getLon(), s1.getSog(), s1.getCog(), s1.getHeading(), s1.getTranscieverClass());
        assertNotEquals(s1, shipPos);
    }

    /**
     * Test to ensure that a shipPosition is not equal to an object of a different class.
     */
    @Test
    public void testNotEqualsDiffClass(){
        String notShipPos = "not a ship pos";
        assertNotEquals(s1, notShipPos);
    }

    /**
     * Test to ensure that a shipPosition is not equal to a null one.
     */
    @Test
    public void testNotEqualsNull(){
        assertNotEquals(s1, null);
    }
}