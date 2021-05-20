public class Lexer {

    private String[] tokens;

    public Lexer (String[] tokens){
        this.tokens = tokens;
    }

    public String lexed(){
        int pos = 1;
        while (pos != tokens.length){
            tokens[pos] = parenCreator(tokens[pos-1], tokens[pos]);
            pos++;
        }
        return tokens[tokens.length - 1];
    }

    public String parenCreator(String first, String second){
        return "(" + first + " " + second + ")";
    }


}
