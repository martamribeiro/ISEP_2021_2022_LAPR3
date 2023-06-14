package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.CargoManifestStoreDB;
import lapr.project.domain.model.Company;

public class AuditTrailController {

    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public AuditTrailController(){
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param companyy instance of company to be used.
     */
    public AuditTrailController(Company companyy){
        this.company=companyy;
    }

    public String getContainerAuditTrail(int containerId, int cargoManifestId){
        CargoManifestStoreDB cargoManifestStoreDB = this.company.getCargoManifestStoreDB();
        System.out.println(cargoManifestStoreDB.getAuditTrailOfContainer(containerId, cargoManifestId));
        return cargoManifestStoreDB.getAuditTrailOfContainer(containerId, cargoManifestId);
    }
}
