package utils;

import java.util.Comparator;

import models.Term;

public class SortByWeightComparator implements Comparator<Term> {
    
    // Compares two term objects by weight
	@Override
    public int compare(Term t1, Term t2) {
		return t2.compareTo(t1);
    }

}
