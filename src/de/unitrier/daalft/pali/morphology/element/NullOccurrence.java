package de.unitrier.daalft.pali.morphology.element;
/**
 * Null occurrence exhibits predictable behavior in all cases
 * @author David
 *
 */
public class NullOccurrence extends Occurrence {

	/**
	 * Constructor
	 * @param in information
	 */
	public NullOccurrence(String in) {}

	/**
	 * Returns the empty string
	 */
	public String getOccurrence () {
		return "";
	}
	
	/**
	 * Does nothing
	 */
	public void setOccurrence (String occ) {}
	
	/**
	 * Returns this class' name
	 */
	public String toString () {
		return this.getClass().getSimpleName();
	}

	@Override
	public String apply(String word) {
		return word;
	}

	@Override
	public ConstructedWord apply(ConstructedWord cw) {
		return cw;
	}
}
