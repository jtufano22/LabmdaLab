import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Tokenizer {

    private Expression input;
    private int pos = 0;

    public Tokenizer (Expression input){
        this.input = input;
    }

    public char getChar(int pos){
        return input.toString().charAt(pos);
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

    public Function makeFunction(int pos2) {
        // in.indexOf(".") when defining Tokenizer t includes a close paren already. This is the extra one
        String in = input.toString();
        String current = in.substring(pos2);
        int cPP = getCloseParenPos(0, in.substring(pos2), new Stack<String>());
        Tokenizer t;

        //92 is backslash
        if (getChar(pos2) == '(') {
            t = new Tokenizer(new Variable(current.substring(current.indexOf(".") + 1, cPP)));
            Variable v = new Variable(new Lexer(t.tokens()).lexed().toString());
            System.out.println(v);

            pos += cPP + 1;
            if (getChar(pos2 + 1) == 'λ') {
                return new Function(new Variable(current.substring(in.indexOf("λ") + 1, in.indexOf(".")).trim()),
                        v);
            } else if (getChar(pos2 + 1) == 92) {
                return new Function(new Variable(current.substring(in.indexOf(92) + 1, in.indexOf(".")).trim()),
                        v);
            }
        }
         if(getChar(pos2) == 'λ' || getChar(pos2) == 92) {
             t = new Tokenizer(new Variable(in.substring(in.indexOf(".") + 1)));
             Variable v = new Variable(new Lexer(t.tokens()).lexed().toString());

             pos += in.length();

             return new Function(new Variable(current.substring(1, in.indexOf(".")).trim()),
                     v);
         }
        return null;
    }

    public Expression extraneousParen(Expression input, int pos2) {
        String in = input.toString();
        int i = in.indexOf('(');
        int splitPos = 0;
        int innerLen = 0;
        int depth = 0;
        while (i < in.length()) {
            while (i != in.length() && getChar(i) != ' ') {
                if (in.charAt(i) == '(') {
                    int nextOpen = in.substring(i+1).indexOf('(');
                    if (nextOpen == -1 || nextOpen > in.substring(i+1).indexOf(')')){
                        splitPos = i+1;
                    }
                    depth++;
                } else if (getChar(i) == ')') {
                    depth--;
                } else {
                    innerLen++;
                }
                i++;
            }
            if (depth <= 1) {
                return extraneousParen(new Variable(in.substring(0, pos2) + in.substring(splitPos, splitPos + innerLen) + in.substring(i)), pos2 + innerLen);
            }
        }
        return new Variable(in);
    }

    public ArrayList<String> tokens(){
        ArrayList<String> varList = new ArrayList<>();
        char in;
        String ret = "";
        int parenpos = 0;
        input = extraneousParen(input, 0);
        while (pos < input.toString().length() && pos >= 0){
            in = getChar(pos);
            if (in == '('){
                if (getChar(pos+1) == 92 || getChar(pos+1) == 'λ') {
                    varList.add(makeFunction(pos).toString());
                    continue;
                }
                ret += "(";
                pos++;
                parenpos++;
                while (parenpos != 0){
                    in = getChar(pos);
                    if (in == '('){
                        ret += "(";
                        parenpos++;
                    }
                    else if (in == ')'){
                        ret += ")";
                        parenpos--;
                    }
                    else{
                        ret += in;

                    }
                    pos++;
                }
                varList.add(ret);
                ret = "";
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
        return varList;
    }
}
