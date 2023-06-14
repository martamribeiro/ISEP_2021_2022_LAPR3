package lapr.project.dto;

import lapr.project.domain.model.Port;

public class PortFileDTO {
    /**
     * identification of the Port
     */
    private final int identification;

    /**
     * name of the Port
     */
    private final String name;

    /**
     * continent where Port is located
     */
    private final String continent;

    /**
     * country where the Port is located
     */
    private final String country;

    /**
     * latitude of Port's location
     */
    private final double lat;

    /**
     * longitude of Port's location
     */
    private final double lon;

    /**
     * constructs an instance of PortFileDTO, receiving as parameter identification, name, continent, country, latitude and longitude
     * @param identification PortFileDTO's Identification
     * @param name PortFileDTO's Name
     * @param continent PortFileDTO's Continent
     * @param country PortFileDTO's Country
     * @param lat PortFileDTO's Latitude
     * @param lon PortFileDTO's Longitude
     */
    public PortFileDTO(int identification, String name, String continent, String country, double lat, double lon) {
        this.identification=identification;
        this.name=name;
        this.continent=continent;
        this.country=country;
        this.lat = lat;
        this.lon=lon;
    }

    public int getIdentificationDto(){
        return this.identification;
    }

    public String getNameDto(){
        return this.name;
    }

    public String getContinentDto(){
        return this.continent;
    }

    public String getCountryDto(){
        return this.country;
    }

    public double getLatDto(){
        return this.lat;
    }

    public double getLonDto(){
        return this.lon;
    }

    @Override
    public boolean equals(Object otherObject){
        if(this == otherObject)
            return true;

        if(otherObject == null || this.getClass() != otherObject.getClass())
            return false;

        Port otherShip = (Port) otherObject;

        if(this.identification == otherShip.getIdentification())
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "PortFileDTO{" +
                "Identification=" + identification +
                ", Name=" + name +
                ", Continent=" + continent +
                ", Country='" + country + '\'' +
                ", Latitude='" + lat + '\'' +
                ", Longitude='" + lon +
                '}';
    }
}
