AI assignment 1.

authors: Vered Shani & Ilya Davidovich

Explanations of Heuristic Function used on part 2 of the assignment:

Description:

For every given state, our heuristic function returns the time
to pass the shortest path from the agent's current position to
it's goal position assuming all vehicles are accessible from
every vertex.

The heuristic function uses Dijkstra algorithm to compute the shortest
path in a graph where the weight of each edge is the time to traverse
this edge (weight divided by the best effective vehicle speed), considering
the edge state (flooded of clear).

Motivation:

We wanted an heuristic function that will solve an easier problem. The main
problem of the agents is that they don't know the positions of the vehicles,
so there are a lot of trade-offs. For example, it is possible that the agent
will need to choose between a fast regular vehicle and a slower amphibious
vehicle, because later on the agent might need to traverse a flooded edge.

We also wanted to choose an admissible heuristic so for A* and RTA*
based agents.  One way to make sure an heuristic function is admissible
is to lower the constraints.  So we ignored the constraint that we might
not have a suitable vehicle when reaching a certain vertex.

A Proof that our heuristic (h) is admissible:

We want to prove that h(n) never overestimates the cost to reach from state n to a
goal state, for every state n.

Let h*(n) be the optimal cost to reach a goal state from n.
We need to show that h(n) <= h *(n), for each state n.

Let P = (x0, x1, ..., xn) be an optimal solution (sequence of actions) so that for every
0<=i<=n, xi = (ti, vi) where ti is the target vertex and vi is the vehicle to traverse with.

so, h*(n) = cost(P) = cost(x0) + cost(x2) + .. + cost(xn), where n is the initial state.
cost(xi) = weight(ti-1,ti) / speed(vi)

In our computation of h(n), we assume that in every edge (ti-1,ti), we can use
the fastest compatible vehicle and we don't consider Tswitch (the time to switch
vehicle). So the actual cost of each action xi is not smaller then the cost we get
in our computation. That is, for every 0<=i<=n, our_cost(xi) <= actual_cost(xi),
and we get: h(n) = our_cost(P) <= actual_cost(P) = h*(n).
 

Results

In order to compare the performance of the agents we performed a series of experiments that 
execute each agent on different environments and print the path each agent 
chooses as well as the performance measure P. 

The files of the form test-7x4.txt include the graph details. In this example the graph has 7 vertices and 4 vehicles. The file of the form test-7x4-resp-a.txt include: Tswitch, number of agents, agents' position and goal and the depth search of rta* (details that is given by the user).   

The experiment results for each graph and algorithm combination are stored
in file results.log.

Conclusion

For high values of P we can estimate the optimality of the solution.
For lower values of P we give more weight to the algorithm computation
effort.

On some test graphs, the greedy algorithm is efficient enougth to finding
an almost optimal solution, and then additional computational effort of
RTA* and A* does not justify the improvement.

However, at least on one test graph, test-7x4, not the biggest one, but
apparently difficult, A* finds a solution which is twice as short as
the solution given by greedy, and also much better than the solutions
given by RTA* for all tested horizon depths, of course at a price of a
greater number of expanded nodes in the search phase.

Comparing the algorithms on more complex graphs, problems with multiple
agents, and different heuristics (both weaker and stronger ones) would
give a better understanding of relative performance and suitability of
the implemented algorithms.




  
