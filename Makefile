all:
	javac -cp bin -sourcepath src -d bin `find src -name '*.java'`

clean:
	find . -name '*.class' | xargs rm
