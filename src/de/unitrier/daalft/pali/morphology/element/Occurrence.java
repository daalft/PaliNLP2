package de.unitrier.daalft.pali.morphology.element;
/**
 * Class representing occurrence objects
 * @author David
 *
 */
public abstract class Occurrence {

	/**
	 * Occurrence information
	 */
	protected String occ;
	
	/**
	 * Constructor
	 */
	public Occurrence() {}
	
	/**
	 * Constructor
	 * @param occ occurrence information
	 */
	public Occurrence (String occ) {
		this.occ = occ;
	}
	
	/**
	 * Returns occurrence information
	 * @return occurrence information
	 */
	public String getOccurrence () {
		return occ;
	}
	
	/**
	 * Sets this occurrence information
	 * @param occ occurrence information
	 */
	public void setOccurrence (String occ) {
		this.occ = occ;
	}
	
	/**
	 * Default toString method
	 */
	public String toString () {
		return occ;
	}
	
	/**
	 * Returns this class' name
	 * @return class name
	 */
	public String getType () {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Applies the current occurrence's properties
	 * to the specified word
	 * <p>
	 * The result is dependent on the type of
	 * occurrence
	 * @param word word to apply occurrence to
	 * @return word with applied occurrence properties
	 */
	public abstract String apply (String word);

	/**
	 * Applies the current occurrence's properties
	 * to the specified constructed word
	 * <p>
	 * The result is dependent on the type of 
	 * occurrence
	 * @param cw word to apply occurrence to
	 * @return word with applied occurrence properties
	 */
	public abstract ConstructedWord apply(ConstructedWord cw);
}
