package lapr.project.domain.store;

import lapr.project.domain.model.Capital;
import lapr.project.domain.model.Country;
import lapr.project.domain.model.Port;
import lapr.project.utils.DistanceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryStore {

    private List<Country> countriesList = new ArrayList<>();

    public void importCountriesList(List<Country> existentCountries){
        for(Country country : existentCountries){
            if(validateCountry(country)){
                countriesList.add(country);
            }
        }
    }

    public boolean validateCountry(Country country){
        return !countriesList.contains(country);
    }

    public List<Country> getCountriesList() {
        return countriesList;
    }

    public Country getCountryByName(String name){
        for (Country country : countriesList){
            if(country.getName().equalsIgnoreCase(name))
                return country;
        }
        throw new RuntimeException("Couldn't find country: " + name);
    }

    public Map<Country, Double> getBordersDistance(Country country){
        List<String> countryBorders = country.getBorders();
        Map<Country, Double> distances = new HashMap<>();
        if(countryBorders.size() < 1){
            return distances;
        }
        double latFromCountry = country.getCapital().getLatitude();
        double lonFromCountry = country.getCapital().getLongitude();
        for(String countryName : countryBorders){
            Country toCountry = getCountryByName(countryName);
            double latToCountry = toCountry.getCapital().getLatitude();
            double lonToCountry = toCountry.getCapital().getLongitude();
            Double distance = DistanceUtils.distanceBetweenInKm(latFromCountry, latToCountry, lonFromCountry, lonToCountry);
            distances.put(toCountry, distance);
        }
        return distances;
    }

    public Port getClosestPortFromCapital(Country country, List<Port> countryPorts){
        Capital capital = country.getCapital();
        double distance = DistanceUtils.distanceBetweenInKm(capital.getLatitude(), countryPorts.get(0).getLatitude(), capital.getLongitude(), countryPorts.get(0).getLongitude());
        Port closest = countryPorts.get(0);
        for(int i=1; i<countryPorts.size(); i++){
            double currentDistance = DistanceUtils.distanceBetweenInKm(capital.getLatitude(), countryPorts.get(i).getLatitude(), capital.getLongitude(), countryPorts.get(i).getLongitude());
            if(currentDistance < distance){
                closest = countryPorts.get(i);
            }
        }
        return closest;
    }

}
