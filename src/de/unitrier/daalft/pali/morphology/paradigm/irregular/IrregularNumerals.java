package de.unitrier.daalft.pali.morphology.paradigm.irregular;

import java.util.ArrayList;
import java.util.List;
import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
/**
 * Utility class that wraps functionality related to irregular numerals
 * <p>
 * Offers access to irregular numeral paradigms in offline context
 * @author David
 *
 */
public class IrregularNumerals implements Irregular {
	/**
	 * List of paradigms
	 */
	private List<Paradigm> paradigms;

	/**
	 * Constructor
	 */
	public IrregularNumerals () {
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
		// case caturo etc
		// return catur
		FeatureSet fs1 = new FeatureSet();
		fs1.add(new Feature("number", "singular"));
		fs1.add(new Feature("case", "vocative"));
		fs1.add(new Feature("gender", "masculine"));
		FeatureSet fs2 = new FeatureSet();
		fs2.add(new Feature("number", "plural"));
		fs2.add(new Feature("case", "vocative"));
		fs2.add(new Feature("gender", "masculine"));
		FeatureSet fs3 = new FeatureSet();
		fs3.add(new Feature("number", "singular"));
		fs3.add(new Feature("case", "nominative"));
		fs3.add(new Feature("gender", "masculine"));
		FeatureSet fs4 = new FeatureSet();
		fs4.add(new Feature("number", "plural"));
		fs4.add(new Feature("case", "nominative"));
		fs4.add(new Feature("gender", "masculine"));
		FeatureSet fs5 = new FeatureSet();
		fs1.add(new Feature("number", "singular"));
		fs1.add(new Feature("case", "vocative"));
		
		FeatureSet fs6 = new FeatureSet();
		fs2.add(new Feature("number", "plural"));
		fs2.add(new Feature("case", "vocative"));
		
		
		for (Paradigm p : paradigms) {
			for (Morpheme m : p.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					if (n.getMorph().equals(word)) {
						if (p.getParadigmByFeatures(fs1) != null && !p.getParadigmByFeatures(fs1).isEmpty()) {
							return p.getParadigmByFeatures(fs1);
						} else if (p.getParadigmByFeatures(fs2) != null && !p.getParadigmByFeatures(fs2).isEmpty()) {
							return p.getParadigmByFeatures(fs2);
						} else if (p.getParadigmByFeatures(fs3) != null && !p.getParadigmByFeatures(fs3).isEmpty()) {
							return p.getParadigmByFeatures(fs3);
						} else if (p.getParadigmByFeatures(fs4) != null && !p.getParadigmByFeatures(fs4).isEmpty()) {
							return p.getParadigmByFeatures(fs4);
						} else if (p.getParadigmByFeatures(fs5) != null && !p.getParadigmByFeatures(fs5).isEmpty()) {
							return p.getParadigmByFeatures(fs5);
						} else if (p.getParadigmByFeatures(fs6) != null && !p.getParadigmByFeatures(fs6).isEmpty()) {
							return p.getParadigmByFeatures(fs6);
						} else {
							return null;
						}
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
