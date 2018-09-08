	.data
a:
	10
	.text
main:
	load %x0, $a, %x4
	divi %x4, 2, %x5
	addi %x5, 2, %x5
	end
