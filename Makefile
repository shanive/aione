all:
	javac -cp bin -d bin -sourcepath src  `find src -name '*.java'`

clean:
	find . -name '*.class' | xargs rm


