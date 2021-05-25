public class Application implements Expression{
    public Expression rightExp;
    public Expression leftExp;

    public Application(Expression leftExp, Expression rightExp){
        this.leftExp = leftExp;
        this.rightExp = rightExp;
    }
    public String toString(){
        return "(" + leftExp+ " " + rightExp + ")";
    }

}
