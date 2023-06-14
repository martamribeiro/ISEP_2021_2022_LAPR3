package lapr.project.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    private String continent1, continent2;

    private final String [] alpha2Codes = {"CY", "MT", "GR", "PT"};

    private final String [] alpha3Codes = {"CYP", "MLT", "GRC", "PRT"};

    private String [] names = {"Cyprus", "Malta", "Greece", "Portugal"};

    private final double [] population = {0.85, 0.44, 10.76, 10.31};

    private Capital capital1, capital2, capital3, capital4;

    private final double [] lats = {-30.033056, -42.033006, -55.022056, 23.008721};
    private final double [] lons = {-51.230000, -47.223056, -46.233056, 24.092123};

    private List<String> borders = new ArrayList<>();

    private Country country1, country2, country3, country4;

    @BeforeEach
    public void setUp() {
        continent1 = "America";
        continent2 = "Europe";

        capital1 = new Capital("Nicosia",lats[0], lons[0], names[0], continent2);
        capital2 = new Capital("Valletta",lats[1],lons[1], names[1], continent2);
        capital3 = new Capital("Athens", lats[2], lons[2], names[2], continent2);
        capital4 = new Capital("Lisbon", lats[3], lons[3], names[3], continent2);

        borders.add("Belize");
        borders.add("Canada");

        country1 = new Country(continent2,names[0],capital1, borders);
        country2 = new Country(continent2,names[1],capital1, borders);
        country3 = new Country(continent2,names[2],capital1, borders);
        country4 = new Country(continent2,names[3],capital1, borders);
    }

    /**
     * Test to ensure Continent Name cannot be null.
     */
    @Test
    public void ensureNullContinentNameNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Country(null,names[0],capital1, borders));
        assertEquals("Continent name cannot be null.", thrown.getMessage());
    }

    /**
     * Test to ensure name can't be null
     */
    @Test
    public void ensureNullNameNotAllowed() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Country(continent2,null,capital1, borders));
        assertEquals("Name cannot be null.", thrown.getMessage());
    }

    /**
     * Test to ensure capital cannot be null
     */
    @Test
    public void ensureNullCapitalNotAllowed() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Country(continent2,names[0],null, borders));
        assertEquals("Capital cannot be null.", thrown.getMessage());
    }

    @Test
    public void getCapitalTest(){
        assertEquals(capital1, country1.getCapital());
    }

    @Test
    public void getNameTest(){
        assertEquals(names[0], country1.getName());
    }


    @Test
    public void getBorders(){
        assertEquals(borders, country1.getBorders());
    }




}