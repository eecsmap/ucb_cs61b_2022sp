
.PHONY: default check compile clean style

default: compile

compile:
	$(MAKE) -C Arithmetic
	$(MAKE) -C CompoundInterest
	$(MAKE) -C MultiArr

check:
	$(MAKE) -C Arithmetic check
	$(MAKE) -C CompoundInterest check
	$(MAKE) -C MultiArr check

style:
	$(MAKE) -C Arithmetic style
	$(MAKE) -C CompoundInterest style
	$(MAKE) -C MultiArr style

clean:
	$(MAKE) -C Arithmetic clean
	$(MAKE) -C CompoundInterest clean
	$(MAKE) -C MultiArr clean
