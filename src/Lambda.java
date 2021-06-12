import java.util.*;
// Mirza Kir and Joseph Tufano
// mods 10-12

public class Lambda {
    // hashmap used to store definitions, and constant for the ASCII values of ( and )
    public static HashMap<String, Expression> dictionary = new HashMap<>();
    public static final char OPEN_PAREN = 40;
    public static final char CLOSE_PAREN = 41;



    public static void main(String[] args) {
        // setting up the prompt
        Scanner in = new Scanner(System.in);
        System.out.print(">");
        String input = in.nextLine().replaceAll("\uFEFF", "");

        while (!input.equals("exit")) {
            //handling comments
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

            // handling definitions
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
                    // this was supposed to replace defined things with the thing that they're stored in. few reasons why it doesn't work
                    // the main reason being that it doesn't check for eta equivalence
                    /*if(dictionary.containsValue(ret)) {
                        System.out.println(getDictKey(ret));
                        continue;
                    } */
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

    //handles "run" statements by recursively finding application that have a function on the left
    private static Expression run(Expression input) {
        String in = input.toString();
        int lambdaPos = in.indexOf('λ');
        //if there is no function in the input, there is nothing to run
        if(lambdaPos < 0) {
            return input;
        }
        Parser p = new Parser(input);
        //this finds the position of the end of the function, and checks if the function is the only thing in the input. if it's solely a function, it returns the function
        int cPP = p.getCloseParenPos(lambdaPos-1, in, new Stack<>());
        if(cPP < 0 || cPP + 1 == in.length()) {
            return input;
        }

        // endApp is the posiiton of the end of the application
        // defining the left expression of the application as the function and the right expression as the expression being passed into the function.
        int endApp = p.getCloseParenPos(lambdaPos-2, in, new Stack<>());
        String expr = in.substring(cPP+1, endApp).trim();
        String function = in.substring(lambdaPos-1, cPP+1);

        // handles alpha-reductions
        // After finding the similar ones, it adds an apostrophe to differentiate them.
        String[] exprVars = expr.replaceAll( "\\(", " ").replaceAll("\\)", " ").split(" ");
        for(String var : exprVars) {
            if(var.equals("")) {
                continue;
            }
            if (function.contains(var)) {
                function = function.replaceAll(var, var + '\'');
            }
        }

        // functionVar is the bound variable being replaced. endScope is the index of the end of the scope of the bound variables in a function
        String functionVar = function.substring(2, function.indexOf('.'));
        String ret = in.substring(0, lambdaPos-1);
        //System.out.printf("input: %s; expr: %s; function: %s; var: %s\n", in, expr, function, functionVar);
        int endScope = function.length()-1;

        // handles variable capture + regular run
        if(function.contains("λ") && function.indexOf("λ" + functionVar,
                function.indexOf("λ" + functionVar) + functionVar.length()+1) >= 0) {
            endScope  = function.indexOf('λ', function.indexOf('.'))-2;
            ret += function.substring(function.indexOf('.')+1, endScope).replaceAll(functionVar, expr);
            ret += function.substring(endScope);
        }
        else
            ret += function.substring(function.indexOf('.')+1, endScope+1).replaceAll(functionVar, expr);
        ret+= in.substring(endApp+1);

        // I realized that each set of non-redundant parentheses always has either a space or a lambda and
        // this checks to see if there are redundant parentheses by using that fact. -Mirza
        if(getCharCount(ret, ' ') + getCharCount(ret, 'λ') != getCharCount(ret, OPEN_PAREN)) {
            ret = p.extraneousParen(new Variable(ret)).toString().trim();
        }
        //checks if it has to run again by checking if there is a function that is the left expression of an application
        if(ret.contains("λ") && ret.charAt(ret.indexOf('λ')-2) == '(')
            ret = run(new Variable(ret)).toString();
        return new Variable(ret);
    }

    // used to find the name of a definition given the value that it's assigned
    private static String getDictKey(Expression expr) {
        for(String key : dictionary.keySet()) {
            if (dictionary.get(key) == expr) {
                return key;
            }
        }
        return null;
    }

    // findss the number of instances of a given character in a given string
    private static int getCharCount(String str, char c) {
        int count = 0;
        for(char c1 : str.toCharArray()) {
            count += c1 == c ? 1 : 0;
        }
        return count;
    }
}
