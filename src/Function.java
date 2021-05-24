public class Function implements Expression{
    private Expression expr;
    private Variable var;

    public Function(Variable var, Expression expr){
        this.expr = expr;
        this.var = var;
    }

    public String toString() {
        return "(λ" + var + "." + expr + ")";
    }
}
