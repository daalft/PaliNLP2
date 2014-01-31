package de.unitrier.daalft.pali.morphology.element;
/**
 * Represents a concrete morph
 * including occurrence information
 * @author David
 *
 */
public class Morph {

	/**
	 * Morph
	 */
	private String morph;
	/**
	 * Occurrence information
	 */
	private Occurrence o;
	
	/**
	 * Constructor
	 */
	public Morph () {
		
	}
	
	/**
	 * Constructor with morph
	 * @param morph morph
	 */
	public Morph (String morph) {
		this();
		this.morph = morph;
	}
	
	/**
	 * Constructor with morph and occurrence information
	 * @param morph morph
	 * @param o occurrence information
	 */
	public Morph (String morph, Occurrence o) {
		this(morph);
		this.o = o;
	}
	
	/**
	 * Returns the morph
	 * @return morph
	 */
	public String getMorph () {
		return morph;
	}
	
	/**
	 * Returns the occurrence information
	 * as Occurrence object
	 * @return occurrence information
	 */
	public Occurrence getOccurrence () {
		return o;
	}
	
	/**
	 * Checks whether this morph has occurrence information
	 * @return true if occurrence information is set
	 */
	public boolean hasOccurrenceInformation () {
		return (o != null);
	}
	
	/**
	 * Returns a string representation
	 */
	public String toString () {
		return morph + (o==null?"":" ("+o+")");
	}
}
