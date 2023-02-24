# Gitlet Design Document

**Name**:

## Classes and Data Structures

### Class 1

#### Fields

1. Field 1
2. Field 2


### Class 2

#### Fields

1. Field 1
2. Field 2


## Algorithms

## Persistence

add思路
首先为目标文件构造一个新的blob
其次将这个blob的sha存储到index blobinfo中
最后将这个blob的sha和之前commit的sha比较，若相同，删除index中这个sha