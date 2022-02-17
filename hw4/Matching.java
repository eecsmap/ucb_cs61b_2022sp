import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;


/** A simple test framework for playing around with Patterns.
 *  @author P. N. Hilfinger.
 */
public class Matching {
    /** java Matching reads in pairs of potentially multi-line
     *  strings, with each string terminated by the sequence
     *  "/<newline>".  For each pair, S and P, it interprets P as a
     *  Pattern and reports whether it matches the string S.  If they
     *  match, it additionally prints out the captured groups,
     *  specified in P by parentheses.  Input terminates at the
     *  string "QUIT/<newline>" */
    public static void main(String[] ignored) {
        Scanner inp = new Scanner(System.in);
        String msg = 
            "Alternately type strings to match and patterns to match against%n"
            + "them. Use \\ at the end of line to enter multi-line strings or%n"
            + "patterns (\\s are removed, leaving newlines).  The program%n"
            + "will indicate whether each pattern matches the ENTIRE%n"
            + "preceding string.  Enter QUIT to end the program.%n";
        System.out.printf(msg);

        while (true) {
            System.out.print("String: ");
            String str = getInput(inp);
            if (str == null) {
                break;
            }
            System.out.print("Pattern: ");
            String patn = inp.nextLine();
            if (patn == null) {
                break;
            }
            try {
                Matcher mat = Pattern.compile(patn).matcher(str);
                if (mat.matches()) {
                    System.out.println("Matches.");
                    for (int i = 1; i <= mat.groupCount(); i += 1) {
                        System.out.printf("  Group %d: '%s'%n",
                                          i, mat.group(i));
                    }
                } else {
                    System.out.println("No match.");
                }
            } catch (PatternSyntaxException excp) {
                System.out.println("*** Bad pattern ***");
            }
        }
    }

    /** Return a line of line from INP.  If a line ends in "\",
     *  concatenates the next line as well, preceded by a newline. */
    private static String getInput(Scanner inp) {
        if (!inp.hasNext() || inp.hasNext("QUIT")) {
            return null;
        }
        String result = "";
        while (inp.hasNextLine()) {
            result += inp.nextLine();
            if (!result.endsWith("\\")) {
                break;
            }
            result = result.substring(0, result.length() - 1) + "\n";
        }
        return result;
    }
}
