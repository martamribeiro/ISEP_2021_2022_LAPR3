#include "main.h"
#include <math.h>

extern float container_x, container_y, container_z;

float get_material_resistance(float thickness, float k, float area){
    return thickness/(k*area);
}

Container* get_coordinates_container(Container *container, int array_size, short x, short y, short z){
    for(int i =0;i<array_size;i++){
        if((container+i)->position_x == x){
            if((container+i)->position_y == y){
                if((container+i)->position_z == z){
                    return (container+i);
                }
            }
        }
    }
    return NULL;
}

float get_energy_power(Container *currentContainer, float externalTemperature){
    float containerArea = ((container_x*container_y)*4) + ((container_y*container_z)*2); //in m²
    float material1_resistance = get_material_resistance(currentContainer->size1, currentContainer->k_material1, containerArea); // K/w
    float material2_resistance = get_material_resistance(currentContainer->size2, currentContainer->k_material2, containerArea);// K/w
    float material3_resistance = get_material_resistance(currentContainer->size3, currentContainer->k_material3, containerArea);// K/w

    float totalResistance = material1_resistance + material2_resistance + material3_resistance; // total resistance is the sum of all materials resis.
    float containerNeededTemp = (float) currentContainer->temperature;

    return ((externalTemperature - containerNeededTemp) / totalResistance)*3600; // returns the joules for an hour
}

float get_needed_energy_for_container(Container *container, int array_size, short x, short y, short z, float externalTemperature){
    if(!is_refrigerated(container, array_size, x, y, z)){ //if not rrefrigerated 0 energy needed
        return 0;
    }

    Container *currentContainer = get_coordinates_container(container,array_size, x, y, z); //retrieve the container with the given coordinates

    return get_energy_power(currentContainer, externalTemperature);
}   


void check_enough_energy(Container *container, int array_size, float externalTemperature, float energyPower){
    float totalContainersPower = 0.0;
    //sum all the needed powers for the manifest
    for(int i =0;i<array_size;i++){
      totalContainersPower = totalContainersPower + get_energy_power((container+i), externalTemperature);
    }

    // if the total is more then the supply warn
    if(totalContainersPower > energyPower){
        printf("\n******************************************************************************************************\n");
        printf("WARNING: THE ENERGY NEEDED FOR THE CONTAINERS IN THE CARGO MANIFEST EXCEED THE POWER SUPPLY OF THE SHIP IN %f JOULES\n", totalContainersPower-energyPower);
        printf("\n******************************************************************************************************\n");
    }else{
        printf("The ship's energy supply is enough for this manifest");
    }

}