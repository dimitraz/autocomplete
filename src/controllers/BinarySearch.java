package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Term;
import utils.SortByWeightComparator;

public class BinarySearch {
    private List<Term> termList = new ArrayList<Term>();

    public BinarySearch(List<Term> termList) {
        this.termList = termList;
    }

    /**
     * Returns subset of matching terms based on highest
     * and lowest indices returned from respective 
     * binary searches
     * @param prefix prefix to search
     * @return returnList sublist of matching terms
     */
    public List<Term> getSubset(String prefix) {
        List<Term> returnList = new ArrayList<Term>();

        // Verify the lower and upper indices exist
        if(binarySearchLowest(prefix) >= 0 && binarySearchHighest(prefix) >= 0) {
            // Return sublist, sorted by weight
            returnList = termList.subList(binarySearchLowest(prefix), binarySearchHighest(prefix) + 1);
            Collections.sort(returnList, new SortByWeightComparator());
        }

        return returnList;
    }

    /**
     * Searches the first occurrence of a given prefix
     * and returns the matching term's index
     * @param prefix substring to search
     * @return result index of the lowest occurrence
     */
    public int binarySearchLowest(String prefix) {
        int low = 0;
        int high = termList.size() - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int condition;
            String term = termList.get(mid).getTerm();

            // Check to see that the term length 
            // is greater than the prefix length
            // if it is, get the substring
            if(term.length() > prefix.length()) 
                condition = prefix.compareTo(term.substring(0, prefix.length()));

            else 
                condition = prefix.compareTo(term);

            if (condition == 0) {
                // If there is a match, keep going down
                // to find the first occurrence
                result = mid;
                high = mid - 1;
            }

            else if (prefix.compareTo(term) < 0)
                high = mid - 1;

            else
                low = mid + 1;
        }

        //  System.out.println("Lowest occurrence is at index: " + result);
        return result;
    }

    /**
     * Searches the last occurrence of a given prefix
     * and returns the matching term's index
     * @param prefix substring to search
     * @return result index of the highest occurrence
     */
    public int binarySearchHighest(String prefix) {
        int low = 0;
        int high = termList.size() - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int condition;
            String term = termList.get(mid).getTerm();

            // Check to see that the term length 
            // is greater than the prefix length
            // if it is, get the substring
            if(term.length() > prefix.length()) 
                condition = prefix.compareTo(term.substring(0, prefix.length()));

            else 
                condition = prefix.compareTo(term);

            if (condition == 0) {
                // If there is a match, keep going up
                // until the last occurrence is found
                result = mid;
                low = mid + 1;
            }

            else if (prefix.compareTo(term) < 0)
                high = mid - 1;

            else
                low = mid + 1;
        }

        // System.out.println("Highest occurrence is at index: " + result);
        return result;
    }

    /**
     * Searches a unique occurrence of a given prefix
     * and returns the matching term's index
     * @param prefix substring to search
     * @return result the index of the occurrence
     */
    public int binarySearch(String prefix) throws NullPointerException {
        if (prefix == null) 
            throw new NullPointerException();

        int low = 0;
        int high = termList.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            String term = termList.get(mid).getTerm();
            if (prefix.equals(term)) {
                return mid;
            }

            if (prefix.compareTo(term) < 0)
                high = mid - 1;

            else
                low = mid + 1;
        }

        // System.out.println("Occurrence is at index: " + result);
        return -1;
    }
}
