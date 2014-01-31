package de.unitrier.daalft.pali.morphology.paradigm.rule;
/**
 * Interface for stem deriving rules
 * @author David
 *
 */
public interface DerivingRule {

	/**
	 * Applies this deriving rule to the specified lemma and
	 * returns the calculated stem
	 * @param lemma lemma
	 * @return stem
	 */
	public String apply(String lemma);
	
}
