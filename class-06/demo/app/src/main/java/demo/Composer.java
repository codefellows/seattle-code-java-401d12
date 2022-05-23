package demo;

public class Composer extends Artist{

    public String agency;


    public Composer(String name, String genre, String agency) {
        super(name, genre);
        this.agency = agency;
    }

    @Override
    public String yell() {
        return "I am a composer!!!!";
    }
}