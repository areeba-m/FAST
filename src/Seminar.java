
import java.util.ArrayList;
import java.util.List;

public class Seminar {
    private String name;
    private String speaker;
    private List<String> attendees;
    private Room room;
    private Slot slot;

    public Seminar(String name, String speaker) {
        this.name = name;
        this.speaker = speaker;
        this.attendees = new ArrayList<>();
    }

    public Seminar() {
    }

    public void addAttendee(String attendee){
        attendees.add(attendee);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
