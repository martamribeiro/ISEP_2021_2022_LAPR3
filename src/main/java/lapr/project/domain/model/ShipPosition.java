package lapr.project.domain.model;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Class to instantiate a new ShipPosition
 *
 * @author Marta Ribeiro (1201592)
 */
public class ShipPosition implements Comparable<ShipPosition> {

    /**
     * The MMSI of the Ship to which the ShipPosition refers to.
     */
    private int MMSI;

    /**
     * The ShipPosition's base date time.
     */
    private Date baseDateTime;

    /**
     * The ShipPosition's latitude.
     */
    private double lat;

    /**
     * The ShipPosition's longitude.
     */
    private double lon;

    /**
     * The ShipPosition's sog.
     */
    private double sog;

    /**
     * The ShipPosition's cog.
     */
    private double cog;

    /**
     * The ShipPosition's heading.
     */
    private int heading;

    /**
     * The ShipPosition's transciever class.
     */
    private String transcieverClass;

    /**
     * Constructs an instance of ShipPosition receiving as a parameter the Ship's MMSI and the ShipPosition's base date time, latitude, longitude, SOG, COG, heading and transciever class.
     * @param mmsi the Ship's MMSI
     * @param baseDateTime the ShipPosition's base date time
     * @param lat the ShipPosition's latitude
     * @param lon the ShipPosition's longitude
     * @param sog the ShipPosition's SOG
     * @param cog the ShipPosition's COG
     * @param heading the ShipPosition's heading
     * @param transcieverClass the ShipPosition's transciever class
     */
    public ShipPosition(int mmsi, Date baseDateTime, double lat, double lon, double sog, double cog, int heading, String transcieverClass){
        checkBaseDateTime(baseDateTime);
        checkLat(lat);
        checkLon(lon);
        checkSog(sog);
        checkCog(cog);
        checkHeading(heading);
        checkTranscieverClass(transcieverClass);
        checkMMSI(mmsi);
        this.MMSI = mmsi;
        this.baseDateTime=new Date(baseDateTime.getTime());
        this.lat=lat;
        this.lon=lon;
        this.sog=sog;
        this.cog=cog;
        this.heading=heading;
        this.transcieverClass=transcieverClass;
    }

    public ShipPosition(Date newDate) {
        this.baseDateTime=new Date(newDate.getTime());
    }

    /**
     * Returns the ShipPosition's base date time.
     * @return the ShipPosition's base date time.
     */
    public Date getBaseDateTime() {
        return new Date(baseDateTime.getTime());
    }

    /**
     * Returns the MMSI of the Ship to which the ShipPosition refers to.
     * @return the MMSI of the Ship to which the ShipPosition refers to.
     */
    public int getMMSI() {
        return MMSI;
    }

    /**
     * Returns the ShipPosition's latitude.
     * @return the ShipPosition's latitude.
     */
    public double getLat() {
        return lat;
    }

    /**
     * Returns the ShipPosition's longitude.
     * @return the ShipPosition's longitude.
     */
    public double getLon() {
        return lon;
    }

    /**
     * Returns the ShipPosition's sog.
     * @return the ShipPosition's sog.
     */
    public double getSog() {
        return sog;
    }

    /**
     * Returns the ShipPosition's cog.
     * @return the ShipPosition's cog.
     */
    public double getCog() {
        return cog;
    }

    /**
     * Returns the ShipPosition's heading.
     * @return the ShipPosition's heading.
     */
    public int getHeading() {
        return heading;
    }

    /**
     * Returns the ShipPosition's transciever class.
     * @return the ShipPosition's transciever class.
     */
    public String getTranscieverClass() {
        return transcieverClass;
    }

    private void checkMMSI(int mmsi){
        if (Integer.toString(mmsi).length()!=9)
            throw new IllegalArgumentException("MMSI must hold 9 digits.");
    }

    /**
     * Checks if the ShipPosition's base date time is correct, and if not throws an error message.
     * @param baseDateTime the ShipPosition's base date time.
     */
    private void checkBaseDateTime(Date baseDateTime){
        if (baseDateTime==null){
            throw new IllegalArgumentException("Base Date Time cannot be null.");
        }
    }

    /**
     * Checks if the ShipPosition's latitude is correct, and if not throws an error message.
     * @param lat the ShipPosition's latitude.
     */
    private void checkLat(double lat){
        if (lat<-90 || (lat>90 && lat!=91)){
            throw new IllegalArgumentException("Latitude must be between -90 and 90. It might also be 91 in case of being unavailable.");
        }
    }

    /**
     * Checks if the ShipPosition's longitude is correct, and if not throws an error message.
     * @param lon the ShipPosition's longitude.
     */
    private void checkLon(double lon){
        if (lon<-180 || (lon>180 && lon!=181)){
            throw new IllegalArgumentException("Longitude must be between -180 and 180. It might also be 181 in case of being unavailable.");
        }
    }

    /**
     * Checks if the ShipPosition's SOG is correct, and if not throws an error message.
     * @param sog the ShipPosition's SOG.
     */
    private void checkSog(double sog){
        if (sog<0){
            throw new IllegalArgumentException("SOG must be positive.");
        }
    }

    /**
     * Checks if the ShipPosition's COG is correct, and if not throws an error message.
     * @param cog the ShipPosition's COG.
     */
    private void checkCog(double cog){
        if (cog<0 || cog>359){
            throw new IllegalArgumentException("COG must be between 0 and 359.");
        }
    }

    /**
     * Checks if the ShipPosition's heading is correct, and if not throws an error message.
     * @param heading the ShipPosition's heading.
     */
    private void checkHeading(int heading){
        if (heading<0 || (heading>359 && heading!=511)){
            throw new IllegalArgumentException("Heading must be between 0 and 359. It might also be 511 in case of being unavailable.");
        }
    }

    /**
     * Checks if the ShipPosition's transciever class is correct, and if not throws an error message.
     * @param transcieverClass the ShipPosition's transciever class.
     */
    private void checkTranscieverClass(String transcieverClass){
        if(Objects.isNull(transcieverClass)){
            throw new IllegalArgumentException("Transciever Class cannot be null.");
        }
        if (StringUtils.isBlank(transcieverClass))
            throw new IllegalArgumentException("Transciever Class cannot be blank.");
    }

    public void setBaseDateTime(Date newDate) {
        this.baseDateTime = newDate;
    }

    /**
     * Method toString.
     * @return a String with the ShipPosition attributes and its values.
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return String.format(">> SHIP POSITION%nBase Date Time: %s%n" +
                "Latitude: %f%nLongitude: %f%nSpeed Over Ground (SOG): %f%nCourse Over Ground (COG): %f%n" +
                "Transciever Class: %s%n%n", sdf.format(baseDateTime), lat, lon, sog, cog, heading, transcieverClass);
    }

    /**
     * Method equals.
     * @param otherObject the object to be compared with.
     * @return true if a ShipPosition is equal to the object in "otherObject";
     * false if a ShipPosition isn't equal to the object in "otherObject".
     */
    @Override
    public boolean equals(Object otherObject){
        if(this == otherObject)
            return true;

        if(otherObject == null || this.getClass() != otherObject.getClass())
            return false;

        ShipPosition otherShipPosition = (ShipPosition) otherObject;

        return baseDateTime.equals(otherShipPosition.baseDateTime) &&
                MMSI == otherShipPosition.MMSI &&
                lat == otherShipPosition.lat &&
                lon == otherShipPosition.lon &&
                sog == otherShipPosition.sog &&
                cog == otherShipPosition.cog &&
                heading == otherShipPosition.heading &&
                transcieverClass.equals(otherShipPosition.transcieverClass);
    }

    /**
     * Method compareTo.
     * @param o the ShipPosition to be compared with.
     * @return 0 if a ShipPosition's baseDateTime is equal to the baseDateTime of the ShipPosition to be compared with;
     * less than 0 if a ShipPosition's baseDateTime is before the baseDateTime of the ShipPosition to be compared with;
     * more than 0 if a ShipPosition's baseDateTime is after the baseDateTime of the ShipPosition to be compared with;
     */
    @Override
    public int compareTo(ShipPosition o) {
        return baseDateTime.compareTo(o.getBaseDateTime());
    }

}
