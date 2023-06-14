package lapr.project.data;

import lapr.project.controller.App;
import oracle.jdbc.OracleTypes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class ShipTripStoreDB {
    private final String header = String.format("%-15s%-15s%-15s%-15s%-14s%-15s%n",
            "Container ID", "ISO Code", "Payload", "Positionx","PositionY", "PositionZ");

    private final String idPort = "Next Port ID: ";

    public void triggerContainers() {
        String createTrigger = "create or replace trigger trgContainers\n" +
                "before insert or update on ShipTrip\n" +
                "for each row\n" +
                "declare\n" +
                "f_shiptrip_id shiptrip.shiptrip_id%type;\n" +
                "f_cargoManifest_id cargomanifest.cargomanifest_id%type;\n" +
                "f_mmsi shipTrip.mmsi%type;\n" +
                "f_estDepDate shipTrip.est_departure_date%type;\n" +
                "f_containers_before integer;\n" +
                "f_containers_max integer;\n" +
                "f_containers_after integer;\n" +
                "begin\n" +
                "f_shiptrip_id:= :new.shiptrip_id;\n" +
                "f_cargoManifest_id:= :new.loading_cargo_id;\n" +
                "f_mmsi:= :new.mmsi;\n" +
                "f_estDepDate:= :new.est_departure_date;\n" +
                "select currentCapacity into f_containers_max from Ship where mmsi=f_mmsi;\n" +
                "f_containers_before:=get_initial_num_containers_per_ship_trip(f_cargoManifest_id,f_estDepDate,f_mmsi);\n" +
                "f_containers_after:=f_containers_before+get_num_containers_per_cargomanifest(f_cargomanifest_id);\n" +
                "if f_containers_after>f_containers_max then\n" +
                "raise_application_error(-20001,'Currently, the ship doesnt have enough capacity for the cargo manifest.');\n" +
                "end if;\n" +
                "end;";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createTriggerStat = connection.createStatement();) {
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void triggerShipAvailability() {
        String createFunction = "create or replace PROCEDURE check_availability_of_ship(daydate in date, ship in Ship.mmsi%type, isValid out boolean)\n" +
                "IS\n" +
                "    estDepartDate date;\n" +
                "    estArrivalDate date;\n" +
                "\n" +
                "Cursor tripsOfShip IS\n" +
                "        select est_departure_date, est_arrival_date\n" +
                "        from shiptrip\n" +
                "        where mmsi=ship;\n" +
                "\n" +
                "BEGIN\n" +
                "open tripsOfShip;\n" +
                "    LOOP\n" +
                "        fetch tripsOfShip INTO estDepartDate, estArrivalDate;\n" +
                "        Exit When tripsOfShip%notfound;\n" +
                "        --if the arriavl date is before the monday is valid\n" +
                "        if daydate > estArrivalDate then\n" +
                "            isValid := true;\n" +
                "        -- if the date was not before monday then if the depart date is AFTER monday ship is available\n" +
                "        elsif daydate < estDepartDate then\n" +
                "            isValid := true;\n" +
                "        -- else false\n" +
                "        else\n" +
                "            isValid := false;\n" +
                "            exit;\n" +
                "        end if;\n" +
                "    END LOOP;\n" +
                "END;";

        String createTrigger ="create or replace trigger trgShipAvailability\n" +
                "before insert or update on ShipTrip\n" +
                "                            for each row\n" +
                "declare\n" +
                "f_mmsi shipTrip.mmsi%type;\n" +
                "f_estDepDate shipTrip.est_departure_date%type;\n" +
                "f_isValid boolean;\n" +
                "\n" +
                "begin\n" +
                "f_mmsi:= :new.mmsi;\n" +
                "f_estDepDate:= :new.est_departure_date;\n" +
                "f_isValid := false;\n" +
                "check_availability_of_ship(f_estDepDate, f_mmsi, f_isValid);\n" +
                "if not f_isValid then\n" +
                "raise_application_error(-20010,'Currently, the ship is not available in this date.');\n" +
                "end if;\n" +
                "end;";

        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createTriggerStat = connection.createStatement();) {
            createTriggerStat.execute(createFunction);
            createTriggerStat.execute(createTrigger);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * Check if shipTrip exists
     * @param shipTripID ship trip id
     * @return 0 if it doesn't exist, otherwise it returns 1
     */
    public int checkIfShipTripExists(int shipTripID) {
        int result = 1;
        String createFunction = "create or replace function check_if_shipTrip_exists(f_shipTrip_id shipTrip.shipTrip_id%type) return integer\n" +
                "is\n" +
                "f_result integer;\n" +
                "begin\n" +
                "select count(*) into f_result\n" +
                "from shipTrip\n" +
                "where shipTrip_id = f_shipTrip_id;\n" +
                "return (f_result);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call check_if_shipTrip_exists(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(shipTripID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Create shipTrip
     * @param shipTripID ship trip id
     * @return -1 if the input information is wrong, otherwise it returns 1
     */
    public int deleteShipTrip(int shipTripID) {
        int result = 1;
        String createFunction = "create or replace function delete_shipTrip\n" +
                "(f_shiptrip_id shiptrip.shiptrip_id%type) return integer\n" +
                "is\n" +
                "begin\n" +
                "delete\n" +
                "from shipTrip\n" +
                "where\n" +
                "shiptrip_id = f_shiptrip_id;\n" +
                "return 1;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return -1;\n" +
                "end;";
        String runFunction = "{? = call delete_shipTrip(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(shipTripID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Create shipTrip
     * @param shipTripID ship trip id
     * @param routeID route id.
     * @param mmsi ship mmsi
     * @param depLocation departure location
     * @param arriLocation arrival location
     * @param loadCargID loading cargo manifest id
     * @param estDepDate estimated departure date
     * @param estArriDate estimates arrival date
     * @return -1 if the input information is wrong, otherwise it returns 1
     */
    public int createShipTrip(int shipTripID, int routeID, int mmsi, int depLocation, int arriLocation, int loadCargID, java.sql.Date estDepDate, java.sql.Date estArriDate) throws SQLException {
        int result = 1;
        String createFunction = "create or replace function create_shipTrip\n" +
                "(f_shiptrip_id shiptrip.shiptrip_id%type, f_route_id route.route_id%type, f_mmsi shiptrip.mmsi%type, f_departure_location shiptrip.departure_location%type,\n" +
                "f_arrival_location shiptrip.arrival_location%type, f_loading_cargo_id shiptrip.loading_cargo_id%type,\n" +
                "f_est_departure_date shiptrip.est_departure_date%type, f_est_arrival_date shiptrip.est_arrival_date%type) return integer\n" +
                "is\n" +
                "f_check integer;\n" +
                "f_check2 integer;\n" +
                "begin\n" +
                "f_check:=check_if_ship_exists(f_mmsi);\n" +
                "if f_check=0 then\n" +
                "return -1;\n" +
                "end if;\n" +
                "f_check2:=check_if_cargoManifest_exists(f_loading_cargo_id);\n" +
                "if f_check2=0 then\n" +
                "return -1;\n" +
                "end if;\n" +
                "insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (f_shiptrip_id, f_route_id, f_mmsi, f_departure_location, f_arrival_location, f_loading_cargo_id, NULL, f_est_departure_date, f_est_arrival_date, NULL, NULL);\n" +
                "return 1;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return -1;\n" +
                "end;";
        String runFunction = "{? = call create_shipTrip(?,?,?,?,?,?,?,?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(shipTripID));
            callableStatement.setString(3, String.valueOf(routeID));
            callableStatement.setString(4, String.valueOf(mmsi));
            callableStatement.setString(5, String.valueOf(depLocation));
            callableStatement.setString(6, String.valueOf(arriLocation));
            callableStatement.setString(7, String.valueOf(loadCargID));
            callableStatement.setString(8, String.valueOf(estDepDate));
            callableStatement.setString(9, String.valueOf(estArriDate));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
            if(e.getErrorCode() == 20010){
                throw new SQLException("ORA-20010 - Currently, the ship is not available in this date.");
            }
            System.out.println(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get MMSI by cargo manifest ID.
     * @param cargoManifestID cargo manifest ID.
     * @return Ship MMSI.
     */
    public int getMmsiByCargoManifestID(int cargoManifestID) {
        int result = 0;
        String createFunction = "create or replace function get_mmsi_by_cargo_manifest_id(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer --est date como parametro\n" +
                "is\n" +
                "f_shiptrip_id shipTrip.shiptrip_id%type;\n" +
                "f_mmsi ship.mmsi%type;\n" +
                "begin\n" +
                "select shiptrip_id into f_shiptrip_id\n" +
                "from shipTrip\n" +
                "where loading_cargo_id=f_cargoManifest_id or unloading_cargo_id=f_cargoManifest_id;\n" +
                "select mmsi into f_mmsi\n" +
                "from shipTrip\n" +
                "where shiptrip_id = f_shiptrip_id;\n" +
                "return f_mmsi;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return -1;\n" +
                "end;";
        String runFunction = "{? = call get_mmsi_by_cargo_manifest_id(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(cargoManifestID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get containers added and/or removed by the time of a cargo manifest for a ship trip.
     * @param cargoManifestID cargo manifest ID.
     * @return the number of containers added and/or removed.
     */
    public int getAddedRemovedContainersShipTripMoment(int cargoManifestID) {
        int result = 0;
        String createFunction = "create or replace function get_added_removed_containers_ship_trip_moment(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer --est date como parametro\n" +
                "is\n" +
                "f_shiptrip_id shipTrip.shiptrip_id%type;\n" +
                "f_loading_cargo_id shipTrip.loading_cargo_id%type;\n" +
                "f_unloading_cargo_id shipTrip.unloading_cargo_id%type;\n" +
                "f_num_added_removed_containers_ship_trip_moment integer:=0;\n" +
                "begin\n" +
                "select shiptrip_id into f_shiptrip_id\n" +
                "from shipTrip\n" +
                "where loading_cargo_id=f_cargoManifest_id or unloading_cargo_id=f_cargoManifest_id;\n" +
                "select loading_cargo_id, unloading_cargo_id into f_loading_cargo_id, f_unloading_cargo_id\n" +
                "from shipTrip\n" +
                "where shiptrip_id = f_shiptrip_id;\n" +
                "f_num_added_removed_containers_ship_trip_moment := f_num_added_removed_containers_ship_trip_moment + get_num_containers_per_cargoManifest(f_loading_cargo_id);\n" +
                "if f_unloading_cargo_id=f_cargoManifest_id then\n" +
                "f_num_added_removed_containers_ship_trip_moment := f_num_added_removed_containers_ship_trip_moment + get_num_containers_per_cargoManifest(f_unloading_cargo_id);\n" +
                "end if;\n" +
                "return f_num_added_removed_containers_ship_trip_moment;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call get_added_removed_containers_ship_trip_moment(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(cargoManifestID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Get the estimated departure date of a ship trip given a cargo manifest ID.
     * @param cargoManifestID cargo manifest ID.
     * @return estimated departure date.
     */
    public java.sql.Date getEstDepartureDateFromShipTrip(int cargoManifestID) {
        java.sql.Date result = new java.sql.Date(new java.util.Date(2020, Calendar.JANUARY,1).getTime());
        String createFunction = "create or replace function get_est_departure_date_from_ship_trip(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return shipTrip.est_departure_date%type\n" +
                "is\n" +
                "f_shiptrip_id shipTrip.shiptrip_id%type;\n" +
                "f_est_departure_date shipTrip.est_departure_date%type;\n" +
                "begin\n" +
                "select shiptrip_id into f_shiptrip_id\n" +
                "from shipTrip\n" +
                "where loading_cargo_id = f_cargoManifest_id OR unloading_cargo_id = f_cargoManifest_id;\n" +
                "select est_departure_date into f_est_departure_date\n" +
                "from shipTrip\n" +
                "where shiptrip_id = f_shiptrip_id;\n" +
                "return f_est_departure_date;\n" +
                "end;";
        String runFunction = "{? = call get_est_departure_date_from_ship_trip(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.DATE);
            callableStatement.setString(2, String.valueOf(cargoManifestID));

            callableStatement.executeUpdate();

            result = callableStatement.getDate(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get a ship trip's initial num of containers given a cargo manifest ID and a estimates departure date.
     * @param cargoManifestID cargo manifest ID.
     * @param estDepartureDate Ship trip's estimated departure date.
     * @return ship trip's initial num of containers.
     */
    public int getInitialNumContainersPerShipTrip(int cargoManifestID, java.sql.Date estDepartureDate, int mmsi) {
        int result = 0;
        String createFunction = "create or replace function get_initial_num_containers_per_ship_trip(f_cargoManifest_id cargoManifest.cargoManifest_id%type,\n" +
                "f_est_departure_date shipTrip.est_departure_date%type, f_mmsi ship.mmsi%type) return integer\n" +
                "is\n" +
                "f_comp_loading_cargo_id cargoManifest.cargoManifest_id%type;\n" +
                "f_comp_unloading_cargo_id cargoManifest.cargoManifest_id%type;\n" +
                "f_initial_num_containers_per_ship_trip integer:=0;\n" +
                "cursor neededShipTrips\n" +
                "is\n" +
                "(select loading_cargo_id, unloading_cargo_id\n" +
                "from shipTrip\n" +
                "where mmsi=f_mmsi AND est_departure_date < f_est_departure_date);\n" +
                "begin\n" +
                "open neededShipTrips;\n" +
                "loop\n" +
                "fetch neededShipTrips into f_comp_loading_cargo_id, f_comp_unloading_cargo_id;\n" +
                "exit when neededShipTrips%notfound;\n" +
                "f_initial_num_containers_per_ship_trip := f_initial_num_containers_per_ship_trip + get_num_containers_per_cargoManifest(f_comp_loading_cargo_id) - get_num_containers_per_cargoManifest(f_comp_unloading_cargo_id);\n" +
                "end loop;\n" +
                "return f_initial_num_containers_per_ship_trip;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call get_initial_num_containers_per_ship_trip(?,?,?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(cargoManifestID));
            callableStatement.setString(3, String.valueOf(estDepartureDate));
            callableStatement.setString(4, String.valueOf(mmsi));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * method to get the id of the nest Port in the Route
     * @param databaseConnection connection to database
     * @param mmsi ship mmsi
     * @return port id
     */
    public int getNextPortID(DatabaseConnection databaseConnection, int mmsi) {

        try {
            Connection connection = databaseConnection.getConnection();
            CallableStatement cs = connection.prepareCall("{? = call get_port(?)}");
            cs.setInt(2, mmsi);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.executeUpdate();
            int portId= cs.getInt(1);
            cs.close();
            return portId;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");
    }

    /**
     * Method to get the list of containers being offloaded in the next Port.
     * @param databaseConnection connection to database
     * @param mmsi ship mmsi
     * @return list with only Containers id's
     * @throws IOException database exception
     */
    public List<Integer> getListOffloadedContainers(DatabaseConnection databaseConnection, int mmsi) throws IOException {

        int portId = getNextPortID(databaseConnection, mmsi);
        System.out.println("Next Port: " + portId);

        File file = new File("offloadedContainers.txt");
        if (!file.exists())
            file.createNewFile();

        List<Integer> idList = new LinkedList<>();
        Connection connection = databaseConnection.getConnection();

        try (FileWriter fw = new FileWriter(file, false); BufferedWriter bw = new BufferedWriter(fw); CallableStatement cs = connection.prepareCall("{? = call  offcontainers_id(?)}")) {


            bw.write(idPort);
            bw.write(portId);
            bw.write(header);

            cs.setInt(2, mmsi);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.executeUpdate();


            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                int containerId = cs1.getInt(1);
                System.out.println("Container id: " + containerId);
                idList.add(containerId);
                String isoCode = cs1.getString(2);
                System.out.println("Container ISO: " + isoCode);
                int payload = cs1.getInt(3);
                System.out.println("Continer Load: " + payload);
                int positionx = cs1.getInt(4);
                int positiony = cs1.getInt(5);
                int positionz = cs1.getInt(6);
                System.out.println("Position in vehicule: " + positionx + ", " + positiony + ", " + positionz);
                System.out.println();


                bw.write(containerId);
                bw.write(isoCode);
                bw.write(payload);
                bw.write(positionx);
                bw.write(positiony);
                bw.write(positionz);
            }
            cs.close();
            return idList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");

    }

    /**
     * Method to get the list of containers being loaded in the next Port.
     * @param databaseConnection connection to database
     * @param mmsi ship mmsi
     * @return list with only Containers id's
     * @throws IOException database exception
     */
    public List<Integer> getListLoadedContainers(DatabaseConnection databaseConnection, int mmsi) throws IOException {

        int portId = getNextPortID(databaseConnection, mmsi);
        System.out.println("Next Port: " + portId);

        File file = new File("loadedContainers.txt");
        if (!file.exists())
            file.createNewFile();

        List<Integer> listID = new LinkedList<>();
        Connection connection = databaseConnection.getConnection();

        try (FileWriter fw = new FileWriter(file, false); BufferedWriter bw = new BufferedWriter(fw); CallableStatement cs = connection.prepareCall("{? = call  load_containers_id(?)}")) {

            bw.write(idPort);
            bw.write(portId);
            bw.write(header);

            cs.setInt(2, mmsi);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.executeUpdate();


            ResultSet cs1 = (ResultSet) cs.getObject(1);

            while (cs1.next()) {
                int containerId = cs1.getInt(1);
                System.out.println("Container id: " + containerId);
                listID.add(containerId);
                String isoCode = cs1.getString(2);
                System.out.println("Container ISO: " + isoCode);
                int payload = cs1.getInt(3);
                System.out.println("Continer Load: " + payload);
                int positionx = cs1.getInt(4);
                int positiony = cs1.getInt(5);
                int positionz = cs1.getInt(6);
                System.out.println("Position in vehicule: " + positionx + ", " + positiony + ", " + positionz);
                System.out.println();

                bw.write(containerId);
                bw.write(isoCode);
                bw.write(payload);
                bw.write(positionx);
                bw.write(positiony);
                bw.write(positionz);
            }
            //cs.close();
            return listID;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");

    }

    /**
     * Method to get the list of voyages with an occupancy rate below 66%
     * @return List with voyages with an occupancy rate below 66%
     */
    public List<Integer> getListVoyages(DatabaseConnection databaseConnection) {
        List<Integer> listID = new LinkedList<>();
        Connection connection = databaseConnection.getConnection();

        ShipStoreDB shipStoreDB = App.getInstance().getCompany().getShipStoreDB();

        try (CallableStatement cs = connection.prepareCall("{? = call  get_all_voyages()}")) {
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.executeUpdate();


            ResultSet cs1 = (ResultSet) cs.getObject(1);

            int i=0;
            while (cs1.next()) {

                int shipID = cs1.getInt(1);

                int maxCapacity = shipStoreDB.getShipMaxCapacity(shipID);
                int tripID = cs1.getInt(2);

                int departureID = cs1.getInt(3);

                int arrivalID = cs1.getInt(4);

                Date departureDate = cs1.getDate(5);

                int capacityTime = shipStoreDB.getNumContainersShipDay(shipID, departureDate);

                Date arrivalDate = cs1.getDate(6);

                if (shipStoreDB.calculateOccupancyRate(maxCapacity, capacityTime) < 66 && !listID.contains(tripID) && maxCapacity!=0) {
                    listID.add(tripID);
                    System.out.println("\n");
                    System.out.println("Ship ID: " + shipID);
                    System.out.println("Ship Trip ID: " + tripID);
                    System.out.println("Departure location ID: " + departureID);
                    System.out.println("Arrival location ID: " + arrivalID);
                    System.out.println("Departure date: " + departureDate.toString());
                    System.out.println("Arrival date: " + arrivalDate.toString());
                    System.out.println("Occupancy rate: " + shipStoreDB.calculateOccupancyRate(maxCapacity, capacityTime));
                }

            }
            return listID;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");
    }
}
