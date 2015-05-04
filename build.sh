#!/bin/sh
rm -rf ./src/main/java*.class
javac -cp ".:/Users/bennett/algs4/*" ./src/main/java/*.java
