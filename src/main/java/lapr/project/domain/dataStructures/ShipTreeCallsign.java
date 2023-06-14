package lapr.project.domain.dataStructures;

import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipSortCallSign;

public class ShipTreeCallsign extends ShipTree{

    @Override
    public Ship createShip(PositionsBST positionsBST, int MMSI, String vesselName, String IMO, String callSign, int vesselTypeID, int length, int width, double draft, String cargo) {
        return new ShipSortCallSign(positionsBST, MMSI, vesselName, IMO, callSign, vesselTypeID, length, width, draft, cargo);
    }

    @Override
    public Ship getShip(String codShip) {
        Ship boat = new ShipSortCallSign(codShip);
        return find(boat);
    }
}
