package image;

/*************************************************************************
 *  Compilation:  javac Out.java
 *  Execution:    java Out
 *
 *  Writes data of various types to: stdout, file, or socket.
 *
 *************************************************************************/


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

/**
 *  This class provides methods for writing strings and numbers to
 *  various output streams, including standard output, file, and sockets.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Out {

    /** Force Unicode UTF-8 encoding; otherwise it's system
     *  dependent. */
    private static final String CHARSET_NAME = "UTF-8";

    /** Assume language = English, country = US for consistency with
     *  In. */
    private static final Locale LOCALE = Locale.US;

    /** The output stream. */
    private PrintWriter out;

   /** Create an Out object using an OS. */
    public Out(OutputStream os) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /** Create an Out object using standard output. */
    public Out() {
        this(System.out);
    }

   /** Create an Out object using SOCKET. */
    public Out(Socket socket) {
        try {
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /** Create an Out object using a file specified by S. */
    public Out(String s) {
        try {
            OutputStream os = new FileOutputStream(s);
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            out = new PrintWriter(osw, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /** Close the output stream. */
    public void close() {
        out.close();
    }

   /** Terminate the line. */
    public void println() {
        out.println();
    }

    /** Print X and then terminate the line. */
    public void println(Object x) {
        out.println(x);
    }

   /** Flush the output stream. */
    public void print() {
        out.flush();
    }

    /** Print X and then flush the output stream. */
    public void print(Object x) {
        out.print(x);
        out.flush();
    }

   /**
     * Print a formatted string according to FORMAT and ARGS,
     * and then flush the output stream.
     */
    public void printf(String format, Object... args) {
        out.printf(LOCALE, format, args);
        out.flush();
    }

   /** Print a formatted string in LOCALE according to FORMAT
    *  arguments ARGS, and then flush the output stream. */
    public void printf(Locale locale, String format, Object... args) {
        out.printf(locale, format, args);
        out.flush();
    }


   /** A test client. */
    public static void main(String[] unused) {
        Out out;

        out = new Out();
        out.println("Test 1");
        out.close();

        out = new Out("test.txt");
        out.println("Test 2");
        out.close();
    }

}
