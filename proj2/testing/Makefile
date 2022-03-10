# Targets that don't correspond to files, but are to be treated as commands.
.PHONY: default check clean std

SCRIPT = tester.py

CPATH = "..:$(CLASSPATH):;..;$(CLASSPATH)"

# The Python interpreter (default value). Settable on the command line.
# If your system calls python3 by a different name (e.g., python), then
# you can indicate this by invoking any of the makefiles with
#        make PYTHON=python check
PYTHON = python3

default: check

check: 
	@if [ -f $(SCRIPT) ]; then \
	    echo 'CLASSPATH=$(CPATH) $(PYTHON) $(SCRIPT)' *-1.in; \
	    CLASSPATH=$(CPATH) $(PYTHON) $(SCRIPT) *-1.in; \
	else \
	    echo; \
	    echo "=========================="; \
	    echo "= No integration tester. ="; \
	    echo "=========================="; \
	    echo; \
	    true; \
	fi
	@echo

# 'make clean' will clean up stuff you can reconstruct.
clean:
	$(RM) *~ *.tmp

