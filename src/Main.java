import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * An adapted version of the Paris OG 2024 entry list maker for the World Champs 2024 in Budapest
 * @author Anton Jansson
 */

public class Main {
    public static void main(String[] args) {

        ScraperController sc = new ScraperController();

        ArrayList<String> entryList = sc.startScrape();

        writeToFile(entryList);
        printSwimmers(sc, 8);

    }

    private static void writeToFile(ArrayList<String> entryList) {
        try {
            String filePath = "src/entryList.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            for (String swimmer : entryList) {
                System.out.println(swimmer);
                bw.write(swimmer + "\n");
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printSwimmers(ScraperController sc, int amountOfEnteredEvents) {

        System.out.println("Swimmers entered in more then "+amountOfEnteredEvents+":");

       for (Swimmer swimmer : sc.getSwimmersScraped()) {
            if ((swimmer.getEvents()).size() > amountOfEnteredEvents) {
                System.out.println(swimmer);
            }
        }
    }
}