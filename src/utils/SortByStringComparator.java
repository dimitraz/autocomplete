package utils;

import java.util.Comparator;

import models.Term;

public class SortByStringComparator implements Comparator<Term> {

    // Compares two term objects by string
	public int compare(Term t1, Term t2) {
		return t1.getTerm().compareToIgnoreCase(t2.getTerm());
	}
	
}
