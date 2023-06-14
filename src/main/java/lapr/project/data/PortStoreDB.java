package lapr.project.data;

import lapr.project.controller.App;
import lapr.project.domain.model.Port;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PortStoreDB implements Persistable {

    /**
     * Get the port max capacity by portID.
     * @param portID port id.
     * @return the port max capacity.
     */
    public int getPortMaxCapacity(int portID) {
        int result = 1;
        String createFunction = "create or replace function get_port_max_capacity(f_port_id port.port_id%type) return integer\n" +
                "is\n" +
                "f_max_capacity integer;\n" +
                "begin\n" +
                "select maxCapacity into f_max_capacity\n" +
                "from port\n" +
                "where port_id = f_port_id;\n" +
                "return (f_max_capacity);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;\n";
        String runFunction = "{? = call get_port_max_capacity(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(portID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, String.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get the port occupancy in a day by portID and date.
     * @param portID port id.
     * @param date date.
     * @return the port occupancy in a day.
     */
    public int getPortOccupancyInDay(int portID, Date date) {
        int result = 1;
        String createFunction = "create or replace function get_current_num_containers_port(f_port_id port.port_id%type,\n" +
                "f_date shipTrip.est_departure_date%type) return integer\n" +
                "is\n" +
                "f_current_num integer:=0;\n" +
                "f_comp_cargo_id cargoManifest.cargoManifest_id%type;\n" +
                "cursor cursorShipTripLoading\n" +
                "is\n" +
                "(select loading_cargo_id\n" +
                "from shipTrip\n" +
                "where departure_location=f_port_id AND real_departure_date < f_date);\n" +
                "cursor cursorShipTripUnloading\n" +
                "is\n" +
                "(select unloading_cargo_id\n" +
                "from shipTrip\n" +
                "where arrival_location=f_port_id AND real_arrival_date < f_date);\n" +
                "cursor cursorTruckTripLoading\n" +
                "is\n" +
                "(select loading_cargo_id\n" +
                "from truckTrip\n" +
                "where departure_location=(select location_id from port where port_id=f_port_id) AND real_departure_date < f_date);\n" +
                "cursor cursorTruckTripUnloading\n" +
                "is\n" +
                "(select unloading_cargo_id\n" +
                "from truckTrip\n" +
                "where arrival_location=(select location_id from port where port_id=f_port_id) AND real_arrival_date < f_date);\n" +
                "begin\n" +
                "open cursorShipTripUnloading;\n" +
                "loop\n" +
                "fetch cursorShipTripUnloading into f_comp_cargo_id;\n" +
                "exit when cursorShipTripUnloading%notfound;\n" +
                "f_current_num:=f_current_num+get_num_containers_per_cargomanifest(f_comp_cargo_id);\n" +
                "end loop;\n" +
                "open cursorTruckTripUnloading;\n" +
                "loop\n" +
                "fetch cursorTruckTripUnloading into f_comp_cargo_id;\n" +
                "exit when cursorTruckTripUnloading%notfound;\n" +
                "f_current_num:=f_current_num+get_num_containers_per_cargomanifest(f_comp_cargo_id);\n" +
                "end loop;\n" +
                "open cursorShipTripLoading;\n" +
                "loop\n" +
                "fetch cursorShipTripLoading into f_comp_cargo_id;\n" +
                "exit when cursorShipTripLoading%notfound;\n" +
                "f_current_num:=f_current_num-get_num_containers_per_cargomanifest(f_comp_cargo_id);\n" +
                "end loop;\n" +
                "open cursorTruckTripLoading;\n" +
                "loop\n" +
                "fetch cursorTruckTripLoading into f_comp_cargo_id;\n" +
                "exit when cursorTruckTripLoading%notfound;\n" +
                "f_current_num:=f_current_num-get_num_containers_per_cargomanifest(f_comp_cargo_id);\n" +
                "end loop;\n" +
                "return f_current_num;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call get_current_num_containers_port(?,?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(portID));
            callableStatement.setString(3, String.valueOf(date));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, String.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Check if port exists by portID.
     * @param portID port id.
     * @return 1 if the port exists, otherwise returns 0.
     */
    public int checkIfPortExists(int portID) {
        int result = 1;
        String createFunction = "create or replace function check_if_port_exists(f_port_id port.port_id%type) return integer\n" +
                "is\n" +
                "f_result integer;\n" +
                "begin\n" +
                "select count(*) into f_result\n" +
                "from port\n" +
                "where port_id = f_port_id;\n" +
                "return (f_result);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call check_if_port_exists(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(portID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, String.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Port> getExistentPorts(DatabaseConnection databaseConnection){
        Connection connection = databaseConnection.getConnection();
        CountryStoreDb countryStoreDb = new CountryStoreDb();
        List<Port> ports = new ArrayList<>();
        String sqlCommand = "select port_id, name, location_id from port";
        String name, countryName, continentName;
        int locationId, portId;
        double latitude, longitude;
        try (PreparedStatement getAllPorts = connection.prepareStatement(
                sqlCommand)) {
            try (ResultSet portsResult = getAllPorts.executeQuery()) {
                while(portsResult.next()){
                    name = portsResult.getNString(2);
                    portId = portsResult.getInt(1);
                    locationId = portsResult.getInt(3);
                    latitude = getLocationLatitude(databaseConnection, locationId);
                    longitude = getLocationLongitude(databaseConnection, locationId);
                    countryName = getLocationCountry(databaseConnection, locationId);
                    continentName = countryStoreDb.getCountryContinentByName(databaseConnection, countryName);
                    Map<Integer, Double> map =getSeaDists(databaseConnection, portId);
                    ports.add(new Port(portId, name, continentName, countryName, latitude, longitude, map));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return ports;
    }

    private Map<Integer, Double> getSeaDists(DatabaseConnection databaseConnection, int fromPortId){
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select * from sea_dist where from_port = ?";
        Map<Integer, Double> seadists = new HashMap<>();
        try (PreparedStatement getPortDists = connection.prepareStatement(
                sqlCommand)) {
            getPortDists.setInt(1,fromPortId);
            try (ResultSet seadistsResult = getPortDists.executeQuery()) {
                while(seadistsResult.next()){
                    int toPortId = seadistsResult.getInt(2);
                    double distance = seadistsResult.getDouble(3);
                    seadists.put(toPortId, distance);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return seadists;
    }

    private double getLocationLatitude(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        double latitude = 0;
        String sqlCommand = "select * from placelocation where location_id = ?";
        try (PreparedStatement getLocation = connection.prepareStatement(
                sqlCommand)) {
            getLocation.setInt(1, id);
            try (ResultSet locationResult = getLocation.executeQuery()) {
                if(locationResult.next()){
                    latitude = locationResult.getDouble(2);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }

        return  latitude;
    }
    private double getLocationLongitude(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        double longitude = 0;
        String sqlCommand = "select * from placelocation where location_id = ?";
        try (PreparedStatement getLocation = connection.prepareStatement(
                sqlCommand)) {
            getLocation.setInt(1, id);
            try (ResultSet locationResult = getLocation.executeQuery()) {
                if(locationResult.next()){
                    longitude = locationResult.getDouble(3);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }

        return  longitude;
    }

    private String getLocationCountry(DatabaseConnection databaseConnection, int id) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select * from placelocation where location_id = ?";
        try (PreparedStatement getLocation = connection.prepareStatement(
                sqlCommand)) {
            getLocation.setInt(1, id);
            try (ResultSet locationResult = getLocation.executeQuery()) {
                if(locationResult.next()){
                    return locationResult.getNString(4).trim();
                }else{
                    return null;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            return null;
        }
    }


    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Connection connection = databaseConnection.getConnection();
        Port port = (Port) object;

        String sqlCommand = "select * from port where port_id = ?";
        boolean returnValue;
        try (PreparedStatement getShipPositionStatement = connection.prepareStatement(
                sqlCommand)) {
            getShipPositionStatement.setInt(1, port.getIdentification());

            try (ResultSet addressesResultSet = getShipPositionStatement.executeQuery()) {
                if (addressesResultSet.next()) {
                    return false;
                } else {
                    createNewPort(databaseConnection, port);
                    returnValue = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }

    private void createNewPort (DatabaseConnection databaseConnection, Port port) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        boolean status = true;
        if(!existsContinent(databaseConnection, port)){
            if(createOrUpdateContinent(databaseConnection, port)){
                if(!createOrUpdateCountry(databaseConnection, port)){
                    status = false;
                }
            }
        }
        if(!existsCountry(databaseConnection, port)){
            if(!createOrUpdateCountry(databaseConnection, port)){
                status = false;
            };
        }
        if(status){
            if(createLocation(databaseConnection, port)){
                int location_id = getLocationId(databaseConnection, port);
                System.out.println(location_id);
                String sqlCommand = "insert into port(port_id, name, location_id) values(?, ?, ?)";
                try (PreparedStatement saveShipUpdate = connection.prepareStatement(
                        sqlCommand)) {
                    saveShipUpdate.setInt(1, port.getIdentification());
                    saveShipUpdate.setString(2, port.getName());
                    saveShipUpdate.setInt(3, location_id);
                    saveShipUpdate.executeUpdate();
                }

                catch (SQLException ex) {
                    Logger.getLogger(PortStoreDB.class.getName())
                            .log(Level.SEVERE, null, ex);
                    databaseConnection.registerError(ex);
                }

            }

        }else{
            throw new SQLException("Failed to create continent and or country"+ port);
        }

    }

    private boolean createLocation(DatabaseConnection databaseConnection, Port port){
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "insert into placeLocation(locationLatitude, locationLongitude, country) values(?, ?, ?)";
        boolean returnValue = false;
        try (PreparedStatement saveShipUpdate = connection.prepareStatement(
                sqlCommand)) {
            saveShipUpdate.setDouble(1,port.getLatitude());
            saveShipUpdate.setDouble(2,port.getLongitude());
            saveShipUpdate.setString(3,port.getCountry());
            saveShipUpdate.executeUpdate();
            returnValue = true;
        }

        catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }
    private int getLocationId(DatabaseConnection databaseConnection, Port port){
        Connection connection = databaseConnection.getConnection();
        int returnVal = -1;
        String sqlCommand = "select * from placeLocation where (locationlatitude = ? AND locationlongitude = ?)";
        try (PreparedStatement getShipPositionStatement = connection.prepareStatement(
                sqlCommand)) {
            getShipPositionStatement.setDouble(1, port.getLatitude());
            getShipPositionStatement.setDouble(2, port.getLongitude());
            try (ResultSet addressesResultSet = getShipPositionStatement.executeQuery()) {
                if (addressesResultSet.next())  {
                    returnVal= addressesResultSet.getInt("location_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return returnVal;
    }

    private int getContinentId(DatabaseConnection databaseConnection, String continentName){
        Connection connection = databaseConnection.getConnection();
        int returnVal = -1;
        String sqlCommand = "select * from continent where continent_name = ?";
        try (PreparedStatement getShipPositionStatement = connection.prepareStatement(
                sqlCommand)) {
            getShipPositionStatement.setString(1, continentName);
            try (ResultSet addressesResultSet = getShipPositionStatement.executeQuery()) {
                if (addressesResultSet.next())  {
                  returnVal= addressesResultSet.getInt("continent_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
        }
        return returnVal;
    }
    private boolean createOrUpdateCountry(DatabaseConnection databaseConnection, Port port){
        Connection connection = databaseConnection.getConnection();
        boolean returnVal = false;
        String sqlCommand = "select * from continent where continent_name = ?";
        int continentId = getContinentId(databaseConnection, port.getContinent());
            if (continentId != -1)  {
                    sqlCommand =
                            "insert into country(country_name, continent_id) values (?, ?)";
                }

            try (PreparedStatement saveShipUpdate = connection.prepareStatement(
                    sqlCommand)) {
                saveShipUpdate.setString(1, port.getCountry());
                saveShipUpdate.setInt(2, continentId);
                saveShipUpdate.executeUpdate();
                returnVal = true;
            }
            catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            return returnVal;
        }
        return returnVal;
    }

    private boolean createOrUpdateContinent(DatabaseConnection databaseConnection, Port port){
        Connection connection = databaseConnection.getConnection();
        boolean returnVal = false;
        String sqlCommand = "select * from continent where continent_name = ?";
        try (PreparedStatement getShipPositionStatement = connection.prepareStatement(
                sqlCommand)) {
            getShipPositionStatement.setString(1, port.getContinent());

            try (ResultSet addressesResultSet = getShipPositionStatement.executeQuery()) {
                if (!addressesResultSet.next())  {
                    sqlCommand =
                            "insert into continent(continent_name) values (?)";
                }

                try (PreparedStatement saveShipUpdate = connection.prepareStatement(
                        sqlCommand)) {
                    saveShipUpdate.setString(1, port.getContinent());
                    saveShipUpdate.executeUpdate();
                    returnVal = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            return returnVal;
        }
        return returnVal;
    }

    private boolean existsContinent(DatabaseConnection databaseConnection, Port port){
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select * from continent where continent_name = ?";
        boolean returnValue = false;
        try (PreparedStatement getShipPositionStatement = connection.prepareStatement(
                sqlCommand)) {
            getShipPositionStatement.setString(1, port.getContinent());

            try (ResultSet continentResultSet = getShipPositionStatement.executeQuery()) {
                if (continentResultSet.next()) {
                    returnValue = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }

    private boolean existsCountry(DatabaseConnection databaseConnection, Port port){
        Connection connection = databaseConnection.getConnection();
        String sqlCommand = "select * from country where country_name = ?";
        boolean returnValue = false;
        try (PreparedStatement getShipPositionStatement = connection.prepareStatement(
                sqlCommand)) {
            getShipPositionStatement.setString(1, port.getCountry());

            try (ResultSet continentResultSet = getShipPositionStatement.executeQuery()) {
                if (continentResultSet.next()) {
                    returnValue = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PortStoreDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Connection conn = databaseConnection.getConnection();

        boolean returnValue = false;

        return returnValue;
    }

}
