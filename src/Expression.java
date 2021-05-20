import java.util.ArrayList;

public class Expression {

    public Variable right;
    public Variable left;
    public Expression rightExp;
    public Expression leftExp;

    public Expression(Variable right, Variable left){
        this.right = right;
        this.left = left;

    }

    public Expression(Expression rightExp, Variable left){
        this.rightExp = rightExp;
        this.left = left;
    }

    public String toString(){
        return "(" + right.toString() + " " + left.toString() + ")";
    }

}
