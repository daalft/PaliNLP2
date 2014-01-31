package de.unitrier.daalft.pali.morphology.paradigm.rule;
/**
 * Derives the stem of a lemma by removing either <em>n</em>
 * characters from the right side of the lemma or by removing
 * the specified ending from the lemma
 * @author David
 *
 */
public class RightDeletingRule implements DerivingRule {
	/**
	 * Ending to remove
	 */
	private String deleteString;
	/**
	 * Number of characters to remove
	 */
	private int deleteNum;
	
	/**
	 * Constructor
	 * @param s ending
	 */
	public RightDeletingRule (String s) {
		deleteString = s;
	}
	
	/**
	 * Constructor
	 * @param n number
	 */
	public RightDeletingRule (int n) {
		deleteNum = n;
	}
	
	@Override
	public String apply(String lemma) {
		if (deleteString == null && deleteNum == 0) {
			System.err.println("Parameters have not been set in RightDeletingRule!");
			return lemma;
		}
		if (deleteNum != 0) {
			return lemma.substring(0, lemma.length()-deleteNum);
		}
		if (deleteString != null) {
			if (lemma.endsWith(deleteString)) {
				int length = deleteString.length();
				return lemma.substring(0, lemma.length()-length);
			} else {
				System.err.println("Could not right-delete " + deleteString + " from " + lemma);
				return lemma;
			}
		}
		System.err.println("Could not apply RightDeletingRule to " + lemma);
		return lemma;
	}
}
