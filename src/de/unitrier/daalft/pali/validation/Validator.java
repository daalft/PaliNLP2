package de.unitrier.daalft.pali.validation;

import java.util.List;

import de.unitrier.daalft.pali.general.Alphabet;
import de.unitrier.daalft.pali.tools.Patterner;
import de.unitrier.daalft.pali.tools.Segmenter;

/**
 * Class to validate input
 * @author David
 *
 */
public class Validator {
	
	public Validator () {
		
	}
	
	public Validator(String pathToRules) {
		// TODO read rules from file!
	}
	
	/**
	 * Returns whether the given string is a word
	 * @param word string to check
	 * @return true if string is a word
	 */
	public boolean isWord (String word) {
		char[] ca = word.toCharArray();
		boolean isChar = true;
		for (int i = 0; i < ca.length; i++) {
			isChar = isChar && (Alphabet.contains(ca[i]) || (ca[i] == '-') || (ca[i] == '\''));
		}
		return isChar;
	}
	
	/**
	 * Returns whether the given string is a <b>valid</b> word
	 * according to the rules of the language
	 * @param word word to check
	 * @return true if word is valid according to the rules of the language
	 */
	public boolean isValidWord (String word) {
		String segmentedWord = Segmenter.segmentToString(word);
		boolean valid = isWord(word);
		if (Segmenter.segmentToArray(word).length < 2) {
			if (word.matches(Patterner.patternGroup(Alphabet.getVowels())))
				return true;
			return false;
		}
		// start with single C/V
		valid = valid && (segmentedWord.matches("^"+patternGroup(Alphabet.getVowels()) +Segmenter.getDelimiter()+ patternOr(Alphabet.getConsonants()) + ".*?") || 
				segmentedWord.matches("^"+patternOr(Alphabet.getConsonants()) +Segmenter.getDelimiter()+ patternGroup(Alphabet.getVowels()) + ".*?"));
		if (!valid) return false;
			//throw new Exception("WordStartViolation");
		// TODO ends in V - ONLY FOR LEMMA
		/*valid = valid && segmentedWord.matches(".*?" + patternGroup(Alphabet.getVowels()));
		if (!valid)
			throw new Exception("WordEndViolation");*/
		// not more than 2 C
		valid = valid && !(segmentedWord.matches(".*?" + patternOr(Alphabet.getConsonants())+Segmenter.getDelimiter()+ patternOr(Alphabet.getConsonants()) + Segmenter.getDelimiter() + 
				patternOr(Alphabet.getConsonants()) + ".*?"));
		if (!valid) return false;
			//throw new Exception("ConsonantClusterViolation");
		// no C after aspirated C
		if (segmentedWord.matches(".*?" + patternOr(Alphabet.getAspirated()) + ".*?")) {
			valid = valid && !(segmentedWord.matches(".*?" + patternOr(Alphabet.getAspirated()) +Segmenter.getDelimiter()+ patternOr(Alphabet.getConsonants()) + ".*?"));
			if (!valid) return false;
				//throw new Exception("AspiratedConsonantViolation");
		}
		// long vowel + single C (+ semivowel)
		// TODO review
		if (segmentedWord.matches(".*?" + patternGroup(Alphabet.getLongVowels()) + Segmenter.getDelimiter() + patternOr(Alphabet.getConsonants())+ ".*?")) {
			valid = valid && (segmentedWord.matches(".*?" + patternGroup(Alphabet.getLongVowels()) + Segmenter.getDelimiter()
					+ patternOr(Alphabet.getConsonants()) + Segmenter.getDelimiter() + patternGroup(Alphabet.getVowels()) + ".*?") ||
					segmentedWord.matches(".*?" + patternGroup(Alphabet.getLongVowels()) + Segmenter.getDelimiter() + 
							patternOr(Alphabet.getConsonants()) + Segmenter.getDelimiter() + "(" + patternOr(Alphabet.getSemiVowels()) + ")?.*?")
					);
			if (!valid) return false;
				//throw new Exception("LawOfMoraViolation");
		}
		return valid;
	}
	
	/**
	 * Returns whether the given string is a <b>probable</b> word
	 * according to the rules of the language. Probable words are valid
	 * words that consist of more than one consonant
	 * @param word word to check
	 * @return true if word is probable word according to the rules of the language
	 * @throws Exception
	 */
	public boolean isProbableWord (String word) throws Exception {
		boolean b = isValidWord(word);
		// consonant-only word match
		boolean a = !word.matches(Patterner.patternOr(Alphabet.getConsonants())+"+");
		return a && b;
	}
	
	/**
	 * Returns whether the given string is a valid <b>noun</b>
	 * according to the rules of the language
	 * @param word word to check
	 * @return true if word is valid noun according to the rules of the language
	 * @throws Exception 
	 */
	public boolean isValidNounLemma (String word) throws Exception {
		return isValidWord(word) && !word.endsWith("e");
	}

	/**
	 * Returns whether the given string is a valid <b>verb</b>
	 * according to the rules of the language
	 * @param word word to check
	 * @return true if word is valid verb according to the rules of the language
	 * @throws Exception 
	 */
	public boolean isValidVerb (String word) throws Exception {
		return isValidWord(word);
	}
	
	/**
	 * Transforms a list of string into a regular expression string
	 * <p>
	 * Every element is separated by a logical OR
	 * @param list list of string
	 * @return regular expression string
	 */
	private String patternOr (List<String> list) {
		StringBuilder sb = new StringBuilder("(");
		for (String s : list) {
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
	private String patternGroup (List<String> list) {
		StringBuilder sb = new StringBuilder("[");
		for (String s : list) {
			sb.append(s);
		}
		sb.append("]");
		return sb.toString();
	}
}
