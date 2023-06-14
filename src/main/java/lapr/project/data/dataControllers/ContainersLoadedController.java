package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.ShipTripStoreDB;
import lapr.project.domain.model.Company;

import java.util.List;

public class ContainersLoadedController {
    /**
    * Company instance of the session.
     */
    Company comp;

    /**
     * Constructor for the controller.
     */
    public ContainersLoadedController() {
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public ContainersLoadedController(Company company) {
        this.comp = company;
    }

    /**
     * method that gets the next Port on the Route and the containers to be loaded in it
     * @param mmsi Ship mmsi
     * @throws Exception database exception
     */
    public List<Integer> getListLoadedContainers(int mmsi) throws Exception {
        ShipTripStoreDB store = comp.getShipTripStoreDB();
        DatabaseConnection connection = App.getInstance().getConnection();
        return store.getListLoadedContainers(connection, mmsi);
        //throw new Exception("to be developed.");
    }
}
