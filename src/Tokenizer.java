import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Tokenizer {

    private Variable input;

    public Tokenizer (Variable input){
        this.input = input;
    }

    public ArrayList<String> tokens(){
        String[] varStr = input.toString().split(" ");
        ArrayList<String> varList = new ArrayList<>();
        for (int i = 0; i < varStr.length; i++){
            varList.add(varStr[i]);
        }
        return varList;
    }
}
