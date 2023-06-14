package lapr.project.data;

import lapr.project.domain.model.ShipPosition;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PositionsDB implements Persistable {
    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Connection connection = databaseConnection.getConnection();
        ShipPosition shipPosition = (ShipPosition) object;

        String sqlCommand = "select * from shipPosition where (baseDateTime = ? AND latitude = ? AND longitude = ? AND mmsi = ?)";
        boolean returnValue = false;
        try (PreparedStatement getShipPositionStatement = connection.prepareStatement(
                sqlCommand)) {
            getShipPositionStatement.setDate(1, new Date(shipPosition.getBaseDateTime().getTime()));
            getShipPositionStatement.setDouble(2,shipPosition.getLat());
            getShipPositionStatement.setDouble(3,shipPosition.getLon());
            getShipPositionStatement.setInt(4,shipPosition.getMMSI());

            try (ResultSet addressesResultSet = getShipPositionStatement.executeQuery()) {
                if (addressesResultSet.next()) {
                    sqlCommand =
                            "update shipposition set sog = ?, cog = ?, heading = ?, transceiver = ? where (baseDateTime = ? AND latitude = ? AND longitude = ? AND mmsi = ?)";
                } else {
                    sqlCommand =
                            "insert into shipPosition(sog, cog, heading, transceiver, baseDateTime, latitude, longitude, mmsi) " +
                                    "values (?, ?, ?, ?, ?, ?, ?, ?)";
                }

                try (PreparedStatement saveShipUpdate = connection.prepareStatement(
                        sqlCommand)) {
                    saveShipUpdate.setDouble(1, shipPosition.getSog());
                    saveShipUpdate.setDouble(2,shipPosition.getCog());
                    saveShipUpdate.setDouble(3,shipPosition.getHeading());
                    saveShipUpdate.setString(4,shipPosition.getTranscieverClass());
                    saveShipUpdate.setDate(5, new Date(shipPosition.getBaseDateTime().getTime()));
                    saveShipUpdate.setDouble(6,shipPosition.getLat());
                    saveShipUpdate.setDouble(7,shipPosition.getLon());
                    saveShipUpdate.setInt(8,shipPosition.getMMSI());
                    saveShipUpdate.executeUpdate();
                    returnValue = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PositionsDB.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        Connection conn = databaseConnection.getConnection();
        ShipPosition shipPosition = (ShipPosition) object;

        boolean returnValue = false;
        try {
            String sqlCommand = "select * from shipPosition where (baseDateTime = ? AND latitude = ? AND longitude = ? AND mmsi = ?)";
            try (PreparedStatement deleteAddressPreparedStatement = conn.prepareStatement(
                    sqlCommand)) {
                deleteAddressPreparedStatement.setDate(1, (Date) shipPosition.getBaseDateTime());
                deleteAddressPreparedStatement.setDouble(2,shipPosition.getLat());
                deleteAddressPreparedStatement.setDouble(3,shipPosition.getLon());
                deleteAddressPreparedStatement.setInt(4,shipPosition.getMMSI());
                deleteAddressPreparedStatement.executeUpdate();
                returnValue = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ShipPosition.class.getName())
                    .log(Level.SEVERE, null, ex);
            databaseConnection.registerError(ex);
            returnValue = false;
        }
        return returnValue;
    }
}
