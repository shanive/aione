import sys

# skip the number of nodes
for line in sys.stdin:
	words = line.split()
	if not words:
		continue
	if words[0]=="#V":
		break

print "graph ATP {\n  rankdir=LR;\n  node [shape=ellipse];\n"
for line in sys.stdin:
	words = line.split()
	if not words:
		continue
	if words[0]=="#V": # break out on vehicles
		break
	v_from,v_to,weight,state = words[1:]
	weight = weight[1:]
	print "  V%s -- V%s [label=%s, style=%s];" % (v_from, v_to, weight, (state=="F" and "dashed" or "bold"))
print "}"

