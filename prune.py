class Node:
    def __init__(self, node_id, node_type):
        self.id = node_id
        self.type = node_type
        self.degree = 0


class Edge:
    def __init__(self, node1, node2, edge_type):
        self.node1 = node1
        self.node2 = node2
        self.type = edge_type


class Automorphism:
    def __init__(self, node, automorphisms):
        self.node = node
        self.automorphisms = automorphisms


class MetaGraph:
    def __init__(self):
        self.nodes = dict()
        self.edges = list()
        self.core_nodes = 0
        self.automorphisms = list()
        self.node_types = set()
        self.edge_types = set()
        self.to_prune = False

    def pruning(self, core_type):
        if self.core_nodes < 2:
            self.to_prune = True
            return
        if len(self.node_types) == 1 and len(self.edge_types) == 1:
            self.to_prune = True
            return
        for node in self.nodes.values():
            if node.type != core_type and node.degree == 1:
                self.to_prune = True
                return

    def to_string(self):
        s = "t #\n"
        for v in self.nodes.values():
            s += ('v ' + v.id + ' ' + v.type + '\n')
        for e in mg.edges:
            s += ('e ' + e.node1 + " " + e.node2 + " " + e.type + '\n')
        for a in mg.automorphisms:
            s += ('a ' + a.node + " " + ' '.join(a.automorphisms) + '\n')
        return s


mgs = set()
core_type = '1'
# with open("dataset/dblp/subgraphsWithAutomorphism", 'r') as f:
with open("tem/t1.q", 'r') as f:
    mg = MetaGraph()
    for line in f.readlines():
        line = line[:-1]
        if line == 't #':
            if len(mg.nodes) == 0:
                continue
            else:
                mg.pruning(core_type)
                if not mg.to_prune:
                    mgs.add(mg)
                else:
                    print("pruning\n")
                    print(mg.to_string())
            mg = MetaGraph()
            continue
        line = line.split()
        if line[0] == 'v':
            n = Node(line[1], line[2])
            mg.nodes[line[1]] = n
            if line[2] == core_type:
                mg.core_nodes += 1
            mg.node_types.add(line[2])
        elif line[0] == 'e':
            e = Edge(line[1], line[2], line[3])
            mg.edges.append(e)
            mg.edge_types.add(line[3])
            mg.nodes[line[1]].degree += 1
            mg.nodes[line[2]].degree += 1
        else:
            a = Automorphism(line[1], line[2:])
            mg.automorphisms.append(a)
    f.close()

with open("tem/t2.q", 'w') as f:
    for mg in mgs:
        f.write(mg.to_string())

print(len(mgs))

