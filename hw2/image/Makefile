STYLEPROG = style61b

# All Java sources.  If you add more files, be sure to add them here as
# well.
STUDENT = MatrixUtils.java MatrixUtilsTest.java

PROVIDED = Rescaler.java In.java Picture.java Utils.java Out.java

ALL_SRCS = $(STUDENT) $(PROVIDED)

# The $(NAME:.java=.class) notation substitutes .java for trailing .class
# extensions.
STUDENT_CLASSES = $(STUDENT:.java=.class)

PROVIDED_CLASSES = $(PROVIDED:.java=.class)

ALL_CLASSES = $(STUDENT_CLASSES) $(PROVIDED_CLASSES)

# Flags to pass to Java compilations (include debugging info) and report
# "unsafe" operations.
JFLAGS = -g 

.PHONY: default style clean

# By default, recompile if any sources have changed since the last build.
# This will fail to recompile properly if you compile, then delete 
# a .class file in the draw package, and then compile again.  You
# shouldn't do that, of course, but if it happens, just do a 'make clean'
# first and then 'gmake'
default: $(ALL_CLASSES)

# Check style of source files with style61b.
style:
	$(STYLEPROG) $(STUDENT)

# Remove files that are unnecessary or that can be regenerated with make.
clean:
	$(RM) *.class *~

check: MatrixUtilsTest.class
	cd ..; java image.MatrixUtilsTest

#what to do when the makefile requests all classes sbould be compiled
#it makes this request as the default option (see above)
$(ALL_CLASSES): $(ALL_SRCS)
	cd ..; javac $(JFLAGS) $(ALL_SRCS:%=image/%)
