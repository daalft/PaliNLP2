package de.unitrier.daalft.pali.tools;

/**
 * Class used to segment a word into phonological units
 * @author David
 *
 */
public class Segmenter {
	/**
	 * Delimiter between phonological units<br/>
	 * DO NOT USE Regexp-interpretable sign
	 */
	private final static String DELIMITER = "#";
	
	/**
	 * No-argument constructor
	 */
	private Segmenter () {
	
	}
	
	/**
	 * Returns the default delimiter
	 * @return default delimiter
	 */
	public static String getDelimiter () {
		return DELIMITER;
	}
	
	/**
	 * Segments the input into phonological units
	 * <p>
	 * Returns a string representation of the segmented input
	 * using the default delimiter
	 * @param input word to segment
	 * @return segmented word
	 */
	public static String segmentToString (String input) {
		// segment to string using default delimiter
		return segmentToString(input, DELIMITER);
	}
	
	/**
	 * Segments the input into phonological units
	 * <p>
	 * Returns a string representation of the segmented input
	 * using the specified delimiter
	 * @param input word to segment
	 * @param delimiter delimiter to use
	 * @return segmented word
	 */
	public static String segmentToString (String input, String delimiter) {
		// if input is less than two characters long, return input
				if (input.length() < 2)
					return input;
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < input.length(); i++) {
					// if character at position i is mute
					if (
							input.charAt(i) == 'k' ||
							input.charAt(i) == 'g' ||
							input.charAt(i) == 'c' ||
							input.charAt(i) == 'j' ||
							input.charAt(i) == 'ṭ' ||
							input.charAt(i) == 'ḍ' ||
							input.charAt(i) == 't' ||
							input.charAt(i) == 'd' ||
							input.charAt(i) == 'p' ||
							input.charAt(i) == 'b'
							) {
						// if following character is 'h' => aspirated consonant
						if ((i+1 < input.length()) && input.charAt(i+1) == 'h') {
							// append aspirated consonant as unit
							sb.append(input.substring(i, i+2)).append(delimiter);
							// jump over the next letter ('h')
							i++;
						} else {
							sb.append(input.substring(i, i+1)).append(delimiter);
						}
					} else {
						sb.append(input.substring(i, i+1)).append(delimiter);
					}
				}
				// delete last delimiter
				sb.deleteCharAt(sb.length()-1);
				return sb.toString();
	}
	
	/**
	 * Segments the input into phonological units
	 * <p>
	 * Returns an array containing the segmented input
	 * @param input word to segment
	 * @return string array
	 */
	public static String[] segmentToArray (String input) {
		// segment to string using default delimiter
		// then split on delimiter and return
		return segmentToString(input, DELIMITER).split(DELIMITER);
	}
}
