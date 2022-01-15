#!/bin/bash
export JAVA_OPTS="-Xmx8192m"
export JAVA_OPTS="-Xms8192m"
make && kotlin HeuristicaRPPKt $1 $2
