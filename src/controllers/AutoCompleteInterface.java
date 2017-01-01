package controllers;
import java.util.List;
import models.Term;

public interface AutoCompleteInterface {

    // Returns the weight of the term, or 0.0 if no such term.
    public List<Term> weightOf(String term);

    // Returns the highest weighted matching term, or null if no matching term.
    public List<Term> bestMatch(String prefix);

    // Returns the highest weighted k matching terms (in descending order of weight), as an
    // iterable.
    // If fewer than k matches, return all matching terms (in descending order
    // of weight).
    public List<Term> matches(String prefix, int k);
}