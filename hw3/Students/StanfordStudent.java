package Students;

public class StanfordStudent extends Student {

    public StanfordStudent(String name, int age) {
        super(name, age);
    }

    public static void motto() {
        System.out.println("Die Luft der Freiheit weht.");
    }

    @Override
    public void study() {
        System.out.println("Meh, they're giving out As for free");
    }

    public void greet(StanfordStudent s) {
        System.out.println("Hi, fellow tree.");
    }

    public void greet(BerkeleyStudent b) {
        System.out.println("I hope I can be like you someday!");
    }

}