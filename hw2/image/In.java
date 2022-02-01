package image;

/*************************************************************************
 *  Compilation:  javac In.java
 *  Execution:    java In   (basic test --- see source for required files)
 *
 *  Reads in data of various types from standard input, files, and URLs.
 *
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
/* import java.net.HttpURLConnection; */
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.function.Function;

/**
 *  <i>Input</i>. This class provides methods for reading strings
 *  and numbers from standard input, file input, URLs, and sockets.
 *  <p>
 *  The Locale used is: language = English, country = US. This is consistent
 *  with the formatting conventions with Java floating-point literals,
 *  command-line arguments (via {@link Double#parseDouble(String)})
 *  and standard output.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *  <p>
 *  Like {@link Scanner}, reading a token also consumes preceding Java
 *  whitespace, reading a full line consumes
 *  the following end-of-line delimeter, while reading a character consumes
 *  nothing extra.
 *  <p>
 *  Whitespace is defined in {@link Character#isWhitespace(char)}. Newlines
 *  consist of \n, \r, \r\n, and Unicode hex code points 0x2028, 0x2029, 0x0085;
 *  see
 *  <tt><a href="http://www.docjar.com/html/api/java/util/Scanner.java.html">
 *  Scanner.java</a></tt> (NB: Java 6u23 and earlier uses only \r, \r, \r\n).
 *
 *  @author David Pritchard
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Paul Hilfinger (editorial and style changes only).
 */
public final class In {

    /** Input source. */
    private Scanner scanner;

    /*** begin: section (1 of 2) of code duplicated from In to StdIn */

    /** Assume Unicode UTF-8 encoding. */
    private static final String CHARSET_NAME = "UTF-8";

    /** Assume language = English, country = US for consistency
     *  with System.out. */
    private static final Locale LOCALE = Locale.US;

    /** The default token separator; we maintain the invariant that this value
     *  is held by the scanner's delimiter between calls. */
    private static final Pattern WHITESPACE_PATTERN
        = Pattern.compile("\\p{javaWhitespace}+");

    /** Whitespace characters significant. */
    private static final Pattern EMPTY_PATTERN = Pattern.compile("");

    /** Reads the entire input. source:
     *  http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
     */
    private static final Pattern EVERYTHING_PATTERN = Pattern.compile("\\A");

    /*** end: section (1 of 2) of code duplicated from In to StdIn */

    /** Create an input stream from the standard input.  */
    public In() {
        scanner = new Scanner(new BufferedInputStream(System.in), CHARSET_NAME);
        scanner.useLocale(LOCALE);
    }

    /** Create an input stream from SOCKET. */
    public In(java.net.Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            scanner = new Scanner(new BufferedInputStream(is), CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (IOException ioe) {
            System.err.println("Could not open " + socket);
        }
    }

    /** Create an input stream from URL. */
    public In(URL url) {
        try {
            URLConnection site = url.openConnection();
            InputStream is     = site.getInputStream();
            scanner            = new Scanner(new BufferedInputStream(is),
                                             CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (IOException ioe) {
            System.err.println("Could not open " + url);
        }
    }

    /** Create an input stream from FILE. */
    public In(File file) {
        try {
            scanner = new Scanner(file, CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (IOException ioe) {
            System.err.println("Could not open " + file);
        }
    }


    /** Create an input stream from a file or web page named NAME. */
    public In(String name) {
        /* First try to find the file in the local file system.  Next tries
         * to find it in the JAR file.  Finally, tries the Web.
         *
         * In order to set User-Agent, replace the definition of site, below,
         * with the two declarations:
         *    HttpURLConnection site = (HttpURLConnection) url.openConnection();
         *    site.addRequestProperty("User-Agent", "Mozilla/4.76");
         * and uncomment the import of HttpURLConnection. */

        try {
            File file = new File(name);
            if (file.exists()) {
                scanner = new Scanner(file, CHARSET_NAME);
                scanner.useLocale(LOCALE);
                return;
            }

            URL url = getClass().getResource(name);

            if (url == null) {
                url = new URL(name);
            }

            URLConnection site = url.openConnection();

            InputStream is = site.getInputStream();
            scanner        = new Scanner(new BufferedInputStream(is),
                                         CHARSET_NAME);
            scanner.useLocale(LOCALE);
        } catch (IOException ioe) {
            System.err.println("Could not open " + name);
        }
    }

    /** Create an input stream from SOURCE; use with
     *  <tt>new Scanner(String)</tt> to read from a string.
     *  <p> This does not create a defensive copy, so the
     *  scanner will be mutated as you read on.
     */
    public In(Scanner source) {
        scanner = source;
    }

    /** Return true iff the input stream exists. */
    public boolean exists()  {
        return scanner != null;
    }

    /* begin: section (2 of 2) of code duplicated from In to StdIn,
     * with all methods changed from "public" to "public static" ***/

    /**
     * Return true iff the input is empty (except possibly for whitespace)?
     * Use this to know whether the next call to {@link #readString()},
     * {@link #readDouble()}, etc. will succeed.
     */
    public boolean isEmpty() {
        return !scanner.hasNext();
    }

    /**
     * Return true iff the input has a next line? Use this to know whether the
     * next call to {@link #readLine()} will succeed. <p> Functionally
     * equivalent to {@link #hasNextChar()}.
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Return true iff the input is empty (including whitespace). Use this
     * to know whether the next call to {@link #readChar()} will succeed.
     * <p> Functionally equivalent to {@link #hasNextLine()}.
     */
    public boolean hasNextChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        boolean result = scanner.hasNext();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }


    /** Read and return the next line. */
    public String readLine() {
        String line;
        line = scanner.nextLine();
        return line;
    }

    /** Read and return the next character. */
    public char readChar() {
        scanner.useDelimiter(EMPTY_PATTERN);
        String ch = scanner.next();
        assert (ch.length() == 1) : "Internal (Std)In.readChar() error!"
            + " Please contact the authors.";
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return ch.charAt(0);
    }


    /** Read and return the remainder of the input as a string. */
    public String readAll() {
        if (!scanner.hasNextLine()) {
            return "";
        }

        String result = scanner.useDelimiter(EVERYTHING_PATTERN).next();
        scanner.useDelimiter(WHITESPACE_PATTERN);
        return result;
    }


    /** Read and return the next string. */
    public String readString() {
        return scanner.next();
    }

    /** Read and return the next int. */
    public int readInt() {
        return scanner.nextInt();
    }

    /** Read and return the next double. */
    public double readDouble() {
        return scanner.nextDouble();
    }

    /** Read and return the next float. */
    public float readFloat() {
        return scanner.nextFloat();
    }

    /** Read and return the next long. */
    public long readLong() {
        return scanner.nextLong();
    }

    /** Read and return the next short. */
    public short readShort() {
        return scanner.nextShort();
    }

    /** Read and return the next byte. */
    public byte readByte() {
        return scanner.nextByte();
    }

    /** Read and return the next boolean, allowing case-insensitive
     *  "true" or "1" for true, and "false" or "0" for false.
     */
    public boolean readBoolean() {
        String s = readString();
        switch (s.toLowerCase()) {
        case "true": case "1":
            return true;
        case "false": case "0":
            return false;
        default:
            throw new InputMismatchException();
        }
    }

    /** Read all strings until the end of input is reached,
     *  and return them. */
    public String[] readAllStrings() {
        /* We could use readAll.trim().split(), but that's not consistent
         * since trim() uses characters 0x00..0x20 as whitespace. */
        String[] tokens = WHITESPACE_PATTERN.split(readAll());
        if (tokens.length == 0 || tokens[0].length() > 0) {
            return tokens;
        }
        String[] decapitokens = new String[tokens.length - 1];
        for (int i = 0; i < tokens.length - 1; i += 1) {
            decapitokens[i] = tokens[i + 1];
        }
        return decapitokens;
    }

    /** Read all remaining lines from input stream and return them as an
     *  array of strings. */
    public String[] readAllLines() {
        ArrayList<String> lines = new ArrayList<String>();
        while (hasNextLine()) {
            lines.add(readLine());
        }
        return lines.toArray(new String[0]);
    }

    /** Read all ints until the end of input is reached, and return them.  */
    public int[] readAllInts() {
        String[] fields = readAllStrings();
        int[] vals = new int[fields.length];
        for (int i = 0; i < fields.length; i += 1) {
            vals[i] = Integer.parseInt(fields[i]);
        }
        return vals;
    }

    /** Read all doubles until the end of input is reached, and return them. */
    public double[] readAllDoubles() {
        String[] fields = readAllStrings();
        double[] vals = new double[fields.length];
        for (int i = 0; i < fields.length; i += 1) {
            vals[i] = Double.parseDouble(fields[i]);
        }
        return vals;
    }

    /*** end: section (2 of 2) of code duplicated from In to StdIn */

    /** Close the input stream. */
    public void close() {
        scanner.close();
    }

    /** Read all ints from the file named FILENAME and return as an array.
     *  Equivalent to <tt>new In(filename)</tt>.{@link #readAllInts()}
     */
    public static int[] readInts(String filename) {
        return new In(filename).readAllInts();
    }

    /** Read all doubles from the file named FILENAME and return as an array.
     *  Equivalent to <tt>new In(filename)</tt>.{@link #readAllDoubles()}
     */
    public static double[] readDoubles(String filename) {
        return new In(filename).readAllDoubles();
    }

    /** Read all strings from the file named FILENAME and return as an
     *  array.
     *  Equivalent to <tt>new In(filename)</tt>.{@link #readAllStrings()}
     */
    public static String[] readStrings(String filename) {
        return new In(filename).readAllStrings();
    }

    /** Read all ints from stdin and return as an array. */
    public static int[] readInts() {
        return new In().readAllInts();
    }

    /** Read all doubles from stdin and return as an array. */
    public static double[] readDoubles() {
        return new In().readAllDoubles();
    }

    /** Read all strings from stdin and return as an array. */
    public static String[] readStrings() {
        return new In().readAllStrings();
    }

    /** Separator line. */
    private static final String DASHES =
        "---------------------------------------------------------------------"
        + "------";

    /** Print all the lines read from IN by FUNC, using TITLE as a preceding
     *  label. */
    private static void process(String title, In in, Function<In, ?> func) {
        System.out.println(title);
        System.out.println(DASHES);
        try {
            while (!in.isEmpty()) {
                System.out.println((Object) func.apply(in));
            }
        } catch (IllegalArgumentException | NullPointerException
                 | IndexOutOfBoundsException excp) {
            System.out.println(excp);
        }
        System.out.println();
    }

    /** Test client. */
    public static void main(String[] unused) {
        String urlName = "http://introcs.cs.princeton.edu/stdlib/InTest.txt";

        process("readAll() from URL " + urlName, new In(urlName), In::readAll);
        process("readLine() from URL " + urlName, new In(urlName),
                In::readLine);
        process("readString() from URL " + urlName, new In(urlName),
                In::readString);
        process("readLine() from current directory", new In("./InTest.txt"),
                In::readLine);
        process("readLine() from relative path",
                new In("../stdlib/InTest.txt"), In::readLine);
        process("readChar() from file", new In("InTest.txt"), In::readChar);
        process("readLine() from absolute OS X / Linux path",
                new In("/n/fs/introcs/www/java/stdlib/InTest.txt"),
                In::readLine);
        process("readLine() from absolute Windows path",
                new In("G:\\www\\introcs\\stdlib\\InTest.txt"), In::readLine);
    }

}
