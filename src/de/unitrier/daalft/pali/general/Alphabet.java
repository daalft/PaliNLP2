package de.unitrier.daalft.pali.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class to manage the alphabet and provide functionality related to the alphabet
 * @author David
 *
 */
public class Alphabet {

	/**
	 * Character sets
	 */
	private final static List<String> 
	shortVowels = 	Arrays.asList("a", "i", "u", "e", "o"),
	longVowels = 	Arrays.asList("ā", "ī", "ū"),
	semiVowels = 	Arrays.asList("y", "r", "l", "ḷ", "v"),
	nasals = Arrays.asList("ṅ", "ñ", "ṇ", "n", "m", "ṃ"),
	sibilants = Arrays.asList("h", "s"),
	mutes = Arrays.asList("k", "kh", "g", "gh", "c", "ch", "j", "jh", "ṭ", "ṭh", "ḍ", "ḍh", "t", "th", "d", "dh", "p", "ph", "b", "bh"),
	dentals = Arrays.asList("t", "th", "d", "dh", "n", "l", "s"),
	palatals = Arrays.asList("c", "ch", "j", "jh", "ñ", "y"),
	gutturals = Arrays.asList("k", "kh", "g", "gh", "ṅ");
	
	private Alphabet () {}

	/**
	 * Returns all vowels
	 * @return all vowels
	 */
	public static List<String> getVowels () {
		List<String> temp = new ArrayList<String>();
		temp.addAll(shortVowels);
		temp.addAll(longVowels);
		return temp;
	}

	/**
	 * Returns all <em>short</em> vowels
	 * @return short vowels
	 */
	public static List<String> getShortVowels () {
		return shortVowels;
	}

	/**
	 * Returns all <em>long</em> vowels
	 * @return long vowels
	 */
	public static List<String> getLongVowels () {
		return longVowels;
	}

	/**
	 * Returns all <em>semi</em>vowels
	 * @return semi vowels
	 */
	public static List<String> getSemiVowels () {
		return semiVowels;
	}

	/**
	 * Returns all consonants
	 * @return all consonants
	 */
	public static List<String> getConsonants () {
		Set<String> temp = new HashSet<String>();
		temp.addAll(semiVowels);
		temp.addAll(nasals);
		temp.addAll(sibilants);
		temp.addAll(mutes);
		temp.addAll(dentals);
		temp.addAll(palatals);
		return sort(new ArrayList<String>(temp));
	}

	/**
	 * Returns all nasals
	 * @return all nasals
	 */
	public static List<String> getNasals () {
		return nasals;
	}

	/**
	 * Returns all sibilants
	 * @return all sibilants
	 */
	public static List<String> getSibilants () {
		return sibilants;
	}

	/**
	 * Returns all mutes
	 * @return all mutes
	 */
	public static List<String> getMutes () {
		return sort(mutes);
	}

	/**
	 * Returns all dentals
	 * @return all dentals
	 */
	public static List<String> getDentals () {
		return sort(dentals);
	}

	/**
	 * Returns all palatals
	 * @return all palatals
	 */
	public static List<String> getPalatals () {
		return sort(palatals);
	}

	/**
	 * Checks whether the one-character string c is a letter of the alphabet
	 * @param c character to check
	 * @return true if the alphabet contains c
	 */
	public static boolean contains (String c) {
		return getConsonants().contains(c) || getVowels().contains(c) || c.equals("'") || c.equals("-");
	}

	/**
	 * Returns all aspirated consonants
	 * @return aspirated consonants
	 */
	public static List<String> getAspirated () {
		List<String> temp = new ArrayList<String>();
		for (String s : getConsonants()) {
			if (s.matches(".h")) {
				temp.add(s);
			}
		}
		return temp;
	}

	/**
	 * Checks whether the character c is a letter of the alphabet
	 * @param c character to check
	 * @return true if the alphabet contains c
	 */
	public static boolean contains (Character c) {
		String s = Character.toLowerCase(c)+"";
		return contains(s);
	}

	/**
	 * Sorts a list by length of strings
	 * @param list list
	 * @return list
	 */
	private static List<String> sort (List<String> list) {
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				if (a.length() > b.length())
					return -1;
				if (a.length() < b.length())
					return 1;
				return 0;
			}
		});
		return list;
	}

	/**
	 * Checks whether the given character is a vowel
	 * @param s character to check
	 * @return true if character is a vowel
	 */
	public static boolean isVowel (String s) {
		return getVowels().contains(s);
	}

	/**
	 * Checks whether the given character is a consonant
	 * @param s character to check
	 * @return true if character is a consonant
	 */
	public static boolean isConsonant (String s) {
		return getConsonants().contains(s);
	}

	/**
	 * Returns the nasal that a niggahita would become by 
	 * regressive assimilation, given a character <em>c</em>
	 * @param c assimilating character
	 * @return assimilated niggahita
	 */
	public static String getAssimilatedNiggahita (String c) {
		if (c.equals("j") || c.equals("c")|| c.equals("h")|| c.equals("e"))
			return "ñ";
		if (c.equals("k") || c.equals("kh"))
			return "ṅ";
		if (c.equals("d") || c.equals("dh") || c.equals("n"))
			return "n";
		if (c.equals("m") || c.equals("p") || c.equals("s") || c.equals("bh") || c.equals("b"))
			return "m";
		if (c.equals("ṭ") || c.equals("ṭh"))
			return "ṇ";
		if (c.equals("l"))
			return "l";
		if (isVowel(c))
			return "m";
		return "";
	}

	/**
	 * Returns the result of sandhi merging the given character <em>c</em>
	 * with <em>ya</em>
	 * @param c given character
	 * @return result of merge with <em>ya</em>
	 */
	public static String getAssimilatedWithYa (String c) {
		if (isVowel(c))
			return c+"ya";
		if (c.equals("dh"))
			return "jjha";
		if (c.equals("d"))
			return "jja";
		if (c.equals("ṇ") || c.equals("n"))
			return "ñña";
		if (c.equals("v"))
			return "bba";
		if (c.equals("t"))
			return "cca";
		if (c.equals("th"))
			return "ccha";
		if (c.equals("h"))
			return "hya";
		return c + c + "a";
	}
	
	/**
	 * Given a short vowel <em>s</em>, returns
	 * the corresponding long vowel
	 * <p>
	 * Returns the character itself in all other cases
	 * @param s character
	 * @return long vowel
	 */
	public static String getStrong(String s) {
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
	 * Returns the result of weakening a long vowel
	 * by reversing guna
	 * <p>
	 * Returns the given character if no weakening
	 * can be applied
	 * @param s character to weaken
	 * @return weakened character
	 */
	public static List<String> getWeak (String s) {
		List<String> result = new ArrayList<String>();
		if (s.equals("ā")) {
			result.add("a");
		} else if (s.equals("e")) {
			result.add("i");
			result.add("ī");
		} else if (s.equals("o")) {
			result.add("u");
			result.add("ū");
		} else {
			result.add(s);
		}
		return result;
	}
	
	/**
	 * Checks whether the given consonant is aspirated
	 * @param s consonant
	 * @return true if consonant is aspirated
	 */
	public static boolean isAspirated (String s) {
		return (s.length() == 2) && (s.endsWith("h"));
	}
	
	/**
	 * Checks whether the given consonant is guttural
	 * @param s consonant
	 * @return true if consonant is guttural
	 */
	public static boolean isGuttural (String s) {
		return gutturals.contains(s);
	}
	
	/**
	 * Returns the palatal corresponding to the given guttural
	 * @param guttural guttural
	 * @return palatal
	 */
	public static String getPalatalForGuttural (String guttural) {
		return palatals.get(gutturals.indexOf(guttural));
	}
	
	public static String getLong (String s) {
		if (s.equals("a")) return "ā";
		if (s.equals("i")) return "ī";
		if (s.equals("u")) return "ū";
		return s;
	}
}
