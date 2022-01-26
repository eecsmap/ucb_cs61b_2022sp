"""This module defines the Tester class, which provides automated
services for performing integration tests.  Here is the outline of a
typical testing script:

    import testing
    import sys, getopt

    class Proj1_Tester(testing.Tester):
        '''customize methods as needed.'''

    show=None
    try:
        opts, args = getopt.getopt(sys.argv[1:], '', ['show='])
        for opt, val in opts:
            if opt == '--show':
                show = int(val)
    except:
        Usage()

    # A tester that tests the program run by the command 'java -ea proj1.Main',
    # gives detailed reports of the first 5 erroneous results,
    # and reports later errors without detail.
    tester = Proj1_Tester(tested_program="java -ea proj1.Main",
                          report_limit=5)

    # Run all tests specified in the command-line arguments and exit
    sys.exit(0 if tester.test_all(args) else 1)

DEFAULT BEHAVIOR
================

By default, testing (via the method .test_all(<TESTS>)) consists of
the following steps:

    1. For each test T of the form B.EXT in TESTS:
       a. Execute the program with the command
            <tested_program> < B.in
          collecting its standard output, standard error, and exit status.
       b. Check that the exit status is 0 (normal Unix exit), the standard
          output is equal to the contents of B.std (if that file exists),
          and the standard error output compares equal to B.err (if that
          file exists).  
       c. If part b fails, print a report containing the contents of
          B.in, B.std, and B.err (the parameter report_limit optionally allows
          limiting the number of erroneous tests that get this full report).

    2. After running all tests, report summary statistics, and return a
       true value iff all of them pass.

CUSTOMIZATION
=============

A good deal of customization is possible.  First, there are various
keyword parameters that you may pass to the constructor for Tester:

    * tested_program: Used in step 1a---the command to run the tested program.
    * time_limit: The time limit (in seconds) for executing the tested program
         for each test [default: 10 seconds]
    * file_size_limit: The limit on the total output of the tested program
         (in 512-byte blocks) [default: 2000]
    * heap_size_limit: Limit on the virtual memory of the tested program (in
         Kbytes) [default: 500000]
    * report_limit: Number of failing test cases to report in detail.
         [default: no limit]
    * report_char_limit: Limit on the number of characters reported for each
         file reported as a result of an erroneous test. [default: 1000].

Second, you may override several methods to affect the test procedure.
For a tester P, the actual test performed for a test that is
designated by T is as follows:

    P.run_program(T)
    P.output_compare(T)
    <report result>
    P.cleanup(T)

By default, the run_program method acts as follows:

    1. Run the command <tested_program> P.command_args(T) (as a shell command),
       with the empty string as the standard input, first using ulimit to
       set the time limit, output limit, and VM limit.
    2. Set P.stdout and P.stderr, respectively, to the standard output and
       standard error of the program.
    3. Set P.rc to the Unix exit status of the program.

You can override the command_args method as desired.  By default, it returns
the string
     < P.standard_input_file(T)

The standard_input_file method is also overridable, and defaults to
returning B.in, where T is B.EXT for some extension EXT (.EXT is
optional), if that file exists, and otherwise returns None.

The output_compare method is supposed to determine the correctness of
P.stdout, P.stderr, and P.rc (as set by run_program) given the test
designation T.  It sets P.reason either to True (indicating the test
passes) or to a string giving a reason for failure.  By default, it
checks P.rc to see that it is 0, compares P.stdout against the
contents of the file named P.stahdard_output_file(T) if the latter
exists (by default, B.std), and P.stderr against the contents of
P.standard_error_file(T) if it exists (by default B.err).  Before the
comparison, it filters P.stdout and P.standard_output_file(T) through
P.output_filter (the identity function by default, and P.stderr and
P.standard_error_file(T) through P.error_filter (the identity by
default).  You can override the standard_output_file,
standard_error_file, output_filter, and error_filter methods.
                                                              
The cleanup method is provided in case a test produces output files
that should be removed.  By default, it does nothing.


REPORTING
=========

When a test fails, then (unless the report_limit is set and has been
reached), the module will produce a report containing the contents of
certain files, as indicated by several overridable methods.  Each of
these methods returns a list of triples of the form (<short name>,
<file name>, <contents>) that describe an input or output of a test
program.  <short name> is used to identify the file on a report.
<file name> is the full pathname of the contents of the file.
Alternatively, if <file name> is None, <contents> is the actual
contents of the file (it is otherwise ignored).

For a testing object P, and test T, the methods are

    * P.input_files(T): Input files.  By default, one entry containing the
        the default value of standard_input_file.
    * P.output_files(T): Output files produced by the test program.
        By default, one entry containing the contents of P.stdout.
    * P.error_files(T): Error files produced by the test program.
        By default, one entry containing the contents of P.stderr.
    * P.standard_output_files(T): The standard (correct) output files.
        By default, one entry containing the default value of
        standard_output_file.
    * P.standard_error_files(T): The standard (correct) error files.
        By default, one entry containing the default value of
        standard_error_file.
"""


import sys, re
import platform
from subprocess import Popen, PIPE
from os.path import splitext, basename, dirname, join, exists
from signal import *

def contents(filename):
    """The contents of FILENAME, or the empty string if the file does not
    exist or is unreadable."""
    try:
        with open(filename) as inp:
            return inp.read()
    except:
        return ''

def interpret_problem(rc, error_output):
    if rc == 1:
        mat = re.search(r'(?m)^Exception in thread ".*" (.*)\s*(at .*)',
                        error_output)
        if mat:
            return "java exception ({exception}) {where}" \
                   .format(exception=mat.group(1), where=mat.group(2))
    if rc < 0:
        rc = 128 - rc
    if 0 < rc < 128:
        return "terminated with non-zero exit code ({rc})"\
               .format(rc=rc)
    elif rc - 128 == SIGKILL:
        return "terminated by kill signal"
    elif rc - 128 == SIGTERM:
        return "terminated by ^C"
    elif rc - 128 == SIGXCPU:
        return "CPU time exceeded"
    elif rc - 128 == SIGXFSZ:
        return "file size limit exceeded (too much data written)"
    else:
        for key in signal.__dict__:
            if re.match(r'SIG', key) and signal.__dict__[key] == rc - 128:
                return "terminated on Unix signal " + key
        return "terminated on Unix signal " + str(rc - 128)

DEFAULT_PARAMS = {
         'tested_program' : None,
         'time_limit' : 10, # seconds
         'file_size_limit' : 2000,  # 512-byte blocks
         'heap_size_limit' : 500000, # KB
         'report_limit' : None,
         'report_char_limit' : 1000,
}        

class Tester:

    def __init__(self, **params):
        self.params = DEFAULT_PARAMS.copy()
        self.params.update(params)
        self.clear()

    def __getattr__(self, name):
        if name in self.params:
            return self.params[name]
        else:
            raise AttributeError(name)

    def clear(self):
        self.passed = self.count = 0
        self.files_shown = set()

    @property
    def failed(self):
        return self.num - self.passed

    def test_all(self, tests):
        self.clear()
        for id in tests:
            self._perform_test(id)
        self._report_summary()
        return self.passed == self.count

    def base_id(self, id):
        """The root name of the test identified by ID. By default, this is
        ID with any parent directory names and any extension stripped."""
        return splitext(basename(id))[0]

    def base_dir(self, id):
        """The directory containing the test file identified by ID.  By
        default, this is the directory segment of ID."""
        return dirname(id)

    def standard_input_file(self, id):
        name = self.base_id(id) + ".in"
        fullname = join(self.base_dir(id), name)
        if exists(fullname):
            return fullname
        else:
            return None

    def standard_output_file(self, id):
        """The standard output file for test ID, if any, else None.
        By default, this is the file in base_dir(ID) with the same simple name
        as ID but with extension .std, if it exists."""
        name = self.base_id(id) + ".std"
        fullname = join(self.base_dir(id), name)
        if exists(fullname):
            return fullname
        else:
            return None

    def standard_error_file(self, id):
        """The standard error file for test ID, if any, else None.
        By default, this is the file in base_dir(ID) with the same simple name
        as ID but with extension .err, if it exists."""
        name = self.base_id(id) + ".err"
        fullname = join(self.base_dir(id), name)
        if exists(fullname):
            return fullname
        else:
            return None

    # Reportable Files

    # Each of the following methods returns a list of triples of the form
    # (<short name>, <file name>, <contents>) that describe an input or output
    # of a test program.  <short name> is used to identify the file on a
    # report.  <file name> is the full pathname of the contents of the file.
    # Alternatively, if <file name> is None, <contents> is the actual contents
    # of the file (it is otherwise ignored). 

    def input_files(self, id):
        """The inputs reportable for test ID. By default, this contains
        the single file in base_dir(ID) with the same simple name
        as ID but with extension .in, if it exists, and otherwise nothing."""
        name = self.base_id(id) + ".in"
        fullname = join(self.base_dir(id), name)
        if exists(fullname):
            return ((name, fullname, None), )
        else:
            return ()

    def output_files(self, id):
        """A list of program outputs to be reported for test ID.  STDOUT is
        the contents of the standard output of the program.  By default
        the result contains the single item ("<standard output>", None, STDOUT)
        unless STDOUT is None, in which case the result is empty."""
        return (() if self.stdout is None
                else (("<standard output>", None, self.stdout),))

    def error_files(self, id):
        """A list of program error outputs to be reported for test ID.  STDERR is
        the contents of the standard error of the program.  By default
        the result contains the single item ("<standard error>", None, STDERR)
        unless STDERR is None, in which case the result is empty."""
        return (() if self.stderr is None
                else (("<standard error>", None, self.stderr),))

    def standard_output_files(self, id):
        """The standard output files to be reported for test ID. By default,
        this contains  the single file in base_dir(ID) with the same simple name
        as ID but with extension .std, if it exists, and otherwise nothing."""
        name = self.base_id(id) + ".std"
        fullname = join(self.base_dir(id), name)
        if exists(fullname):
            return ((name, fullname, None), )
        else:
            return ()

    def standard_error_files(self, id):
        """The standard error files to be reported for test ID. By default,
        this contains  the single file in base_dir(ID) with the same simple name
        as ID but with extension .err, if it exists, and otherwise nothing."""
        name = self.base_id(id) + ".err"
        fullname = join(self.base_dir(id), name)
        if exists(fullname):
            return ((name, fullname, None), )
        else:
            return ()

    # End of Reportable Files

    def run_program(self, id):
        """Run the test program for test ID, placing the standard output in
        SELF.stdout the standard error output in SELF.stderr, and the return
        code in SELF.rc. By default, runs SELF._command_line(ID), pipes
        the results to SELF.stdout and SELF.stderr, and places the return code
        in SELF.rc.  Overridings may set .stdout or .stderr to None, indicating
        that they are irrelevant."""
        proc = Popen(self._command_line(id),
                     shell=True, universal_newlines=True, 
                     stdout=PIPE, stderr=PIPE, stdin=PIPE)
        self.stdout, self.stderr = proc.communicate('')
        self.rc = proc.returncode

    def _perform_test(self, id):
        self.reason = None
        self.count += 1
        self.run_program(id)
        self.output_compare(id)
        if self.reason is True:
            self.passed += 1
            self._report_pass(id)
        else:
            self._report_fail(id)
        self.cleanup(id)

    def command_args(self, testid):
        """The default command-line arguments that follow the shell syntax
        to invoke the tested program.  The default is simply a redirect of
        the standard input from the input file for TESTID, as determined by
        .standard_input_file."""
        return " <" + self.standard_input_file(testid) + " "

    def _command_line(self, id):
        if platform.system() == "Windows" or platform.system() == "Darwin":
            command_fmt = "{command} {args}"
        else:
            command_fmt = "ulimit -St {timeout}; ulimit -f {filesize}; " \
                          "ulimit -d {memsize}; " \
                          "{command} {args}"
        return command_fmt.format(command=self.tested_program,
                                  args=self.command_args(id),
                                  timeout=self.time_limit,
                                  filesize=self.file_size_limit,
                                  memsize=self.heap_size_limit)

    def output_compare(self, testid):
        """The default comparison procedure to use with test TESTID.  Sets
        self.reason to the result of the test.  In what follows, STDOUT
        and STDERR are the output sent by the tested program to the standard
        output and standard error, respectively.  RC is the return code
        returned by the process.

        Sets .reason to True iff 
        (1) RC is 0; and
        (2) .stdout compares equal against the contents of file
            .standard_output_file(TESTID), after first converting both
            with .output_filter.
        (3) .stderr compares equal against the contents of file
            .standard_error_file(TESTID), after first converting both
            with .error_filter.
        Otherwise, sets .reason to message describing error."""

        if self.rc != 0:
            self.reason = "Program exited abnormally: {}" \
                              .format(interpret_problem(self.rc, self.stderr))
        elif self.stdout is not None and self.standard_output_file(testid) \
             and self.output_filter(testid, self.stdout) \
                 != self.output_filter(testid,
                                       contents(self.standard_output_file(testid))):
            self.reason = "Output does not match expected output."
        elif self.stderr is not None and self.standard_error_file(testid) \
             and self.error_filter(testid, self.stderr) \
                 != self.error_filter(testid,
                                      contents(self.standard_error_file(testid))):
            self.reason = "Error output does not match expected output."
        else:
            self.reason = True
    
    def output_filter(self, id, content):
        """A filter applied to the standard output before comparing with
        expected output for test ID."""
        return content

    def error_filter(self, id, content):
        """A filter applied to the standard error before comparing with
        expected error output for test ID."""
        return content

    def cleanup(self, id):
        """Clean up any files or resources used in this test.  The default
        implementation does nothing."""
        pass

    def _report_pass(self, id):
        print("** {id} PASSED.".format(id=self.base_id(id)))
        sys.stdout.flush()
        
    def _report_fail(self, id):
        base = self.base_id(id)
        print("** {id} FAILED ({reason})".format(id=base, reason=self.reason))
        sys.stdout.flush()
        suppress = (self.report_limit is not None and 
                    self.count - self.passed > self.report_limit)
        if suppress and self.count - self.passed == self.report_limit + 1:
            print()
            print("*** Encountered more than {limit} errors.  "
                  "Further detailed reports suppressed. ***"
                  .format(limit=self.report_limit))
            print()
        if not suppress:
            for title, name_pairs in (
                ("**** INPUT FILES:", self.input_files(id)), 
                ("**** OUTPUTS FROM TEST PROGRAM:", self.output_files(id)),
                ("**** EXPECTED OUTPUTS:", self.standard_output_files(id)),
                ("**** ERROR OUTPUTS FROM TEST PROGRAM:", self.error_files(id)),
                ("**** EXPECTED ERROR OUTPUTS:", self.standard_error_files(id))):

                self._print_report(title, name_pairs)
            print("** End of {id} error report **".format(id=base))
            print()

    def _print_report(self, title, name_pairs):
        if not name_pairs:
            return
        print()
        print(title)
        limit = self.report_char_limit

        for name, filename, content in name_pairs:
            if filename is not None:
                if filename in self.files_shown:
                    print("[{name} shown previously]".format(name=name))
                    continue
                content = contents(filename)
                self.files_shown.add(filename)
            truncated = False
            if limit and len(content) > limit:
                end = content.rfind("\n", 0, limit)
                if end == -1:
                    segment = content[:limit] + "\n"
                    truncated = limit
                else:
                    segment = content[:end+1]
                    truncated = end+1
            else:
                segment = content
                if segment != "" and not segment.endswith('\n'):
                    segment += "\n<does not end with newline>\n"
            if name:
                print("+--- " + name + " " + "-" * (60-len(name)) + "+")
            else:
                print("+" + "-" * 65 + "+")
            sys.stdout.write(segment)
            if truncated:
                print("... + {} more characters [listing truncated]"\
                      .format(truncated))
            print("+" + "-" * 65 + "+")

    def _report_summary(self):
        print()
        if self.passed == self.count:
            print("Passed all {num} tests.".format(num=self.count))
        else:
            print("Passed {passed} out of {num} tests."
                  .format(passed=self.passed, num=self.count))
