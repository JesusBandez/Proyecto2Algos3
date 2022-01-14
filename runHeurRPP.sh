#!/bin/bash
export JAVA_OPTS="-Xmx8g"
make
kotlin HeuristicaRPPKt $1 $2
