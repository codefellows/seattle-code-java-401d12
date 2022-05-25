package demo;

import java.util.ArrayList;

public class Dog {
    public String breed;
    public int size;
    public boolean hasTail;
    public ArrayList<String> colors;

    public Dog() {
    }

    public Dog(String breed, int size, boolean hasTail, ArrayList<String> colors) {
        this.breed = breed;
        this.size = size;
        this.hasTail = hasTail;
        this.colors = colors;
    }
}
