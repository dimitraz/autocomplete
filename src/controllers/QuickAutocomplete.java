package controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.Term;
import utils.SortByStringComparator;

public class QuickAutocomplete implements AutoCompleteInterface {
    private List<Term> termList = new ArrayList<Term>();
    private BinarySearch binary = new BinarySearch(termList);

    public QuickAutocomplete() {
    }

    /**
     * Reads in a text file containing two parameters, weight and term, and
     * populates a list of _term_ objects sorted by descending weight.
     * @param path path of file to read
     * @throws IOException
     */
    public void parseDataFile(String path) throws IOException, FileNotFoundException, IllegalArgumentException, NullPointerException {
        long id = 0;
        BufferedReader in = null;
        
        try {

            if (path == null)
                throw new NullPointerException();

            in = new BufferedReader(new FileReader(path));
            in.readLine(); // Skip first line

            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {

                // Trim, split on tab and add to array
                String[] tokens = inputLine.trim().split("\t");

                // Create Term objects
                if (tokens.length == 2) {
                    Long weight = Long.parseLong(tokens[0]);
                    String term = tokens[1];

                    // Prevent duplicates
                    for (Term t : this.termList)
                        if (t.getTerm().equals(term))
                            throw new IllegalArgumentException(
                                    "Duplicate terms detected; the data file '" + path + "' could not be parsed.");

                    this.termList.add(new Term(weight, term, id++));
                } else {
                    in.close();
                    throw new IOException("Invalid member length: " + tokens.length);
                }
            }
        } finally {
            if (in != null)
                in.close();
        }

        // Sorts list lexicographically
        Collections.sort(this.termList, new SortByStringComparator());
    }

    /**
     * Returns all highest weighted matching terms 
     * (in descending order of weight), as a list of
     * term objects. 
     * @param prefix substring to search
     * @return
     */
    public List<Term> matches(String prefix) throws NullPointerException {
        if (prefix == null)
            throw new NullPointerException();

        return binary.getSubset(prefix);
    }
    
    /**
     * Returns the highest weighted k matching terms 
     * (in descending order of weight), as an iterable. 
     * If fewer than k matches, return all matching terms 
     * (in descending order of weight).
     * @param prefix substring to search
     * @param k return k matches
     */
    @Override
    public ArrayList<Term> matches(String prefix, int k) throws NullPointerException, IllegalArgumentException {
        ArrayList<Term> returnList = new ArrayList<Term>(); // List of strings to return
        List<Term> matchesList = new ArrayList<Term>();

        if (k < 0)
            throw new IllegalArgumentException("Please enter a positive number.");

        if (prefix == null)
            throw new NullPointerException();

        matchesList = matches(prefix);        
        for (Term term : matchesList)
            if (returnList.size() < k) {
                returnList.add(term);
            }
                
        // Return list
        return returnList;
    }

    /**
     * Finds the weight of a specified term
     * @param term term of which to find associated weight
     */
    @Override
    public List<Term> weightOf(String term) throws NullPointerException {
        ArrayList<Term> returnList = new ArrayList<Term>();
    
        if (term == null)
            throw new NullPointerException();
        
       int index = binary.binarySearch(term);
       if (index > 0) {
            returnList.add(termList.get(index));
       }
       else {
           returnList.add(new Term(0l, "", -1));
       }
       
        return returnList;
    }
    
    /**
     * Return the highest weighted matching term, 
     * given a specified prefix.
     * @param prefix substring to search
     * @throws NullPointerException if prefix is null
     */
    @Override
    public List<Term> bestMatch(String prefix) throws NullPointerException {
        if(prefix == null)
            throw new NullPointerException();
        
        ArrayList<Term> returnList;
        returnList = matches(prefix, 1);

        if(returnList.size() <= 0)
            return null;

        // Return first item
        return returnList;
    }

    /**
     * Returns list of term objects
     * @return termList
     */
    public List<Term> getList() {
        return termList;
    }
}