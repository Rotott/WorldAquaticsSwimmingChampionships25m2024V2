import java.util.ArrayList;

public class Swimmer {
    private final String name;
    private final String country;
    private final String birthDate;
    ArrayList<Event> events=new ArrayList<>();

    public Swimmer(String name, String country, String birthDate) {
        this.name = name;
        this.country = country;
        this.birthDate = birthDate;
    }

    public void addEvent(Event event){
        events.add(event);
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }


    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Swimmer{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", events=" + events +
                '}';
    }


}
