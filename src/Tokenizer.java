import java.util.ArrayList;

public class Tokenizer {

    private Expression input;
    private int pos = 0;

    public Tokenizer (Expression input){
        this.input = input;
    }

    public char getChar(int pos){
        return input.toString().charAt(pos);
    }


    public int getCloseParenPos(int pos2, String in, ArrayList<String> parens){
        if(pos2 >= in.length()) {
            return -1;
        }
        else if (in.charAt(pos2) == ')' && parens.size() == 1) {
            return pos2;
        }
        else if (in.charAt(pos2) == '(') {
            parens.add("(");
            getCloseParenPos(++pos2, in, parens);
        }

        else if (in.charAt(pos2) == ')') {
            parens.remove(parens.size()-1);
            getCloseParenPos(++pos2, in, parens);
        }
        else
            getCloseParenPos(++pos2, in, parens);
        return -1;
    }


    public Function makeFunction(int pos2) {
        // in.indexOf(".") when defining Tokenizer t includes a close paren already. This is the extra one
        String in = input.toString();
        Tokenizer t = new Tokenizer(new Variable(in.substring(in.indexOf(".")+1)));
        Variable v = new Variable(new Lexer(t.tokens()).lexed().toString());

        //92 is backslash
        if (getChar(pos2) == '(') {
            pos += getCloseParenPos(0, in.substring(pos), new ArrayList<String>());
            if(getChar(pos2+1) == 'λ') {
            return new Function(new Variable(in.substring(in.indexOf("λ")+1, in.indexOf(".")).trim()),
                    v);
            }
            else if(getChar(pos2+1) == 92) {
                return new Function(new Variable(in.substring(in.indexOf(92)+1, in.indexOf(".")).trim()),
                        v);
            }
        }
        else if(getChar(pos2) == 'λ' || getChar(pos2) == 92) {
            if (getCloseParenPos(0, in.substring(pos), new ArrayList<String>()) < 0) {
                pos += in.length();
            }
            else {
                pos += getCloseParenPos(0, in.substring(pos), new ArrayList<String>());
            }
            return new Function(new Variable(in.substring(pos2+1, in.indexOf(".")).trim()),
                    v);
        }
        return null;
    }


    public ArrayList<String> tokens(){
        ArrayList<String> varList = new ArrayList<>();
        char in;
        String ret = "";
        int parenpos = 0;
        while (pos < input.toString().length() && pos >= 0){
            in = getChar(pos);
            if (in == '('){
                // 92 is ASCII value for backslash
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

            //92 is backslash
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
