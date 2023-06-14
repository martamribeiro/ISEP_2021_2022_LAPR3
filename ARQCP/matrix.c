#include <stdio.h>
#include <stdlib.h>
#include "asm.h"

int maxX, maxY, maxZ;																				
int *ptr1, *pos_pointer;																			
int ship_capacity, pos_size;
int x, y, z;

void zero_matrix(int mat[maxX][maxY][maxZ]){														//method to make all positions of the matrix =0
	for(int i=0; i<maxX;i++){
		for(int j=0; j<maxY;j++){
			for(int g=0;g<maxZ;g++){
				mat[i][j][g] =0;
			}
		}
	}
}

void fill_matrix(FILE *file, int mat[maxX][maxY][maxZ]){											//method to extract information from cargo manifest and fill the matrix
	int test =0;
	
	int x=0, y=0, z=0;
	while(fscanf(file, "id: %d\t\tx: %d\t\ty: %d\t\tz: %d\t\t", &test, &x, &y, &z) != EOF){			//loop to search for the containers information to add to the matrix
		mat[x][y][z] = test;
	};
}

int* get_pointer(FILE *file, int mat[maxX][maxY][maxZ]){											//method to get the pointer of the container's matrix
	
	if(!file) {																						//check to see if file exists
		printf("File not found!");
		exit(-1);
	}
	
	fscanf(file, "ship capacity -> x: %d\t\ty: %d\t\tz: %d\t\t", &maxX, &maxY, &maxZ);				//extract information related to the ship's size
	
	
	
	
	ship_capacity = maxX*maxY*maxZ;																	//calculate ship capacity with the given sizes
	
	zero_matrix(mat);																				//call for method to zero matrix
	
	fill_matrix(file, mat);																			//call for method to fill the matrix
	
	print_matrix(mat, maxX, maxY, maxZ);															//call for method to print the matrix
	
	return (int*) mat;																				//return pointer of matrix
}

void print_matrix(int mat[maxX][maxY][maxZ], int maxX, int maxY, int maxZ){							//method to print matrix
	
	
	for(int i=0; i<maxX;i++){
		for(int j=0; j<maxY;j++){
			for(int g=0;g<maxZ;g++){
				printf("position %d %d %d: %d       ", i, j, g, mat[i][j][g]);
			}
			printf("\n");
		}
		printf("\n");
	}
	
}


