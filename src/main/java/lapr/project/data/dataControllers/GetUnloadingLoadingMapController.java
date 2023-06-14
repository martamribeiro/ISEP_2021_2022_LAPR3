package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.CargoManifestStoreDB;
import lapr.project.data.DatabaseConnection;
import lapr.project.domain.model.Company;

import java.util.Map;

/**
 * Controller class to get the unloading and loading map for next week
 *
 * @author Ana Albergaria <1201518@isep.ipp.pt>
 */
public class GetUnloadingLoadingMapController {
    /**
     * Company instance of the session.
     */
    private Company company;
    /**
     * Constructor for the controller.
     */
    public GetUnloadingLoadingMapController() {
        this(App.getInstance().getCompany());
    }
    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public GetUnloadingLoadingMapController(Company company) {
        this.company=company;
    }

    public Map<String, String> getUnloadingLoadingMap() {
        CargoManifestStoreDB cargoManifestStoreDB = this.company.getCargoManifestStoreDB();
        DatabaseConnection databaseConnection = App.getInstance().getConnection();
        Map<String, String> map = cargoManifestStoreDB.getUnLoadingLoadingMap(databaseConnection);
        return map;
    }
}
