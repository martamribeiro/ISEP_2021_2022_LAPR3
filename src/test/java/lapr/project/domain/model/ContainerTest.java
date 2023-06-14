package lapr.project.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {

    public int id = 123456789;

    public double payload = 23.5;

    public double tare = 12.5;

    public double gross = payload + tare;

    public String iso = "ISO4123";

    public List<ContainerLayer> layers = new ArrayList<>();

    ContainerLayer con1 = new ContainerLayer(0.003, 55);
    ContainerLayer con2 = new ContainerLayer(0.007, 0.038);
    ContainerLayer con3 = new ContainerLayer(0.09, 0.0045);

    @BeforeEach
    public void setUp() {
        layers.add(con1);
        layers.add(con2);
        layers.add(con3);
    }

    @Test
    void getIndividualThermalResistance(){
        double expected = 7.52E-7;
        double value = Math.round(con1.getThermalResistance(72.5)*1000000000);
        value = value / 1000000000;
        assertEquals(expected, value);
    }

    @Test
    void getTotalThermalResistance() {
        double expected = 0.2784;
        Container container = new Container(id, payload, tare, gross, iso, layers, 7.0);
        double value = Math.round((container.getTotalThermalResistance(72.5)*10000));
        value = value /10000;

        assertEquals(expected, value);
    }

    @Test
    void getTempTest(){
        Container c1 = new Container(id, payload, tare, gross, iso, layers, 7);
        double exp = 7;
        assertEquals(exp, c1.getTemperature());
    }

    @Test
    public void ensureThicknessNotNegative(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ContainerLayer(-10, 0.2));
        assertEquals("Thicknness has to be above 0.", thrown.getMessage());
    }

    @Test
    public void ensureThicknessNotZero(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ContainerLayer(0, 0.2));
        assertEquals("Thicknness has to be above 0.", thrown.getMessage());
    }

    @Test
    public void ensureConstantNotNegative(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ContainerLayer(10, -0.2));
        assertEquals("Thermal constant has to be above 0.", thrown.getMessage());
    }

    @Test
    public void ensureConstantNotZero(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new ContainerLayer(10, 0));
        assertEquals("Thermal constant has to be above 0.", thrown.getMessage());
    }

    @Test
    public void ensureNegativeIdNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Container(-281, payload, tare, gross, iso, layers, 7.0));
        assertEquals("Identification has to be above 0.", thrown.getMessage());
    }

    @Test
    public void ensureZeroIdNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Container(0, payload, tare, gross, iso, layers, 7.0));
        assertEquals("Identification has to be above 0.", thrown.getMessage());
    }


    @Test
    public void ensureNegativeTareNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Container(id, payload, -10, gross, iso, layers, 7.0));
        assertEquals("Tare has to be above 0.", thrown.getMessage());
    }

    @Test
    public void ensureZeroTareNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Container(id, payload, 0, gross, iso, layers, 7.0));
        assertEquals("Tare has to be above 0.", thrown.getMessage());
    }


    @Test
    public void ensureNullIsoNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Container(id, payload, tare, gross, null, layers, 7.0));
        assertEquals("Iso cannot be null.", thrown.getMessage());
    }

    @Test
    public void ensureEmptyListNotAllowed(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new Container(id, payload, tare, gross, iso, new ArrayList<>(), 7.0));
        assertEquals("Tha layers of the container cannot be empty", thrown.getMessage());
    }


}