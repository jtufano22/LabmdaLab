// import sun.tools.jstat.Token;

import java.util.Arrays;
import java.util.Scanner;

public class Lambda {

    public static String input;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print(">");
        input = in.nextLine().replaceAll("\uFEFF", "");

        while (!input.equals("exit")) {
            int pos = input.indexOf(';');
            if (pos >= 0) {
                input = input.substring(0, pos).trim();
            }
            Variable expr = new Variable(input);
            if(input.length() == 1) {
                System.out.println(input);
            }
            else {
                Tokenizer t = new Tokenizer(expr);
                System.out.println("Tokens: " + t.tokens());
                Lexer l = new Lexer(t.tokens());
                System.out.println(l.lexed());
            }
            System.out.print(">");
            input = in.nextLine();
        }
        System.out.println("Goodbye!");
    }

}
