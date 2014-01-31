package de.unitrier.daalft.pali.phonology.element;

import java.util.Map;

/**
 * Class to represent sandhi rules (euphony)
 * @author David
 *
 */
public class SandhiRule {
	/**
	 * Rule definition
	 */
	private String pattern, replacement;
	/**
	 * Internal ID
	 */
	private int id;
	/**
	 * Class counter
	 */
	private static int counter;
	/**
	 * Map of references - back-references
	 */
	private Map<String, String> map;
	/**
	 * Constructor with rule
	 * @param rule rule
	 */
	public SandhiRule (String rule) {
		if (!rule.contains(":"))
			throw new IllegalArgumentException("Cannot instatiate SandhiRule with argument " + rule);
		id = counter++;
		String[] s = rule.split(":");
		pattern = s[0];
		replacement = s[1];
	}
	/**
	 * Constructor with pattern and replacement
	 * @param pattern pattern 
	 * @param replacement replacement
	 */
	public SandhiRule (String pattern, String replacement) {
		this.id = counter++;
		this.pattern = pattern;
		this.replacement = replacement;
	}
	/**
	 * Sets this map
	 * @param m map
	 */
	public void setMap(Map<String, String> m) {
		map = m;
	}
	/**
	 * Returns this map
	 * @return map
	 */
	public Map<String, String> getMap () {
		return map;
	}
	/**
	 * Globally applies this rule to the given word
	 * @param word given word
	 * @return word with rule applied
	 */
	public String apply (String word) {
		return word.replaceAll(pattern, replacement);
	}
	/**
	 * Applies this rule to the given word once
	 * @param word given word
	 * @return word with rule applied
	 */
	public String applyFirst (String word) {
		return word.replaceFirst(pattern, replacement);
	}
	/**
	 * Applies this rule to the given words
	 * @param w1 word 1
	 * @param w2 word 2
	 * @return words with rule applied
	 */
	public String apply (String w1, String w2) {
		String[] patternSplit = pattern.split(" ");
		// if pattern does not contain two separate patterns
		if (patternSplit.length < 2) {
			throw new UnsupportedOperationException("Cannot apply SandhiRule " + pattern + "->" + replacement + " to arguments" + w1 + "/" + w2);
		}
		// if pattern is not applicable to word w1 and w2
		if ((!w1.matches(".*"+patternSplit[0])) && (!w2.matches(patternSplit[1]+".*"))) {
			// premature return
			return w1 + " " + w2;
		}
		// if pattern 1 ends with $
		if (patternSplit[0].endsWith("$")) {
			// remove dollar sign
			return (w1 + " " + w2).replaceAll(patternSplit[0].substring(0, patternSplit[0].length()-1) + " " + patternSplit[1], replacement);
		}
		return (w1 + " " + w2).replaceAll(pattern, replacement);
	}
	
	/**
	 * Applies this rule at the specified position and returns
	 * the result split on whitespace
	 * <p>
	 * Position should be determined automatically and 
	 * refer to a valid position within the word where this
	 * rule can be applied. <br/><br/><b>
	 * The method does not check for validity of position</b>
	 * @param pos position
	 * @param word word
	 * @return word with rule applied at position 
	 */
	public String[] applyPosSplit (int pos, String word) {
		// delete non-back-referencing dollar signs
		if (replacement.contains("$ ")) {
			if (replacement.contains("$ $")) 
				replacement = replacement.replace("$ $", " $");
			else
				replacement = replacement.replace("$ ", "");
		}
		// split the word into two at position 
		String a = word.substring(0, pos);
		String b = word.substring(pos);
		// apply rule
		String c = b.replaceFirst(pattern, replacement);
		return (a+c).split(" ");
	}
	/**
	 * Checks whether this rule can be applied to the specified arguments
	 * @param w1 argument 1
	 * @param w2 argument 2
	 * @return true if rule can be applied
	 */
	public boolean isApplicable (String w1, String w2) {
		String[] ps = pattern.split(" ");
		// if pattern does not contain two parts
		if (ps.length < 2)
			// pattern is not applicable to two words
			return false;
		// if argument 1 ends with pattern part 1
		// and argument 2 starts with pattern part 2
		if (w1.matches(".*"+ps[0]) && w2.matches(ps[1]+".*"))
			return true;
		return false;
	}
	/**
	 * Checks whether this rule can be applied to the specified argument
	 * @param word argument
	 * @return true if rule can be applied
	 */
	public boolean isApplicable (String word) {
		return word.matches(pattern+".*");
	}
	/**
	 * Returns this pattern
	 * @return pattern
	 */
	public String getPattern () {
		return pattern;
	}
	/**
	 * Returns this replacement
	 * @return replacement
	 */
	public String getReplacement () {
		return replacement;
	}
	/**
	 * Sets this pattern
	 * @param p pattern
	 */
	public void setPattern (String p) {
		pattern = p;
	}
	/**
	 * Sets this replacement
	 * @param r replacement
	 */
	public void setReplacement (String r) {
		replacement = r;
	}
	/**
	 * Returns this ID
	 * @return ID
	 */
	public int getID () {
		return id;
	}
	/**
	 * Checks whether this rule is atomic
	 * <p>
	 * Atomic rules contains no methods, 
	 * no backreferences and no parentheses
	 * @return true if rule is atomic
	 */
	public boolean isAtomic () {
		// does not contain methods
		return (!pattern.contains("+")) && (!replacement.contains("+")) &&
				// does not contain back-references
				(!pattern.matches(".*\\$\\d.*")) && (!replacement.matches(".*\\$\\d.*")) &&
				// does not contain parentheses
				(!pattern.matches(".*\\(.*?\\).*")) && (!replacement.matches(".*\\(.*?\\).*"));
	}
	
	/**
	 * Returns true if this rule requires the result to be two separate words
	 * @return true if rule results in two separate words
	 */
	public boolean isForceSplit () {
		return (replacement.split(" ")[0].endsWith("$"));
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder("Id:" + id).append(System.lineSeparator());
		sb.append("Pattern: ").append(pattern).append(System.lineSeparator());
		sb.append("Replacement: ").append(replacement).append(System.lineSeparator());
		return sb.toString();
	}

	/**
	 * Returns true if this rule replaces one character by another character
	 * @return true if rule is replacing rule
	 */
	public boolean isReplacingRule() {
		String p1 = "";
		if (pattern.startsWith("^"))
			p1 = pattern.substring(1);
		if (pattern.endsWith("$")) {
			if (p1.isEmpty())
				p1 = pattern.substring(0, pattern.length()-1);
			else
				p1 = p1.substring(0, p1.length()-1);
		}
		if (p1.isEmpty())
			p1 = pattern;
		String p2 = "";
		if (replacement.startsWith("^"))
			p2 = replacement.substring(1);
		if (replacement.endsWith("$")) {
			if (p2.isEmpty())
				p2 = replacement.substring(0, replacement.length()-1);
			else
				p2 = p2.substring(0, p2.length()-1);
		}
		if (p2.isEmpty())
			p2 = replacement;
		int i1 = p1.split(" ").length;
		int i2 = p2.split(" ").length;
		return (i1 == 1 && (i1 == i2));
	}
}
