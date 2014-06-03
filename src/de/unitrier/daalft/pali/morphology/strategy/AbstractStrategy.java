package de.unitrier.daalft.pali.morphology.strategy;

import java.util.List;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;

/**
 * Abstract strategy class extended by all strategies
 * @author David
 *
 */
public abstract class AbstractStrategy {

	/**
	 * Returns the strategy name
	 * <p>
	 * The strategy name corresponds to the
	 * strategy class name
	 * @return strategy name
	 */
	public String getStrategyName() {
		return this.getClass().getSimpleName();
	}
	/**
	 * Applies the strategy to the given lemma
	 * <p>
	 * Options specifies word-class-specific options<br/>
	 * Used by the generator
	 * @param lemma lemma
	 * @param options options
	 * @return lemma with strategy applied
	 */
	public abstract List<ConstructedWord> apply(String lemma, String... options);
	
	/**
	 * Calculates the union between two feature sets
	 * @param f1 feature set 1
	 * @param f2 feature set 2
	 * @return union of feature set
	 */
	protected static FeatureSet union (FeatureSet f1, FeatureSet f2) {
		FeatureSet u = new FeatureSet();
		for (Feature f : f1)
			u.add(f);
		for (Feature f : f2) {
			if (!u.contains(f))
				u.add(f);
		}
		return u;
	}
	

	/**
	 * Creates a deep copy of the specified ConstructedWord
	 * @param cw constructed word
	 * @return deep copy
	 */
	ConstructedWord copy (ConstructedWord cw) {
		ConstructedWord copy = new ConstructedWord();
		copy.setWord(cw.getWord());
		FeatureSet fscopy = new FeatureSet();
		for (Feature f : cw.getFeatureSet()) {
			fscopy.add(f);
		}
		copy.setFeatureSet(fscopy);
		copy.setLemma(cw.getLemma());
		return copy;
	}
}
