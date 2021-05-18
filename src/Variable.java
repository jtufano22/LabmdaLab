public class Variable {
    private String name;
    private String value;

    public Variable(String name, String value) {
        this(name);
        this.value = value;
    }

    public Variable(String name) {
        int pos = name.indexOf(';');
        if (pos >= 0) {
            String subName = name.substring(0, pos);
            name = subName.trim();
        }
        this.name = name;
        this.value = name;
    }

    public String toString() {
        if(value.equals(""))
            return "";
        return value + "\n";
    }
}
