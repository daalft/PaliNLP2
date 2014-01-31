package de.unitrier.daalft.pali.phonology.tools;

import java.util.List;
import java.util.ArrayList;

/**
 * Utility class that expands expandable strings into separate combinations.
 * Expandable strings have the form (a|b). Given the following expandable strings:
 * (a|b)(c|d|e), the method will return a c,a d,a e,b c,b d,b e 
 * @author David
 *
 */
public class Expander {
	/**
	 * Combinations
	 */
	private static List<String> combo;
	
	/**
	 * Expands all expandable strings into 
	 * separate combinations
	 * @param strings list of strings
	 * @return list of strings
	 */
	public static List<String> expand (String... strings) {
		// reset combination list
		combo = new ArrayList<String>();
		// generate all combinations
		expandRecursion("",0,strings);
		return combo;
	}
	
	/**
	 * Recursively builds all combinations 
	 * @param res current result
	 * @param p pointer
	 * @param s list of strings
	 */
	private static void expandRecursion (String res, int p, String... s) {
		if (p >= s.length) {
			combo.add(res.substring(1));
			return;
		}
		String[] sp = splitToArray(s[p]);
		for (String e : sp) {
			expandRecursion(res+" "+e, p+1, s);
		}
	}
	
	/**
	 * Returns an array of elements. If s is a group
	 * of the form (a|b), the method returns an array containing
	 * a and b. If s is not a group, the method
	 * returns an array containing s
	 * @param s string
	 * @return array
	 */
	private static String[] splitToArray (String s) {
		if (s.startsWith("("))
			return trimSplit(s);
		String[] b = {s};
		return b;
	}
	
	/**
	 * Removes the first and last character from string,
	 * then splits string on pipe character
	 * @param a string to trim and split
	 * @return string array
	 */
	private static String[] trimSplit (String a) {
		return a.substring(1, a.length()-1).split("\\|");
	}
}
