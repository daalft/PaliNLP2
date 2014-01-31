package de.unitrier.daalft.pali.morphology.paradigm.rule;
/**
 * Derives the stem of a lemma by removing either <em>n</em>
 * characters from the left side of the lemma or by removing
 * the specified prefix from the lemma
 * @author David
 *
 */
public class LeftDeletingRule implements DerivingRule {
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
	public LeftDeletingRule (String s) {
		deleteString = s;
	}
	
	/**
	 * Constructor
	 * @param n number
	 */
	public LeftDeletingRule (int n) {
		deleteNum = n;
	}
	
	@Override
	public String apply(String lemma) {
		if (deleteString == null && deleteNum == 0) {
			System.err.println("Parameters have not been set in LeftDeletingRule!");
			return lemma;
		}
		if (deleteNum != 0) {
			return lemma.substring(deleteNum);
		}
		if (deleteString != null) {
			if (lemma.startsWith(deleteString)) {
				int length = deleteString.length();
				return lemma.substring(length);
			} else {
				System.err.println("Could not left-delete " + deleteString + " from " + lemma);
				return lemma;
			}
		}
		System.err.println("Could not apply LeftDeletingRule to " + lemma);
		return lemma;
	}

}
