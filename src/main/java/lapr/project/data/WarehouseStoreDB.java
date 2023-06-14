package lapr.project.data;

import lapr.project.controller.App;

import java.sql.*;
import java.util.Calendar;

public class WarehouseStoreDB {

    /**
     * Get an estimate of the containers leaving the warehouse during the next 30 days.
     * @param warehouse_id warehouse id.
     * @return estimate of the containers leaving the warehouse during the next 30 days.
     */
    public int getNumContainersOutWarehouse(int warehouse_id){
        int result = 0;
        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
        Calendar sum30 = Calendar.getInstance();
        sum30.clear();
        sum30.setTime(currentDate);
        sum30.add(Calendar.DATE, 30);
        Date finalDate = new Date(sum30.getTime().getTime());
        String createFunction = "create or replace function get_num_containers_out_warehouse(f_warehouse_id warehouse.warehouse_id%type,f_currentDate truckTrip.est_departure_date%type, f_finalDate truckTrip.est_departure_date%type) return integer\n" +
                "is\n" +
                "f_comp_manifest cargoManifest.cargoManifest_id%type;\n" +
                "f_num_containers_out_warehouse integer:=0;\n" +
                "cursor desiredManifests\n" +
                "is\n" +
                "(select loading_cargo_id from trucktrip where departure_location=(select location_id from warehouse where warehouse_id=f_warehouse_id)\n" +
                "AND est_departure_date > f_currentDate and est_departure_date <= f_finalDate);\n" +
                "begin\n" +
                "open desiredManifests;\n" +
                "loop\n" +
                "fetch desiredManifests into f_comp_manifest;\n" +
                "exit when desiredManifests%notfound;\n" +
                "f_num_containers_out_warehouse:=f_num_containers_out_warehouse+get_num_containers_per_cargoManifest(f_comp_manifest);\n" +
                "end loop;\n" +
                "return (f_num_containers_out_warehouse);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call get_num_containers_out_warehouse(?,?,?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(warehouse_id));
            callableStatement.setString(3, String.valueOf(currentDate));
            callableStatement.setString(4, String.valueOf(finalDate));

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
     * Get number of containers currently in the warehouse.
     * @param warehouse_id Warehouse's id.
     * @return number of containers currently in the warehouse.
     */
    public int getCurrentContainersWarehouse(int warehouse_id) {
        int result = -1;
        Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
        String createFunction = "create or replace function get_current_containers_warehouse(f_warehouse_id warehouse.warehouse_id%type, f_current_date shipTrip.est_departure_date%type) return integer\n" +
                "is\n" +
                "f_comp_cargo_id cargoManifest.cargoManifest_id%type;\n" +
                "f_current_containers integer:=0;\n" +
                "cursor neededCargosPut\n" +
                "is\n" +
                "(select unloading_cargo_id\n" +
                "from truckTrip\n" +
                "where arrival_location=(select location_id from warehouse where warehouse_id=f_warehouse_id) AND est_arrival_date < f_current_date);\n" +
                "cursor neededCargosTake\n" +
                "is\n" +
                "(select loading_cargo_id\n" +
                "from truckTrip\n" +
                "where departure_location=(select location_id from warehouse where warehouse_id=f_warehouse_id) AND est_departure_date < f_current_date);\n" +
                "begin\n" +
                "open neededCargosPut;\n" +
                "loop\n" +
                "fetch neededCargosPut into f_comp_cargo_id;\n" +
                "exit when neededCargosPut%notfound;\n" +
                "f_current_containers:= f_current_containers + get_num_containers_per_cargoManifest(f_comp_cargo_id);\n" +
                "end loop;\n" +
                "open neededCargosTake;\n" +
                "loop\n" +
                "fetch neededCargosTake into f_comp_cargo_id;\n" +
                "exit when neededCargosTake%notfound;\n" +
                "f_current_containers:= f_current_containers - get_num_containers_per_cargoManifest(f_comp_cargo_id);\n" +
                "end loop;\n" +
                "return f_current_containers;\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return -1;\n" +
                "end;\n";
        String runFunction = "{? = call get_current_containers_warehouse(?,?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(warehouse_id));
            callableStatement.setString(3, String.valueOf(currentDate));

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
     * Check if warehouse exists in the data base.
     * @param warehouse_id Warehouse's id.
     * @return 1 if warehouse exists and 0 if it doesn't.
     */
    public int checkIfWarehouseExists(int warehouse_id) {
        int result = 0;
        String createFunction = "create or replace function check_if_warehouse_exists(f_warehouse_id warehouse.warehouse_id%type) return integer\n" +
                "is\n" +
                "f_result integer;\n" +
                "begin\n" +
                "select count(*) into f_result\n" +
                "from warehouse\n" +
                "where warehouse_id = f_warehouse_id;\n" +
                "return (f_result);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call check_if_warehouse_exists(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(warehouse_id));

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
     * Get warehouse max capacity.
     * @param warehouse_id Warehouse to analyse.
     * @return warehouse max capacity.
     */
    public int getWarehouseMaxCapacity(int warehouse_id) {
        int result = 0;
        String createFunction = "create or replace function get_warehouse_max_capacity(f_warehouse_id warehouse.warehouse_id%type) return integer\n" +
                "is\n" +
                "f_max_capacity integer;\n" +
                "begin\n" +
                "select maxCapacity into f_max_capacity\n" +
                "from warehouse\n" +
                "where warehouse_id = f_warehouse_id;\n" +
                "return (f_max_capacity);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;\n";
        String runFunction = "{? = call get_warehouse_max_capacity(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(warehouse_id));

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
