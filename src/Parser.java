import java.util.*;

public class Parser extends Lambda{

    // input is the input; pos is the current position in the input; keys is the set of keys in the hashmap used to store definitions
    private Expression input;
    private int pos = 0;
    private static Set<String> keys = dictionary.keySet();
    private final int BACKSLASH = 92;


    public Parser (Expression input){
        this.input = input;
    }


    // helper function to find the character of an expression at a given pos
    public char getChar(int pos){
        return input.toString().charAt(pos);
    }


    public String isVar(String in){
        if (in.contains(" ")){
            in = in.substring(0, in.indexOf(" "));
        }
        for(int i = 0; i < keys.size(); i++){
            if (keys.contains(in)){
                return in;
            }
        }
        return null;
    }


    //finds the position of the closed parenthesis that is associated with the first open paren given a string, a starting position, and a stack
    public int getCloseParenPos(int pos, String in, Stack<String> parens){
        //System.out.println("Pos: " + pos + "; In: " + in + "; Parens: " + parens);
        if(pos >= in.length() || pos < 0) {
            return -1;
        }
        else if (in.charAt(pos) == ')' && parens.size() == 1) {
            return pos;
        }
        else if (in.charAt(pos) == '(') {
            parens.push("(");
            return getCloseParenPos(++pos, in, parens);
        }

        else if (in.charAt(pos) == ')') {
            parens.pop();
            return getCloseParenPos(++pos, in, parens);
        }
        else
            return getCloseParenPos(++pos, in, parens);
    }


    // makes a function given a starting position in the input
    public Function makeFunction(int pos2) {
        String in = input.toString();
        String current = in.substring(pos2);
        int cPP = getCloseParenPos(0, current, new Stack<String>());
        Parser p;

        if (getChar(pos2) == '(') {
            p = new Parser(new Variable(current.substring(current.indexOf(".") + 1, cPP)));
            Variable expr = new Variable(p.print(p.tokens()).toString());

            pos += cPP + 1;
            if (getChar(pos2 + 1) == 'λ') {
                return new Function(new Variable(current.substring(current.indexOf("λ") + 1, current.indexOf(".")).trim()),
                        expr);
            } else if (getChar(pos2 + 1) == BACKSLASH) {
                return new Function(new Variable(current.substring(current.indexOf(BACKSLASH) + 1, current.indexOf(".")).trim()),
                        expr);
            }
        }
         if(getChar(pos2) == 'λ' || getChar(pos2) == BACKSLASH) {
             p = new Parser(new Variable(current.substring(current.indexOf(".")+1)));
             Variable expr = new Variable(p.print(p.tokens()).toString());
             pos += current.length();

             return new Function(new Variable(current.substring(1, current.indexOf(".")).trim()),
                     expr);
         }
        return null;
    }


    // removes all extraneous parentheses by using the fact stated in line 131 of Lambda.java
    // and by checking if two pairs of parentheses are right after one another such as in ((a)), but not in ((a b) c)
    // Once it finds a redundancy, it omits the redundancy and moves on to the next open parenthesis in the expression
    public Expression extraneousParen(Expression e) {
        // p is needed for the getCloseParenPos() method; expr is the given expression as a String;
        // parenPos is the position of the current open parenthesis; cPP is the corresponding closed parenthesis
        Parser p = new Parser(new Variable(""));
        String expr = e.toString();
        int parenPos = expr.indexOf(OPEN_PAREN);
        int cPP = p.getCloseParenPos(parenPos, expr, new Stack<>());

        while(parenPos >= 0) {
            String inParen = expr.substring(parenPos, cPP+1); // everything inside the current pair of parentheses
            if(expr.charAt(parenPos+1) == OPEN_PAREN && expr.charAt(cPP-1) == CLOSE_PAREN) { // read line 92 in Parser.java
                expr = expr.substring(0, parenPos+1) + expr.substring(parenPos+2, cPP-1) + expr.substring(cPP);
            }
            else if(!inParen.contains(" ") && !inParen.contains("λ")) { // read line 91 in Parser.java
                try { //in cases such as (((a))), the position of the close parenthesis is also the last position in the string so it will throw an exception
                    expr = expr.substring(0, parenPos) + expr.substring(parenPos + 1, cPP) + expr.substring(cPP + 1);
                }
                catch(StringIndexOutOfBoundsException ex) {
                    expr = expr.substring(0, parenPos) + expr.substring(parenPos + 1, cPP);
                }
            }
            parenPos = expr.indexOf(OPEN_PAREN, parenPos+1);
            cPP = p.getCloseParenPos(parenPos, expr, new Stack<>());
        }
        return new Variable(expr);
    }


    // main parser/lexer
    public ArrayList<String> tokens(){
        // varList is the list of expressions that will (in most cases) be passed into this.print()
        ArrayList<String> varList = new ArrayList<>();
        // in will be the current character; dictValue will only be assigned a non-null value if it is a key in the hashmap for stored values; ret is what will be added to varList
        char in;
        String dictValue;
        String ret = "";
        //cleans up the input before doing any work
        input = new Variable(extraneousParen(input).toString().trim());

        while (pos < input.toString().length() && pos >= 0){
            dictValue = isVar(input.toString().substring(pos)); // if the dictionary contains the next token, dictValue becomes equal to it
            if (dictValue != null){
                varList.add(dictionary.get(dictValue).toString());
                pos += dictValue.length(); // pos moves past the dictionary key to the next token
                continue;
            }

            in = getChar(pos);
            if (in == '('){ // handles applications and functions
                if (getChar(pos+1) == BACKSLASH || getChar(pos+1) == 'λ') {
                    varList.add(makeFunction(pos).toString());
                    continue;
                }
                int close = getCloseParenPos(0, input.toString().substring(pos), new Stack<>());
                Parser t = new Parser(new Variable(input.toString().substring(pos+1, pos+close)));
                ArrayList<String> inParens = t.tokens();
                varList.add(t.print(inParens).toString());
                pos += close + 1;
            }

            else if (in == BACKSLASH || in == 'λ') { // handles functions
                varList.add(makeFunction(pos).toString());
            }

            else{ //ignore spaces. if the character is not followed by a space, add the character to ret; otherwise, add ret + character to varList and reset ret
                if (in == ' '){
                    pos++;
                }

                else{
                    if (pos+1 != input.toString().length() && getChar(pos+1) != ' '){
                        ret += getChar(pos);
                    }

                    else{
                        varList.add(ret + getChar(pos));
                        ret = "";
                    }

                    pos++;
                }
            }
        }
        pos = 0;
        return varList;
    }


    // makes sense of the list given by the tokens() method
    public Expression print(ArrayList<String> tokens){
        int pos = 1;
        Application app = null;
        while (tokens.size() != 1 && pos < tokens.size()){
            if (app == null){
                app = new Application(new Variable(tokens.get(pos-1)), new Variable(tokens.get(pos)));
            }
            else{
                app = new Application(app, new Variable(tokens.get(pos)));
            }
            pos++;
        }
        if (tokens.size() == 1){
            return new Variable(tokens.get(0));
        }
        return app;
    }
}
