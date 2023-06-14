package lapr.project.domain.model;

import java.util.Objects;

public abstract class Location {
    /**
     * The location latitude.
     */
    private final double latitude;
    /**
     * The location longitude.
     */
    private final double longitude;
    /**
     * The location's country name.
     */
    private final String countryName;

    private final String continent;

    /**
     * Constructs an instance of Location receiving the following parameters:
     *
     * @param latitude latitude
     * @param longitude longitude
     * @param countryName country name
     */
    public Location(double latitude, double longitude, String countryName, String continent) {
        checkLat(latitude);
        checkLon(longitude);
        checkCountryName(countryName);
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryName = countryName;
        this.continent = continent;
    }

    /**
     * Checks if the latitude is correct, and if not throws an error message.
     * @param latitude the Port's latitude.
     */
    private void checkLat(double latitude){
        if (latitude<-90 || (latitude>90 && latitude!=91)){
            throw new IllegalArgumentException("Latitude must be between -90 and 90. It might also be 91 in case of being unavailable.");
        }
    }

    /**
     * Checks if the longitude is correct, and if not throws an error message.
     * @param longitude the Port's longitude.
     */
    private void checkLon(double longitude){
        if (longitude<-180 || (longitude>180 && longitude!=181)){
            throw new IllegalArgumentException("Longitude must be between -180 and 180. It might also be 181 in case of being unavailable.");
        }
    }

    /**
     * Checks if the name of the Country is correct, and if not throws an error message.
     * @param countryName the name
     */
    public void checkCountryName(String countryName){
        if(Objects.isNull(countryName)){
            throw new IllegalArgumentException("Name cannot be null.");
        }
    }

    /**
     * returns the Port's Country
     * @return Port's Country
     */
    public String getCountry(){
        return this.countryName;
    }

    /**
     * returns the Latitude
     * @return the Latitude
     */
    public double getLatitude(){
        return this.latitude;
    }

    /**
     * returns the Longitude
     * @return the Longitude
     */
    public double getLongitude(){
        return this.longitude;
    }


    public String getContinent(){
        return this.continent;
    }

    @Override
    public String toString() {
        return "latitude=" + latitude +
                ", longitude=" + longitude +
                ", countryName='" + countryName + '\'' +
                ", continent='" + continent + '\'' +
                "}\n";
    }
}
