package lapr.project.utils;

import lapr.project.domain.model.Ship;

import java.util.Comparator;

public class ShipTotalMovementsComparator implements Comparator<Ship> {

    @Override
    public int compare(Ship e1, Ship e2) {
        double d1 = e1.getPositionsBST().size();
        double d2 = e2.getPositionsBST().size();
        return Double.compare(d1, d2);
    }



}
