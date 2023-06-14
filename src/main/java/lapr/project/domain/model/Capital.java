package lapr.project.domain.model;

import java.util.Objects;

public class Capital extends Location {
    /**
     * The capital name
     */
    private final String name;

    /**
     * Constructs an instance of Capital receiving the following parameters:
     *
     * @param name name
     * @param latitude latitude
     * @param longitude longitude
     * @param countryName the country name
     */
    public Capital(String name, double latitude, double longitude, String countryName, String continent) {
        super(latitude, longitude, countryName, continent);
        checkName(name);
        this.name = name;
    }

    /**
     * Checks if the name of the Capital is correct, and if not throws an error message.
     * @param name of the capital
     */
    public void checkName(String name){
        if(Objects.isNull(name)){
            throw new IllegalArgumentException("Name cannot be null.");
        }
    }


    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Capital{" +
                "name='" + name + '\'' +
                super.toString()
                ;
    }
}
