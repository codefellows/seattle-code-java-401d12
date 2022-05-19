package bakery;

public class CinnamonRolls extends BakedGoods{

    public boolean hasSwirl;

    public CinnamonRolls(boolean isIced, boolean isRolled, double weight, int size, String flavor, boolean hasSwirl) {
        super(isIced, isRolled, weight, size, flavor);
        this.hasSwirl = hasSwirl;
    }
}
