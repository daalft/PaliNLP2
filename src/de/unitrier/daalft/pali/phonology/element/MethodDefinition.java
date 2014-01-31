package de.unitrier.daalft.pali.phonology.element;
/**
 * Class representing method definitions
 * @author David
 *
 */
public class MethodDefinition {
	/**
	 * Method name
	 */
	private String name;
	/**
	 * Constructor
	 * @param info information
	 */
	public MethodDefinition (String info) {
		this.name = info.split("\\(")[0];
	}
	/**
	 * Returns this method's name
	 * @return method name
	 */
	public String getName () {
		return name;
	}
	/**
	 * Applies this method to the given string and
	 * returns the result
	 * @param arg string to apply method to
	 * @return string with applied method
	 */
	public String getResult (String arg) {
		return apply(arg);
	}
	/**
	 * Applies this method to the given string and
	 * returns the result
	 * @param word word to apply method to
	 * @return word with applied method
	 */
	private String apply (String word) {
		switch (name) {
		case "short": return shorten(word);
		case "long": return lengthen(word);
		case "strong": return strengthen(word);
		case "asp": return aspirate(word);
		case "unasp": return unaspirate(word);
		case "duplicate": return duplicate(word);
		default: return word;
		}
	}
	/**
	 * Shortens a long vowel
	 * or returns the argument if the argument
	 * is not a long vowel
	 * @param s long vowel
	 * @return short vowel
	 */
	private String shorten (String s) {
		if (s.equals("ā"))
			return "a";
		if (s.equals("ī"))
			return "i";
		if (s.equals("ū"))
			return "u";
		return s;
	}
	/**
	 * Lengthens a short vowel
	 * or returns the argument if the argument
	 * is not a short vowel
	 * @param s short vowel
	 * @return long vowel
	 */
	private String lengthen (String s) {
		if (s.equals("a")) 
			return "ā";
		if (s.equals("i"))
			return "ī";
		if (s.equals("u"))
			return "ū";
		return s;
	}
	/**
	 * Strengthens a vowel by applying guna
	 * or returns the argument if the argument
	 * cannot be gunated
	 * @param s vowel
	 * @return gunated vowel
	 */
	private String strengthen (String s) {
		if (s.equals("a"))
			return "ā";
		if (s.equals("i"))
			return "e";
		if (s.equals("ī"))
			return "e";
		if (s.equals("u"))
			return "o";
		if (s.equals("ū"))
			return "o";
		return s;
	}
	/**
	 * Aspirates a consonant
	 * or returns the argument if the argument
	 * cannot be aspirated
	 * @param s consonant
	 * @return aspirated consonant
	 */
	private String aspirate (String s) {
		if (	s.equals("k") ||
				s.equals("c") ||
				s.equals("ṭ") ||
				s.equals("t") ||
				s.equals("p") ||
				s.equals("g") ||
				s.equals("j") ||
				s.equals("ḍ") ||
				s.equals("d") ||
				s.equals("b")
			)
			return s + "h";
		return s;
	}
	/**
	 * Removes aspiration from a consonant
	 * or returns the argument if the argument
	 * is not aspirated
	 * @param s aspirated consonant
	 * @return unaspirated consonant
	 */
	private String unaspirate (String s) {
		// avoid "unaspirating" the letter h by itself
		if (s.equals("h"))
			return s;
		// else remove 'h' from consonant
		if (s.endsWith("h"))
			return s.substring(0, s.length()-1);
		// else return argument
		return s;
	}
	/**
	 * Duplicates a letter
	 * <p>
	 * Unaspirated consonants are duplicated by themselves<br/>
	 * Aspirated consonants are duplicated by their unaspirated counterpart
	 * @param s letter
	 * @return duplicated letter
	 */
	private String duplicate (String s) {
		return (unaspirate(s) + s);
	}
	/**
	 * Default toString method
	 */
	public String toString () {
		return name;
	}
}
