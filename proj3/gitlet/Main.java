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
                case "init":
                    cmd_init();
                    break;
                case "log":
                    cmd_log();
                    break;
                case "add":
                    cmd_add(args);
                    break;
                case "commit":
                    cmd_commit(args);
                    break;
                case "checkout":
                    cmd_checkout(args);
                    break;
                default: break;
            }
        } catch (GitletException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void cmd_log() {
        Gitlet.load().log();
    }

    private static void cmd_init() {
        Gitlet.init();
    }

    private static void cmd_add(String[] args) {
        if (args.length != 2) {
            throw Utils.error(Gitlet.ERROR_INCORRECT_OPERANDS);
        }
        Gitlet.load().add(args[1]);
    }

    private static void cmd_commit(String[] args) {
        if (args.length < 2) {
            throw Utils.error(Gitlet.ERROR_NO_COMMIT_MESSAGE);
        }
        Gitlet.load().commit(args[1]);
    }

    private static void cmd_checkout(String[] args) {
        String commitId = null;
        String filename = null;
        if (args.length == 3) {
            assert(args[1].compareTo("--") == 0);
            filename = args[2];
        } else if (args.length == 4) {
            commitId = args[1];
            assert(args[2].compareTo("--") == 0);
            filename = args[3];
        } else {
            throw Utils.error(Gitlet.ERROR_INCORRECT_OPERANDS);
        }
        Gitlet.load().checkout(commitId, filename);
    }
}
