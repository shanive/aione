SOURCES=src/iai/lib/Graph.java

all:
	javac -cp bin -d bin -sourcepath src  $(SOURCES)

.PHONY: doc

doc:
	javadoc -d doc $(SOURCES)

clean:
	find . -name '*.class' | xargs rm


