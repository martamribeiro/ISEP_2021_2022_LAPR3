package lapr.project.domain.model;

public class Mass {
    /**
     * Distance from the origin until the center of mass on x coordinate
     */
    private Double x;
    /**
     * Distance from the origin until the center of mass on y coordinate
     */
    private Double y;
    /**
     * The length of the mass
     */
    private double length;
    /**
     * The width of the mass
     */
    private double width;

    //for US418
    public Mass(double x, double length, double width) {
        this.x = x;
        this.y = width / 2;
        this.length = length;
        this.width = width;
    }

    //for US419
    public Mass(Double x, Double y, double length, double width) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
    }

    public double getArea() {
        return length * width;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public void setX(Double x) {
        this.x = x;
    }
}
