package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.domain.model.Container;
import lapr.project.domain.model.ContainerLayer;
import lapr.project.domain.store.ContainerStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerEnergyControllerTest {
    Company cmp1;
    ContainerStore containerStore;
    Container c1;
    Container c2;
    Container c3;
    Container c4;
    Container c5;
    Container c6;
    Container c7;
    Container c8;
    Container c9;
    Container c10;
    Container c11;
    Container c12;
    int[] ids = {12345,1234,123456,123457, 1,2,3,4,5,67,8,9};
    List<ContainerLayer> layersMinus5 = new ArrayList<>();
    List<ContainerLayer> layerSeven = new ArrayList<>();

    @BeforeEach
    public void setUp() throws ParseException {
        cmp1 = new Company("that company");
        containerStore = cmp1.getContainerStore();
        double[] payloads = {12.3, 14.5, 12.1, 10.0};
        double[] tare = {10.3, 11.5, 2.1, 1.0};
        double[] gross = {22.6, 26.0, 14.2, 11.0};
        String[] iso = {"ISO1", "ISO2", "ISO3", "ISO4"};

        ContainerLayer iron = new ContainerLayer(0.003, 55);
        ContainerLayer rockWool = new ContainerLayer(0.09, 0.045);
        ContainerLayer wood = new ContainerLayer(0.007, 0.13);
        ContainerLayer phenolicFoam = new ContainerLayer(0.09, 0.023);
        ContainerLayer cork = new ContainerLayer(0.007, 0.038);

        layersMinus5.add(iron);
        layersMinus5.add(phenolicFoam);
        layersMinus5.add(cork);

        layerSeven.add(iron);
        layerSeven.add(rockWool);
        layerSeven.add(wood);

        c1 = new Container(ids[0], payloads[0], tare[0], gross[0], iso[0], layersMinus5, -5);
        c2 = new Container(ids[1], payloads[1], tare[1], gross[1], iso[1], layerSeven, 7);
        c3 = new Container(ids[2], payloads[2], tare[2], gross[2], iso[2], layersMinus5, -5);
        c4 = new Container(ids[3], payloads[3], tare[3], gross[3], iso[3], layerSeven, 7);
        c5 = new Container(ids[4], payloads[0], tare[0], gross[0], iso[0], layersMinus5, -5);
        c6 = new Container(ids[5], payloads[1], tare[1], gross[1], iso[1], layerSeven, 7);
        c7 = new Container(ids[6], payloads[2], tare[2], gross[2], iso[2], layersMinus5, -5);
        c8 = new Container(ids[7], payloads[3], tare[3], gross[3], iso[3], layerSeven, 7);
        c9 = new Container(ids[8], payloads[0], tare[0], gross[0], iso[0], layersMinus5, -5);
        c10 = new Container(ids[9], payloads[1], tare[1], gross[1], iso[1], layerSeven, 7);
        c11= new Container(ids[10], payloads[2], tare[2], gross[2], iso[2], layersMinus5, -5);
        c12= new Container(ids[11], payloads[3], tare[3], gross[3], iso[3], layerSeven, 7);
        containerStore.saveContainer(c1);
        containerStore.saveContainer(c2);
        containerStore.saveContainer(c3);
        containerStore.saveContainer(c4);
        containerStore.saveContainer(c5);
        containerStore.saveContainer(c6);
        containerStore.saveContainer(c7);
        containerStore.saveContainer(c8);
        containerStore.saveContainer(c9);
        containerStore.saveContainer(c10);
        containerStore.saveContainer(c11);
        containerStore.saveContainer(c12);

        int [] extraIds = new int[29];

        for (int i = 1; i<29;i++){
            extraIds[i] =(i+10);
        }

        for (int i = 1; i<19;i++){ //now total 7 degrees containers are 25
            containerStore.saveContainer(new Container(extraIds[i], payloads[3], tare[3], gross[3], iso[3], layerSeven, 7));
        }

        for (int i = 19; i<29;i++){ //now total -5 degrees containers are 15
            containerStore.saveContainer(new Container(extraIds[i], payloads[3], tare[3], gross[3], iso[3], layersMinus5, -5));
        }
    }


    @Test
    void caculateEnergyFor2Point5hours(){
        //us 412
        ContainerEnergyController controller = new ContainerEnergyController(cmp1);
        double containerArea = ((6*2.5)*4)+(2.5*2.5)*2; // considering all sides exposed and a 6x2.5 container.
        //energy for 7 degrees container with 20 external
        double exp = 4129946;
        System.out.printf("Energy for 7 degrees in 2h30m and 20 external degrees(J): %.4f\n",controller.getEnergyConsumptionOfContainer(150, ids[1], containerArea, 20));
        assertEquals(Math.round(exp), Math.round(controller.getEnergyConsumptionOfContainer(150, ids[1], containerArea, 20)));

        //energy for -5 degrees container with 20 external
        double exp1 = 3981272;
        System.out.printf("Energy for -5 degrees in 2h30m and 20 external degrees(J): %.4f\n",controller.getEnergyConsumptionOfContainer(150, ids[0], containerArea, 20));
        assertEquals(Math.round(exp1), Math.round(controller.getEnergyConsumptionOfContainer(150, ids[0], containerArea, 20)));
    }

    @Test
    void calculateTripSectionWithoutDiff(){
        ContainerEnergyController controller = new ContainerEnergyController(cmp1);

        double [] minutesOfStepsTrip = {90, 105};
        double [] temperatures = {20, 28};

        double totalEnergy = 0.0;


        List<Container> cargo = containerStore.getContainersList();

        for(int i=0; i<40;i++){
            System.out.println(cargo.get(i).getTemperature());
            System.out.println(controller.distinguishBySideContainerEnergy(6,minutesOfStepsTrip, cargo.get(i).getId(), temperatures));
            totalEnergy += controller.distinguishBySideContainerEnergy(6,minutesOfStepsTrip, cargo.get(i).getId(), temperatures);
        }
        double expected = 269.0;
        System.out.println("The result is " + (totalEnergy/1000000) +" MJ/s");
        assertEquals(expected,Math.round(totalEnergy/1000000), "Should result in 225 MJ");
    }

    @Test
    void numberOfGenerators(){
        ContainerEnergyController controller = new ContainerEnergyController(cmp1);
        double containerArea = ((6*2.5)*4)+(2.5*2.5)*2; // considering all sides exposed and a 6x2.5 container.
        int exp = 1; //1 needed generator of 75kw is expected for the 12 containers in the list, 6 of 7 degrees and 6 of -5

        assertEquals(exp, controller.getNumberOfAuxiliaryPower(75, containerArea, 20));
    }
    @Test
    void distinguishBySideContainerEnergyTestToException(){
        ContainerEnergyController controller = new ContainerEnergyController(cmp1);

        double [] minutesOfStepsTrip = {90, 105};
        double [] temperatures = {20, 28};

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> controller.distinguishBySideContainerEnergy(-1,minutesOfStepsTrip, 1, temperatures));
        assertEquals("Unknown side", thrown.getMessage());

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> controller.distinguishBySideContainerEnergy(7,minutesOfStepsTrip, 1, temperatures));
        assertEquals("Unknown side", thrown2.getMessage());
    }

    @Test
    void distinguishBySideContainerEnergyTest(){
        ContainerEnergyController controller = new ContainerEnergyController(cmp1);

        double [] minutesOfStepsTrip = {90, 105};
        double [] temperatures = {20, 28};

        double totalEnergy = 0.0;

        System.out.printf("40 containers of 2.5x2.5x6 (two equal sides) meters in a 5x2x4 disposition would have\n" +
                "8 faces with axb + bxc (or ab + ac) = type 0\n" +
                "4 faces with ab + bc + ac = type 1\n" +
                "20 faces of bxc(or axc) = type 3\n" +
                "8 faces of axb = type 2\n" +
                "so the calculations would be like this:\n\n");


        List<Container> cargo = containerStore.getContainersList();
        System.out.println("type 0 to be added 4 containers of 7 degrees and 4 of -5 degrees");
        for(int i=0; i<8;i++){
            System.out.println(cargo.get(i).getTemperature());
            System.out.println(controller.distinguishBySideContainerEnergy(0,minutesOfStepsTrip, cargo.get(i).getId(), temperatures));
            totalEnergy += controller.distinguishBySideContainerEnergy(0,minutesOfStepsTrip, cargo.get(i).getId(), temperatures);
        }
        System.out.println();

        System.out.println("type 1 to be added 2 containers of 7 degrees and 2 of -5 degrees");
        for(int i=8; i<12;i++){
            System.out.println(cargo.get(i).getTemperature());
            System.out.println(controller.distinguishBySideContainerEnergy(1,minutesOfStepsTrip, cargo.get(i).getId(), temperatures));
            totalEnergy += controller.distinguishBySideContainerEnergy(1,minutesOfStepsTrip, cargo.get(i).getId(), temperatures);
        }

        System.out.println();

        System.out.println("type 2 to be added 8 containers of -5 degrees");
        for(int i=32; i<40;i++){
            System.out.println(cargo.get(i).getTemperature());
            System.out.println(controller.distinguishBySideContainerEnergy(2,minutesOfStepsTrip, cargo.get(i).getId(), temperatures));
            totalEnergy += controller.distinguishBySideContainerEnergy(2,minutesOfStepsTrip, cargo.get(i).getId(), temperatures);
        }
        System.out.println();
        System.out.println("type 3 to be added 19 containers of 7 degrees and 1 of -5 degrees");
        for(int i=12; i<32;i++){
            System.out.println(cargo.get(i).getTemperature());
            System.out.println(controller.distinguishBySideContainerEnergy(3,minutesOfStepsTrip, cargo.get(i).getId(), temperatures));
            totalEnergy += controller.distinguishBySideContainerEnergy(3,minutesOfStepsTrip, cargo.get(i).getId(), temperatures);
        }
        double expected = 225.0;
        System.out.println("The result is " + (totalEnergy/1000000) +" MJ/s");
        assertEquals(expected,Math.round(totalEnergy/1000000), "Should result in 225 MJ");
    }


}