.data
z_str: .asciiz "1234"
z: .word 0

.text

user_main:
subu $sp, $sp, 4
sw $ra 0($sp)
subu $sp, $sp, 4
sw $fp 0($sp)
move $fp, $sp
sub $sp, $sp, 16

# inline implementation of PrintString
lw $t0, z
li $v0, 4
move $a0, $t0
syscall
j user_main_epilogue

user_main_epilogue:
move $sp, $fp
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 4
jr $ra

main:
# init global strings
la $s0, z_str
sw $s0, z

jal user_main
li $v0, 10
li $a0, 1
syscall
