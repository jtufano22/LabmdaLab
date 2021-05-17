import java.util.Scanner;

public class Lambda {

    public static String input;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print(">");
        input = in.nextLine();
        while (!input.equals("exit")) {
            Variable var = new Variable(input);
            System.out.print(var);
            System.out.print(">");
            input = in.nextLine();
        }
        System.out.println("Goodbye!");
    }

}
