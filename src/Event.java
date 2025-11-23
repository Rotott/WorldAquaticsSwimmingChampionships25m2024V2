public class Event {
    String eventName;
    String entryTime;

    public Event(String eventName, String entryTime) {
        this.eventName = eventName;
        this.entryTime = entryTime;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEntryTime() {
        return entryTime;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", entryTime='" + entryTime + '\'' +
                '}';
    }
}
