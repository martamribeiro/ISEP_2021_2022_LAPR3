#include "main.h"
#include <stdio.h>
#include <stdlib.h>

int array_size;
int maxX, maxY, maxZ;	
float container_x=6, container_y=2.5, container_z=2.5;

int main(void) {
    FILE *file = fopen("manifest2.txt", "r");
    Container *pointer = NULL;
    pointer = get_pointer(file);
    array_size = get_arr_size(file);

    printf("needed energy for an hour (J/h)(410): %f\n", get_needed_energy_for_container(pointer, array_size, 1,1,1, 20));
    check_enough_energy(pointer, array_size, 20.0, 20.0);
}

