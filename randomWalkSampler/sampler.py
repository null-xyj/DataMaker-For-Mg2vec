ps = set()
with open('pair_catch.txt', 'r') as f:
    for line in f:
        line = line.split()
        ps.add((int(line[0]), int(line[1])))

tot = 0
with open('../res.txt', 'w') as fout:
    with open('../tem/res1.txt', 'r') as f:
        cnt = 0
        for line in f:
            cnt += 1
            if cnt % 100000 == 0:
                print('sampling {}'.format(cnt))
            t = line.split()
            if (int(t[0]), int(t[1])) in ps or (int(t[1]), int(t[0])) in ps:
                fout.write(line)
                tot += 1

print('original {} now {}'.format(cnt, tot))
