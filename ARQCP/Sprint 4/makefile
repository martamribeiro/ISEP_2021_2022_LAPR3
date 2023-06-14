#Makefile

INCLUDES = 
SOURCES = main.c is_refrigerated.s energy.c maindinamico.c 
OBJFILES = main.o is_refrigerated.o energy.o maindinamico.o
EXEC = matrix

.SUFFIXES : .c .o

.c.o:
	gcc -Wall -g -c $<
	
.s.o:
	gcc -Wall -g -c $<
	
${EXEC}: ${OBJFILES}	
	gcc -Wall -g -o ${EXEC} ${OBJFILES}
	
${OBJFILES}: ${SOURCES} ${INCLUDES}

run: ${EXEC}
		./${EXEC}
		
clean:
		rm -f ${OBJFILES} ${EXEC}
		
debug: 
		gdb -tui ./${EXEC}	
		
vg: 
		valgrind ./${EXEC}		
