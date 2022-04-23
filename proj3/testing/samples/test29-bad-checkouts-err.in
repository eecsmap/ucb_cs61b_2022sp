# Check that we can check out a previous version.
I prelude1.inc
+ wug.txt wug.txt
> add wug.txt
<<<
> commit "version 1 of wug.txt"
<<<
+ wug.txt notwug.txt
> add wug.txt
<<<
> commit "version 2 of wug.txt"
<<<
> log
===
${COMMIT_HEAD}
version 2 of wug.txt

===
${COMMIT_HEAD}
version 1 of wug.txt

===
${COMMIT_HEAD}
initial commit

<<<*
# UID of second version
D UID2 "${1}"
# UID of current version
D UID1 "${2}"
> checkout ${UID2} -- warg.txt
File does not exist in that commit.
<<<
> checkout 5d0bc169a1737e955f9cb26b9e7aa21e4afd4d12 -- wug.txt
No commit with that id exists.
<<<
> checkout ${UID2} ++ wug.txt
Incorrect operands.
<<<
> checkout foobar
No such branch exists.
<<<
> checkout master
No need to checkout the current branch.
<<<
