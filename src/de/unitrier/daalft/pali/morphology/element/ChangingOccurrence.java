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
		// deleting
		if (occ.matches("m(\\d+)")) {
			int offset = Integer.parseInt(occ.substring(1));
			return word.substring(0, word.length()-offset);
		}
		// lengthening of vowel
		if (occ.matches("l(\\d+)[aiu]")) {
			int offset = word.length() - Integer.parseInt(occ.substring(1,occ.length()-1));
			if (!(word.charAt(offset)+"").matches("[aiu]")) {
				return word;
			}
			char toLengthen = word.charAt(offset);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < word.length(); i++) {
				if (i == offset && word.charAt(i) == toLengthen) {
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
