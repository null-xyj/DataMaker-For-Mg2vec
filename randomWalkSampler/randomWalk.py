import torch
from dgl import graph
from dgl import sampling

cores = set()
U = list()
V = list()
count_node = 0
with open("../Datasets/dblp.lg", 'r') as f:
    for line in f:
        line = line.strip().split()
        if line[0] == 'v':
            if line[2] == '1':
                cores.add(int(line[1]))
        elif line[0] == 'e':
            u = int(line[1])
            v = int(line[2])
            U.append(u)
            U.append(v)
            V.append(v)
            V.append(u)

g = graph((torch.tensor(U), torch.tensor(V)))
print(g)

core_list = list(cores) * 10
trace = sampling.random_walk(g, core_list, length=100)
trace = trace[0]
import numpy as np

trace = trace.numpy().tolist()
print(len(trace))

window_size = 5
pair_catch = set()
cnt = 0
for t in trace:
    cnt+=1
    if cnt % 1000 == 0:
        print('the {} trace start'.format(cnt))
    for i, u in enumerate(t):
        for j, v in enumerate(t[max(i - window_size, 0):i + window_size]):
            if u < 0 or v < 0:
                continue
            if u >= g.num_nodes() or v >= g.num_nodes():
                continue
            if u not in cores or v not in cores:
                continue

            pair_catch.add((int(u), int(v)))

print(pair_catch)
with open('pair_catch.txt', 'w') as f:
    for i in pair_catch:
        s = str(i[0]) + ' ' + str[i[1]] + '\n'
        f.write(s)

