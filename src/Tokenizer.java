import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Tokenizer {

    private Variable input;

    public Tokenizer (Variable input){
        this.input = input;
    }

    public ArrayList<Variable> tokens(){
        String[] varStr = input.toString().split(" ");
        ArrayList<Variable> varList = new ArrayList<>();
        for (int i = 0; i < varStr.length; i++){
            varList.add(new Variable(varStr[i]));
        }
        return varList;
    }
}
