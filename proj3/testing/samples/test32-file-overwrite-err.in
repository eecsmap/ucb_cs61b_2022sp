# Make sure checkout does not overwrite files.
I prelude1.inc
> branch other
<<<
+ f.txt wug.txt
+ g.txt notwug.txt
> add g.txt
<<<
> add f.txt
<<<
> commit "Main two files"
<<<
E f.txt
E g.txt
> checkout other
<<<
+ f.txt notwug.txt
> checkout master
There is an untracked file in the way; delete it, or add and commit it first.
<<<
