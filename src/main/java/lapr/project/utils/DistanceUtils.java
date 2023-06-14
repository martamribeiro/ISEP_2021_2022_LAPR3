package lapr.project.utils;

import lapr.project.domain.shared.Constants;

import java.util.Objects;

public class DistanceUtils {

    /**
     * This uses the ‘haversine’ formula to calculate the great-circle distance between two points – that is, the shortest distance over the earth’s surface – giving an ‘as-the-crow-flies’
     * distance between the points (ignoring any hills they fly over, of course!).
     *
     * Reference: http://www.movable-type.co.uk/scripts/latlong.html
     * @param lat1 latitude of the first point
     * @param lat2 latitude of the second point
     * @param lon1 longitude of the first point
     * @param lon2 longitude of the second point
     * @return the distance in kilometers of the two given points
     */
    public static Double distanceBetweenInKm(Double lat1, Double lat2, Double lon1, Double lon2){
        if(Objects.equals(lat1, lat2) && Objects.equals(lon1, lon2)){
            return 0.0;
        }
        if(lat1 == null || lat2 == null || lon1 == null || lon2 == null){
            throw new IllegalArgumentException("cannot calculate distance with a null value of latitude and/or longitude");
        }
        if(lat1 == 91 || lat2 == 91 || lon1 == 181 || lon2 == 181){
            throw new IllegalArgumentException("Latitude and/or longitude not available");
        }
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2-lat1);
        double deltaLon = Math.toRadians(lon2-lon1);

        double a = (Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)) +
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2) *
                        Math.cos(lat1Rad) * Math.cos(lat2Rad);
        Double c = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));

        return (Constants.RADIUS_OF_EARTH_IN_METERS * c)/1000;
    }
}
