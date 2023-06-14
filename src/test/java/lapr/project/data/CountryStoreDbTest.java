package lapr.project.data;

import lapr.project.controller.App;
import lapr.project.domain.model.Country;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.util.List;

public class CountryStoreDbTest {

    @Disabled
    @Test
    public void countryStoreDbTest(){
        CountryStoreDb countryStoreDb = new CountryStoreDb();
        List<Country> countryList = countryStoreDb.getExistentCountries(App.getInstance().getConnection());
        for (Country c : countryList){
            System.out.println(c);
        }
    }

    @Disabled
    @Test
    public void portStoreDbTest(){
        PortStoreDB portStoreDB = new PortStoreDB();
        System.out.println(portStoreDB.getExistentPorts(App.getInstance().getConnection()));
    }

}