import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private String seminarAttendeeFilename;
    private String scheduleFilename;
    private List<Seminar> all_seminars;

    public Scheduler(String seminarAttendeeFilename, String scheduleFilename){
        this.seminarAttendeeFilename = seminarAttendeeFilename;
        this.scheduleFilename = scheduleFilename;
        this.all_seminars = new ArrayList<>();
    }

    public void processSchedule() throws IOException, IncorrectFileFormatException {
        String line1, line2;
        int len, seminarCount = 0;

        BufferedReader reader1 = new BufferedReader(new FileReader(seminarAttendeeFilename));
        BufferedReader reader2 = new BufferedReader(new FileReader(scheduleFilename));

        while(((line1 = reader1.readLine()) != null) && (line2 = reader2.readLine())!= null){
            String[] args1 = line1.split(",");
            String[] args2 = line2.split(",");

            if(!(args1[0].equals(args2[0]))){
                // the names of seminars on the same line need to be equal
                throw new IncorrectFileFormatException("Files formatted incorrectly.\n");
            }

            all_seminars.add(new Seminar(args1[0], args1[1]));
            len = args1.length;
            for(int i=2; i<len; i++){
                all_seminars.get(seminarCount).addAttendee(args1[i]);
            }
            all_seminars.get(seminarCount).setRoom(new Room(args2[1]));
            all_seminars.get(seminarCount).setSlot(new Slot(Integer.parseInt(args2[2])));

            seminarCount++;
        }
        reader1.close();
        reader2.close();

    }

    public List<Seminar> findOverlappingAttendees(Seminar seminar)
            throws IllegalStateException {
        // returns list of seminars with overlapping attendees

        if(all_seminars.isEmpty()){
            throw new IllegalStateException("Exception: Seminar list is empty.");
        }

        List<Seminar> ret_seminars = new ArrayList<>();
        List<String> all_attendees; //contains attendees of each seminar
        List<String> sem_attendees = seminar.getAttendees();

        for(Seminar s: all_seminars){
            if(!(s.getName().equals(seminar.getName()))) {
                all_attendees = s.getAttendees();

                Seminar temp = getOverlappingSeminar(s, sem_attendees, all_attendees);
                // if attendees overlapped, add that seminar to my return list
                if(!(temp.getAttendees().isEmpty())){
                    ret_seminars.add(temp);
                }
            }
        }

        if(ret_seminars.isEmpty())
            ret_seminars = null;

        return ret_seminars;
    }

    private static Seminar getOverlappingSeminar(Seminar s, List<String> sem_attendees, List<String> all_attendees) {
        Seminar temp = new Seminar(s.getName(), s.getSpeaker());
        temp.setSlot(s.getSlot());
        temp.setRoom(s.getRoom());

        for (String sem_attendee: sem_attendees) {
            for(String attendee: all_attendees) {
                if (sem_attendee.equals(attendee)) {
                    temp.addAttendee(attendee); // overlapping attendee added to ret list
                    break;
                }
            }
        }
        return temp;
    }

    public List<Seminar> findTimeSlotConflict(Seminar seminar)
            throws IllegalStateException {
        // returns list of seminars it will conflict with
        // a conflict will occur if seminar slotNo has overlapping attendees

        if(all_seminars.isEmpty()){
            throw new IllegalStateException("Seminar list is empty.");
        }

        // list of seminars that have overlap attendees with given seminar
        List<Seminar> overlapping_seminars = findOverlappingAttendees(seminar);
        List<Seminar> ret_seminars = new ArrayList<>();

        for(Seminar s: overlapping_seminars){
            if(s.getSlot().getSlotNo() == seminar.getSlot().getSlotNo()){
                ret_seminars.add(s); //conflict arose
            }
        }

        boolean found = false;
        for(Seminar s: all_seminars) {
            if(s.getSlot().getSlotNo() == seminar.getSlot().getSlotNo() &&
                    s.getRoom().getRoomNo().equals(seminar.getRoom().getRoomNo()) &&
                    !(s.getName().equals(seminar.getName()))){

                for (Seminar retSeminar : ret_seminars) {
                    //make sure the same seminar hasn't been added already
                    if ((retSeminar.getName().equals(s.getName()))) {
                        found = true;
                        break;
                    }
                }

                if(!found)
                    ret_seminars.add(s);
                else
                    found = false;

            }
        }

        if(ret_seminars.isEmpty())
            ret_seminars = null; // in main i can check if returned list is empty i.e no conflict

        return ret_seminars;
    }

    public void displaySeminarForMenu(){
        for(int i=0; i<all_seminars.size();i++) {
            System.out.println(i+1 + ") " + all_seminars.get(i).getName());
        }
    }

    public void displaySeminarOverlap(List<Seminar> seminarList, Seminar seminar){

        if(seminarList == null){
            System.out.println("\n" + seminar.getName() + " has no overlapping attendees.");
            return;
        }

        System.out.println("\n" + seminar.getName() + " shares attendees with :");
        for(Seminar s: seminarList){
            System.out.print("<" +s.getName()+ ">" + " attendee IDs ");
            for(int i=0; i<s.getAttendees().size(); i++){
                System.out.print("<" + s.getAttendees().get(i)+ "> ");
            }
            System.out.println();
        }
    }

    public void displaySeminarConflict(List<Seminar> seminarList, Seminar seminar){
        if(seminarList == null){
            System.out.println("\n" + seminar.getName() + " has no conflict.");
            return;
        }
        System.out.println("\n" + seminar.getName() + " conflicts with :");
        for(Seminar s: seminarList){
            if(s.getRoom().getRoomNo().equals(seminar.getRoom().getRoomNo())){ //conflict in same room
                System.out.print("<" +s.getName()+ ">");
                System.out.println(" on room number <" + s.getRoom().getRoomNo() + "> and slot number <" + s.getSlot().getSlotNo()+ ">");
            }else{ // conflict with attendees
                System.out.print("<" +s.getName()+ ">");
                System.out.print(" for attendees ");
                for(int i=0; i<s.getAttendees().size(); i++){
                    System.out.print("<" + s.getAttendees().get(i)+ "> ");
                }
                System.out.println("on slot number <" + s.getSlot().getSlotNo()+ ">");
            }
        }
    }
    public List<Seminar> getAll_seminars() {
        return all_seminars;
    }
}
