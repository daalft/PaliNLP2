package de.unitrier.daalft.pali.morphology.paradigm.rule;
/**
 * Null deriving rule. Returns the lemma
 * @author David
 *
 */
public class NullDerivingRule implements DerivingRule {

	@Override
	public String apply(String lemma) {
		return lemma;
	}

}
