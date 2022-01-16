#!/bin/bash
export JAVA_OPTS="-Xmx2048m"
export JAVA_OPTS="-Xms2048m"
make && kotlin HeuristicaRPPKt $1 $2
