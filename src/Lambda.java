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
            }
            else if (semiColonPos >= 0) {
                input = input.substring(0, semiColonPos).trim();
                if (input.equals("")) {
                    System.out.print(">");
                    input = in.nextLine();
                    continue;
                }
            }

            if (input.contains("run ")) {
                Parser p = new Parser(new Variable(input.substring(input.indexOf("run ")+4)));
                String ret = run(p.print(p.tokens())).toString();
                System.out.println(ret);
            }
            else if (input.contains("=")) {
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

    private static Expression run(Expression input) {
        String in = input.toString();
        int lambdaPos = in.indexOf('λ');
        if(lambdaPos < 0) {
            return input;
        }
        Parser p = new Parser(input);
        int cPP = p.getCloseParenPos(0, in.substring(1, in.length()-1), new Stack<>());
        if(cPP < 0) {
            return input;
        }
        if(cPP + 1 + 2 == in.length()) {
            return input;
        }

        String expr = in.substring(cPP+1 + 1, in.length()-1).trim();
        String function = in.substring(1, cPP+1+1);
        String functionVar = function.substring(2, function.indexOf('.'));
        String ret = function.substring(function.indexOf('.')+1, function.length()-1).replaceAll(functionVar, expr);
        if(ret.contains("λ") && ret.charAt(1) != 'λ') {
            ret = run(new Variable(ret)).toString();
        }
        return new Variable(ret);
    }
}
