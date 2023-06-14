package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.ShipStoreDB;
import lapr.project.domain.model.Company;

public class NumCargoManifestsController {
    /**
     * The company associated to the Controller.
     */
    private final Company company;

    /**
     * empty constructor for the TopNController Class
     */
    public NumCargoManifestsController(){
        this.company= App.getInstance().getCompany();
    }


    public NumCargoManifestsController(Company company) {this.company = company;}

    public String getNumberOfCMAndAverageContForYear(int year, int mmsi){
        ShipStoreDB shipStoreDB = this.company.getShipStoreDB();
        return shipStoreDB.getNumberOfCMAndAverageContForYear(year, mmsi);
    }
}
