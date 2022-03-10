#!/usr/bin/env python3
# -*-Python-*-

import os, sys, re
import io
import getopt
from os.path import join, exists, dirname, basename
from testing import Tester, contents, interpret_problem

PYTHON = os.environ.get('PYTHON', 'python3')

PROGRAM = f"{PYTHON} {join(dirname(__file__),'test-ataxx')}"

os.environ.pop('DISPLAY', None)

def safe_remove(files):
    for f in files:
        try:
            os.remove(f)
        except IOError:
            pass

class Proj2_Tester(Tester):
    def _command_line(self, id):
        return "{command} {args}" \
             .format(command=self.tested_program,
                     args=self.command_args(id))

    def input_files(self, id):
        std = super().input_files(id)
        name2 = re.sub(r'-1\.in$', '-2.in', std[0][0])
        fullname2 = join(self.base_dir(id), name2)
        if exists(fullname2):
            return std + ((name2, fullname2, None),)
        else:
            return std

    def _conv_inputs(self, id, repl):
        files = self.input_files(id)
        patn = r"(.*)\.in$"
        return tuple ( ( (re.sub(patn, repl, entry[0]),
                          re.sub(patn, repl, entry[1]), None)
                         for entry in files ) )

    def output_files(self, id):
        return [ (basename(e[0]), basename(e[1]), None) for e in
                 self._conv_inputs(id, r'\1.out') ]

    def error_files(self, id):
        return [ (basename(e[0]), basename(e[1]), None) for e in
                 self._conv_inputs(id, r'\1.err') ]

    def standard_output_files(self, id):
        return self._conv_inputs(id, r'\1.std')

    def standard_error_files(self, id):
        return []

    def command_args(self, testid):
        return " ".join([ fullname for _, fullname, _
                          in self.input_files(testid) ])

    def output_filter(self, id, text):
        text = re.sub(r'(?m)(?:#.*|[ \t]+$)', '', text)
        text = re.sub(r'(?m)(?:^\r?\n)', '', text)
        return text

    def output_compare(self, id):
        if self.rc == 0:
            self.reason = True
            for (_, output, _), (_, std, _) \
                    in zip(self.output_files(id),
                           self.standard_output_files(id)):
                if self.output_filter(id, contents(output)) \
                       != self.output_filter(id, contents(std)):
                    self.reason = "Output(s) do not all match expected output(s)."
                    return
        elif not self.stderr.strip():
            self.reason = "unknown error"
        else:
            self.reason = self.stderr.strip()

    def cleanup(self, id):
        if not self.keep:
            safe_remove([ f for _, f, _ in
                          self.output_files(id) + self.error_files(id)])


show = None
keep = False
try:
    opts, args = getopt.getopt(sys.argv[1:], '', ['show=', 'keep', 'PYTHON='])
    for opt, val in opts:
        if opt == '--show':
            show = int(val)
        elif opt == '--keep':
            keep = True
        elif opt == '--PYTHON':
            PYTHON = val
            PROGRAM = PYTHON + " " + "./test-ataxx"
        else:
            assert False
except:
    print("Usage: python3 tester.py [--show=N] [--PYTHON=python] TEST.in...",
          file=sys.stderr)
    sys.exit(1)

tester = Proj2_Tester(tested_program=PROGRAM, report_limit=show,
                      report_char_limit=10000, keep=keep)

sys.exit(0 if tester.test_all(args) else 1)
