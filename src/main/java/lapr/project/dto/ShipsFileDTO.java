package lapr.project.dto;

/**
 * Class to instantiate a new ShipsFileDTO
 *
 * @author Marta Ribeiro (1201592)
 */
public class ShipsFileDTO {

    /**
     * The ShipFileDTO's positionDTO.
     */
    PositionDTO positionDTO;

    /**
     * The ShipFileDTO's mmsi.
     */
    private final int mmsi;

    /**
     * The ShipFileDTO's vessel name.
     */
    private final String vesselName;

    /**
     * The ShipFileDTO's imo.
     */
    private final String imo;

    /**
     * The ShipFileDTO's call sign.
     */
    private final String callSign;

    /**
     * The ShipFileDTO's vessel type.
     */
    private final int vesselType;

    /**
     * The ShipFileDTO's ship length.
     */
    private final int length;

    /**
     * The ShipFileDTO's ship width.
     */
    private final int width;

    /**
     * The ShipFileDTO's ship draft.
     */
    private final double draft;

    /**
     * The ShipFileDTO's ship cargo.
     */
    private final String cargo;

    /**
     * Constructs an instance of ShipFileDTO receiving as a parameter the ShipFileDTO's positionDTO, MMSI, vessel name, IMO, call sign, vessel type, length, width, draft and cargo.
     * @param positionDTO the ShipFileDTO's positionDTO
     * @param mmsi the ShipFileDTO's MMSI
     * @param vesselName the ShipFileDTO's vessel name
     * @param imo the ShipFileDTO's IMO
     * @param callSign the ShipFileDTO's call sign
     * @param vesselType the ShipFileDTO's vessel type
     * @param length the ShipFileDTO's length
     * @param width the ShipFileDTO's width
     * @param draft the ShipFileDTO's draft
     * @param cargo the ShipFileDTO's cargo
     */
    public ShipsFileDTO(PositionDTO positionDTO, int mmsi, String vesselName, String imo, String callSign,
                        int vesselType, int length, int width, double draft, String cargo) {
        this.positionDTO = positionDTO;
        this.mmsi=mmsi;
        this.vesselName=vesselName;
        this.imo=imo;
        this.callSign=callSign;
        this.vesselType=vesselType;
        this.length=length;
        this.width=width;
        this.draft=draft;
        this.cargo=cargo;
    }

    /**
     * Returns the ShipFileDTO's PositionDTO.
     *
     * @return the ShipFileDTO's PositionDTO.
     */
    public PositionDTO getPositionDTO() {
        return positionDTO;
    }

    /**
     * Returns the ShipFileDTO's MMSI.
     *
     * @return the ShipFileDTO's MMSI.
     */
    public int getMmsi() {
        return mmsi;
    }

    /**
     * Returns the ShipFileDTO's vessel name.
     *
     * @return the ShipFileDTO's vessel name.
     */
    public String getVesselName() {
        return vesselName;
    }

    /**
     * Returns the ShipFileDTO's imo.
     *
     * @return the ShipFileDTO's imo.
     */
    public String getImo() {
        return imo;
    }

    /**
     * Returns the ShipFileDTO's call sign.
     *
     * @return the ShipFileDTO's call sign.
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * Returns the ShipFileDTO's vessel type.
     *
     * @return the ShipFileDTO's vessel type.
     */
    public int getVesselType() {
        return vesselType;
    }

    /**
     * Returns the ShipFileDTO's length.
     *
     * @return the ShipFileDTO's length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the ShipFileDTO's width.
     *
     * @return the ShipFileDTO's width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the ShipFileDTO's cargo.
     *
     * @return the ShipFileDTO's cargo.
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Returns the ShipFileDTO's draft.
     *
     * @return the ShipFileDTO's draft.
     */
    public double getDraft() {
        return draft;
    }

    /**
     * Method equals.
     * @param otherObject the object to be compared with.
     * @return true if a ShipsFileDTO is equal to the object in "otherObject";
     * false if a ShipsFileDTO isn't equal to the object in "otherObject".
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || this.getClass() != otherObject.getClass()) return false;
        ShipsFileDTO otherShipsFileDTO = (ShipsFileDTO) otherObject;
        if (this.getMmsi()==otherShipsFileDTO.getMmsi() && this.getPositionDTO()==otherShipsFileDTO.getPositionDTO())
            return true;
        else
            return false;
    }

    /**
     * Method toString.
     * @return a String with the ShipsFileDTO attributes and its values.
     */
    @Override
    public String toString() {
        return "ShipsFileDTO{" +
                "positionDTO=" + positionDTO +
                ", mmsi=" + mmsi +
                ", vesselType=" + vesselType +
                ", length=" + length +
                ", width=" + width +
                ", cargo=" + cargo +
                ", draft=" + draft +
                ", vesselName='" + vesselName + '\'' +
                ", imo='" + imo + '\'' +
                ", callSign='" + callSign + '\'' +
                '}';
    }

}
