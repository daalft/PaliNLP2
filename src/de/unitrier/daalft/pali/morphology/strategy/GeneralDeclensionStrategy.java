package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.List;

import de.unitrier.daalft.pali.general.Alphabet;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.rule.DerivingRule;
import de.unitrier.daalft.pali.phonology.SandhiManager;
import de.unitrier.daalft.pali.tools.Patterner;
import de.unitrier.daalft.pali.validation.Validator;
/**
 * General declension strategy
 * @author David
 *
 */
public class GeneralDeclensionStrategy {
	
	/**
	 * Validator
	 */
	private static Validator v = new Validator();
	/**
	 * Sandhi manager
	 */
	private static SandhiManager sm = SandhiManager.getInstance();
	
	/**
	 * Applies this strategy by combining every element of paradigm with the 
	 * word stem that is derived by applying the specified deriving rule
	 * to the specified lemma
	 * @param lemma lemma
	 * @param paradigm paradigm
	 * @param rule deriving rule
	 * @return morphological forms
	 */
	public List<ConstructedWord> apply (String lemma, Paradigm paradigm, DerivingRule rule) {
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		String stem = rule.apply(lemma);
		String vowel = Patterner.patternGroup(Alphabet.getVowels());
		for (Morpheme me : paradigm.getMorphemes()) {
			ConstructedWord cw = new ConstructedWord();
			cw.setLemma(lemma);
			for (Morph m : me.getAllomorphs()) {
				if (m.hasOccurrenceInformation()) {				
					cw.setStem(stem);
					cw.setInfo(me.getFeatureSet());
					cw = m.getOccurrence().apply(cw);
					String stemTemp = cw.getStem();
					String morph = m.getMorph();
					
					// especially for verbs
					if (paradigm.getParadigmByFeatures(new FeatureSet("paradigm", "verb")) != null)
						if (stemTemp.matches(".+"+vowel) && morph.matches(vowel+".+"))
							stemTemp = stemTemp.substring(0, stemTemp.length()-1);
					cw.setWord(stemTemp + morph);	
					out.add(copy(cw));			
				} else {
					// create new morph
					if (paradigm.getParadigmByFeatures(new FeatureSet("paradigm", "verb")) != null)
						if (stem.matches(".+"+vowel) && m.getMorph().matches(vowel+".+"))
							stem = stem.substring(0, stem.length()-1);
						
					String form = stem + m.getMorph();
						
					try {
						if (v.isValidWord(form)) {			
							cw.setWord(form);
							cw.setInfo(me.getFeatureSet());
							out.add(copy(cw));
						}
					} catch (Exception e) {
						// try sandhi merge
						for (String ls : sm.merge(stem, m.getMorph())) {
							ConstructedWord c = new ConstructedWord();
							c.setInfo(me.getFeatureSet());
							c.setWord(ls);
							c.setLemma(lemma);
							out.add(copy(c));
						}
					}
				}
			}
		}
		return out;
	}
	
	/**
	 * Creates a deep copy of the specified ConstructedWord
	 * @param cw constructed word
	 * @return deep copy
	 */
	private ConstructedWord copy (ConstructedWord cw) {
		ConstructedWord copy = new ConstructedWord();
		copy.setWord(cw.getWord());
		FeatureSet fscopy = new FeatureSet();
		for (Feature f : cw.getInfo()) {
			fscopy.add(f);
		}
		copy.setInfo(fscopy);
		copy.setLemma(cw.getLemma());
		return copy;
	}
}
