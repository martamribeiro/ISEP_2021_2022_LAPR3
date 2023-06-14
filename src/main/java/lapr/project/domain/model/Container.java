package lapr.project.domain.model;

import java.util.List;
import java.util.Objects;

public class Container {

    private final int id;

    private final double payload;

    private final double tare;

    private final double gross;

    private final String iso;

    private double temperature;

    private final List<ContainerLayer> layers;

    public Container(int id, double payload, double tare, double gross, String iso, List<ContainerLayer> layers, double temperature) {
        checkIdentification(id);
        checkIso(iso);
        checkLayers(layers);
        checkTare(tare);
        this.id = id;
        this.payload = payload;
        this.tare = tare;
        this.gross = gross;
        this.iso = iso;
        this.layers = layers;
        this.temperature = temperature;
    }

    public void checkIso(String iso){
        if(Objects.isNull(iso)){
            throw new IllegalArgumentException("Iso cannot be null.");
        }
    }

    public void checkIdentification(int id) {
        if (id <=0) {
            throw new IllegalArgumentException("Identification has to be above 0.");
        }
    }

    public void checkTare(double tare) {
        if (tare <=0) {
            throw new IllegalArgumentException("Tare has to be above 0.");
        }
    }

    private void checkLayers(List<ContainerLayer> layers){
        if(layers.isEmpty()){
            throw new IllegalArgumentException("Tha layers of the container cannot be empty");
        }
    }

    public int getId() {
        return id;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTotalThermalResistance(double area){
        double totalThermal=0.0;
        for(ContainerLayer containerLayer : this.layers){
            totalThermal += containerLayer.getThermalResistance(area);
        }
        return totalThermal;
    }
}
