package bakery;

public class Muffins extends BakedGoods{

    boolean hasMuffinTop;

    public Muffins(boolean isIced, boolean isRolled, double weight, int size, String flavor, boolean hasMuffinTop) {
        super(isIced, isRolled, weight, size, flavor);
        this.hasMuffinTop = hasMuffinTop;
    }
}
