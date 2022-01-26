#!/usr/bin/env python3
# -*-Python-*-

from testing import Tester, contents, interpret_problem
import os, sys, re
import io
import getopt
from os.path import join

PROGRAM = "java -ea blocks.Main --testing --no-display"

os.environ.pop('DISPLAY', None)

def compare_boards(received, expected_pattern):
    """Compares user output RECEIVED to expected output EXPECTED_PATTERN.

       Returns either True (if OK) or an error message string."""
    actual_boards = re.findall(r'(?ms)^B\[.*?\]', received)
    expected_boards = re.findall(r'(?ms)^\*?\*?B\[.*?\]', expected_pattern)
    if len(actual_boards) != len(expected_boards):
        return "There are {} output boards; expected {}."\
               .format(len(actual_boards), len(expected_boards))
    for cnt, (actual_board, std_board) in \
        enumerate(zip(actual_boards, expected_boards), 1):
        if actual_board != std_board:
            return f"Board #{cnt} does not match standard"
    return True
                           
class Proj0_Tester(Tester):
    def output_filter(self, id, text):
        text = re.sub(r'#.*\r?\n','',text)
        return text

    def output_compare(self, testid):
        standard = self.standard_output_file(testid)
        if self.rc != 0:
            self.reason = "Program exited abnormally: {}" \
                              .format(interpret_problem(self.rc, self.stderr))
            return
        elif self.stdout is not None and standard:
            self.reason = compare_boards(self.output_filter(testid, self.stdout),
                                        self.output_filter(testid,
                                                           contents(standard)))
            if self.reason is not True:
                return
        if self.stderr:
            self.reason = "Error output is not empty."
        else:
            self.reason = True

show=None
try:
    opts, args = getopt.getopt(sys.argv[1:], '', ['show='])
    for opt, val in opts:
        if opt == '--show':
            show = int(val)
        else:
            assert False
except:
    print("Usage: python3 tester.py [--show=N] TEST.in...",
          file=sys.stderr)
    sys.exit(1)

tester = Proj0_Tester(tested_program=PROGRAM, report_limit=show,
                      report_char_limit=10000)

sys.exit(0 if tester.test_all(args) else 1)

