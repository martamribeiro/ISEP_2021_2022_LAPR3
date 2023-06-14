#include <stdio.h>
#include <stdlib.h>
#include "asm.h"

int maxX, maxY, maxZ;														//ships max for each coordinate
int *ptr1, *pos_pointer;													//pointers for matrix and position's array, respectivaly
int ship_capacity, pos_size;												//ship's capacity and size of position's array	
int x, y, z;																//coordinates

int main(void) {
	FILE *file = fopen("manifest.txt", "r");								//cargo manifest file
	int mat[maxX][maxY][maxZ];												//matrix of containers
	
	int *temp = get_pointer(file, mat);										//pointer of the container's matrix
	
	
	int n_zeros;
	ptr1 =temp;																//pointer to use in zero_count()
	n_zeros = zero_count();													//call to function zero_count() in Assembly
	printf("\n number of occupied: %d", n_zeros);
	
	x=0;
	y=0;
	z=0;
	int result = 0;
	ptr1 =temp;																//pointer of the container's matrix
	int pos[] = {0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0};		//array with the positions to be checked in chek_set()
	pos_pointer = (int*) pos;												//pointer of pos
	pos_size = sizeof(pos)/sizeof(pos[0]);									//size of pos
	
	
	result = check_set();													//call to function check_set() in Assembly
	printf("\n checkposition position: %d", result);
	

	return 0;
}
