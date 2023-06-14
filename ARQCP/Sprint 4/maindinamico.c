#include <stdio.h>
#include <stdlib.h>
#include "main.h"

//US 409:::
extern int maxX, maxY, maxZ;

void fill_array(FILE *file, Container containers_array[], int array_size){							//method to extract information from cargo manifest and fill the array
	int id =0;
	
	int x=0, y=0, z=0, i=0;
	int is_refrigerated=0, temp=0;
	float k_material1=0.0, k_material2=0.0, k_material3=0.0, size1=0.0, size2=0.0, size3=0.0;
	while(
		fscanf(
			file, 
			"id: %d\t\tx: %d\t\ty: %d\t\tz: %d\t\tis_refrigerated: %d\t\ttemperature: %d\t\t k_material1: %f\t\t size1: %f\t\t k_material2: %f\t\t size2: %f\t\t k_material3: %f\t\t size3: %f\t\t",
			 &id, &x, &y, &z, &is_refrigerated, &temp, &k_material1, &size1,&k_material2, &size2,&k_material3, &size3) != EOF){			//loop to search for the containers information to add to the array
		containers_array[i].container_id=id;
		containers_array[i].position_x=x;
		containers_array[i].position_y=y;
		containers_array[i].position_z=z;
		containers_array[i].is_refrigerated=is_refrigerated;
		containers_array[i].temperature=temp;
		containers_array[i].k_material1=k_material1;
		containers_array[i].size1=size1;
		containers_array[i].k_material2=k_material2;
		containers_array[i].size2=size2;
		containers_array[i].k_material3=k_material3;
		containers_array[i].size3=size3;
		i++;
	};
}

int get_arr_size(FILE *file){
	if(!file) {																						//check to see if file exists
		printf("File not found!");
		exit(-1);
	}
	
	fscanf(file, "ship capacity -> x: %d\t\ty: %d\t\tz: %d\t\t", &maxX, &maxY, &maxZ);				//extract information related to the ship's size	
	
	
	return maxX*maxY*maxZ;
}


Container* get_pointer(FILE *file){						//method to get the pointer of the array
	
	if(!file) {																						//check to see if file exists
		printf("File not found!");
		exit(-1);
	}
	
	int array_size = get_arr_size(file);
	Container *ptr = NULL;
	
	ptr = (Container *) malloc(array_size * sizeof(Container));															//calculate array size using the max of each coordinate and adding the necessary memory slots
																									//for each value of the struct
	printf("Total array size: %d", array_size);
	
	
	fill_array(file, ptr, array_size);																//call for method to fill the array
	
	print_array(ptr, array_size);  /*call for method to print the matrix*/
	
	return ptr;																	//return pointer of array
}

void print_array(Container containers_array[], int array_size){							//method to print array
	printf("\n");
	printf("ARRAY SIZE: %d\n", array_size);
	printf("US 409:\n\n");

	for(int j=0; j<array_size; j++){
		printf("id: %d\n", (containers_array+j)->container_id);
		printf("x: %d\n", (containers_array+j)->position_x);
		printf("y: %d\n", (containers_array+j)->position_y);
		printf("z: %d\n", (containers_array+j)->position_z);
		printf("Refrigerated: %d\n", (containers_array+j)->is_refrigerated);
		printf("Temperature: %d\n", (containers_array+j)->temperature);
		printf("material1: %f\n", (containers_array+j)->k_material1);
		printf("size1: %f\n", (containers_array+j)->size1);
		printf("material2: %f\n", (containers_array+j)->k_material2);
		printf("size2: %f\n", (containers_array+j)->size2);
		printf("material3: %f\n", (containers_array+j)->k_material3);
		printf("size3: %f\n", (containers_array+j)->size3);
		printf("\n");
	}
	
}

