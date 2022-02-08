package Students;

public class BerkeleyStudent extends Student {

    static double avgGPA = 3.89;
    String favClassAtCal = "Frank Ocean Decal";

    public BerkeleyStudent(String name, int age) {
        super(name, age);
    }

    public static void motto() {
        System.out.println("Fiat lux!");
    }

    @Override
    public void study() {
        System.out.println("On my way to Moffitt!");
    }

    public void greet(BerkeleyStudent b) {
        System.out.println("Go bears!");
    }

    public void greet(StanfordStudent s) {
        System.out.println("Uh, hey.");
    }

    public void greet(CS61BStudent s) {
        System.out.println("WOAH, you made the Blocks game??");
    }

}
