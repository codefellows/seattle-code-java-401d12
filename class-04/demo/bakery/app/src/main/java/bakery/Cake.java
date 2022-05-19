package bakery;

public class Cake extends BakedGoods {

    int layers;

    public Cake(boolean isIced, boolean isRolled, double weight, int size, String flavor, int layers) {
        super(isIced, isRolled, weight, size, flavor);
        this.layers = layers;
    }

    @Override
    public String toString() {
        String cake = "I am cake: " + isIced;
        return cake;
    }
}