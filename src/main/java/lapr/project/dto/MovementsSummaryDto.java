package lapr.project.dto;

import java.util.Date;

public class MovementsSummaryDto {
    private final String name;
    private final int MMSI;
    private final Date startDate;
    private final Date endDate;
    private final double maxSog;
    private final double meanSog;
    private final double meanCog;
    private final double departLatitude;
    private final double departLongitude;
    private final double arrivalLatitude;
    private final double arrivalLongitude;
    private final double travelledDist;
    private final double deltaDist;

    public MovementsSummaryDto(String name, Date startDate, Date endDate, double maxSog, double meanSog,
                               double meanCog, double departLatitude, double departLongitude, double arrivalLatitude,
                               double arrivalLongitude, double travelledDist, double deltaDist, int mmsi) {
        this.name = name;
        this.MMSI = mmsi;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxSog = maxSog;
        this.meanSog = meanSog;
        this.meanCog = meanCog;
        this.departLatitude = departLatitude;
        this.departLongitude = departLongitude;
        this.arrivalLatitude = arrivalLatitude;
        this.arrivalLongitude = arrivalLongitude;
        this.travelledDist = travelledDist;
        this.deltaDist = deltaDist;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getMaxSog() {
        return maxSog;
    }

    public double getMeanSog() {
        return meanSog;
    }

    public double getMeanCog() {
        return meanCog;
    }

    public double getDepartLatitude() {
        return departLatitude;
    }

    public double getDepartLongitude() {
        return departLongitude;
    }

    public double getArrivalLatitude() {
        return arrivalLatitude;
    }

    public double getArrivalLongitude() {
        return arrivalLongitude;
    }

    public double getTravelledDist() {
        return travelledDist;
    }

    public double getDeltaDist() {
        return deltaDist;
    }

    @Override
    public String toString() {
        return "\nMovementsSummary:" +
                "\nname='" + name + '\'' +
                "\nMMSI='" + MMSI + '\'' +
                ",\nstartDate=" + startDate +
                ",\nendDate=" + endDate +
                ",\nmaxSog=" + maxSog +
                ",\nmeanSog=" + meanSog +
                ",\nmeanCog=" + meanCog +
                ",\ndepartLatitude=" + departLatitude +
                ",\ndepartLongitude=" + departLongitude +
                ",\narrivalLatitude=" + arrivalLatitude +
                ",\narrivalLongitude=" + arrivalLongitude +
                ",\ntravelledDist=" + travelledDist +
                ",\ndeltaDist=" + deltaDist +
                '}';
    }
}