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
            int pos = input.indexOf(';');
            if (pos >= 0) {
                input = input.substring(0, pos).trim();
                if (input.equals("")){
                    System.out.print(">");
                    input = in.nextLine();
                    continue;
                }
            }
            if(input.length() == 1) {
                System.out.println(input);
            }
            else {
                if (input.contains("=")){
                    String varName = input.substring(0, input.indexOf(" "));
                    if (dictionary.get(varName) != null){
                        System.out.print(varName + " is already defined\n>");
                        input = in.nextLine();
                        continue;
                    }
                    Tokenizer t = new Tokenizer(new Variable(input.substring(input.indexOf("=")+1)));
                    Lexer l = new Lexer(t.tokens());
                    Expression e = l.lexed();
                    dictionary.put(varName, e);
                    System.out.println("Added " + e.toString() + " as " + input.substring(0, input.indexOf(" ")));
                }
                else{
                    Tokenizer t = new Tokenizer(new Variable(input));
                    Lexer l = new Lexer(t.tokens());
                    System.out.println(l.lexed());
                }
            }
            System.out.print(">");
            input = in.nextLine();
        }
        System.out.println("Goodbye!");
    }

}
