import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scheduler schedule = new Scheduler("file1.txt", "file2.txt");
        try {
            schedule.processSchedule();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (IncorrectFileFormatException e) {
            System.out.println("Incorrect File Format Exception: " + e.getMessage());
        }
        System.out.println("\n > Seminar information successfully processed.");

        int menu = 1;
        Scanner input = new Scanner(System.in);

        while (menu != 0) {
            System.out.println("\n=========================================");
            System.out.println(" > Select a method (0 to exit): ");
            System.out.println("\t(1) Find Overlapping attendees");
            System.out.println("\t(2) Find Conflicts with time slot");
            System.out.print(" >> ");

            try {
                menu = Integer.parseInt(input.next());
                input.nextLine();

                if (menu == 1) {
                    System.out.println(" > Select a seminar: ");
                    schedule.displaySeminarForMenu();
                    int chosen = Integer.parseInt(input.next());
                    input.nextLine();

                    Seminar chosenSeminar = schedule.getAll_seminars().get(chosen - 1);
                    List<Seminar> returnList = schedule.findOverlappingAttendees(chosenSeminar);
                    schedule.displaySeminarOverlap(returnList, chosenSeminar);

                } else if (menu == 2) {
                    System.out.println(" > Select a Seminar: ");
                    schedule.displaySeminarForMenu();
                    int chosen = Integer.parseInt(input.next());
                    input.nextLine();

                    Seminar chosenSeminar = schedule.getAll_seminars().get(chosen - 1);
                    List<Seminar> returnList = schedule.findTimeSlotConflict(chosenSeminar);
                    schedule.displaySeminarConflict(returnList,chosenSeminar);

                } else if(menu != 0){
                    System.out.println(" [!] Invalid input");
                }
            } catch (NumberFormatException e) {
                System.out.println("Format Exception: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Input Exception: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Illegal State Exception: " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Index Out of Bounds Exception: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }
}

