import java.util.*;

public class Tokenizer extends Lambda{

    private Expression input;
    private int pos = 0;
    private static Set<String> keys = dictionary.keySet();

    public Tokenizer (Expression input){
        this.input = input;
    }

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

    public int getCloseParenPos(int pos, String in, Stack<String> parens){
        //System.out.println("Pos: " + pos + "; In: " + in + "; Parens: " + parens);
        if(pos >= in.length()) {
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

    public ArrayList<String> smartSplit(String in, ArrayList<String> tokens, String var) {
        int close;
        char inChar = in.charAt(0);
        //try statement used if the substring goes out of bounds, it was the easiest idea i thought of lol
        try {
            if (inChar == '(') {
                close = getCloseParenPos(0, in, new Stack<>()) + 1;
                tokens.add(in.substring(0, close));
                return smartSplit(in.substring(close), tokens, var);
            } else if (inChar == ' ') {
                var = "";
                return smartSplit(in.substring(1), tokens, var);
            } else {
                var += inChar;
                if (in.length() > 1 && in.charAt(1) == ' '){
                    tokens.add(var);
                    return smartSplit(in.substring(1), tokens, var);
                }
                return smartSplit(in.substring(1), tokens, var);
            }
        } catch (Exception StringIndexOutOfBoundsException) {
            if (!var.equals("")){
                tokens.add(var);
            }
            return tokens;
        }
    }

    public Function makeFunction(int pos2) {
        // in.indexOf(".") when defining Tokenizer t includes a close paren already. This is the extra one
        String in = input.toString();
        String current = in.substring(pos2);
        int cPP = getCloseParenPos(0, current, new Stack<String>());
        Tokenizer t;

        //92 is backslash
        if (getChar(pos2) == '(') {
            t = new Tokenizer(new Variable(current.substring(current.indexOf(".") + 1, cPP)));
            Variable expr = new Variable(new Lexer(t.tokens()).lexed().toString());

            pos += cPP + 1;
            if (getChar(pos2 + 1) == 'λ') {
                return new Function(new Variable(current.substring(current.indexOf("λ") + 1, current.indexOf(".")).trim()),
                        expr);
            } else if (getChar(pos2 + 1) == 92) {
                return new Function(new Variable(current.substring(current.indexOf(92) + 1, current.indexOf(".")).trim()),
                        expr);
            }
        }
         if(getChar(pos2) == 'λ' || getChar(pos2) == 92) {
             t = new Tokenizer(new Variable(current.substring(current.indexOf(".")+1)));
             Variable expr = new Variable(new Lexer(t.tokens()).lexed().toString());
             pos += current.length();

             return new Function(new Variable(current.substring(1, current.indexOf(".")).trim()),
                     expr);
         }
        return null;
    }

    public Expression extraneousParen(Expression input) {
        ArrayList<String> tokens = smartSplit(input.toString(), new ArrayList<>(), "");
        int pos2;
        for (int i = 0; i < tokens.size(); i++) {
            pos2 = 0;
            String token = tokens.get(i);
            while (getCloseParenPos(0, token, new Stack<>()) == token.length() - 1){
                token = tokens.get(i);
                if (token.charAt(pos2 + 1) == '(' && getCloseParenPos(1, token, new Stack<>()) == token.length() - 2){
                    tokens.set(i, token.substring(1, token.length()-1));
                }
                else if (token.charAt(pos2 + 1) == 92){
                    tokens.set(i, token);
                    break;
                }
                else{
                    if (token.substring(token.indexOf("(")+1, token.indexOf(")")).length() == 1){
                        tokens.set(i, String.valueOf(token.charAt(1)));
                    }
                    break;
                }
            }
        }

        String fixedInput = "";
        for(int i = 0; i < tokens.size(); i++){
            if (i+1 == tokens.size()){
                return new Variable(fixedInput + " " + tokens.get(i));
            }
            else if (i == 0){
                fixedInput += tokens.get(i);
            }
            else{
                fixedInput += " " + tokens.get(i);
            }
        }

        return null;
    }

    public ArrayList<String> tokens(){
        ArrayList<String> varList = new ArrayList<>();
        char in;
        String dictVaule;
        String ret = "";
        int parenpos = 0;
        input = new Variable(extraneousParen(input).toString().trim());
        while (pos < input.toString().length() && pos >= 0){
            dictVaule = isVar(input.toString().substring(pos)); // if the dictionary contains the next token, dictValue becomes equal to it
            if (dictVaule != null){
                varList.add(dictionary.get(dictVaule).toString());
                pos += dictVaule.length(); // pos moves past the dictionary key to the next token
                continue;
            }
            in = getChar(pos);
            if (in == '('){
                if (getChar(pos+1) == 92 || getChar(pos+1) == 'λ') {
                    varList.add(makeFunction(pos).toString());
                    continue;
                }
                int close = getCloseParenPos(pos, input.toString(), new Stack<>());
                varList.add(input.toString().substring(pos, close + 1));
                pos = close + 1;
//                ret += "(";
//                pos++;
//                parenpos++;
//                while (parenpos != 0){
//                    in = getChar(pos);
//                    if (in == '('){
//                        ret += "(";
//                        parenpos++;
//                    }
//                    else if (in == ')'){
//                        ret += ")";
//                        parenpos--;
//                    }
//                    else{
//                        ret += in;
//
//                    }
//                    pos++;
//                }
//                varList.add(ret);
//                ret = "";
            }
            else if (in == 92 || in == 'λ') {
                varList.add(makeFunction(pos).toString());
            }
            else{
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
}
