.PHONY: all build repl test clean

all: build

build:
	./lein uberjar

repl:
	./lein run

test:
	./lein test

clean:
	./lein clean

deps:
	./lein deps
