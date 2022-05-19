package bakery;

public class BakedGoods {
    boolean isIced;
    boolean isRolled;
    double weight;
    int size;
    String flavor;
    static String baker = "Clari";


    public BakedGoods(boolean isIced, boolean isRolled, double weight, int size, String flavor) {
        this.isIced = isIced;
        this.isRolled = isRolled;
        this.weight = weight;
        this.size = size;
        this.flavor = flavor;
    }

    public String printString(){
        return "I AM STRING";
    }
}
