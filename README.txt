AI assignment 1.

authors: Vered Shani & Ilya Davidovich

Explenations of Heuristic Function used on part 2 of the assignment:

Describtion:

For every given state, our heuristic function returns the time 
to pass the shortest path from the agent's cuurent possition to
it's goal possition assuming all vehicles are accessible from 
every vertex.

The huristic function uses Dijkstra algorithm to compute the shortest
path in a graph where the weight of each edge is the time to traverse 
this edge (weight divided by the best effective vehicle speed), considering
the edge state (flooded of clear).

Motivation:

We wanted an heuristic function that will solve an easier problem. The main 
problem of the agents is that they don't know the possitions of the vehicles,
so there are a lot of trade-offs. For example, it is possible that the agent 
will need to choose between a fast regular vehicle and a slower amphibious 
vehicle, because later on he might need to traverse a flooded edge.

We also wanted to choose an admissible heuristic so for A* and RTA* based agents.
One way to make sure an heuristic function is admissible is to lower constraints.
So we ignored the constraint that we might not have an compatible vehicle when 
reaching a certain vertex.

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
vehicle). So the actual cast of each action xi is not smaller then the cost we get
if our computation. Meaning, for every 0<=i<=n, our_cost(xi) <= actual_cost(xi), 
and we get: h(n) = our_cost(P) <= actual_cost(P) = h*(n).
 

  