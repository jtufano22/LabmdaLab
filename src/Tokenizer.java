import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Tokenizer {

    private Expression input;

    public Tokenizer (Expression input){
        this.input = input;
    }

    public ArrayList<String> tokens(){
        ArrayList<String> varList = new ArrayList<>();
        int pos = 0;
        String paren = "";
        int parenpos = 0;
        while (pos != input.toString().length()){
            if (input.toString().charAt(pos) == '('){
                paren += "(";
                pos++;
                parenpos++;
                while (parenpos != 0){
                    if (input.toString().charAt(pos) == '('){
                        paren += "(";
                        parenpos++;
                    }
                    else if (input.toString().charAt(pos) == ')'){
                        paren += ")";
                        parenpos--;
                    }
                    else{
                        paren += input.toString().charAt(pos);
                    }
                    pos++;
                }
                varList.add(paren);
                paren = "";
            }
            else{
                if (input.toString().charAt(pos) == ' '){
                    pos++;
                }
                else{
                    varList.add(input.toString().substring(pos, pos+1));
                    pos++;
                }
            }
        }
        return varList;
    }
}
