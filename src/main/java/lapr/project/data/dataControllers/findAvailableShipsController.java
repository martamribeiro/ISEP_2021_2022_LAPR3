package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.ShipStoreDB;
import lapr.project.domain.model.Company;

public class findAvailableShipsController {

    private final Company company;

    public findAvailableShipsController(){
        this.company= App.getInstance().getCompany();
    }

    public findAvailableShipsController(Company company) {
        this.company = company;
    }

    public String getNextMondayAvailableShips(){
        ShipStoreDB shipStoreDB = this.company.getShipStoreDB();
        return shipStoreDB.getNextMondayAvailableShips();
    }
}
