// import sun.tools.jstat.Token;

import java.util.*;

public class Lambda {

    public static String input;
    public static HashMap<String, Expression> dictionary = new HashMap<>();

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print(">");
        input = in.nextLine().replaceAll("\uFEFF", "");

        while (!input.equals("exit")) {
            int semiColonPos = input.indexOf(';');
            if (input.trim().equals("")) {
                System.out.print(">");
                input = in.nextLine();
                continue;
            } else if (semiColonPos >= 0) {
                input = input.substring(0, semiColonPos).trim();
                if (input.equals("")) {
                    System.out.print(">");
                    input = in.nextLine();
                    continue;
                }
            }

            if (input.contains("=")) {
                String varName = input.substring(0, input.indexOf("=")).trim();
                if (dictionary.get(varName) != null) {
                    System.out.print(varName + " is already defined\n>");
                    input = in.nextLine();
                    continue;
                }
                Parser t = new Parser(new Variable(input.substring(input.indexOf("=") + 1)));
                Expression e = t.print(t.tokens());
                dictionary.put(varName, e);
                System.out.println("Added " + e.toString() + " as " + input.substring(0, input.indexOf("=")).trim());
            }
            else if (dictionary.get(input) != null) {
                System.out.println(dictionary.get(input));
            }
            else {
                Parser t = new Parser(new Variable(input));
                System.out.println(t.print(t.tokens()).toString());
            }
            System.out.print(">");
            input = in.nextLine();
        }

        System.out.println("Goodbye!");
    }

}
