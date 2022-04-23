package gitlet;

import java.io.File;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // FILL THIS IN
        if (args.length == 0) {
            System.out.println("TODO: put some help message here");
            return;
        }

        String cmd = args[0];
        try {
            switch (cmd) {
                case "init": Gitlet.init(); break;
                default: break;
            }
        } catch (GitletException e) {
            System.err.println(e.getMessage());
        }
    }
}
