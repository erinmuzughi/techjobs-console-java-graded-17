import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
                Collections.sort(values); //sorts the list in ascending order alphabetically
            }
        }
        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> copyOfAllJobs = new ArrayList<>(allJobs); //this creates a new ArrayList which contains the data from allJobs but any modifications to it will not affect allJobs

        return copyOfAllJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue.toUpperCase().contains(value.toUpperCase())) { //compares the search term and values in the hashmap in uppercase without changing the data to ensure case insensitivity
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        // TODO - implement this method - which is to enable a search that looks for the search term in all columns;
        //  go through each hashmap and check against every value for the search term; will need to build some sort of
        //local list, another ArrayList of HashMaps to keep track of the results that contain the search term; need
        // to make sure I don't get any duplicates in my results;
        ArrayList<HashMap<String, String>> jobsByValue = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) { //iterates through the ArrayList
            for (String rowValue : row.values()) { //iterates through the HashMap

                if (rowValue.toUpperCase().contains(value.toUpperCase())) { //checks to see if the rowValue contains the searchTerm (value) by comparing them both as uppercase to ensure case insensitivity without changing the case itself
                        jobsByValue.add(row); //if it does match, the HashMap is added to our new ArrayList
                    break; //breaks out of this inner loop so we continue to iterate through the ArrayList and we don't get any duplicates
                    }
                }
            }
        return jobsByValue; //returns the new ArrayList based on the search term the user entered and is called in the TechJobs class, and results are printed via the printJobs method in that class
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records - meaning this pulls out the column information and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format, this reformats the records into a hashmap which has string keys and string values
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob); //this allows you to add new jobs to the ArrayList
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
