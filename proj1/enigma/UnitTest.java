package enigma;

import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("checkpoint")) {
            System.exit(textui.runClasses(PermutationTest.class,
                    MovingRotorTest.class));
        }
        System.exit(textui.runClasses(PermutationTest.class,
                MovingRotorTest.class,
                MachineTest.class));
    }

}
