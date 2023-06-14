package lapr.project.domain.model;

import java.util.Map;
import java.util.Objects;

public class Port extends Location implements Comparable<Port> {
    /**
     * identification of the Port
     */
    private final int identification;

    /**
     * name of the Port
     */
    private final String name;


    /**
     * The reachable ports and the seadistances
     */
    private Map<Integer, Double> toPortsDistance;

    /**
     * constructs an instance of Port, receiving as parameter identification, name, continent, country, latitude and longitude
     * @param identification Port's Identification
     * @param name Port's Name
     * @param continent Port's Continent
     * @param country Port's Country
     * @param lat Port's Latitude
     * @param lon Port's Longitude
     */
    public Port(int identification, String name, String continent, String country, double lat, double lon) {
        super(lat, lon, country, continent);
        checkIdentification(identification);
        checkPortName(name);
        checkContinentName(continent);
        this.identification=identification;
        this.name=name;
    }

    /**
     * constructs an instance of Port, receiving as parameter identification, name, continent, country, latitude and longitude
     * @param identification Port's Identification
     * @param name Port's Name
     * @param continent Port's Continent
     * @param country Port's Country
     * @param lat Port's Latitude
     * @param lon Port's Longitude
     * @param toPortsDistance the reachable ports and the seadistances
     */
    public Port(int identification, String name, String continent, String country, double lat, double lon, Map<Integer, Double> toPortsDistance) {
        super(lat, lon, country, continent);
        checkIdentification(identification);
        checkPortName(name);
        checkContinentName(continent);
        this.identification=identification;
        this.name=name;
        this.toPortsDistance = toPortsDistance;
    }

    /**
     * Checks if the Port's Identification is correct, and if not throws an error message
     * @param identification Port's Identification
     */
    public void checkIdentification(int identification) {
        if (identification <=0) {
            throw new IllegalArgumentException("Port identification as to be above 0.");
        }
    }

    /**
     * Checks if the Port's Name is correct, and if not throws an error message.
     * @param portName the Port's Name.
     */
    public void checkPortName(String portName){
        if(Objects.isNull(portName)){
            throw new IllegalArgumentException("Port name cannot be null.");
        }
    }

    /**
     * Checks if the Port's Continent Name is correct, and if not throws an error message.
     * @param continentName the Port's Continent Name.
     */
    public void checkContinentName(String continentName){
        if(Objects.isNull(continentName)){
            throw new IllegalArgumentException("Continent name cannot be null.");
        }
    }

    /**
     * Checks if the Port's Country Name is correct, and if not throws an error message.
     * @param countryName the Port's  Country Name.
     */
    public void checkCountryName(String countryName){
        if(Objects.isNull(countryName)){
            throw new IllegalArgumentException("Country name cannot be null.");
        }
    }
    
    /**
     * returns the Port's Identification
     * @return Port's Identification
     */
    public int getIdentification(){
        return this.identification;
    }

    /**
     * returns the Port's Name
     * @return Port's Name
     */
    public String getName(){
        return this.name;
    }


    public Map<Integer, Double> getToPortsDistance() {
        return toPortsDistance;
    }

    @Override
    public String toString() {
        return "Port{" +
                "identification=" + identification +
                ", name='" + name + '\'' +
                ", toPortsDistance=" + toPortsDistance +
                '}'+
                "\n"
                ;
    }

    /**
     * Method equals.
     * @param otherObject the object to be compared with.
     * @return true if a Port is equal to the object in "otherObject";
     * false if a Port isn't equal to the object in "otherObject".
     */
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


    /**
     * Method compareTo.
     * @param o the Port to be compared with.
     * @return the difference between the two Port's Identification.
     */
    @Override
    public int compareTo(Port o) {
        return this.identification - o.identification;
    }
}
