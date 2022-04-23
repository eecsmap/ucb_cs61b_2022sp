# Check various merge error cases.
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
> merge other
Cannot merge a branch with itself.
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
> merge foobar
A branch with that name does not exist.
<<<
+ k.txt wug.txt
> merge other
There is an untracked file in the way; delete it, or add and commit it first.
<<<
- k.txt
I blank-status2.inc
+ k.txt wug.txt
> add k.txt
<<<
> merge other
You have uncommitted changes.
<<<
> rm k.txt
<<<
- k.txt
I blank-status2.inc
