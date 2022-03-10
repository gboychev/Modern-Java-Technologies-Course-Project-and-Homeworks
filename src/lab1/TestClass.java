package lab1;

import java.util.Collection;
import java.util.Set;

public class TestClass {
//    private static void printCollection(Collection<?> c) {
//        for (Object e : c) {
//            System.out.println(e);
//        }
//    }

    public static void main(String[] args) {
        //printCollection(Set.of("aba", "papa", "bar", "rakiq", "raketa", "kola"));
        Person per = new Person("go6o");
        StringBuilder n = per.getName();
        n.append("dwuhdwahjdawwedw");
        per.printName();
    }
}
