package demo;

public abstract class Artist{
    protected String name;
    String genre;
    private boolean hasToes = true;
    static boolean hasHat = false;

    public void writeMusic(){
        System.out.println("DOO DOO BEE BOP");
    }

    // method overloading
    public void writeMusic(String song){
        System.out.println("dis my song: " + song);
    }

    public abstract String yell();

    public Artist(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }

    // method overloading
    // u can have multiple methods named the same, but accepting different params.
    public Artist(String name){
        this.name = name;
    }

    public Artist() {
        this.name="new band";
    }
}
