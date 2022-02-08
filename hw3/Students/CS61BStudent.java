package Students;

public class CS61BStudent extends BerkeleyStudent {

    static double avgGPA = 4.0;
    String favClassAtCal = "CS61B";

    public CS61BStudent(String name, int age) {
        super(name, age);
    }

    public static void motto() {
        System.out.println("Live, love, debug.");
    }

    @Override
    public void study() {
        System.out.println("Remember, Strings use .length() and arrays use .length");
    }

    @Override
    public void greet(CS61BStudent s) {
        System.out.println("Did you go to lecture?");
    }



}