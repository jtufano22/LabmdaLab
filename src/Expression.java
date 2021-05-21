import java.util.ArrayList;

public class Expression {

    public String right;
    public String left;
    public Expression rightExp;
    public Expression leftExp;

    //for vars
    public Expression () {
    }

    public Expression(String left, String right){
        this.right = right;
        this.left = left;

    }

    public Expression(Expression leftExp, String right){
        this.leftExp = leftExp;
        this.right = right;
    }

    public String toString(){
        if(leftExp != null) {
            return "(" + leftExp + " " + right + ")";
        }
        return "(" + left + " " + right + ")";
    }

}
