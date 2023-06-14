package lapr.project.data;


import lapr.project.controller.App;
import lapr.project.domain.store.ShipStore;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Marta Ribeiro (1201592)
 */
public class CargoManifestStoreDB{
    /*public static void main(String[] args) {
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Map<String, String> map = getUnLoadingLoadingMap(databaseConnection);
        System.out.println(map.get("31/01/2022"));
    }*/


    public Map<String, String> getUnLoadingLoadingMap(DatabaseConnection databaseConnection) {
        Map<String, String> map = new LinkedHashMap<>();

        //obtaining the beginning of next week
        LocalDate dt = LocalDate.now();
        LocalDate currentDate = dt.with( TemporalAdjusters.nextOrSame( DayOfWeek.MONDAY ) ) ;
        String date = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        //obtaining the end of the week
        String endWeek = currentDate.with(TemporalAdjusters.next( DayOfWeek.MONDAY )).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            try(Connection connection = databaseConnection.getConnection();
                CallableStatement cs = connection.prepareCall("{? = call get_loading_unloading_day(?)}")) {


                while(!date.equals(endWeek)) {
                    cs.setString(2, date);
                    cs.registerOutParameter(1, Types.VARCHAR);
                    cs.executeUpdate();
                    String dayMap = cs.getString(1);

                    map.put(date, dayMap);

                    currentDate = currentDate.plusDays(1);
                    date = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }

                cs.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        return map;
        //throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");
    }

    public String getAuditTrailOfContainer(int containerId, int cargoManifestId){
        StringBuilder result = new StringBuilder();
        result.append("Audit trail:%n");
        int n = 0;
        String query = "select * from auditcontainerinmanifest where container_id=? AND cargomanifest_id=? order by AUD_WHEN";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (PreparedStatement createQuery = connection.prepareStatement(query)){
             createQuery.setInt(1, containerId);
             createQuery.setInt(2, cargoManifestId);

            try(ResultSet queryResult = createQuery.executeQuery()) {
                while (queryResult.next()) {
                    n++;
                    result.append(String.format("Register number %d%n", n));
                    result.append(String.format("Audit id: %d,", queryResult.getInt(1)));
                    result.append(String.format("User: %s,", queryResult.getNString(2)));
                    result.append(String.format("Date: %s,", queryResult.getDate(3)));
                    result.append(String.format("Operation performed: %s,", queryResult.getNString(4)));
                    result.append(String.format("Container id: %d,", queryResult.getInt(5)));
                    result.append(String.format("Cargo manifest id: %d,", queryResult.getInt(6)));
                    result.append(String.format("Temperature kept: %f%n", queryResult.getDouble(7)));
                }
            }
        } catch (SQLException e) {
            result.append("ERROR GETTING THE AUDIT TRAIL OF THE CONTAINER OF THE CARGO MANIFEST");
            Logger.getLogger(CargoManifestStoreDB.class.getName())
                    .log(Level.SEVERE, String.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(n == 0){
            return "No registers were found";
        }
        return result.toString();
    }

    /**
     * Check if cargo manifest exists in the data base.
     * @param cargoManifestID Cargo manifest ID.
     * @return 1 if cargo manifest exists and 0 if it doesn't.
     */
    public int checkIfCargoManifestExists(int cargoManifestID) {
        int result = 0;
        String createFunction = "create or replace function check_if_cargoManifest_exists(f_cargoManifest_id cargomanifest.cargomanifest_id%type) return integer%n" +
                "is%n" +
                "f_result integer;%n" +
                "begin%n" +
                "select count(*) into f_result%n" +
                "from cargomanifest%n" +
                "where cargomanifest_id = f_cargomanifest_id;%n" +
                "return (f_result);%n" +
                "exception%n" +
                "when no_data_found then%n" +
                "return 0;%n" +
                "end;";
        String runFunction = "{? = call check_if_cargoManifest_exists(?)}";
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
            Logger.getLogger(CargoManifestStoreDB.class.getName())
                    .log(Level.SEVERE, String.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get the number of containers loaded or unloaded by a cargo manifest.
     * @param cargoManifestID cargo manifest ID.
     * @return number of containers loaded or unloaded by a cargo manifest.
     */
    public int getNumContainersPerCargoManifest(int cargoManifestID){
        int result = 0;
        String createFunction = "create or replace function get_num_containers_per_cargoManifest(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer%n" +
                "is%n" +
                "f_num_containers_per_cargoManifest integer;%n" +
                "begin%n" +
                "select count(*) into f_num_containers_per_cargoManifest%n" +
                "from containerInCargoManifest%n" +
                "where cargoManifest_id = f_cargoManifest_id;%n" +
                "return (f_num_containers_per_cargoManifest);%n" +
                "exception%n" +
                "when no_data_found then%n" +
                "return 0;%n" +
                "end;";
        String runFunction = "{? = call get_num_containers_per_cargoManifest(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try(Statement createFunctionStat = connection.createStatement();
            CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(cargoManifestID));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        }catch (SQLException e) {
            Logger.getLogger(CargoManifestStoreDB.class.getName())
                    .log(Level.SEVERE, String.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get a cargo manifest given a mmsi and a date.
     * @param mmsi ship's mmsi.
     * @param date date to analyse.
     * @return cargo manifest ID.
     */
    public int getCargoManifestByMmsiAndDate(int mmsi, Date date){
        int result = 0;
        String createFunction = "create or replace function get_cargo_manifest_by_mmsi_and_date(f_mmsi shipTrip.mmsi%type, f_date shipTrip.est_departure_date%type) return cargoManifest.cargoManifest_id%type%n" +
                "is%n" +
                "f_shiptrip_id shipTrip.shiptrip_id%type;%n" +
                "f_unloading_cargo_id cargoManifest.cargoManifest_id%type;%n" +
                "f_est_departure_date shipTrip.est_departure_date%type;%n" +
                "f_est_arrival_date shipTrip.est_arrival_date%type;%n" +
                "f_cargoManifest_id cargoManifest.cargoManifest_id%type;%n" +
                "begin%n" +
                "%n" +
                "select shiptrip_id, unloading_cargo_id, est_departure_date, est_arrival_date, loading_cargo_id%n" +
                "into f_shiptrip_id, f_unloading_cargo_id, f_est_departure_date, f_est_arrival_date, f_cargoManifest_id%n" +
                "from%n" +
                "(select * from %n" +
                "(select * from shipTrip%n" +
                "where mmsi=f_mmsi AND est_departure_date<=f_date)%n" +
                "order by est_departure_date desc)%n" +
                "where rownum=1;%n" +
                "%n" +
                "if f_est_arrival_date <= f_date then%n" +
                "select unloading_cargo_id into f_cargoManifest_id%n" +
                "from shipTrip%n" +
                "where shiptrip_id=f_shiptrip_id;%n" +
                "end if;%n" +
                "%n" +
                "return (f_cargoManifest_id);%n" +
                "exception%n" +
                "when no_data_found then%n" +
                "return -1;%n   " +
                "end;";
        String runFunction = "{? = call get_cargo_manifest_by_mmsi_and_date(?,?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try(Statement createFunctionStat = connection.createStatement();
            CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(mmsi));
            callableStatement.setString(3, String.valueOf(date));

            callableStatement.executeUpdate();

            result = callableStatement.getInt(1);
        }catch (SQLException e) {
            Logger.getLogger(CargoManifestStoreDB.class.getName())
                    .log(Level.SEVERE, String.format("SQL State: %s%n%s", e.getSQLState(), e.getMessage()));
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
