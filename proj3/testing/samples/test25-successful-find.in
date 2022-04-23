# Test the find command when it succeeds.
I setup2.inc
> rm f.txt
<<<
> commit "Remove one file"
<<<
+ f.txt notwug.txt
> add f.txt
<<<
> commit "Two files"
<<<
> log
===
${COMMIT_HEAD}
Two files

===
${COMMIT_HEAD}
Remove one file

===
${COMMIT_HEAD}
Two files

===
${COMMIT_HEAD}
initial commit

<<<*
# UID of initial version
D UID1 "${4}"
# UID of second version
D UID2 "${3}"
# UID of third version
D UID3 "${2}"
# UID of current version
D UID4 "${1}"
> find "Two files"
(${UID4}\n${UID2}|${UID2}\n${UID4})
<<<*
> find "initial commit"
${UID1}
<<<
> find "Remove one file"
${UID3}
<<<
