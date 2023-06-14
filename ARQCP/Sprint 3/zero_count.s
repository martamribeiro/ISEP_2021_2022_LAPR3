.section .data
	.global ptr1						#pointer to matrix
	.global ship_capacity				#size of matrix
	
.section .text
	.global zero_count      			#counts empty container position in the ship
	
zero_count:
	movq ptr1(%rip), %rsi    			#moving pointer to rsi
	movl ship_capacity (%rip), %ebx 	#moving number of ships to ebx
	movl $0, %edx            			#pointer controller
	movq $0, %rax            			#zero counter
	
zero_loop:
		movl (%rsi), %ecx     			#moving first int to ecx
		cmpl %ebx, %edx		 			#check if its the end of vector
		je end_zero_loop	 	
		cmpl $0, %ecx		 			#check if int is 0
		je inc_zero
		addq $4, %rsi					#next int in vec
		incl %edx
		jmp zero_loop
		
inc_zero:	
		incl %eax            			#increase zero count
		addq $4, %rsi            		#next int in vec
		incl %edx
		jmp zero_loop	
		
end_zero_loop:
		rol $32, %rax					#store number of empty positions in most significant bytes of rax
		subl %eax, %ebx
		movl %ebx, %eax      			#number of occupied slots in 4 least significant bytes
		ret
