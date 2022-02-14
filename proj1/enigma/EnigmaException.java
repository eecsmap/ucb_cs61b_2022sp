package enigma;

/** A general-purpose error-reporting exception for this package.  All
 *  anticipated user or I/O errors should be reported through this
 *  exception, with the message being the error message to be printed.
 *  @author P. N. Hilfinger
 */
class EnigmaException extends RuntimeException {

    /** An exception whose getMessage() value is MSG. */
    EnigmaException(String msg) {
        super(msg);
    }

    /** A utility method that returns a new exception with a message
     *  formed from MSGFORMAT and ARGUMENTS, interpreted as for the
     *  String.format method or the standard printf methods.
     *
     *  The use is thus 'throw error(...)', which tells the compiler that
     *  execution will terminate at that point, and avoid insistance on
     *  an explicit return in a value-returning function.)  */
    static EnigmaException error(String msgFormat, Object... arguments) {
        return new EnigmaException(String.format(msgFormat, arguments));
    }

}
