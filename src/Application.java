public class Application extends Expression{
    public Expression rightExp;
    public Expression leftExp;

    public Application(String left, String right){
        this(new Expression(left), new Expression(right));
    }

    public Application(Expression left, String right){
        this(left, new Expression(right));
    }

    public Application(String left, Expression right){
        this(new Expression(left),right);
    }

    public Application(Expression leftExp, Expression rightExp){
        this.leftExp = leftExp;
        this.rightExp = rightExp;
    }

    public String toString(){
        return "(" + leftExp+ " " + rightExp + ")";
    }

}
