import sys

# skip the number of nodes
for line in sys.stdin:
	words = line.split()
	if not words:
		continue
	if words[0]=="#V":
		break

print "graph ATP {\n  rankdir=LR;\n"
ivcl=1
for line in sys.stdin:
	words = line.split()
	if not words:
		continue
	if words[0]=="#V": 
		vcl_at,speed,factor = words[1:]
		print "  node [shape=box];"
		print "  Vcl%s -- V%s [label=\"%s/%s\"];" % (ivcl, vcl_at, float(speed), float(speed)*float(factor))
		ivcl=ivcl+1
	elif words[0]=="#E":
		v_from,v_to,weight,state = words[1:]
		weight = weight[1:]
		print "  node [shape=ellipse];"
		print "  V%s -- V%s [label=%s, style=%s];" % (v_from, v_to, weight, (state=="F" and "dashed" or "bold"))

# vehicles
print "}"

