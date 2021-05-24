import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Tokenizer {

    private Variable input;

    public Tokenizer (Variable input){
        this.input = input;
    }

    public ArrayList<String> tokens(){
        ArrayList<String> varList = new ArrayList<>();
        int pos = 0;
        String paren = "";
        while (pos != input.toString().length()){
            if (input.toString().charAt(pos) == '('){
                paren += "(";
                while (input.toString().charAt(pos++) != ')'){
                    paren += input.toString().charAt(pos);
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
