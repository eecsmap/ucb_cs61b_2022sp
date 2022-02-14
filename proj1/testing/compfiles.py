import sys, re

def canon(text):
    text = text.rstrip()
    text = re.sub('\r', '', text)
    text = re.sub(r'(?m) +$', '', text)
    text = re.sub(r'  +', ' ', text)
    return text

with open(sys.argv[1]) as inp1, open(sys.argv[2]) as inp2:
    data1 = canon(inp1.read())
    data2 = canon(inp2.read())

if data1 == data2:
    sys.exit(0)
else:
    sys.exit(1)
