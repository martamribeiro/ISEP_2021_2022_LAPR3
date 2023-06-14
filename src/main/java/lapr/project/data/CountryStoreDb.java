package lapr.project.data;

import lapr.project.domain.model.Capital;
import lapr.project.domain.model.Country;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountryStoreDb {

    public List<Country> getExistentCountries(DatabaseConnection databaseConnection){
        Connection connection = databaseConnection.getConnection();
        List<Country> countries = new ArrayList<>();
        String sqlCommand = "select * from country";
        String continent, name;
        try (PreparedStatement getAllCountries = connection.prepareStatement(
                sqlCommand)) {
            try (ResultSet countriesResultSet = getAllCountries.executeQuery()) {
                int count = 0;
                while(countriesResultSet.next()){
                    name = countriesResultSet.getNString(1);

                    List<String> borders = getCountryBorders(databaseConnection,name);
                    continent = getCountryContinent(databaseConnection, countriesResultSet.getInt(2));
                    Capital c =  getCountryCapital(databaseConnection, name, continent);
                    countries.add(new Country(continent, name, c, borders));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return countries;
    }

    private Capital getCountryCapital(DatabaseConnection databaseConnection, String country, String continent){
        Capital c1 = null;
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select * from capital where country_name = ?";
        try (PreparedStatement getCapital = connection.prepareStatement(
                sqlCommand)) {
            getCapital.setString(1, country);
            try (ResultSet foundCapital = getCapital.executeQuery()) {
                if(foundCapital.next()){
                    c1 = new Capital(foundCapital.getNString(1), foundCapital.getDouble(3),
                            foundCapital.getDouble(4), foundCapital.getNString(2), continent);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return c1;
    }

    private List<String> getCountryBorders(DatabaseConnection databaseConnection, String country){
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select * from border where country_a = ?";
        List<String> borders = new ArrayList<>();
        try (PreparedStatement getCountryBorders = connection.prepareStatement(
                sqlCommand)) {
            getCountryBorders.setString(1,country);
            try (ResultSet bordersResult = getCountryBorders.executeQuery()) {
                    while(bordersResult.next()){
                        borders.add(bordersResult.getNString(2).trim());
                    }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return borders;
    }

    private String getCountryContinent(DatabaseConnection databaseConnection, int cont_id){
        String continent="NA";
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select continent_name from continent where continent_id = ?";
        try (PreparedStatement getContinent = connection.prepareStatement(
                sqlCommand)) {
            getContinent.setInt(1,cont_id);
            try (ResultSet continentResult = getContinent.executeQuery()) {
                if(continentResult.next()){
                    continent =  continentResult.getNString(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return continent;
    }

    public String getCountryContinentByName(DatabaseConnection databaseConnection, String country_name){
        String continent="NA";
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select continent_id from country where country_name = ?";
        try (PreparedStatement getContinent = connection.prepareStatement(
                sqlCommand)) {
            getContinent.setString(1,country_name);
            try (ResultSet continentResult = getContinent.executeQuery()) {
                if(continentResult.next()){
                    continent =  getCountryContinent(databaseConnection, continentResult.getInt(1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return continent;
    }
}
