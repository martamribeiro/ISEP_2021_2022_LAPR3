.section .data
	.equ TOTAL_SIZE, 36
	.equ X_OFFSET, 28
	.equ Y_OFFSET, 30
	.equ Z_OFFSET, 32
	.equ TEMPERATURE_OFFSET, 34
.section .text
	.global is_refrigerated		
	
	is_refrigerated:
		# prologue
		pushq %rbp # save the original value of RBP
		movq %rsp, %rbp # copy the current stack pointer to RBP
		subq $2, %rsp
		pushq %rbx
		# ###########################################
		movslq %esi, %rsi # making size quad to compare with counter
		movq $0, %r9 # counter is quad to be part of the offset
		movq $0, %r10 # will be the array offset mover since we cannot use the offset normally because of the size not being literal i.e size>8
		find_container:
		# here improvising the offset function with 4 (multiple of total size 36) to move the array r10 will be updated with 9 values every time (9*4=36)
			cmpw X_OFFSET(%rdi,%r10, 4), %dx # compare x
			jne keep_search
			cmpw Y_OFFSET(%rdi, %r10, 4), %cx # compare y
			jne keep_search
			cmpw Z_OFFSET(%rdi, %r10, 4), %r8w # compare z
			jne keep_search

		# found the container now pass the address to check the refrigeration
			leaq (%rdi, %r10, 4), %rbx
			movq %rbx, %rdi
			jmp check_refrigeration

		keep_search:
			incq %r9
			addq $9, %r10
			cmpq %rsi, %r9
			jl find_container

		check_refrigeration:
			movl $0, %eax
			cmpb $0, TEMPERATURE_OFFSET(%rdi)
			je end
			movl $1, %eax

		end:
			# Epilogue	
			popq %rbx
			movq %rbp, %rsp # restore the previous stack pointer ("clear" the stack)
			popq %rbp    # restore the previous stack frame pointer	
			ret
		
		
