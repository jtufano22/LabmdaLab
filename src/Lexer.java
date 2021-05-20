import java.util.ArrayList;

public class Lexer {

    private ArrayList<Variable> tokens;
    private Expression exp;

    public Lexer (ArrayList<Variable> tokens){
        this.tokens = tokens;
    }

    public Expression lexed(){
        int pos = 1;
        while (tokens.size() != 1 && pos < tokens.size()){
            if (exp == null){
                exp = new Expression(tokens.get(pos-1), tokens.get(pos));
            }
            else{
                exp = new Expression(exp, tokens.get(pos));
            }
            pos++;
        }
        return exp;
    }

}
