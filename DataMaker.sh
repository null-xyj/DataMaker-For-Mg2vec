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
cd ../randomWalkSampler
python randomWalk.py
cd ..
python dataProcess.py
rm -rf output
rm -rf tem