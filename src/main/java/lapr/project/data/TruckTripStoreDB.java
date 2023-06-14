package lapr.project.data;

import lapr.project.controller.App;
import java.sql.*;

public class TruckTripStoreDB {

    public void triggerContainersWarehouse() {
        String createTrigger = "create or replace trigger trgContainersWarehouse\n" +
                "before insert or update on TruckTrip\n" +
                "for each row\n" +
                "declare\n" +
                "f_trucktrip_id trucktrip.trucktrip_id%type;\n" +
                "f_cargoManifest_id cargomanifest.cargomanifest_id%type;\n" +
                "f_arrival_location truckTrip.arrival_location%type;\n" +
                "f_warehouse_id warehouse.warehouse_id%type;\n" +
                "f_estArrDate truckTrip.est_arrival_date%type;\n" +
                "f_containers_before integer;\n" +
                "f_containers_max integer;\n" +
                "f_containers_after integer;\n" +
                "begin\n" +
                "f_trucktrip_id:= :new.trucktrip_id;\n" +
                "f_cargoManifest_id:= :new.unloading_cargo_id;\n" +
                "f_arrival_location:= :new.arrival_location;\n" +
                "select warehouse_id into f_warehouse_id from warehouse where location_id=f_arrival_location;\n" +
                "f_estArrDate:= :new.est_arrival_date;\n" +
                "f_containers_before:=get_current_containers_warehouse(f_warehouse_id,f_estArrDate);\n" +
                "select maxCapacity into f_containers_max from Warehouse where warehouse_id=f_warehouse_id;\n" +
                "f_containers_after:=f_containers_before+get_num_containers_per_cargomanifest(f_cargomanifest_id);\n" +
                "if f_containers_after>f_containers_max then\n" +
                "raise_application_error(-20030,'Currently, the destiny warehouse doesnt have enough capacity for the containers in the unloading cargo manifest.');\n" +
                "end if;\n" +
                "end;";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createTriggerStat = connection.createStatement();) {
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Create truckTrip
     * @param truckTripID truck trip id
     * @param routeID route id
     * @param truckID truck id
     * @param depLocation departure location
     * @param arriLocation arrival location
     * @param loadCargID loading cargo manifest id
     * @param unloadCargID unloading cargo manifest id
     * @param estDepDate estimated departure date
     * @param estArriDate estimates arrival date
     * @return -1 if the input information is wrong, otherwise it returns 1
     */
    public int createTruckTripWithUnloading(int truckTripID,int routeID, int truckID, int depLocation, int arriLocation, int loadCargID, int unloadCargID, java.sql.Date estDepDate, java.sql.Date estArriDate) throws SQLException {
        int result = 1;
        String createFunction = "create or replace function create_truckTrip_with_unloading\n" +
                "(f_trucktrip_id trucktrip.trucktrip_id%type, f_route_id route.route_id%type,f_truck_id trucktrip.truck_id%type, f_departure_location trucktrip.departure_location%type,\n" +
                "f_arrival_location trucktrip.arrival_location%type, f_loading_cargo_id trucktrip.loading_cargo_id%type, f_unloading_cargo_id trucktrip.unloading_cargo_id%type,\n" +
                "f_est_departure_date trucktrip.est_departure_date%type, f_est_arrival_date trucktrip.est_arrival_date%type) return integer\n" +
                "is\n" +
                "f_check integer;\n" +
                "f_check2 integer;\n" +
                "f_check3 integer;\n" +
                "begin\n" +
                "f_check:=check_if_truck_exists(f_truck_id);\n" +
                "if f_check=0 then\n" +
                "return -1;\n" +
                "end if;\n" +
                "f_check2:=check_if_cargoManifest_exists(f_loading_cargo_id);\n" +
                "if f_check2=0 then\n" +
                "return -1;\n" +
                "end if;\n" +
                "f_check3:=check_if_cargoManifest_exists(f_unloading_cargo_id);\n" +
                "if f_check3=0 then\n" +
                "return -1;\n" +
                "end if;\n" +
                "insert into trucktrip (trucktrip_id, route_id, truck_id, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (f_trucktrip_id, f_route_id, f_truck_id, f_departure_location, f_arrival_location, f_loading_cargo_id, f_unloading_cargo_id, f_est_departure_date, f_est_arrival_date, NULL, NULL);\n" +
                "return 1;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return -1;\n" +
                "end;";
        String runFunction = "{? = call create_truckTrip_with_unloading(?,?,?,?,?,?,?,?,?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(truckTripID));
            callableStatement.setString(3, String.valueOf(routeID));
            callableStatement.setString(4, String.valueOf(truckID));
            callableStatement.setString(5, String.valueOf(depLocation));
            callableStatement.setString(6, String.valueOf(arriLocation));
            callableStatement.setString(7, String.valueOf(loadCargID));
            callableStatement.setString(8, String.valueOf(unloadCargID));
            callableStatement.setString(9, String.valueOf(estDepDate));
            callableStatement.setString(10, String.valueOf(estArriDate));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Check if truckTrip exists
     * @param truckTripID truck trip id
     * @return 0 if it doesn't exist, otherwise it returns 1
     */
    public int checkIfTruckTripExists(int truckTripID) {
        int result = 1;
        String createFunction = "create or replace function check_if_truckTrip_exists(f_truckTrip_id truckTrip.truckTrip_id%type) return integer\n" +
                "is\n" +
                "f_result integer;\n" +
                "begin\n" +
                "select count(*) into f_result\n" +
                "from truckTrip\n" +
                "where truckTrip_id = f_truckTrip_id;\n" +
                "return (f_result);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call check_if_truckTrip_exists(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(truckTripID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Create truckTrip
     * @param truckTripID truck trip id
     * @return -1 if the input information is wrong, otherwise it returns 1
     */
    public int deleteTruckTrip(int truckTripID) {
        int result = 1;
        String createFunction = "create or replace function delete_truckTrip\n" +
                "(f_trucktrip_id trucktrip.trucktrip_id%type) return integer\n" +
                "is\n" +
                "begin\n" +
                "delete\n" +
                "from truckTrip\n" +
                "where\n" +
                "trucktrip_id = f_trucktrip_id;\n" +
                "return 1;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return -1;\n" +
                "end;";
        String runFunction = "{? = call delete_truckTrip(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(truckTripID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Check if truck exists in the data base.
     * @param truckID truck id.
     * @return 1 if truck exists and 0 if it doesn't.
     */
    public int checkIfTruckExists(int truckID) {
        int result = 0;
        String createFunction = "create or replace function check_if_truck_exists(f_truck_id truck.truck_id%type) return integer\n" +
                "is\n" +
                "f_result integer;\n" +
                "begin\n" +
                "select count(*) into f_result\n" +
                "from truck\n" +
                "where truck_id = f_truck_id;\n" +
                "return (f_result);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call check_if_truck_exists(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(truckID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
