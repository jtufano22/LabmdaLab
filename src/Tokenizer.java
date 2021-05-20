import java.util.Arrays;

public class Tokenizer {

    private Variable input;

    public Tokenizer (Variable input){
        this.input = input;
    }

    public String[] tokens(){
        return input.toString().split(" ");
    }

}
