package lapr.project.domain.store;

import lapr.project.domain.model.Capital;
import lapr.project.domain.model.Country;
import lapr.project.domain.model.Port;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CountryStoreTest {

    CountryStore cs1;
    Country c1;
    Country c2;
    Country c3;
    Country c4;
    Country c5;

    @BeforeEach
    void setUp(){
        cs1 = new CountryStore();
        List<String> borders = new ArrayList<>();
        borders.add("Country 1");
        borders.add("country 2");
        borders.add("country 3");
        List<String> bordersPt = new ArrayList<>();
        bordersPt.add("Spain");
        List<String> bordersEsp = new ArrayList<>();
        bordersEsp.add("Portugal");
        bordersEsp.add("France");
        Capital br = new Capital("Brasilia", 12.3, 12.3, "Brazil", "South America");
        Capital pt = new Capital("Lisbon",  38.736946, -9.142685, "Portugal", "Europe");
        Capital esp = new Capital("Madrid", 40.416775, -3.703790, "Spain", "Europe");
        Capital eng = new Capital("London", 10.2, 14.2, "England", "Europe");
        Capital fr = new Capital("Paris", 4.5, 12.2, "France", "Europe");
        c1 = new Country("America", "Brazil", br, new ArrayList<>());
        c2 = new Country("Europe", "Portugal", pt, bordersPt);
        c3 = new Country("Europe", "England", eng, borders);
        c4 = new Country("Europe", "France", fr, borders);
        c5 = new Country("Europe", "Spain", esp, bordersEsp);
    }

    @Test
    void importCountriesList() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(c1);
        countryList.add(c2);
        countryList.add(c3);
        countryList.add(c4);

        cs1.importCountriesList(countryList);
        assertEquals(cs1.getCountriesList().size(), 4, "Expected to have four countries");

        cs1.importCountriesList(countryList);
        assertEquals(cs1.getCountriesList().size(), 4, "Should remain the same size since there are no new countries to be added");
    }


    @Test
    void validateCountryTrue() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(c1);
        countryList.add(c2);
        countryList.add(c3);
        cs1.importCountriesList(countryList);

        assertTrue(cs1.validateCountry(c4), "Country should pass validation");
    }

    @Test
    void validateCountryFalse() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(c1);
        countryList.add(c2);
        countryList.add(c3);
        cs1.importCountriesList(countryList);

        assertFalse(cs1.validateCountry(c3), "Country shouldn't pass validation");

    }

    @Test
    void getCountriesList() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(c1);
        countryList.add(c2);
        countryList.add(c3);
        countryList.add(c4);

        cs1.importCountriesList(countryList);

        List<Country> getCountries = cs1.getCountriesList();
        for (int i = 0; i<countryList.size(); i++){
            assertEquals(countryList.get(i), getCountries.get(i));
        }
    }

    @Test
    void getCountryByNameTest() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(c1);
        countryList.add(c2);
        countryList.add(c3);
        countryList.add(c4);
        cs1.importCountriesList(countryList);

        assertEquals(c1, cs1.getCountryByName("Brazil"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> cs1.getCountryByName("Algeria"));
        assertEquals("Couldn't find country: Algeria", thrown.getMessage(), "Not found country should throw an exception");

    }

    @Test
    void getBorderDistanceTest() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(c1);
        countryList.add(c2);
        countryList.add(c3);
        countryList.add(c4);
        countryList.add(c5);
        cs1.importCountriesList(countryList);
        Map<Country, Double> bords = cs1.getBordersDistance(c2);
        for (Country c : bords.keySet()){
            assertEquals(c, c5, "country should be spain");
            assertEquals(502.0, Math.round(bords.get(c)));
        }

        Map<Country, Double> emptyBords = cs1.getBordersDistance(c1);
        assertTrue(emptyBords.isEmpty());
    }

    @Test
    void getClosestPortFromCapital() {
        List<Country> countryList = new ArrayList<>();
        countryList.add(c2);
        cs1.importCountriesList(countryList);
        List<Port> ptPorts = new ArrayList<>();
        Port leix = new Port(13012,"Leixoes", "Europe", "Portugal", 41.18333333, -8.7);
        Port setub = new Port(13390,"Setubal", "Europe", "Portugal", 38.5, -8.916666667);
        ptPorts.add(leix);
        ptPorts.add(setub);

        assertEquals(setub, cs1.getClosestPortFromCapital(c2, ptPorts));
        ptPorts.remove(1);
        assertEquals(leix, cs1.getClosestPortFromCapital(c2, ptPorts));
    }
}