public class Variable implements Expression{
    private String name;
    private String value;

    public Variable(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public Variable(String name) {
        this(name, name);
    }

    public String toString() {
        if(value.equals(""))
            return "";
        return value;
    }

    public String getName(){
        return this.name;
    }

}
