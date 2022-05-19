package bakery;

public class Doughnut extends BakedGoods{

    boolean hasHole;

    public Doughnut(boolean isIced, boolean isRolled, double weight, int size, String flavor, boolean hasHole) {
        super(isIced, isRolled, weight, size, flavor);
        this.hasHole = hasHole;
    }
    @Override
    public String toString(){
        String doughnut = "isIced: " + isIced + "\nflavor: " + flavor + "\nsize: "
                + size + "\nweight: " + weight + "\nbaker" + baker;

        return doughnut;
    }
}
