
# All Java sources.  If you add more files, be sure to add them here as
# well.
SRCS = Matching.java P2Pattern.java ReadInts.java TestP2Pattern.java TestReadInts.java Utils.java

CLASSES = $(SRCS:.java=.class)

JFLAGS = -g

.PHONY: default style clean

default: $(CLASSES)

style:
	style61b $(SRCS)

clean:
	$(RM) *.class *~

check: default
	java TestReadInts
	java TestP2Pattern

$(CLASSES): $(SRCS)
	javac $(JFLAGS) $(SRCS)
