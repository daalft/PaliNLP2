package de.unitrier.daalft.pali.morphology.paradigm.irregular;

import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
/**
 * Interface for irregular handlers
 * @author David
 *
 */
public interface Irregular {

	/**
	 * Adds an irregular paradigm to this
	 * @param current paradigm
	 */
	void add (Paradigm current);
	/**
	 * Checks whether a given word is irregular
	 * <p>
	 * A given word is regarded as irregular, if 
	 * it is contained within this handler
	 * @param word word to check
	 * @return true if word is irregular
	 */
	boolean isIrregular (String word);
	/**
	 * Returns all morphological forms for
	 * a given word
	 * @param word word
	 * @return morphological forms
	 */
	Paradigm getForms (String word);
	/**
	 * Returns the lemma of a given irregular word
	 * @param word word
	 * @return lemma
	 */
	Paradigm getLemma (String word);
	/**
	 * Default toString method
	 * @return String representation
	 */
	String toString ();
}
