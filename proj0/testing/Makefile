# This makefile is defined to give you the following targets:
#
#    default: Same as check
#    check: For each file, F.in, use F.in as input to "java $(MAIN_CLASS)" and
#          compare the output to the contents of the file names F.std.
#          Report discrepencies.
#    clean: Remove all the .class files produced by java compilation, 
#          all Emacs backup files, and testing output files.
#    outputs: Create .std files from current application and .in files.
#
# You can use this file without understanding most of it, of course, but
# I strongly recommend that you try to figure it out, and where you cannot,
# that you ask questions.  The Lab Reader contains documentation.

SHELL = /bin/bash

PYTHON = python3

# Flags to Java interpreter: check assertions
JFLAGS = -ea

# Name of class containing main procedure 
MAIN = blocks.Main

# Running the program.
PROG = java $(JFLAGS) -cp $(CPATH) $(MAIN)

TESTER_FLAGS =

TESTS := *.in

.PHONY: default check clean outputs

# First, and therefore default, target.
default:
	"$(MAKE)" -C .. 
	"$(MAKE)" check

# A non-standard classpath that works on Linux, Mac, and Windows.
CPATH = "..:$(CLASSPATH):;..;$(CLASSPATH)"

# 'make check' will run the tests in the testing directory.
# For each .in file, it will "pipe" that file through your program
# (that is, cause the .in file to be the standard input, rather as if it
# were typed into the terminal), and compare the result with the
# corresponding .std file.  It will report any failures.
# The "CLASSPATH=..." clause in front of the command below temporarily 
# changes the location in which Java searches for classes to be the directory
# that contains your project.
check: 
	@echo "Testing application $(MAIN)..."
	@CLASSPATH=$(CPATH) "$(PYTHON)" tester.py $(TESTER_FLAGS) $(TESTS)

# 'make clean' will clean up stuff you can reconstruct.
clean:
	$(RM) -r *~ OUTPUT* *.log *.diff __pycache__

# Regenerate .std files.
outputs: 
	"$(MAKE)" -C ..
	@for inp in $(TESTS); do \
	    out=$$(basename $$inp .in).std; \
	    echo -n $$inp "=>" $$out; \
	    $(RM) $$out.tmp; \
	    CLASSPATH=..:$(CLASSPATH) \
               java $(JFLAGS) $(MAIN) --testing --no-display \
		   < $$inp > $$out.tmp; \
            if [ -f $$out ] && cmp -s $$out $$out.tmp; then \
	        $(RM) $$out.tmp; echo; \
	    else \
	        mv $$out.tmp $$out; echo " (changed)"; \
	    fi; \
	done
