// import sun.tools.jstat.Token;

import java.util.*;

public class Lambda {

    public static String input;
    public static HashMap<String, Expression> dictionary = new HashMap<>();

    public static void main(String[] args) {
        dictionary.put("succ", new Variable("(λn.(λf.(λx.(f ((n f) x)))))"));
        dictionary.put("0", new Variable("(λf.(λx.x))"));
        dictionary.put("+", new Variable("(λm.(λn.(λf.(λx.((m f) ((n f) x))))))"));

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

            if (input.contains("=")) {
                String varName = input.substring(0, input.indexOf("=")).trim();
                if (dictionary.get(varName) != null) {
                    System.out.print(varName + " is already defined\n>");
                    input = in.nextLine();
                    continue;
                }
                Parser p;
                Expression e;
                if (input.contains("run "))
                    p = new Parser(new Variable(input.substring(input.indexOf("run ")+4)));
                else
                    p = new Parser(new Variable(input.substring(input.indexOf("=") + 1)));

                e = run(p.print(p.tokens()));
                dictionary.put(varName, e);
                System.out.println("Added " + e.toString() + " as " + input.substring(0, input.indexOf("=")).trim());
            }
            else if (dictionary.get(input) != null) {
                System.out.println(dictionary.get(input));
            }
            else {
                if (input.contains("run ")) {
                    Parser p = new Parser(new Variable(input.substring(input.indexOf("run ")+4)));
                    Expression ret = run(p.print(p.tokens()));
                    if(dictionary.containsValue(ret)) {
                        System.out.println(getDictKey(ret));
                        continue;
                    }
                    System.out.println(ret);
                    System.out.print(">");
                    input = in.nextLine();
                    continue;
                }
                Parser p = new Parser(new Variable(input));
                System.out.println(p.print(p.tokens()).toString());
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

        String[] exprVars = expr.replaceAll( "\\(", " ").replaceAll("\\)", " ").split(" ");
        for(String var : exprVars) {
            if(var.equals("")) {
                continue;
            }
            if (function.contains(var)) {
                function = function.replaceAll(var, var + '\'');
            }
        }
        String functionVar = function.substring(2, function.indexOf('.'));
        String ret;
        //System.out.printf("input: %s; expr: %s; function: %s; var: %s\n", in, expr, function, functionVar);
        int endScope = function.length()-1;

        if(function.contains("λ") && function.indexOf("λ" + functionVar,
                function.indexOf("λ" + functionVar) + functionVar.length()+1) >= 0) {
            endScope  = function.indexOf("(λ", 2);
            ret = function.substring(function.indexOf('.')+1, endScope).replaceAll(functionVar, expr);
            ret += function.substring(endScope, function.length()-1);
        }
        else
            ret = function.substring(function.indexOf('.')+1, endScope).replaceAll(functionVar, expr);

        if(ret.contains("λ") && ret.charAt(1) == '(') {
            ret = run(new Variable(ret)).toString();
        }
        return new Variable(ret);
    }

    private static String getDictKey(Expression expr) {
        for(String key : dictionary.keySet()) {
            if (dictionary.get(key) == expr) {
                return key;
            }
        }
        return null;
    }
}
