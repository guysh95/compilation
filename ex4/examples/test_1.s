.data

.text

f:
subu $sp, $sp, 4
sw $ra 0($sp)
subu $sp, $sp, 4
sw $fp 0($sp)
move $fp, $sp
sub $sp, $sp, 16

lw $t0, 8($fp)
lw $t1, 12($fp)
add $t2, $t0, $t1
sw $t2, -4($fp)
lw $v0, -4($fp)
j f_epilogue

f_epilogue:
move $sp, $fp
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 4
jr $ra

user_main:
subu $sp, $sp, 4
sw $ra 0($sp)
subu $sp, $sp, 4
sw $fp 0($sp)
move $fp, $sp
sub $sp, $sp, 16

li $t0, 2
subu $sp, $sp, 4
sw $t0 0($sp)
li $t1, 1
subu $sp, $sp, 4
sw $t1 0($sp)
jal f
addu $sp, $sp, 8
move $t2, $v0
sw $t2, -4($fp)
# inline implementation of PrintInt
lw $t3, -4($fp)
li $v0, 1
move $a0, $t3
syscall
j user_main_epilogue

user_main_epilogue:
move $sp, $fp
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 4
jr $ra

main:
jal user_main
li $v0, 10
li $a0, 1
syscall
