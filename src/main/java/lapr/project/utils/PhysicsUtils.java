package lapr.project.utils;

public class PhysicsUtils {

    public static double calculateEnergyPowerForContainer(double externalTemp, double contTemperature, double totalResistance){
        double neededPower = (externalTemp-contTemperature)/totalResistance; //result in WATTS unit of POWER

        return (neededPower);// returns in Joules since we want ENERGY
    }
}
