import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** Autograder tests for the Year class using the Test61B package.
 *  @author Sarah Kim, P. N. Hilfinger
 */
public class AGTestYear {

    @Test
    public void test400() {
        String msg = "should be leap year";
        assertTrue(msg, Year.isLeapYear(2000));
        assertTrue(msg, Year.isLeapYear(2400));
    }

    @Test
    public void testLeapYearNotDivisible100() {
        String msg = "should be leap year";
        assertTrue(msg, Year.isLeapYear(2004));
        assertTrue(msg, Year.isLeapYear(2008));
    }

    @Test
    public void testDivisible400And100() {
        String msg = "should not be leap year";
        assertFalse(msg, Year.isLeapYear(1900));
        assertFalse(msg, Year.isLeapYear(2100));
        assertFalse(msg, Year.isLeapYear(2300));
    }

    @Test
    public void testDivisibleNot4() {
        String msg = "should not be leap year";
        assertFalse(msg, Year.isLeapYear(1901));
        assertFalse(msg, Year.isLeapYear(2003));
    }

    public static void main(String[] args) {
        System.exit(textui.runClasses(AGTestYear.class));
    }

}
