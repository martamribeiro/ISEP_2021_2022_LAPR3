package lapr.project.domain.model;

public class ContainerLayer {

    private final double thickness;

    private final double thermalConstant;

    public ContainerLayer(double thickness, double thermalConstant) {
        checkConstant(thermalConstant);
        checkThickness(thickness);
        this.thickness = thickness;
        this.thermalConstant = thermalConstant;
    }

    public void checkThickness(double thick) {
        if (thick <=0) {
            throw new IllegalArgumentException("Thicknness has to be above 0.");
        }
    }

    public void checkConstant(double constant) {
        if (constant <=0) {
            throw new IllegalArgumentException("Thermal constant has to be above 0.");
        }
    }

    public double getThermalResistance(double area){
        return (thickness/(thermalConstant*area));
    }

}
