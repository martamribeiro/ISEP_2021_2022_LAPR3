package lapr.project.domain.dataStructures;

import lapr.project.genericDataStructures.AVL;
import lapr.project.domain.model.Ship;

public abstract class ShipTree <E extends  Ship> extends AVL<Ship> {

    public abstract Ship createShip(PositionsBST positionsBST, int MMSI,
                                    String vesselName, String IMO, String callSign, int vesselTypeID, int length, int width, double draft, String cargo);

    public abstract Ship getShip(String codShip);

    protected Ship find(Ship boat){
        if(find(root, boat) == null){
            return null;
        }
        return find(root, boat).getElement();
    }

}
