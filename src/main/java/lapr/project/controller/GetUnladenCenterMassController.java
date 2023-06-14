package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;

import java.awt.geom.Point2D;


public class GetUnladenCenterMassController {
    /**
     * Company instance of the session.
     */
    private Company company;
    /**
     * Constructor for the controller.
     */
    public GetUnladenCenterMassController() {
        this(App.getInstance().getCompany());
    }
    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public GetUnladenCenterMassController(Company company) {
        this.company=company;
    }

    public Point2D.Double getUnladenCenterOfMass(Ship ship) {
        Point2D.Double unladenCenterOfMass = ship.getCenterOfMass();
        return unladenCenterOfMass;
    }


}
