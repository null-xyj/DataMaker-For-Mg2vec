mkdir tem
cd GraMi-master
./grami -f dblp.lg -s 1 -t 0 -p 0 -n 6 -o ../tem/t1.q -o2 ../tem/raw.txt
cd ..
python prune.py
cd tem
rm -f t1.q raw.txt
cd ..
mkdir output
cd symiso
./symiso data=../Datasets/dblp.lg query=../tem/t2.q subgraph=../tem/t.gdb stats=../output
cd ..
python dataProcess.py
cd randomWalkSampler
python randomWalk.py
python sampler.py
rm -f pair_catch.txt
cd ..
rm -rf output
rm -rf tem
