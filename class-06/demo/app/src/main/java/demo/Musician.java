package demo;

public class Musician extends Artist{
    public int totalSongs;

    public Musician(String name, String genre, int totalSongs) {
        super(name, genre);
        this.totalSongs = totalSongs;
    }

    @Override
    public String yell() {
        return null;
    }
}
