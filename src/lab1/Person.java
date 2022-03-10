package lab1;

public class Person {
    private StringBuilder name;

    public Person(String name) {
        this.name = new StringBuilder(name);
    }

    public StringBuilder getName() {
        return name;
    }
    public void printName() {
        System.out.println(name);
    }
}
