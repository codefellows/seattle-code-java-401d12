package flower;

// Classes are blueprints
public class Flower {

    // This is publically accesible, can be modified directly
    // OUTSIDE THIS CLASS
    public String name;

    // default
    boolean hasPetals;

    private int lifespan;

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public static int chopHeight = 1;

    // CONSTANT THAT WON'T CHANGE!!!
    public static final int CHOP_HEIGHT = 1;
    // INteger.MAX_VALUE -- use all caps nomenclature (UPPER_SNAKE_CASE)

    public Flower(String name) {
        // the difference is in the this.
        // this = scope
        this.name = name;
    }

    public Flower(String name, boolean hasPetals, int lifespan) {
        this.name = name;
        this.hasPetals = hasPetals;
        this.lifespan = lifespan;
    }

    @Override
    public String toString() {
        return "I'm a flower and my name is: " + name;
    }
}
