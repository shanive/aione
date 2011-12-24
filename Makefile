SOURCES=\
  src/iai/lib/Graph.java src/iai/lib/Dijkstra.java src/iai/lib/Test.java \
  src/iai/atp/World.java src/iai/atp/InOut.java \
  src/iai/atp/State.java


all:
	javac -cp bin -d bin -sourcepath src  $(SOURCES)

.PHONY: doc

doc:
	javadoc -d doc $(SOURCES)

clean:
	find . -name '*.class' | xargs rm


