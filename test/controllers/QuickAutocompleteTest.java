package controllers;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Iterables;

import controllers.QuickAutocomplete;
import models.Term;
import utils.SortByStringComparator;
import utils.SortByWeightComparator;


public class QuickAutocompleteTest {
    QuickAutocomplete autocomplete;

    @Before 
    public void setup() throws Exception {
        autocomplete = new QuickAutocomplete();
        autocomplete.parseDataFile("data/wiktionary.txt");
    }

    @After
    public void tearDown() {
        autocomplete = null;
    }

    @Test 
    public void testDataParse() throws Exception {
        List<Term> termList = autocomplete.getList();

        assertNotNull(termList);
        assertEquals(termList.size(), 84);
        assertEquals(termList.get(0).getTerm(), "account");
        assertEquals(termList.get(0).getWeight(), Long.parseLong("17845200"), 0.0);
    }

    @Test (expected = IllegalArgumentException.class) 
    public void testDuplicateTerm() throws Exception {
        QuickAutocomplete fixtures = new QuickAutocomplete();
        fixtures.parseDataFile("data/fixtures.txt");
    }

    @Test (expected = IllegalArgumentException.class) 
    public void testNegativeWeight() throws Exception {
        QuickAutocomplete fixtures = new QuickAutocomplete();
        fixtures.parseDataFile("data/negative.txt");
    }

    @Test
    public void testWeightOf() {
        // Test a valid (existing) term
        String termOne = "sun";
        List<Term> weightOne = autocomplete.weightOf(termOne);

        assertEquals(weightOne.get(0).getWeight(), 1627887200, 0.01);

        // Test a non-existent term
        String termTwo = "Lalala";
        List<Term> weightTwo = autocomplete.weightOf(termTwo);

        assertEquals(weightTwo.get(0).getWeight(), 0l, 0.0);

        // Test another non-existent term
        String termThree = "*\\}'/";
        List<Term> weightThree = autocomplete.weightOf(termThree);

        assertEquals(weightThree.get(0).getWeight(), 0l, 0.0);
    }

    @Test
    public void testBestMatch() {
        // Test a valid (existing) term
        List<Term> best = autocomplete.bestMatch("su");
        assertEquals(best.get(0).getTerm(), "surridge");

        List<Term> bestTwo = autocomplete.bestMatch("a");
        assertEquals(bestTwo.get(0).getTerm(), "as");

        // Test a non-existent term
        List<Term> noBest = autocomplete.bestMatch("Zeplin");
        assertNull(noBest);

        // Test an extremely large string
        List<Term> noBestTwo = autocomplete.bestMatch("1. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolor 2.  magna aliquyam erat, sed diam voluptua. 3.  At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum  4.  dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore 5.  magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren,  6.  no sea takimata sanctus est Lorem ipsum dolor sit amet");
        assertNull(noBestTwo);
    }

    @Test 
    public void testMatches() {
        List<Term> matches = autocomplete.matches("s", 5);
        String [] matchesList = new String[5];
        String [] matchesListTwo = new String [8];

        for(int i = 0; i < 5; i++) {
            matchesList[i] = matches.get(i).getTerm();
        }

        String[] terms = {"surridge", "sunshine", "supper", "sunny", "soup"};
        assertArrayEquals(matchesList, terms);

        // Test large int
        List<Term> matchesTwo = autocomplete.matches("a", 2143648);

        for(int i = 0; i < 8; i++) {
            matchesListTwo[i] = matchesTwo.get(i).getTerm();
        }

        String[] termsTwo = {"as", "at", "all", "are", "an", "any", "account", "age"};
        assertArrayEquals(termsTwo, matchesListTwo);
    }

    @Test
    public void testComparators() {
        List<Term> termList = autocomplete.getList();

        // Test string comparator
        Collections.sort(termList, new SortByStringComparator());
        assertEquals("account", termList.get(0).getTerm());

        // Test weight comparator
        Collections.sort(termList, new SortByWeightComparator());
        assertEquals("surridge", termList.get(0).getTerm());
    }
}
