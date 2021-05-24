import java.util.ArrayList;

public class Tokenizer {

    private Expression input;

    public Tokenizer (Expression input){
        this.input = input;
    }

    public char getChar(int pos){
        return input.toString().charAt(pos);
    }

    public ArrayList<String> tokens(){
        ArrayList<String> varList = new ArrayList<>();
        char in;
        int pos = 0;
        String ret = "";
        int parenpos = 0;
        while (pos != input.toString().length()){
            in = getChar(pos);
            if (in == '('){
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
