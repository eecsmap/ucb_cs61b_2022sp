/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

/** A general-purpose error-reporting exception for this package.  All
 *  anticipated user or I/O errors should be reported through this
 *  exception, with the message being the error message to be printed.
 *  @author P. N. Hilfinger
 */
class GameException extends RuntimeException {

    /** An exception whose getMessage() value is MSG. */
    GameException(String msg) {
        super(msg);
    }

    /** A utility method that returns a new exception with a message
     *  formed from MSGFORMAT and ARGS, interpreted as for the
     *  String.format method or the standard printf methods.
     *
     *  The use is thus 'throw error(...)', which tells the compiler that
     *  execution will terminate at that point, and avoid insistance on
     *  an explicit return in a value-returning function.)  */
    static GameException error(String msgFormat, Object... args) {
        return new GameException(String.format(msgFormat, args));
    }

}
