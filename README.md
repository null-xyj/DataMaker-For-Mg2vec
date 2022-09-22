# DataMaker-For-Mg2vec
This repository aims to help you learn how to process data in order to use mg2vec model.

## Method

![DataMaker](/Users/idealist/Documents/DataMaker.svg)

### 1. What data we should prepare

All we need to prepare is only a file to describe the topological structure of our graph following the format like the dblp.lg (what has been prepared in the Datasets Folder). For details, please visit https://www.yfang.site/data-and-tools/labeled-graph-format.

After we have prepared the file in proper format, just put the file in the Datasets folder.

### 2. What environment we should prepare

1. Linux Operation System
2. A jdk to execute java program (Grami and SymISO)
3. Python3 with DGL library

### 3. How to use our tool

After we have prepared all the data and environment, just run the commend:

```shell
./DataMaker.sh
```

Then, our tool will give you a file (res.txt) and you can run the mg2vec model with this file.

### Appendix

For more details about Grami and SymISO, please visit http://www.yfang.site/data-and-tools/grami-with-automorphism and http://www.yfang.site/data-and-tools/submatch.
