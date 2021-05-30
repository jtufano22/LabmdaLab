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
        int cPP = getCloseParenPos(0, in.substring(pos2), new Stack<String>());
        Tokenizer t;
        if(cPP < 0) {
            t = new Tokenizer(new Variable(in.substring(in.indexOf(".")+1)));
        }
        else
            t = new Tokenizer(new Variable(in.substring(in.indexOf(".")+1, cPP)));
        Variable v = new Variable(new Lexer(t.tokens()).lexed().toString());


        //if (pos2-1 > 0 && pos2 + 4 < in.length() && in.charAt(pos-1) == '(' && in.charAt(pos+ 4) == ')'){
        //    pos+=4;
        //    return new Function(new Variable(in.substring(pos2+1, pos2+4)), v);
        //}

        //92 is backslash
        if (getChar(pos2) == '(') {
            pos += cPP+1;
            if(getChar(pos2+1) == 'λ') {
            return new Function(new Variable(in.substring(in.indexOf("λ")+1, in.indexOf(".")).trim()),
                    v);
            }
            else if(getChar(pos2+1) == 92) {
                return new Function(new Variable(in.substring(in.indexOf(92)+1, in.indexOf(".")).trim()),
                        v);
            }
        }
         if(getChar(pos2) == 'λ' || getChar(pos2) == 92) {

            if (cPP < 0) {
                pos += in.length();
            }
            else {
                pos += cPP + 1;
            }
            return new Function(new Variable(in.substring(pos2+1, in.indexOf(".")).trim()),
                    v);
        }
        return null;
    }

    public Expression extraneousParen(Expression in){
        String inStr = in.toString();
        if (inStr.charAt(0) == '(' && inStr.charAt(inStr.length()-1) == ')'){
            return extraneousParen(new Variable(inStr.substring(1, inStr.length() - 1)));
        }
        else{
            return in;
        }
    }

    public ArrayList<String> tokens(){
        ArrayList<String> varList = new ArrayList<>();
        char in;
        String ret = "";
        int parenpos = 0;
        input = extraneousParen(input);
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
