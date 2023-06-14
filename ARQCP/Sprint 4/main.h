#ifndef ASM_H
#define ASM_H
#include <stdio.h>
#include <stdlib.h>
//US 409:
typedef struct{
	float k_material1; //4
	float size1; //4
	float k_material2; //4
	float size2; //4
	float k_material3; //4
	float size3; //4
	int container_id; //4
	short position_x; //2
	short position_y; //2
	short position_z; //2
	char is_refrigerated; //1
	char temperature; //1
} Container; //36
void fill_array(FILE *file, Container containers_array[], int array_size);
void print_array(Container containers_array[], int array_size);
Container* get_pointer(FILE *file);
int get_arr_size(FILE *file);	
//US 410:
int is_refrigerated(Container *container, int array_size, short x, short y, short z);
float get_needed_energy_for_container(Container *container, int array_size, short x, short y, short z, float externalTemperature);
//us 411
void check_enough_energy(Container *container, int array_size, float externalTemperature, float energyPower);
#endif