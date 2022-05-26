package pokeman;

import java.util.ArrayList;

public class Pokemon {

    String name;
    int order;
    int weight;
    ArrayList<AbilityWrapper> abilities;

    class AbilityWrapper{
        Ability ability;
        boolean is_hidden;
        int slot;
    }

    class Ability {
        String name;
        String url;
    }

}
