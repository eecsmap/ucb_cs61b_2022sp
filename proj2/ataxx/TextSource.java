/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.io.Reader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/** A CommandSource that takes commands from a Reader.
 *  @author P. N. Hilfinger
 */
class TextSource implements CommandSource {

    /** A source of commands read from the concatenation of the content of
     *  READERS. */
    TextSource(List<Reader> readers) {
        if (readers.isEmpty()) {
            throw new IllegalArgumentException("must be at least one reader");
        }
        _readers = new ArrayList<>(readers);
        _inp = new Scanner(readers.remove(0));
    }

    @Override
    public String getCommand(String prompt) {
        if (prompt != null) {
            System.out.print(prompt);
            System.out.flush();
        }
        if (_inp.hasNextLine()) {
            return _inp.nextLine();
        } else if (!_readers.isEmpty()) {
            _inp = new Scanner(_readers.remove(0));
            return getCommand(prompt);
        } else {
            return null;
        }
    }

    /** Source of command input. */
    private Scanner _inp;
    /** Readers to use after the first. */
    private ArrayList<Reader> _readers;
}
