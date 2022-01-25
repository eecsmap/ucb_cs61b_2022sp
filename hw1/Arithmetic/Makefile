#    make	      # Defaults to the first target: default
#    make default     # Same as gmake compile.
#    make compile     # Compiles Java code in this directory.
#    make adhoc-check # Compile if needed and test using ArithmeticTest.
#    make check       # Compile if needed and test using ArithmeticJUnitTest.
                      # We give the check label to this test instead of the
                      # other ad-hoc because it is better.

#    make clean       # Remove extraneous or rebuildable files

STYLEPROG = style61b

# Define $(SRCS) to be a list of Java files.
SRCS = Arithmetic.java ArithmeticTest.java ArithmeticJUnitTest.java

# The targets following .PHONY aren't actually files; they serve as commands.
.PHONY: compile default check clean style

# By default, compile all sources
default: compile

# To compile all source files, compile all java source files.
compile: $(SRCS) 
	javac -g $(SRCS)

# Test the compiled program, after first making sure it is up-to-date.
# This type of testing is relatively lame.
adhoc-check: compile
	java ArithmeticTest

# Check style of program
style:
	$(STYLEPROG) $(SRCS)

# Test the compiled program, after first making sure it is up-to-date.
check: compile
	java ArithmeticJUnitTest 

# Remove extraneous or reconstructable files.
clean:
	$(RM) *.class *~
