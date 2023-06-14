package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.RouteStoreDB;
import lapr.project.domain.model.Company;

import java.sql.SQLException;

/**
 * Controller class to get the current situation of a container
 *
 * @author Ana Albergaria <1201518@isep.ipp.pt>
 */
public class GetContainerSituationController {
    /**
     * Company instance of the session.
     */
    private Company company;
    /**
     * Constructor for the controller.
     */
    public GetContainerSituationController() {
        this(App.getInstance().getCompany());
    }
    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public GetContainerSituationController(Company company) {
        this.company=company;
    }

    /**
     * Method which obtains the current location of a certain container.
     * @param containerID the container id
     * @param registrationCode the shipment id
     *
     * @return current location of the container
     */
    public String getLocation(int containerID, int registrationCode) throws SQLException {
        RouteStoreDB routeStoreDB = this.company.getRouteStoreDB();
        DatabaseConnection connection = App.getInstance().getConnection();
        String routeID = routeStoreDB.getRouteId(connection, containerID, registrationCode);
        if(routeID.equals("10 – invalid container id") || routeID.equals("11 – container is not leased by client") || routeID.equalsIgnoreCase("Invalid data"))
            return routeID;

        String containerLocation = routeStoreDB.getContainerSituation(connection, Integer.parseInt(routeID));
        return containerLocation;
    }
}
