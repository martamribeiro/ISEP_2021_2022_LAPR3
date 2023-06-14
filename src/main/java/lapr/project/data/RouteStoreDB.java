package lapr.project.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class RouteStoreDB {

    public String getRouteId(DatabaseConnection databaseConnection, int containerId, int registrationCode) throws SQLException {
        try {
            Connection connection = databaseConnection.getConnection();
            CallableStatement cs = connection.prepareCall("{? = call get_route_id(?, ?)}");
            cs.setInt(2, containerId);
            cs.setInt(3, registrationCode);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.executeUpdate();
            String routeId = cs.getString(1);
            cs.close();
            return routeId;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");
    }

    public String getContainerPath(DatabaseConnection databaseConnection, int routeId) {
        try {
            Connection connection = databaseConnection.getConnection();
            CallableStatement cs = connection.prepareCall("{? = call get_path_function(?)}");
            cs.setInt(2, routeId);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.executeUpdate();
            String route = cs.getString(1);
            cs.close();
            return route;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");
    }

    public String getContainerSituation(DatabaseConnection databaseConnection, int routeId) {
        try {
            Connection connection = databaseConnection.getConnection();
            CallableStatement cs = connection.prepareCall("{? = call get_location(?)}");
            cs.setInt(2, routeId);
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.executeUpdate();
            String location = cs.getString(1);
            cs.close();
            return location;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        throw new UnsupportedOperationException("Some error with the Data Base occured. Please try again.");
    }

}
