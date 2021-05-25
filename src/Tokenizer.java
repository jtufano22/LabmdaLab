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
        if (in.charAt(pos2) == ')' && parens.size() == 0) {
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
        return -1;
    }

    public Function makeFunction(int pos2) {
        String in = input.toString();

        //92 is backslash
        if (getChar(pos2) == '(') {
            pos += getCloseParenPos(0, in.substring(pos), new ArrayList<String>());
            if(getChar(pos2+1) == 'λ') {
            return new Function(new Variable(in.substring(in.indexOf("λ")+1, in.indexOf(".")).trim()),
                    new Variable(in.substring(in.indexOf(".")+1).trim()));
            }
            else if(getChar(pos2+1) == 92) {
                return new Function(new Variable(in.substring(in.indexOf(92)+1, in.indexOf(".")).trim()),
                        new Variable(in.substring(in.indexOf(".")+1).trim()));
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
                    new Variable(in.substring(in.indexOf(".")+1).trim()));
        }
        return null;
    }

    public ArrayList<String> tokens(){
        ArrayList<String> varList = new ArrayList<>();
        char in;
        String ret = "";
        int parenpos = 0;
        while (pos != input.toString().length()){
            in = getChar(pos);
            if (in == '('){
                // 92 is ASCII value for backslash
                if (getChar(pos+1) == 92 || getChar(pos+1) == 'λ') {
                    varList.add(makeFunction(pos).toString());
                    //need to do sum stuff w pos here
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
                        varList.add(ret + input.toString().substring(pos, pos+1));
                        ret = "";
                    }
                    pos++;
                }
            }
        }
        return varList;
    }
}
