/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.IOException;

/** Provides command input from a Reader.
 *  @author P. N. Hilfinger
 */
class ReaderSource implements CommandSource {

    /** A new source that reads from INPUT and prints prompts
     *  if SHOULDPROMPT. */
    ReaderSource(Reader input, boolean shouldPrompt) {
        _input = new BufferedReader(input);
        _shouldPrompt = shouldPrompt;
    }

    @Override
    public String getCommand(String prompt) {
        if (_input == null) {
            return null;
        }

        try {
            if (_shouldPrompt) {
                System.out.print(prompt);
                System.out.flush();
            }
            String result = _input.readLine();
            if (result == null) {
                _input.close();
            }
            return result;
        } catch (IOException excp) {
            return null;
        }
    }

    /** Input source. */
    private BufferedReader _input;
    /** True if we request a prompt for each getLine. */
    private boolean _shouldPrompt;
}

