import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that handeles the data retrival
 */
public class ScraperController {
    private ArrayList<Swimmer> swimmersScraped = new ArrayList<>();

    public ArrayList<String> startScrape() {
        String filePath = "src/Links.txt";
        List<String> links = readLinksFromFile(filePath);
        ArrayList<String> entryList = new ArrayList<>();
        WebDriver driver = new ChromeDriver();
        //Goes through every link in the file and fetches swimmers data (with 100 milliseconds delay between links)
        for (String link : links) {
            entryList.addAll(scrape(link, driver));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        // Close the driver
        driver.quit();
        return entryList;
    }

    private ArrayList<String> scrape(String link, WebDriver driver) {
        ArrayList<String> eventEntry = new ArrayList<>();
        try {
            driver.get(link); // Open the website
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("json-formatter-container")));
            if (element == null) {
                return null;
            }


            WebElement responseElement = driver.findElement(By.tagName("pre"));
            String response = responseElement.getText();


            String[] responseSplit = response.split("\",\"");
            List<String> responseSplited = new ArrayList<>(List.of(responseSplit));


            eventEntry = extractData(responseSplited);

        } catch (Exception e) {
            e.printStackTrace();
        }


        sortEventEntry(eventEntry);
        eventEntry.add("||||||||||||||||||||||||||||||||||||||");
        return eventEntry;
    }

    private ArrayList<String> extractData(List<String> responseSplited) {
        ArrayList<String> eventEntry = new ArrayList<>();


        String eventName = "";

        for (String value : responseSplited) {

            if (value.contains("disciplineName")) {
                System.out.println();
                System.out.println("-----------------------");
                String[] splitEventName = value.split("\":\"");
                System.out.println(splitEventName[1]);
                eventName = splitEventName[1];
                eventEntry.add("\n" + eventName);
                break;
            }
        }

        String entryTime = "";
        String swimmerName = "";
        String birthDate = "";
        String country = "";

        for (int i = 0; i < responseSplited.size(); i++) {
            String currentString = responseSplited.get(i);
            if (currentString.contains("rank\"")) { //Splits for each athlete
                String[] splitEventName = currentString.split("\":\"");
                entryTime = splitEventName[1];

            }
            if (currentString.contains("fullName\"")) { //Splits for each athlete
                String[] splitEventName = currentString.split("\":\"");
                swimmerName = splitEventName[1];

            }
            if (currentString.contains("dateOfBirth\"")) { //Splits for each athlete
                String[] splitEventName = currentString.split("\":\"");
                birthDate = splitEventName[1].split("T")[0];

            }
            if (currentString.contains("participantCountryName\"")) { //Splits for each athlete
                String[] splitEventName = currentString.split("\":\"");
                country = splitEventName[1];


                eventEntry.add(swimmerName + " | " + country + " | " + entryTime);
                doSwimmerLogic(swimmerName, country, birthDate, entryTime, eventName);

                entryTime = "";
                swimmerName = "";
                birthDate = "";
                country = "";
            }


        }
        return eventEntry;
    }

    //TODO old method from OG Paris 2024
    private ArrayList<String> extractData(WebDriver driver, String eventName) {
        ArrayList<String> eventEntry = new ArrayList<>();
        String swimmersTextBox = driver.findElement(By.xpath("//*[@id=\"mirs-table-athletes\"]/tbody")).getText();
        String[] listOfSwimmers = swimmersTextBox.split("\n");
        //Funkar        String timePattern = "\\b\\d{1,2}:\\d{2}\\.\\d{2}\\b|\\b\\d{2}\\.\\d{2}\\b";
        String timePattern = "(\\d{1,2} \\w{3} \\d{4}) (\\d{1,2}:\\d{2}\\.\\d{2}|\\d{1,2}\\.\\d{2})";
        Pattern pattern = Pattern.compile(timePattern);
        for (int i = 0; i < listOfSwimmers.length; i += 4) {
            String country = listOfSwimmers[i];
            String swimmerName = listOfSwimmers[i + 2];
            Matcher matcher2 = pattern.matcher(listOfSwimmers[i + 3]);
            String entryTime = "--:--:--";
            String birthDate = "-- XXXX ----";
            if (matcher2.find()) {
                entryTime = matcher2.group(2);
                birthDate = matcher2.group(1);
            }
            eventEntry.add(swimmerName + " | " + country + " | " + entryTime);
            doSwimmerLogic(swimmerName, country, birthDate, entryTime, eventName);
        }
        return eventEntry;
    }

    private void doSwimmerLogic(String swimmerName, String country, String birthDate, String entryTime, String eventName) {

        boolean swimmerFound = false;
        for (Swimmer swimmer : swimmersScraped) {
            if ((swimmer.getName().equals(swimmerName)) && (swimmer.getCountry().equals(country)) && swimmer.getBirthDate().equals(birthDate)) {
                swimmer.addEvent(new Event(eventName, entryTime));
                swimmerFound = true;
                break;
            }
        }
        if (!swimmerFound) {
            Swimmer newSwimmer = new Swimmer(swimmerName, country, birthDate);
            newSwimmer.addEvent(new Event(eventName, entryTime));
            swimmersScraped.add(newSwimmer);
        }
    }

    private void sortEventEntry(ArrayList<String> eventEntry) {
        // Separate eventName from competitors
        String eventName = eventEntry.remove(0);

        // Sort the ArrayList by time
        eventEntry.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // Extract times from the strings
                String time1 = extractTime(s1);
                String time2 = extractTime(s2);

                double seconds1 = convertToSeconds(time1);
                double seconds2 = convertToSeconds(time2);

                // Compare the times
                return Double.compare(seconds1, seconds2);
            }

            private String extractTime(String s) {
                String[] parts = s.split(" ");
                return parts[parts.length - 1];
            }

            private double convertToSeconds(String time) {
                String[] parts = time.split(":");
                if (parts.length == 2) {
                    int minutes = Integer.parseInt(parts[0]);
                    double seconds = Double.parseDouble(parts[1]);
                    return minutes * 60 + seconds;
                } else {
                    return Double.parseDouble(time);
                }
            }
        });

        int ranking = 1;
        for (int i = 0; i < eventEntry.size(); i++) {
            String swimmer = eventEntry.get(i);
            swimmer = ranking + ". " + swimmer;
            eventEntry.set(i, swimmer);
            ranking++;
        }

        // Add eventName back to the sorted list
        eventEntry.add(0, eventName);

    }

    private List<String> readLinksFromFile(String filePath) {
        List<String> links = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String linkFromFile;
            while ((linkFromFile = bufferedReader.readLine()) != null) {
                links.add(linkFromFile);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.out.println("File:" + filePath + " not found");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading file: " + filePath);
        }
        return links;
    }


    public ArrayList<Swimmer> getSwimmersScraped() {
        return swimmersScraped;
    }
}
