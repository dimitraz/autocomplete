package models;
/**
 * Returns an immutable Term object that records the term and
 * its corresponding weight.
 * 
 * @author 20072495 - Dimitra Zuccarelli
 *
 */
public class Term implements Comparable<Term> {
	private final Long weight;
	private final String term;
	private long id;
	
	/**
	 * Term object records a term string and its
	 * corresponding weight (long)
	 * @param weight weight of the term
	 * @param term	 the term
	 */
	public Term(Long weight, String term, long id) throws IllegalArgumentException, NullPointerException {
	    
		// Weight should be strictly positive
		if(weight < 0) 
			throw new IllegalArgumentException();
		
		// Argument is null
		if(weight == null || term == null)
			throw new NullPointerException();
		
		this.weight = weight;
		this.term = term.toLowerCase();
		this.id = id;
	}

	
	@Override
	public int compareTo(Term that) {
		if (this.weight < that.weight) return -1;
		if (this.weight > that.weight) return +1;
		return 0;
	}
	
	
	public String toString() {
		return "Term: " + getTerm() + ", " + weight;
	}

	public String getTerm() {
		return term;
	}

	public Long getWeight() {
		return weight;
	}

}
