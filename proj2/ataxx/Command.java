/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static ataxx.PieceColor.*;
import static ataxx.GameException.*;

/** All things to do with parsing commands.
 *  @author P. N. Hilfinger
 */
class Command {

    /** A list of all commands. */
    private static final String[] COMMAND_NAMES = {
        "auto", "block", "board", "dump", "help", "manual",
        "new", "q", "quiet", "quit", "seed", "undo", "verbose",
    };

    /** Command types.  PIECEMOVE indicates a move of the form
     *  c0r0-c1r1.  ERROR indicates a parse error in the command.
     *  All other commands are upper-case versions of what the
     *  programmer writes. */
    enum Type {
        COMMENT("#.*|$"),
        AUTO("auto\\s+(red|blue)"),
        BLOCK("block\\s+([a-g][1-7])"),
        MANUAL("manual\\s+(red|blue)"),
        SEED("seed\\s+(\\d+)"),
        START,
        /* Regular moves. */
        PIECEMOVE("(-|[a-g][1-7]-[a-g][1-7])"),
        QUIT("q|quit"),
        NEW, DUMP, HELP,
        /* Extra commands. */
        BOARD, VERBOSE, QUIET, UNDO,
        /* Special "commands" internally generated. */
        /** Syntax error in command. */
        ERROR(".*"),
        /** End of input stream. */
        EOF;

        /** PATTERN is a regular expression string giving the syntax of
         *  a command of the given type.  It matches the entire command,
         *  assuming no leading or trailing whitespace.  The groups in
         *  the pattern capture the operands (if any). */
        Type(String pattern) {
            _pattern = Pattern.compile(pattern + "$");
        }

        /** A Type whose pattern is the lower-case version of its name. */
        Type() {
            _pattern = Pattern.compile(this.toString().toLowerCase() + "$");
        }

        /** The Pattern descrbing syntactically correct versions of this
         *  type of command. */
        private final Pattern _pattern;

    }

    /** A new Command of type TYPE with OPERANDS as its operands. */
    Command(Type type, String... operands) {
        _type = type;
        _operands = operands;
    }

    /** Return the type of this Command. */
    Type commandType() {
        return _type;
    }

    /** Returns this Command's operands. */
    String[] operands() {
        return _operands;
    }

    /** Parse COMMAND, returning the command and its operands. */
    static Command parseCommand(String command) {
        if (command == null) {
            return new Command(Type.EOF);
        }
        command = canonicalizeCommand(command);
        for (Type type : Type.values()) {
            Matcher mat = type._pattern.matcher(command);
            if (mat.matches()) {
                String[] operands = new String [mat.groupCount()];
                for (int i = 1; i <= operands.length; i += 1) {
                    operands[i - 1] = mat.group(i);
                }
                return new Command(type, operands);
            }
        }
        throw new Error("Internal failure: error command did not match.");
    }

    /** Return COMMAND with the full command name that uniquely fits
     *  substituted for the command name. COMMAND may start with any
     *  prefix of a valid command name, as long as that name is unique.
     *  If the name is not unique or no command name matches,
     *  returns COMMAND. */
    private static String canonicalizeCommand(String command) {
        command = command.trim();

        if (command.length() == 0) {
            return  "";
        } else if (command.startsWith("#")) {
            return "#";
        }
        command = command.toLowerCase();

        int prefixLen = Math.max(command.indexOf(" "), command.length());
        String prefix = command.substring(0, prefixLen);

        String fullName;
        fullName = null;
        for (String name : COMMAND_NAMES) {
            if (name.equals(prefix)) {
                fullName = prefix;
                break;
            }
            if (name.startsWith(prefix)) {
                if (fullName != null) {
                    throw error("%s is not a unique prefix abbreviation",
                                prefix);
                }
                fullName = name;
            }
        }
        if (fullName != null) {
            return fullName + command.substring(prefixLen);
        } else {
            return command;
        }
    }

    /** The command name. */
    private final Type _type;
    /** Command arguments. */
    private final String[] _operands;
}
