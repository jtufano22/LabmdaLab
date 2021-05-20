public class Variable {
    private String name;
    private String value;

    public Variable(String name, String value) {
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


}
