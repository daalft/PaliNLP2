package de.unitrier.daalft.pali.tools;

import java.util.List;

/**
 * Builds regular expression patterns from lists
 * @author David
 *
 */
public class Patterner {

	/**
	 * Transforms a list of string into a regular expression string
	 * <p>
	 * Every element is separated by a logical OR
	 * @param list list of string
	 * @return regular expression string
	 */
	public static String patternOr (List<?> list) {
		StringBuilder sb = new StringBuilder("(");
		for (Object s : list) {
			sb.append(s).append("|");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * Transforms a list of string into a regular expression string
	 * <p>
	 * Returns a group of elements
	 * @param list list of string
	 * @return regular expression group string
	 */
	public static String patternGroup (List<String> list) {
		StringBuilder sb = new StringBuilder("[");
		for (String s : list) {
			sb.append(s);
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Returns a linear string representation of the specified list
	 * <p>
	 * The string is a simple concatenation of all list elements
	 * @param list list of string
	 * @return linear string
	 */
	public static String linearize (List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String s : list)
			sb.append(s);
		return sb.toString();
	}
}
