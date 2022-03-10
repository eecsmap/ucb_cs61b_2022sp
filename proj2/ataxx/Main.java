/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import static ataxx.Utils.*;

import ucb.util.CommandArgs;

/** The main program for Ataxx.
 *  @author P. N. Hilfinger */
public class Main {

    /** Location of usage message resource. */
    static final String USAGE = "ataxx/Usage.txt";

    /** Run Ataxx game.  Options (in ARGS0):
     *       --display: Use GUI.
     *       --timing: Display think times for AI.
     *       --version: Print version number and exit.
     *       --log: Print commands.
     *       --strict: Strict mode---players errors cause error exit.
     *       --debug: Set level of debugging information.
     *  Trailing arguments are input files; the standard input is the
     *  default.
     */
    public static void main(String[] args0) {
        CommandArgs args =
            new CommandArgs("--display{0,1} --strict --version --timing --log"
                            + " --debug=(\\d+){0,1} --=(.*){0,}", args0);


        System.out.println("CS61B Ataxx! Version 3.0");

        if (!args.ok()) {
            usage();
            return;
        }

        if (args.contains("--version")) {
            System.err.printf("Version %s%n", Defaults.VERSION);
            System.exit(0);
        }

        _strict = args.contains("--strict");
        boolean log = args.contains("--log");
        if (args.contains("--debug")) {
            Utils.setMessageLevel(args.getInt("--debug"));
        }

        Game game;
        if (args.contains("--display")) {
            GUI display = new GUI("Ataxx!");
            game = new Game(display, display, display, log);
            display.pack();
            display.setVisible(true);
        } else {
            TextSource source;
            ArrayList<Reader> inReaders = new ArrayList<>();
            if (args.get("--").isEmpty()) {
                inReaders.add(new InputStreamReader(System.in));
            } else {
                for (String name : args.get("--")) {
                    if (name.equals("-")) {
                        inReaders.add(new InputStreamReader(System.in));
                    } else {
                        try {
                            inReaders.add(new FileReader(name));
                        } catch (IOException excp) {
                            System.err.printf("Could not open %s", name);
                            System.exit(1);
                        }
                    }
                }
            }
            game = new Game(new TextSource(inReaders),
                            (b) -> { }, new TextReporter(), log);
        }
        System.exit(game.play());
    }

    /** Print usage message. */
    private static void usage() {
        printHelpResource(USAGE, System.err);
    }

    /* STRICT MODE */

    /** Return true iff --strict flag supplied. */
    static boolean isStrict() {
        return _strict;
    }

    /* TIMING */

    /** Start timing an operation. */
    static void startTiming() {
        if (_timing) {
            _startTime = System.currentTimeMillis();
        }
    }

    /** End the timing started with the last call to startTiming().
     *  Report result if we are timing. */
    static void endTiming() {
        if (_timing) {
            long time = System.currentTimeMillis() - _startTime;
            System.err.printf("[%d msec]%n", time);
            _maxTime = Math.max(_maxTime, time);
            _totalTime += time;
            _numTimedOps += 1;
        }
    }

    /** Report total time statistics, if timing. */
    static void reportTotalTimes() {
        if (_timing && _numTimedOps > 0) {
            System.err.printf("[Total time: %d msec for %d operations. "
                              + "Avg: %d msec/operation. "
                              + "Max: %d msec]%n", _totalTime,
                              _numTimedOps, _totalTime / _numTimedOps,
                              _maxTime);
        }
    }

    /** True iff AIs should time. */
    private static boolean _timing;

    /** True iff using strict mode (in which errors detected by the
     *  manual player terminate the program with an error code of 2. */
    private static boolean _strict;

    /** Accumulated time. */
    private static long _totalTime;

    /** Last start time. */
    private static long _startTime;

    /** Number of operations timed. */
    private static int _numTimedOps;

    /** Maximum operation time. */
    private static long _maxTime;

}
