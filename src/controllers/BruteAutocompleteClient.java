package controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import models.Term;

public class BruteAutocompleteClient {
    private Scanner input;

    public BruteAutocompleteClient() {
        input = new Scanner(System.in);
    }

    public static void main(String[] args) {
        BruteAutocompleteClient driver = new BruteAutocompleteClient();
        BruteAutocomplete auto = new BruteAutocomplete();

        try {
            auto = new BruteAutocomplete();
            auto.parseDataFile("data/wiktionaryTerms.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(-1);
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            System.out.println("Could not parse content for 'data/wiktionaryTerms.txt', please validate data file.");
            System.exit(-1);
        }

        driver.run(auto);
    }

    /**
     * Main menu method Returns integer with selected option
     * 
     * @return option number of option selected
     */
    private int menu() {
        System.out.println("-------------------------------------");
        System.out.println("Search Terms");
        System.out.println("-------------------------------------");
        System.out.println("  1) Search");
        System.out.println("  2) Restrict Search");
        System.out.println("  3) Best Match");
        System.out.println("  4) Weight of Term");
        System.out.println("  0) Exit");
        System.out.print("> ");

        int option = 0;
        boolean valid = false;
        do {
            try {
                option = input.nextInt();
                valid = true;
            } catch (Exception e) {
                input.nextLine();
                System.out.print("\nInvalid option.\nEnter a number: ");
            }
        } while (!valid);
        return option;
    }

    private void run(BruteAutocomplete auto) {
        int option = menu();

        while (option != 0) {
            switch (option) {
            case 1:
                String s = search();

                List<Term> array = auto.matches(s);
                if (array.size() == 0)
                    System.out.println("No matches found.");
                else
                    System.out.println("\nMatches: ");
                for (Term t : array)
                    System.out.println(t);
                break;
            case 2:
                restrictSearchObject obj = restrictSearch();

                List<Term> matchesList = auto.matches(obj.getString(), obj.getInt());

                if (matchesList.size() == 0)
                    System.out.println("No matches found.");
                else
                    System.out.println("\nMatches: ");
                for (Term r : matchesList)
                    System.out.println(r);
                break;
            case 3:
                String s2 = bestMatch();

                List<Term> best = auto.bestMatch(s2);
                if (best == null)
                    System.out.println("No match found.");
                else
                    System.out.println("Best match is: " + best);
                break;
            case 4:
                String s3 = findWeight();
                List<Term> weight = auto.weightOf(s3);
                if (weight.size() == 0)
                    System.out.println("Term not found.");
                else
                    System.out.println("Weight of " + s3 + " is: " + auto.weightOf(s3));
                break;
            default:
                System.out.println("Invalid option selected.");
                break;
            }

            System.out.println("");
            option = menu();
        }
        System.out.println("Exiting..");
    }

    /**
     * Takes in input to pass through search method
     * 
     * @return s string of prefix to search
     */
    private String search() {
        System.out.println("\nSearch");
        System.out.println("-------------------------------------");

        input.nextLine();
        System.out.print("Substring to search: ");
        String s = input.nextLine();
        return s;
    }

    /**
     * Takes input (String, int) and converts it to a restrictSearchObject
     */
    private restrictSearchObject restrictSearch() {
        System.out.println("\nRestrict search");
        System.out.println("-------------------------------------");

        input.nextLine();

        System.out.print("Substring to search: ");
        String s = input.nextLine();

        System.out.print("Restrict to number: ");
        boolean valid = false;
        int n = 0;
        do {
            try {
                n = input.nextInt();
                valid = true;
            } catch (Exception e) {
                input.nextLine();
                System.out.print("\nInvalid input entered.");
                System.out.print("\nRestrict to number: ");
            }
        } while (!valid);

        return new restrictSearchObject(n, s.toLowerCase());
    }

    /**
     * Instantiate objects to pass through the restricted search method
     */
    public class restrictSearchObject {
        public int n;
        public String s;

        public restrictSearchObject(int n, String s) {
            this.n = n;
            this.s = s;
        }

        public int getInt() {
            return n;
        }

        public String getString() {
            return s;
        }
    }

    /**
     * Takes in input to pass through best match method
     * 
     * @return s2 string of prefix to search
     */
    private String bestMatch() {
        System.out.println("\nBest match");
        System.out.println("-------------------------------------");

        input.nextLine();
        System.out.print("Substring or term: ");
        String s2 = input.nextLine();
        return s2.toLowerCase();
    }

    /**
     * Takes in input to pass through weightOf method
     * 
     * @return s3 string of term to search
     */
    private String findWeight() {
        System.out.println("\nFind weight of term");
        System.out.println("-------------------------------------");

        input.nextLine();
        System.out.print("Term: ");
        String s3 = input.nextLine();
        return s3.toLowerCase();
    }
}