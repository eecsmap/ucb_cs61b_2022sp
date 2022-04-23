# Create two branches and merge other into master.
I setup2.inc
> branch other
<<<
+ h.txt wug2.txt
> add h.txt
<<<
> rm g.txt
<<<
> commit "Add h.txt and remove g.txt"
<<<
> checkout other
<<<
> rm f.txt
<<<
+ k.txt wug3.txt
> add k.txt
<<<
> commit "Add k.txt and remove f.txt"
<<<
> checkout master
<<<
> merge other
<<<
* f.txt
* g.txt
= h.txt wug2.txt
= k.txt wug3.txt
> log
===
${COMMIT_HEAD}
Merged other into master\.

${ARBLINES}
<<<*
I blank-status2.inc
