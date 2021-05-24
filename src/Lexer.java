import java.util.ArrayList;

public class Lexer {

    private ArrayList<String> tokens;
    private Application app;

    public Lexer (ArrayList<String> tokens){
        this.tokens = tokens;
    }

    public Expression lexed(){
        int pos = 1;
        while (tokens.size() != 1 && pos < tokens.size()){
            if (app == null){
                app = new Application(new Variable(tokens.get(pos-1)), new Variable(tokens.get(pos)));
            }
            else{
                app = new Application(app, new Variable(tokens.get(pos)));
            }
            pos++;
        }
        if (tokens.size() == 1){
            return new Variable(tokens.get(0));
        }
        return app;
    }

}
