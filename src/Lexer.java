import java.util.ArrayList;

public class Lexer {

    private ArrayList<String> tokens;
    private Expression exp;

    public Lexer (ArrayList<String> tokens){
        this.tokens = tokens;
    }

    public Expression lexed(){
        int pos = 1;
        while (tokens.size() != 1 && pos < tokens.size()){
            if (exp == null){
                exp = new Application(tokens.get(pos-1), tokens.get(pos));
            }
            else{
                exp = new Application(exp, tokens.get(pos));
            }
            pos++;
        }
        return exp;
    }

}
