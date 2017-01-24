package edu.gatech.oad.antlab.person;

import java.util.*;

/**
 *  A simple class for person 2
 *  returns their name and a
 *  modified string
 *
 * @author Bob, Teju
 * @version 1.2
 */
public class Person2 {
    /** Holds the persons real name */
    private String name;
	 /**
	 * The constructor, takes in the persons
	 * name
	 * @param pname the person's real name
	 */
	 public Person2(String pname) {
	   name = pname;
	 }
	/**
	 * This method should take the string
	 * input and return its characters in
	 * random order.
	 * given "gtg123b" it should return
	 * something like "g3tb1g2".
	 *
	 * @param input the string to be modified
	 * @return the modified string
	 */
	private String calc(String input) {
        // Person 2 put your implementation here

        // Add all the characters to a list
        List<Character> chars = new ArrayList();
        for (int i = 0; i < input.length(); i++) {
            chars.add(input.charAt(i));
        }

        StringBuilder sb = new StringBuilder(input.length());
        Random rand = new Random();

        // Pull out random characters and place them in the string builder
        while (chars.size() > 0) {
            int index = rand.nextInt(chars.size());
            char c = chars.remove(index);
            sb.append(c);
        }

        return sb.toString();
	}
	/**
	 * Return a string rep of this object
	 * that varies with an input string
	 *
	 * @param input the varying string
	 * @return the string representing the
	 *         object
	 */
	public String toString(String input) {
        return name + calc(input);
	}
}
