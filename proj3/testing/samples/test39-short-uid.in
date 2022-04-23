# Test of abbreviated UIDs.
# Check that we can check out a previous version.
> init
<<<
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
= wug.txt notwug.txt
# Each ${HEADER} captures an abbreviation of its full commit UID.
# With high probability, it is unique.
D HEADER "commit ([a-f0-9]{8})[a-f0-9]+"
D DATE "Date: \w\w\w \w\w\w \d+ \d\d:\d\d:\d\d \d\d\d\d [-+]\d\d\d\d"
> log
===
${HEADER}
${DATE}
version 2 of wug.txt

===
${HEADER}
${DATE}
version 1 of wug.txt

===
${HEADER}
${DATE}
initial commit

<<<*
# UID of second version
D UID2 "${1}"
# UID of current version
D UID1 "${2}"
> checkout ${UID1} -- wug.txt
<<<
= wug.txt wug.txt
> checkout ${UID2} -- wug.txt
<<<
= wug.txt notwug.txt
