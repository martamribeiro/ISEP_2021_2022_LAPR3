package lapr.project.dto;

import java.util.Date;

/**
 * Class to instantiate a new PositionDTO
 *
 * @author Marta Ribeiro (1201592)
 */
public class PositionDTO {

    /**
     * The PositionDTO's base date time.
     */
    private final Date baseDateTime;

    /**
     * The PositionDTO's latitude.
     */
    private final double lat;

    /**
     * The PositionDTO's longitude.
     */
    private final double lon;

    /**
     * The PositionDTO's sog.
     */
    private final double sog;

    /**
     * The PositionDTO's cog.
     */
    private final double cog;

    /**
     * The PositionDTO's heading.
     */
    private final int heading;

    /**
     * The PositionDTO's transciever class.
     */
    private final String transcieverClass;

    /**
     * Constructs an instance of PositionDTO receiving as a parameter the PositionDTO's base date time, latitude, longitude, SOG, COG, heading and transciever class.
     * @param baseDateTime the PositionDTO's base date time
     * @param lat the PositionDTO's latitude
     * @param lon the PositionDTO's longitude
     * @param sog the PositionDTO's SOG
     * @param cog the PositionDTO's COG
     * @param heading the PositionDTO's heading
     * @param transcieverClass the PositionDTO's transciever class
     */
    public PositionDTO(Date baseDateTime, double lat, double lon, double sog, double cog, int heading, String transcieverClass){
        this.baseDateTime=baseDateTime;
        this.lat=lat;
        this.lon=lon;
        this.sog=sog;
        this.cog=cog;
        this.heading=heading;
        this.transcieverClass=transcieverClass;
    }

    /**
     * Returns the PositionDTO's base date time.
     * @return the PositionDTO's base date time.
     */
    public Date getBaseDateTime() {
        return baseDateTime;
    }

    public double getLatDto() {
        return lat;
    }

    public double getLonDto() {
        return lon;
    }

    public double getSogDto() {
        return sog;
    }

    public double getCogDto() {
        return cog;
    }

    public int getHeadingDto() {
        return heading;
    }

    public String getTranscieverClassDto() {
        return transcieverClass;
    }

}
