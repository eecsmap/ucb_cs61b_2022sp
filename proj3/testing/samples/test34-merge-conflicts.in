# Create two branches and merge other into master with a merge conflict.
I setup2.inc
> branch other
<<<
+ h.txt wug2.txt
> add h.txt
<<<
> rm g.txt
<<<
+ f.txt wug2.txt
> add f.txt
<<<
> commit "Add h.txt, remove g.txt, and change f.txt"
<<<
> checkout other
<<<
+ f.txt notwug.txt
> add f.txt
<<<
+ k.txt wug3.txt
> add k.txt
<<<
> commit "Add k.txt and modify f.txt"
<<<
> checkout master
<<<
> log
===
${COMMIT_HEAD}
${ARBLINES}
<<<*
D MASTER_HEAD "${1}"
> merge other
Encountered a merge conflict.
<<<
* g.txt
= h.txt wug2.txt
= k.txt wug3.txt
= f.txt conflict1.txt
> log
${COMMIT_LOG}
===
commit ${MASTER_HEAD}
${ARBLINES}
<<<*
> status
=== Branches ===
\*master
other

=== Staged Files ===

=== Removed Files ===

=== Modifications Not Staged For Commit ===

=== Untracked Files ===

<<<*

