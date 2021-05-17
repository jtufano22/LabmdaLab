import java.util.Scanner;

public class Prompt{
    private static Scanner in =  new Scanner(System.in);

    public Prompt() {
    }

    public static void main(String[] args) {

        System.out.print(">");
        String input = in.nextLine();
        while(!input.equals("exit")) {
            System.exit(0);
            System.out.println("\n>");
            input = in.nextLine();
        }
    }
}
