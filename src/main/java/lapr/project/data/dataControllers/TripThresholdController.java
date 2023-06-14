package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.DatabaseConnection;
import lapr.project.domain.model.Company;

import java.util.LinkedList;
import java.util.List;

public class TripThresholdController {
    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public TripThresholdController () {this(App.getInstance().getCompany());}

    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public TripThresholdController(Company company) {
        this.company=company;
    }

    /**
     * Method to get the list with the voyages with occupancy rate below 66%
     * @return List with voyages with an occupancy rate below 66%
     */
    public List<Integer> getVoyagesBelowThreshold() {
        DatabaseConnection connection = App.getInstance().getConnection();
        return company.getShipTripStoreDB().getListVoyages(connection);
    }
}
