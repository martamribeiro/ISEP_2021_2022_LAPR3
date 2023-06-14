package lapr.project.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceUtilsTest {

    @Test
    public void distanceBetweenInKmTest(){
        double lat = 12.2;
        double lat2 = 32;
        double lon = 90;
        double lon2 = 112;
        double dist = 3146; //calculated in reference website: http://www.movable-type.co.uk/scripts/latlong.html

        Double nullLat = null;
        Double invalidLat = 91.0;
        Double invalidLon = 181.0;

        assertEquals(0.0, DistanceUtils.distanceBetweenInKm(lat, lat, lon, lon), "Distance of the same points is zero");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> DistanceUtils.distanceBetweenInKm(nullLat, lat, lon, lon2));
        assertEquals("cannot calculate distance with a null value of latitude and/or longitude", thrown.getMessage(), "Null values should throw exception");

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> DistanceUtils.distanceBetweenInKm(invalidLat, lat, invalidLon, lon2));
        assertEquals("Latitude and/or longitude not available", thrown2.getMessage(), "invalid values should throw exception");

        assertEquals(dist, Math.round(DistanceUtils.distanceBetweenInKm(lat, lat2, lon, lon2)));

    }
}