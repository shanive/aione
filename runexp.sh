#!/bin/bash

EXPDIR=experiments/
GRAPHS="500graph test-14x9 test-7x4 test-4x4"
ALGORITHMS="greedy rta-3 rta-6 rta-10 a"

for graph in $GRAPHS; do
    for alg in $ALGORITHMS; do
        echo
        echo "Graph: $graph"
        echo "Algorithm: $alg"
        java -cp bin com.ATP.ATPenv -b $EXPDIR/$graph.txt < $EXPDIR/$graph-resp-$alg.txt | tail -3 | head -2
    done
done
    
