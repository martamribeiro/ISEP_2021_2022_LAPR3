.section .text
	.global verify_position       		#verify if position has container
	
verify_position:						#x in edi, y in esi, z in edx, ptr1 in rcx, maxY in r8, maxZ in r9
		pushq %rbx						#save previous value of rbx -> callee-saved
		pushq %rcx						#save the initial value of pointer
		
		imull $16, %edi					#multiply x with 16
		imull $8, %esi					#multiply y with 8
		imull $4, %edx					#multiply z with 4
		
		addl %edi, %esi					#x*16+y*8
		addl %esi, %edx					#x*16+y*8+z*4
		addq %rdx, %rcx					#ptr+x*16+y*8+z*4
		movl (%rcx), %eax				#move pointed value of rcx to rax
		
	
		cmpl $0, %eax   				#check if position is empty
		je empty_position
		movq $1, %rax
		popq %rcx
		popq %rbx
		jmp end
		
	
empty_position:
		movq $0, %rax
		popq %rcx
		popq %rbx
		jmp end 
		
end:
		ret
