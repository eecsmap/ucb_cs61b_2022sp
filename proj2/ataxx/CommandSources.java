/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.Stack;

/** Provides command input from a stack of CommandSource objects.
 *  @author P. N. Hilfinger
 */
class CommandSources implements CommandSource {

    /** Read and return a line of input from the input stream,
     *  removing comments and leading and trailing whitespace,
     *  and skipping blank lines.  Returns null when input exhausted.
     *  PROMPT suggests a prompt string that might be used, if
     *  appropriate to the input method. */
    public String getCommand(String prompt) {
        while (!_inputs.isEmpty()) {
            String line;
            line = _inputs.peek().getCommand(prompt);
            if (line != null) {
                if (line.indexOf('#') != -1) {
                    line = line.substring(0, line.indexOf('#'));
                }
                line = line.trim();
                if (line.length() > 0) {
                    return line;
                }
            } else {
                _inputs.pop();
            }
        }
        return null;
    }

    /** Make SOURCE the latest input source from which subsequent input
     *  will be read. */
    void addSource(CommandSource source) {
        _inputs.add(source);
    }

    /** Stack of input sources, most recent on top. */
    private Stack<CommandSource> _inputs = new Stack<>();

}
