import java.util.Scanner;

public class Lambda {

    public static String input;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        do{
            System.out.print(">");
            input = in.nextLine();
        } while (!input.equals("exit"));
    }

}
