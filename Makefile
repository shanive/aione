SOURCES=src/iai/lib/Graph.java

all:
	javac -cp bin -d bin -sourcepath src  $(SOURCES)

clean:
	find . -name '*.class' | xargs rm


