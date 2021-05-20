import sun.tools.jstat.Token;

import java.util.Arrays;
import java.util.Scanner;

public class Lambda {

    public static String input;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print(">");
        input = in.nextLine();

        while (!input.equals("exit")) {
            int pos = input.indexOf(';');
            if (pos >= 0) {
                input = input.substring(0, pos).trim();
            }
            Variable var = new Variable(input);
            Tokenizer t = new Tokenizer(var);
            Lexer l = new Lexer(t.tokens());
            System.out.println(l.lexed());
            System.out.println();
            System.out.print(">");
            input = in.nextLine();
        }
        System.out.println("Goodbye!");
    }

}
