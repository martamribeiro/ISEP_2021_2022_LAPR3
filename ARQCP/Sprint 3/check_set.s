.section .data
	.global pos_pointer					#pointer to vector of positions
	.global ptr1						#pointer to matrix
	.global pos_size					#size of positions vector
	.global maxZ						#maxZ of ship to use in element access
	.global maxY						#maxY of ship to use in element access
	
.section .text
	.global check_set      				#verify set of positions
	
check_set:
	movq ptr1(%rip), %rcx				#pointer from matrix
	movq pos_pointer(%rip), %rbx		#pointer from pos
	
	movslq maxY(%rip), %r8				#maxY in r8
	movslq maxZ(%rip), %r9				#maxZ in r9
	
	movq $0, %r10						#used to store the sum of verify_position
	
	movq pos_size(%rip), %r11			#pos size in r11 to control the loop
	
	movq $0, %r12						#counter to compare with pos size
	movq $0, %rax						#result register
	
check_loop:

	movq $0, %rdi						#argument to receive x
	movq $0, %rsi						#argument to receive y
	movq $0, %rdx						#argument to receive z
	
	movl (%rbx), %edi					#x of position to be verified in rdi
	addq $4, %rbx						#move to next int in pos
	incq %r12							#counter increase	
	
	movl (%rbx), %esi					#y of position to be verified in rsi
	addq $4, %rbx						#move to next int in pos
	incq %r12							#counter increase
	
	movl (%rbx), %edx					#z of position to be verified in rdx
	addq $4, %rbx						#move to next int in pos
	incq %r12							#counter increase
	
	pushq %rax							#rax will be modified in verify_position -> caller saved
	call verify_position				#call verify positions
	addq %rax, %r10						#add acoordingly to being empty or not
	popq %rax							#restore originaç value of rax
	
	
	
	cmpq %r11, %r12						#check if its the end of pos vector
	je end_check
	jmp check_loop
	
	

end_check:
		movq %r10, %rax					#move stored sum to rax
		ret
	
	
