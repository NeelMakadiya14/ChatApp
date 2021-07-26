package ChatApp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("Enter The UserName : ");
        Scanner sc = new Scanner(System.in);

        User user = new User(sc.nextLine(),sc);

        user.run();
    }
}
