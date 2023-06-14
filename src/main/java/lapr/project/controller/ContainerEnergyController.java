package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.domain.model.Container;
import lapr.project.domain.shared.Constants;
import lapr.project.domain.store.ContainerStore;

import lapr.project.utils.PhysicsUtils;

import java.util.List;

public class ContainerEnergyController {
    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor receiving the company as an argument.
     *
     * @param company instance of company to be used.
     */
    public ContainerEnergyController(Company company) {
        this.company=company;
    }

    public double getEnergyConsumptionOfContainer(double minutes, int id, double area, double externalTemp){
        ContainerStore containerStore = this.company.getContainerStore();
        Container container = containerStore.getContainerById(id);

        double totalResistance = container.getTotalThermalResistance(area);
        double contTemperature = container.getTemperature();

        double timeInSeconds =( minutes*60);

        return (PhysicsUtils.calculateEnergyPowerForContainer(externalTemp, contTemperature, totalResistance)*timeInSeconds);
    }

    public int getNumberOfAuxiliaryPower(double auxiliaryPower, double area, double externalTemp){
        ContainerStore containerStore = this.company.getContainerStore();
        List<Container> allContainers = containerStore.getContainersList();
        double consumptionTotal = 0.0;
        for (Container container : allContainers){
            double totalResistance = container.getTotalThermalResistance(area);
            double contTemperature = container.getTemperature();
            System.out.printf("id: %s,consumption: %f\n",container.getId(), PhysicsUtils.calculateEnergyPowerForContainer(externalTemp, contTemperature, totalResistance));
            consumptionTotal += PhysicsUtils.calculateEnergyPowerForContainer(externalTemp, contTemperature, totalResistance); //give in power already
        }

        consumptionTotal = consumptionTotal / 1000; // watts to killowatts
        return (consumptionTotal % auxiliaryPower) != 0 ? (int) ((consumptionTotal / auxiliaryPower)+1) : (int) (consumptionTotal / auxiliaryPower);
    }

    public double distinguishBySideContainerEnergy(int side, double [] minutesOfSections, int id, double [] externalTempSections){

        if(side > 6 || side < 0){
            throw new IllegalArgumentException("Unknown side");
        }
        double total = 0.0;

        for(int i=0; i< minutesOfSections.length; i++) {
            double area1 = Constants.AREAS[side];
            double baseArea = Constants.AREAS[Constants.AREAS.length-1]-area1;
            total += getEnergyConsumptionOfContainer(minutesOfSections[i], id,area1, externalTempSections[i]);
            total += getEnergyConsumptionOfContainer(minutesOfSections[i], id, baseArea, externalTempSections[0]);
        }

        return total;
    }
}
