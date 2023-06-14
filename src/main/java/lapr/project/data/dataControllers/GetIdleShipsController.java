package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.ShipStoreDB;
import lapr.project.domain.model.Company;
import lapr.project.domain.store.ShipStore;

/**
 * Controller class to get the number of days each ship has been idle since the beginning of year
 *
 * @author Ana Albergaria <1201518@isep.ipp.pt>
 */
public class GetIdleShipsController {
    /**
     * Company instance of the session.
     */
    private Company company;
    /**
     * Constructor for the controller.
     */
    public GetIdleShipsController() {
        this(App.getInstance().getCompany());
    }
    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public GetIdleShipsController(Company company) {
        this.company=company;
    }

    /**
     * Method which returns the number of days in which each ship which has been idle since the beginning of the current year
     *
     * @return number of days in which each ship which has been idle since the beginning of the current year
     */
    public String getIdleShips() {
        ShipStoreDB shipStoreDB = this.company.getShipStoreDB();
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        String idleShips = shipStoreDB.getIdleShips(databaseConnection);
        return idleShips;
    }
}
