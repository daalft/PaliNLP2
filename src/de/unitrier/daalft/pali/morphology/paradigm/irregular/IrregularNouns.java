package de.unitrier.daalft.pali.morphology.paradigm.irregular;

import java.util.ArrayList;
import java.util.List;
import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
/**
 * Utility class that wraps functionality related to irregular noun paradigms
 * <p>
 * Offers access to irregular noun paradigms in offline context
 * @author David
 *
 */
public class IrregularNouns implements Irregular {

	/**
	 * List of paradigms
	 */
	private List<Paradigm> paradigms;

	/**
	 * Constructor
	 */
	public IrregularNouns () {
		paradigms = new ArrayList<Paradigm>();
	}

	public void add (Paradigm p) {
		paradigms.add(p);
	}

	public boolean isIrregular (String word) {
		for (Paradigm p : paradigms) {
			for (Morpheme m : p.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					if (n.getMorph().equals(word))
						return true;
				}
			}
		}
		return false;
	}

	public Paradigm getForms (String word) {
		for (Paradigm p : paradigms) {
			for (Morpheme m : p.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					if (n.getMorph().equals(word))
						return p;
				}
			}
		}
		return null;
	}

	public Paradigm getLemma (String word) {
		FeatureSet feat = new FeatureSet();
		
		feat.add(new Feature("case", "nominative"));
		feat.add(new Feature("number", "singular"));
		for (Paradigm p : paradigms) {
			for (Morpheme m : p.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					if (n.getMorph().equals(word)) {
						return p.getParadigmByFeatures(feat);
					}
				}
			}
		}
		return null;
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		for (Paradigm p : paradigms) {
			sb.append(p);
		}
		return sb.toString();
	}
}
