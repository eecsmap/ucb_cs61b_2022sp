# This a Makefile, an input file for the GNU 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     make 
# You can also clean up junk files and .class files with
#     make clean
# To run style61b (our style enforcer) over your source files, type
#     make style
# Finally, you can run any tests you'd care to with
#     make check

PACKAGE = ataxx

STYLEPROG = style61b

# Targets that don't correspond to files, but are to be treated as commands.
.PHONY: default check clean style acceptance unit

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked 

default:
	"$(MAKE)" -C $(PACKAGE) default

check:
	"$(MAKE)" -k unit acceptance

unit: default
	"$(MAKE)" -C $(PACKAGE) unit

acceptance: default
	"$(MAKE)" -C testing check

style:
	"$(MAKE)" -C $(PACKAGE) STYLEPROG=$(STYLEPROG) style

# 'make clean' will clean up stuff you can reconstruct.
clean:
	$(RM) *~ 
	"$(MAKE)" -C $(PACKAGE) clean
	"$(MAKE)" -C testing clean
