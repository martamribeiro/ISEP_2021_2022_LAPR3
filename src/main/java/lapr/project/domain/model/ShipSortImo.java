package lapr.project.domain.model;

import lapr.project.domain.dataStructures.PositionsBST;

public class ShipSortImo extends Ship{
    /**
     * Constructs an instance of Ship receiving as a parameter the Ship's positions BST, MMSI, vessel name, IMO, call sign, vessel type, length, width, draft and cargo.
     *
     * @param positionsBST the Ship's positions BST.
     * @param MMSI         the Ship's MMSI
     * @param vesselName   the Ship's vessel name
     * @param IMO          the Ship's IMO
     * @param callSign     the Ship's call sign
     * @param vesselTypeID the Ship's vessel type
     * @param length       the Ship's length
     * @param width        the Ship's width
     * @param draft        the Ship's draft
     * @param cargo        the Ship's cargo
     */
    public ShipSortImo(PositionsBST positionsBST, int MMSI, String vesselName, String IMO, String callSign, int vesselTypeID, int length, int width, double draft, String cargo) {
        super(positionsBST, MMSI, vesselName, IMO, callSign, vesselTypeID, length, width, draft, cargo);
    }

    public ShipSortImo(String code){
        super();
        this.setImo(code);
    }

    @Override
    public int compareTo(Ship o) {
        return this.getImo().compareTo(o.getImo());
    }
}
