package lapr.project.utils;

import lapr.project.domain.model.Ship;

import java.util.Comparator;

public class ShipTravelledDistanceComparator implements Comparator<Ship> {

    @Override
    public int compare(Ship e1, Ship e2) {
        double d1 = e1.getPositionsBST().getTotalDistance();
        double d2 = e2.getPositionsBST().getTotalDistance();
        return Double.compare(d2, d1);
    }




}
