package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;

import java.util.HashMap;
import java.util.Map;

public class GetVesselSinkController {
    /**
     * Company instance of the session.
     */
    private Company company;
    /**
     * Constructor for the controller.
     */
    public GetVesselSinkController() {
        this(App.getInstance().getCompany());
    }
    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public GetVesselSinkController(Company company) {
        this.company=company;
    }

    public Map<String, Double> getHowMuchSinkShip(Ship ship, int numContainers) {
        Map<String, Double> map = new HashMap<>();
        map.put("Total Mass Placed", ship.totalMassPlaced(numContainers));
        map.put("Pressure", ship.getPressure(numContainers));
        map.put("Difference in Height", ship.getDiffHeightAboveWaterLevel(numContainers));
        return map;
    }
}
