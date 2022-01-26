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

SHELL = bash

STYLEPROG = style61b

PYTHON = python3

PACKAGE = blocks

# A non-standard classpath that works on Linux, Mac, and Windows.
# To Unix-like systems (Linux and Mac) it has the form
#     <valid classpath>:<garbage classpath (ignored)>
# while to Windows systems it looks like
#     <garbage classpath (ignored)>;<valid classpath>
CPATH = "..:$(CLASSPATH):;..;$(CLASSPATH)"

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked -cp $(CPATH) -d .. -encoding utf8

CLASSDEST = ..

# All .java files to be compiled.
SRCS = $(wildcard *.java)

CLASSES = $(SRCS:.java=.class)

# Tell make that these are not really files.
.PHONY: clean default compile style  \
	check unit acceptance

%.class: %.java
	javac $(JFLAGS) -d "$(CLASSDEST)" $^ || { $(RM) $@; false; }

# By default, compile all classes if any sources have changed after the last
# compilation of class Main.
default: compile

compile: Main.class

style:
	$(STYLEPROG) $(SRCS) 

Main.class: $(SRCS)
	javac $(JFLAGS) -d "$(CLASSDEST)" $(SRCS) || { $(RM) $@; false; }

# Run Tests.
check: 
	code=0; \
	"$(MAKE)" unit || code=1; \
	"$(MAKE)" acceptance || code=1; \
	exit $$code

# Run unit tests in this directory
unit: compile
	cd ..; java -ea $(PACKAGE).UnitTests

acceptance: compile
	"$(MAKE)" -C ../testing PYTHON=$(PYTHON) check

unit-jar: unit-tests.jar

unit-tests.jar: compile
	jar cf $@ ../$(PACKAGE)/*Tests.class


# Find and remove all *~ and *.class files.
clean:
	$(RM) *.class *~ unit-tests.jar
