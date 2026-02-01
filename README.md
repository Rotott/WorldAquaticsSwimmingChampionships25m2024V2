# WorldAquaticsSwimmingChampionships25m2024V2

A Java-based tool designed to retrieve swimmer data and automatically generate the entry list for the World Aquatics Swimming Championships (25m) 2024.

This project was developed due to the lack of comprehensive and accessible entry lists online, which are essential for obtaining a clear and detailed overview of the competition. Such lists are particularly valuable for individuals interested in creating well-informed fantasy teams, thereby enhancing the overall experience for both enthusiasts and participants.


---

## Overview
This project fetches and processes swimmer entry data for the **World Aquatics Swimming Championships 2024 (25m pool)**.  
It scrapes event information from provided links, extracts athlete data, organizes entries by event and time, and outputs a clean, formatted **entryList.txt** file.

This tool is an adapted and improved version of the entry list generator originally created for the **Paris 2024 Olympic Games**.

---

## ️ Features
- **Automated web scraping** using Selenium
- Extracts:
    - Athlete name
    - Country
    - Birth date
    - Entry time
    - Event name
- **Sorts each event by entry time** (fastest first)
- **Ranks swimmers automatically**
- Generates a structured textual entry list
- Identifies swimmers entered in **more than X events**
- Handles multiple event pages listed in a `Links.txt` file

---

## Project Structure

### **Main.java**
- Starts the scraping process
- Writes the final entry list to `src/entryList.txt`

### **ScraperController.java**
- Loads Event URLs from `src/Links.txt`
- Uses Selenium WebDriver to open and parse each link
- Extracts and sorts swimmer entries
- Keeps a master list of all swimmers for later evaluation

### **Swimmer.java / Event.java**
- Object models for storing athlete info and events

### **Links.txt**
- Contains all URLs to scrape (one URL per line)

---

## 📦 Requirements

### **Software**
- Java 22+
- Google Chrome
- ChromeDriver (matching your Chrome version)

### **JAR Dependencies (Manual Setup)**
This project **does not use Maven**, so you must manually download and include all required JAR files in your classpath.

You will need to download:

- **selenium-java 4.27.0**
    - Includes Selenium client JAR
    - Includes multiple dependency JARs in the ZIP (these must all be added)
- **WebDriver Manager (optional)** if you prefer automated driver setup


---
## 📝 Output Example (simplified)

**Men’s 100m Freestyle**

1.John Doe | USA | 47.12  
2.Alex Svensson | SWE | 47.80  
3.Rafa Pérez | ESP | 48.21  

**Women’s 200m Butterfly**

1.Anna Li | CHN | 2:06.12  
2.Lara Moon | GBR | 2:07.92  

---

## Author
**Anton Jansson**  
