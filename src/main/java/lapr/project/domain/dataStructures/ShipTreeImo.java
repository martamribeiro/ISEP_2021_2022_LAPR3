package lapr.project.domain.dataStructures;

import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipSortImo;

public class ShipTreeImo extends ShipTree{

    @Override
    public Ship createShip(PositionsBST positionsBST, int MMSI, String vesselName, String IMO, String callSign, int vesselTypeID, int length, int width, double draft, String cargo) {
        return new ShipSortImo(positionsBST, MMSI, vesselName, IMO, callSign, vesselTypeID, length, width, draft, cargo);
    }

    @Override
    public Ship getShip(String codShip) {
        Ship boat = new ShipSortImo(codShip);
        return find(boat);
    }
}
