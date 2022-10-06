import numpy as np
import pandas as pd


class metaGraph:
    def __init__(self):
        self.num = -1
        self.nodeNum = -1
        self.edgeNum = -1
        self.frequency = 0
        self.edges = list()


mgFilename = "tem/t.gdb"
instanceFilename = "output/"

mgs = list()
mg = metaGraph()
with open(mgFilename, 'r') as f:
    for line in f:
        line = line[:-1]
        line = line.split('\t')
        if line[0] == '#':
            mg = metaGraph()
            mg.num = int(line[1])
            mg.nodeNum = int(line[2])
            mg.edgeNum = int(line[3])
            mg.coreList = list()
        elif line[0] == 'T':
            for t in range(len(line[1:])):
                if line[t + 1] == '1':
                    mg.coreList.append(t)
        elif line[0] == 'E':
            for s in range(1, len(line), 2):
                mg.edges.append([int(line[s]), int(line[s + 1])])
        elif line[0] == 'F':
            mg.frequency = int(line[1])
            mgs.append(mg)

ps = set()
with open('tem/pair_catch.txt', 'r') as f:
    for line in f:
        line = line.split()
        ps.add((int(line[0]), int(line[1])))
        ps.add((int(line[1]), int(line[0])))

with open(r"res.txt", 'w') as fout:
    cnt = 0
    for mg in mgs:
        filename = instanceFilename + str(mg.num)
        try:
            with open(filename, 'r') as f:
                print(filename)
                pair_dict = dict()
                pair_dict.clear()
                for line in f:
                    line = line[:-1]
                    line = line.split('\t')
                    core_nodes = [int(line[i]) for i in mg.coreList]
                    core_nodes = sorted(core_nodes)
                    core_num = len(core_nodes)
                    for i in range(core_num):
                        for j in range(i, core_num):
                            x = core_nodes[i]
                            y = core_nodes[j]
                            if (x, y) in ps:
                                if x == y:
                                    if (x, x) in pair_dict:
                                        pair_dict[(x, x)] += 1
                                    else:
                                        pair_dict[(x, x)] = 1
                                else:
                                    if (x, y) in pair_dict:
                                        pair_dict[(x, y)] += 1
                                    else:
                                        pair_dict[(x, y)] = 1

                for key, val in pair_dict.items():
                    x = key[0]
                    y = key[1]
                    val = np.around(np.log(int(line[3][1:]) + 1), 4)
                    toWrite = str(x) + ' ' + str(y)
                    toWrite += (' m' + str(mg.num))
                    toWrite += (' f' + str(val) + '\n')
                    fout.write(toWrite)
                    toWrite = str(y) + ' ' + str(x)
                    toWrite += (' m' + str(mg.num))
                    toWrite += (' f' + str(val) + '\n')
                    fout.write(toWrite)

        finally:
            if f:
                f.close()
