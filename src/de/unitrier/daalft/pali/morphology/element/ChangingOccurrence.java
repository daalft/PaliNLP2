package de.unitrier.daalft.pali.morphology.element;

import de.unitrier.daalft.pali.general.Alphabet;

/**
 * Changes the word to which it is attached
 * @author David
 *
 */
public class ChangingOccurrence extends Occurrence {

	/**
	 * Constructor
	 * @param in information
	 */
	public ChangingOccurrence (String in) {
		super(in);
	}

	@Override
	public String apply(String word) {
		// determine type of change
		if (occ.matches("m(\\d+)")) {
			int offset = Integer.parseInt(occ.substring(1));
			return word.substring(0, word.length()-offset);
		}
		if (occ.matches("l(\\d+)")) {
			int offset = word.length() - Integer.parseInt(occ.substring(1)) + 1;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < word.length(); i++) {
				if (i == offset) {
					sb.append(Alphabet.getLong(word.charAt(i)+""));
				} else {
					sb.append(word.charAt(i));
				}
			}
			return sb.toString();
		}
		return word;
	}

	@Override
	public ConstructedWord apply(ConstructedWord cw) {
		String w  = cw.getStem();
		String w2 = apply(w);
		cw.setStem(w2);
		return cw;
	}
	
}
