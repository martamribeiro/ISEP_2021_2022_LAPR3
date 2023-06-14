package lapr.project.domain.model;

import java.util.List;
import java.util.Objects;

public class Country {
    /**
     * The continent of the country
     */
    private final String continent;
    /**
     * The name of the country
     */
    private final String name;
    /**
     * The capital of the country
     */
    private final Capital capital;
    /**
     * The borders of the country
     */
    private final List<String> borders;

    /**
     * Constructs an instance of Country receiving continent, alpha2code, alpha3code,
     * name, population, capital, latitude, longitude
     *
     * @param continent the continent
     * @param name the name
     * @param capital the capital
     */
    public Country(String continent,
                   String name,
                   Capital capital,
                   List<String> borders) {
        checkContinent(continent);
        checkName(name);
        checkCapital(capital);
        this.continent = continent;
        this.name = name;
        this.capital = capital;
        this.borders = borders;
    }

    /**
     * Checks if the Continent of the Country is correct, and if not throws an error message.
     * @param continent the continent
     */
    public void checkContinent(String continent){
        if(Objects.isNull(continent)){
            throw new IllegalArgumentException("Continent name cannot be null.");
        }
    }

    /**
     * Checks if the name of the Country is correct, and if not throws an error message.
     * @param name the name
     */
    public void checkName(String name){
        if(Objects.isNull(name)){
            throw new IllegalArgumentException("Name cannot be null.");
        }
    }

    /**
     * Checks if the the Capital is correct, and if not throws an error message.
     * @param capital capital
     */
    public void checkCapital(Capital capital){
        if(Objects.isNull(capital)){
            throw new IllegalArgumentException("Capital cannot be null.");
        }
    }

    /**
     * Checks if the borders of the country is correct, and if not throws an error message.
     * @param borders
     */
    private void checkBorders(List<String> borders){
        if(borders.isEmpty()){
            throw new IllegalArgumentException("A country must have borders.");
        }
    }

    public Capital getCapital() {
        return capital;
    }

    public String getName() {
        return name;
    }

    public List<String> getBorders() {
        return borders;
    }

    @Override
    public String toString() {
        return "Country{" +
                "continent='" + continent + '\'' +
                ", name='" + name + '\'' +
                ", capital=" + capital +
                ", borders=" + borders +
                '}';
    }
}
