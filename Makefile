SOURCES=\
  src/iai/lib/Edge.java src/iai/lib/WEdge.java \
  src/iai/lib/Graph.java src/iai/lib/Dijkstra.java src/iai/lib/Test.java \
  src/iai/search/Model.java \
  src/iai/search/MinMax.java \
  src/iai/search/MaxSum.java \
  src/iai/atp/World.java src/iai/atp/InOut.java \
  src/iai/atp/State.java src/iai/atp/Move.java \
  src/iai/atp/Agent.java src/iai/atp/Tour.java \
  src/iai/atp/HumanAgent.java \
  src/iai/atp/Heur.java \
  src/iai/atp/AIAgent.java \
  src/iai/atp/MinMaxAgent.java \
  src/iai/atp/MaxSumAgent.java \
  src/iai/atp/Game.java \

all:
	javac -cp bin -d bin -sourcepath src  $(SOURCES)

.PHONY: doc

doc:
	javadoc -private -d doc $(SOURCES)

clean:
	find . -name '*.class' | xargs rm


