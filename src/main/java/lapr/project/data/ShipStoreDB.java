package lapr.project.data;

import lapr.project.controller.App;
import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.model.ShipSortMmsi;
import lapr.project.domain.store.ShipStore;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShipStoreDB implements Persistable{

    public String getIdleShips(DatabaseConnection databaseConnection) {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement cs = connection.prepareCall("{? = call all_ships_idle(?)}");){

            Calendar cal = Calendar.getInstance();
            int currentYear =  cal.get(Calendar.YEAR);
            cal.set(currentYear, 0, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = sdf.format(cal.getTime());
            cs.setString(2, dateString);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.executeUpdate();
            String ships = cs.getString(1);
            cs.close();
            return ships;


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");
    }

    /**
     * Get ship's max capacity by mmsi.
     * @param mmsi ship's mmsi.
     * @return ship's max capacity.
     */
    public int getShipMaxCapacity(int mmsi) {
        int result = 0;
        String createFunction = "create or replace function get_ship_max_capacity(f_mmsi ship.mmsi%type) return integer\n" +
                "is\n" +
                "\n" +
                "    f_max_capacity integer;\n" +
                "    teste varchar(10);\n" +
                "\n" +
                "begin\n" +
                "\n" +
                "    select currentCapacity into teste\n" +
                "        from ship\n" +
                "        where mmsi = f_mmsi;\n" +
                "\n" +
                "    if teste = 'NA' then return null;\n" +
                "    else f_max_capacity := to_number(teste);\n" +
                "    end if;\n" +
                "    return (f_max_capacity);\n" +
                "\n" +
                "    exception\n" +
                "        when no_data_found then\n" +
                "        return 0;\n" +
                "        \n" +
                "end;";
        String runFunction = "{? = call get_ship_max_capacity(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(mmsi));

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
     * Get number of container in ship on a day.
     * @param mmsi ship's mmsi.
     * @param someDate day to be analysed.
     * @return number of container in ship on a day.
     */
    public int getNumContainersShipDay(int mmsi, Date someDate) {
        int result = 0;
        String createFunction = "create or replace function get_num_containers_ship_day(f_mmsi ship.mmsi%type, f_date shipTrip.real_departure_date%type) return integer\n" +
                "is\n" +
                "\n" +
                "    f_current_num integer:=0;\n" +
                "    f_shiptrip_id shipTrip.shiptrip_id%type;\n" +
                "    f_loading_cargo_id shipTrip.loading_cargo_id%type;\n" +
                "    f_unloading_cargo_id shipTrip.unloading_cargo_id%type;\n" +
                "    \n" +
                "    cursor loadingCargos\n" +
                "        is\n" +
                "        (select loading_cargo_id\n" +
                "        from shipTrip\n" +
                "        where mmsi=f_mmsi AND real_departure_date < f_date);\n" +
                "        \n" +
                "    cursor unloadingCargos\n" +
                "        is\n" +
                "        (select unloading_cargo_id\n" +
                "        from shipTrip\n" +
                "        where mmsi=f_mmsi AND real_arrival_date < f_date);\n" +
                "        \n" +
                "begin\n" +
                "    \n" +
                "    open loadingCargos;\n" +
                "    loop\n" +
                "        fetch loadingCargos into f_loading_cargo_id;\n" +
                "        exit when loadingCargos%notfound;\n" +
                "        f_current_num := f_current_num + get_num_containers_per_cargoManifest(f_loading_cargo_id);\n" +
                "    end loop;\n" +
                "    \n" +
                "    open unloadingCargos;\n" +
                "    loop\n" +
                "        fetch unloadingCargos into f_unloading_cargo_id;\n" +
                "        exit when unloadingCargos%notfound;\n" +
                "        f_current_num := f_current_num + get_num_containers_per_cargoManifest(f_unloading_cargo_id);\n" +
                "    end loop;\n" +
                "    \n" +
                "    return f_current_num;\n" +
                "    \n" +
                "    exception\n" +
                "        when no_data_found then\n" +
                "        return 0;\n" +
                "        \n" +
                "end;\n";
        String runFunction = "{? = call get_num_containers_ship_day(?,?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(mmsi));
            callableStatement.setString(3, String.valueOf(someDate));

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
     * Check if ship exists in the data base.
     * @param mmsi Ship's mmsi.
     * @return 1 if ship exists and 0 if it doesn't.
     */
    public int checkIfShipExists(int mmsi) {
        int result = 0;
        String createFunction = "create or replace function check_if_ship_exists(f_mmsi ship.mmsi%type) return integer\n" +
                "is\n" +
                "f_result integer;\n" +
                "begin\n" +
                "select count(*) into f_result\n" +
                "from ship\n" +
                "where mmsi = f_mmsi;\n" +
                "return (f_result);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call check_if_ship_exists(?)}";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try (Statement createFunctionStat = connection.createStatement();
             CallableStatement callableStatement = connection.prepareCall(runFunction)) {
            createFunctionStat.execute(createFunction);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, String.valueOf(mmsi));

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

    public String getNextMondayAvailableShips(){
        String returnMessage = "";
        String createValidatedShipProc = "create or replace PROCEDURE get_validated_ship_location(daydate in date, ship in Ship.mmsi%type, locations out varchar)\n" +
                "IS\n" +
                "    locationCode port.port_id%type;\n" +
                "    shiptripid Shiptrip.shiptrip_id%type;\n" +
                "    \n" +
                "BEGIN\n" +
                "        -- since we know the ship is available on monday we have to get the closest arrival to monday and extract its port\n" +
                "        select shiptrip_id into shiptripid from shiptrip where mmsi = ship AND  \n" +
                "        EST_ARRIVAL_DATE = (select max(EST_ARRIVAL_DATE) from shiptrip where mmsi=ship);\n" +
                "        select arrival_location into locationCode from shiptrip where shiptrip_id = shiptripid;\n" +
                "        select name into locations from port where port_id = locationCode;\n" +
                "        dbms_output.put_line('cargo load id: ' || locations);\n" +
                "END;";

        String createFindLocationProc = "create or replace PROCEDURE check_availability_of_ship(daydate in date, ship in Ship.mmsi%type, isValid out boolean)\n" +
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
        String createMainProc = "create or replace PROCEDURE available_ships_after_day (daydate in Varchar, ships out Varchar, locationsOfShips out Varchar)\n" +
                "IS\n" +
                "    shipscode Ship.mmsi%type;\n" +
                "    hasArrivalBefore integer;\n" +
                "    availableShips Varchar(32700);\n" +
                "    locations Varchar(32700);\n" +
                "    nextmonday date;\n" +
                "    isAvailable boolean;\n" +
                "    \n" +
                "    Cursor shipsInTrips IS\n" +
                "        select distinct (mmsi)\n" +
                "        from shiptrip;\n" +
                "\n" +
                "BEGIN\n" +
                "\n" +
                "    nextMonday := TO_DATE(daydate, 'dd/mm/yyyy');\n" +
                "    locationsOfShips := '';\n" +
                "    ships := '';\n" +
                "   open shipsInTrips;\n" +
                "    LOOP\n" +
                "        fetch shipsInTrips INTO shipscode;\n" +
                "         dbms_output.put_line('code ' || shipscode);\n" +
                "        Exit When shipsInTrips%notfound;\n" +
                "        --makes avaialbility false by default\n" +
                "        isAvailable := false;\n" +
                "        --checks ships is available;\n" +
                "        check_availability_of_ship(nextmonday, shipscode, isAvailable);\n" +
                "        if isAvailable then\n" +
                "             ships := ships || ' ' || shipscode;\n" +
                "             get_validated_ship_location(nextMonday, shipscode, locations);\n" +
                "             locationsOfShips := locationsOfShips || ' ' || locations;\n" +
                "        end if;\n" +
                "    END LOOP;\n" +
                "    dbms_output.put_line('trip ' || ships);\n" +
                "    dbms_output.put_line('trip ' || locationsOfShips);\n" +
                "     EXCEPTION\n" +
                "        WHEN no_data_found THEN \n" +
                "        ships := 'no data';\n" +
                "        locationsOfShips := 'no data';\n" +
                "END;";

        String runSP = "{ call available_ships_after_day(?,?,?) }";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try(Statement createPrcedureStat = connection.createStatement();
            CallableStatement callableStatement = connection.prepareCall(runSP)) {
            createPrcedureStat.execute(createValidatedShipProc);
            createPrcedureStat.execute(createFindLocationProc);
            createPrcedureStat.execute(createMainProc);
            LocalDate dt = LocalDate.now();
            String nextMonday = dt.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println(nextMonday);
            callableStatement.setString(1, nextMonday);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            callableStatement.registerOutParameter(3, Types.VARCHAR);


            callableStatement.executeUpdate();

            String ships = callableStatement.getString(2);
            String locations = callableStatement.getString(3);
            if(ships != null && locations !=null) {
                StringBuilder result = new StringBuilder("Available ships:\n_______\n");
                String[] locationsArray = locations.split(" ");
                String[] shipsArray = ships.split(" ");
                for (int i = 1; i < shipsArray.length; i++) {
                    result.append(String.format("Ship code: %s\nLocation(Port): %s\n_______\n", shipsArray[i], locationsArray[i]));
                }
                returnMessage = result.toString();
            }else{
                return "No ship was found.";
            }
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    /**
     * Get a ship cargo (maximum capacity) by a cargo manifest ID.
     * @param cargoManifestID cargo manifest ID.
     * @return ship cargo.
     */
    public int getShipCargo(int cargoManifestID){
        int result = 0;
        String createFunction = "create or replace function get_max_capacity(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer\n" +
                "is\n" +
                "f_mmsi shipTrip.mmsi%type;\n" +
                "f_max_capacity integer;\n" +
                "begin\n" +
                "select mmsi into f_mmsi\n" +
                "from shipTrip\n" +
                "where loading_cargo_id = f_cargoManifest_id OR unloading_cargo_id = f_cargoManifest_id;\n" +
                "select to_number(currentCapacity) into f_max_capacity\n" +
                "from ship\n" +
                "where mmsi = f_mmsi;\n" +
                "return (f_max_capacity);\n" +
                "exception\n" +
                "when no_data_found then\n" +
                "return 0;\n" +
                "end;";
        String runFunction = "{? = call get_max_capacity(?)}";
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
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getNumberOfCMAndAverageContForYear(int year, int mmsi){
        String returnMessage=String.format("None cargo manifests were carryed in %d", year);
        String createProcedure = "CREATE OR REPLACE PROCEDURE count_CargoManifests_Avg_Containers (givenYear in Varchar, mmsiCode in Varchar, numCargoManifests out Integer, mediaCont out Integer)\n" +
                "IS\n" +
                "    shipscode Ship.mmsi%type;\n" +
                "    loadcargoscode cargomanifest.cargomanifest_id%type;\n" +
                "    unloadcargoscode cargomanifest.cargomanifest_id%type;\n" +
                "    numContainersInCm Integer;\n" +
                "    somaTotalContainer Integer;\n" +
                "    contadorCm Integer;\n" +
                "\n" +
                "    Cursor cargosLoad IS\n" +
                "        Select loading_cargo_id\n" +
                "        from shiptrip\n" +
                "        where mmsi=mmsiCode AND extract(year from real_departure_date)=givenYear;\n" +
                "\n" +
                "    Cursor cargosUnload IS\n" +
                "        Select unloading_cargo_id\n" +
                "        from shiptrip\n" +
                "        where mmsi=mmsiCode AND extract(year from real_arrival_date)=givenYear;\n" +
                "BEGIN\n" +
                "    contadorCm :=0;\n" +
                "    somaTotalContainer:=0;\n" +
                "\n" +
                "   open cargosLoad;\n" +
                "    LOOP\n" +
                "        fetch cargosLoad INTO loadcargoscode;\n" +
                "        Exit When cargosLoad%notfound;\n" +
                "        dbms_output.put_line('cargo load id: ' || loadcargoscode);\n" +
                "        contadorCm := contadorCm + 1;\n" +
                "        dbms_output.put_line('cargocount ' || contadorCm);\n" +
                "        select count(*) into numContainersInCm from containerincargomanifest where cargomanifest_id=loadcargoscode;\n" +
                "        somaTotalContainer := somaTotalContainer + numContainersInCm;\n" +
                "        dbms_output.put_line('cont count ' || somaTotalContainer);\n" +
                "    END LOOP;\n" +
                "\n" +
                "    open cargosUnload;\n" +
                "     LOOP\n" +
                "        fetch cargosUnload INTO unloadcargoscode;\n" +
                "        Exit When cargosUnload%notfound;\n" +
                "        dbms_output.put_line('cargo unload id: ' || unloadcargoscode);\n" +
                "        contadorCm := contadorCm + 1;\n" +
                "        dbms_output.put_line('cargocount ' || contadorCm);\n" +
                "        select count(*) into numContainersInCm from containerincargomanifest where cargomanifest_id=unloadcargoscode;\n" +
                "        somaTotalContainer := somaTotalContainer + numContainersInCm;\n" +
                "        dbms_output.put_line('cont count ' || somaTotalContainer);\n" +
                "    END LOOP;\n" +
                "\n" +
                "    numCargoManifests := contadorCm;\n" +
                "    mediaCont := (somaTotalContainer / contadorCm);\n" +
                "    dbms_output.put_line('final number of CM ' || numCargoManifests);\n" +
                "    dbms_output.put_line('mean of containers ' || mediaCont);\n" +
                "END;";

        String runSP = "{ call count_CargoManifests_Avg_Containers(?,?,?,?) }";
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Connection connection = databaseConnection.getConnection();
        try(Statement createPrcedureStat = connection.createStatement();
            CallableStatement callableStatement = connection.prepareCall(runSP)) {
            createPrcedureStat.execute(createProcedure);
            callableStatement.setString(1, String.valueOf(year));
            callableStatement.setString(2, String.valueOf(mmsi));
            callableStatement.registerOutParameter(3, Types.INTEGER);
            callableStatement.registerOutParameter(4, Types.INTEGER);

            callableStatement.executeUpdate();

            int numCm = callableStatement.getInt(3);
            int avgContainer = callableStatement.getInt(4);
            returnMessage = String.format("Number of cargo manifests in %d:\n%d\nAverage number of containers per cargo manifest:\n%d\n", year, numCm, avgContainer);
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        boolean returnValue;
        returnValue = false;
        if(object instanceof Ship){
            try{
                Ship toSave = (Ship) object;

                saveNewShipOnDatabase(toSave, databaseConnection);

                saveShipPositions(toSave, databaseConnection);
            }catch (SQLException ex) {
                Logger.getLogger(ShipStore.class.getName())
                        .log(Level.SEVERE, null, ex);
                databaseConnection.registerError(ex);
                returnValue = false;
            }
        }else if(object instanceof ShipPosition){
            ShipPosition positionToSave = (ShipPosition) object;
            Ship mmsiShipToSearch = new ShipSortMmsi(positionToSave.getMMSI());
            try {
                if (!isShipInDataBase(databaseConnection, mmsiShipToSearch)) {
                    throw new SQLException("Ship must exist to save a position with its mmsi");
                }
                PositionsDB positionsStore = new PositionsDB();
                positionsStore.save(databaseConnection, positionToSave);
            }catch (SQLException ex) {
                Logger.getLogger(ShipStore.class.getName())
                        .log(Level.SEVERE, ex.getMessage(), ex);
                databaseConnection.registerError(ex);
                returnValue = false;
            }
        }
        return returnValue;
    }

    private void saveNewShipOnDatabase(Ship ship, DatabaseConnection databaseConnection) throws SQLException {
        if(isShipInDataBase(databaseConnection, ship)){
            updateShipOnDatabase(databaseConnection, ship);
        }else{
            insertShipOnDataBase(databaseConnection, ship);
        }
    }

    private void saveShipPositions(Ship ship, DatabaseConnection databaseConnection) throws SQLException {
        if(ship.getPositionsBST().size() > 0) {
            PositionsDB positionsStore = new PositionsDB();
            if(!isShipInDataBase(databaseConnection, ship)) {
                throw new SQLException("Ship must exist to save a position with its mmsi");
            }
            for (ShipPosition shipPosition : ship.getPositionsBST().inOrder()) {
                if (!positionsStore.save(databaseConnection, shipPosition)) {
                    throw databaseConnection.getLastError();
                }
            }
        }
    }

    private void insertShipOnDataBase(DatabaseConnection databaseConnection, Ship ship) throws SQLException {
        Connection connection = databaseConnection.getConnection();
        String sqlCommand =
                "insert into ship(mmsi, vesselTypeId, imo, callSign, shipName, currentCapacity, draft, length, width) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement saveShipStatement =
                connection.prepareStatement(
                        sqlCommand)) {
            saveShipStatement.setInt(1, ship.getMmsi());
            saveShipStatement.setInt(2, ship.getVesselTypeID());
            saveShipStatement.setString(3, ship.getImo());
            saveShipStatement.setString(4, ship.getCallSign());
            saveShipStatement.setString(5, ship.getVesselName());
            saveShipStatement.setString(6, ship.getCargo());
            saveShipStatement.setDouble(7, ship.getDraft());
            saveShipStatement.setInt(8, ship.getLength());
            saveShipStatement.setInt(9, ship.getWidth());
            System.out.println(ship);
            saveShipStatement.executeUpdate();
        }
    }

    private void updateShipOnDatabase(DatabaseConnection databaseConnection, Ship ship) throws SQLException {
        String sqlCommand =
                "update ship set mmsi = ?, vesseltypeid = ? imo = ? callsign = ? " +
                        "shipname = ? currentcapacity = ? draft = ? where mmsi = ?";

        executeShipStatement(databaseConnection, ship,
                sqlCommand);
    }

    private void executeShipStatement(
            DatabaseConnection databaseConnection,
            Ship ship, String sqlCommand) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        try(PreparedStatement saveShipStatement =
                connection.prepareStatement(
                        sqlCommand)) {
            saveShipStatement.setInt(1, ship.getMmsi());
            saveShipStatement.setInt(2, ship.getVesselTypeID());
            saveShipStatement.setString(3, ship.getImo());
            saveShipStatement.setString(4, ship.getCallSign());
            saveShipStatement.setString(5, ship.getVesselName());
            saveShipStatement.setString(6, ship.getCargo());
            saveShipStatement.setDouble(7, ship.getDraft());
            saveShipStatement.setInt(8, ship.getMmsi());
            saveShipStatement.executeUpdate();
        }
    }

    private boolean isShipInDataBase(DatabaseConnection databaseConnection,
                                     Ship ship) throws SQLException {
        Connection connection = databaseConnection.getConnection();

        boolean isShipInDataBase = false;

        String sqlCommand = "select * from ship where mmsi = ?";

        try(PreparedStatement getShipPreparedStatement =
                connection.prepareStatement(sqlCommand)) {
            getShipPreparedStatement.setInt(1, ship.getMmsi());

            try (ResultSet shipResultSet = getShipPreparedStatement.executeQuery()) {

                if (shipResultSet.next()) {
                    // if client already exists in the database
                    isShipInDataBase = true;
                } else {
                    // if client does not exist in the database
                    isShipInDataBase = false;
                }
            }
        }
        return isShipInDataBase;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        boolean returnValue = false;
        Connection connection = databaseConnection.getConnection();
        Ship ship = (Ship) object;

        try {
            String sqlCommand;
            sqlCommand = "delete from ship where mmsi = ?";
            try (PreparedStatement deleteClienteAddressesPreparedStatement = connection.prepareStatement(
                    sqlCommand)) {
                deleteClienteAddressesPreparedStatement.setInt(1,
                        ship.getMmsi());
                deleteClienteAddressesPreparedStatement.executeUpdate();
            }

            //dont need to delete positions because table has ON DELETE CASCADE
            returnValue = true;

        } catch (SQLException exception) {
            Logger.getLogger(ShipStore.class.getName())
                    .log(Level.SEVERE, null, exception);
            databaseConnection
                    .registerError(exception);
            returnValue = false;
        }

        return returnValue;
    }


    /**
     * Calculate occupancy rate with maxCapacity and currentCapacity.
     * @param maxCapacity warehouse max capacity.
     * @param currentCapacity number of containers in the ship at a time.
     * @return ship occupancy rate at a time.
     */
    public double calculateOccupancyRate(double maxCapacity, double currentCapacity){
        if (currentCapacity>maxCapacity){
            return -1; //when invalid
        } else {
            return (currentCapacity*100.0/maxCapacity);
        }
    }

}
