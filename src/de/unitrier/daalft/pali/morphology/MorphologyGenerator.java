package de.unitrier.daalft.pali.morphology;

import java.util.ArrayList;
import java.util.List;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNouns;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNumerals;
import de.unitrier.daalft.pali.morphology.strategy.StrategyManager;
import de.unitrier.daalft.pali.morphology.tools.WordClassGuesser;

/**
 * Given a lemma, returns all possible morphological word forms
 * @author David
 *
 */

public class MorphologyGenerator {

	/**
	 * Returns all possible morphological word forms 
	 * for the given lemma and the given word class
	 * <p>
	 * <b>
	 * Please note that the input is expected to
	 * be a valid lemma
	 * </b>
	 * @param lemma lemma
	 * @param wc word class of the lemma
	 * @param options options
	 * @return list of constructed words
	 */
	public static List<ConstructedWord> generate (String lemma, String wc, String... options) {
		ParadigmAccessor pa = new ParadigmAccessor();
		IrregularNouns ino = pa.getIrregularNouns();
		IrregularNumerals inu = pa.getIrregularNumerals();
		List<ConstructedWord> irrOut = new ArrayList<ConstructedWord>();
		if (ino.isIrregular(lemma)) {
			Paradigm p = ino.getForms(lemma);
			for (Morpheme m : p.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					ConstructedWord cw = new ConstructedWord(n.getMorph(), m.getFeatureSet());
					cw.setLemma(lemma);
					irrOut.add(cw);
				}
			}
		}
		
		if (inu.isIrregular(lemma)) {
			Paradigm p = inu.getForms(lemma);
			for (Morpheme m : p.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					ConstructedWord cw = new ConstructedWord(n.getMorph(), m.getFeatureSet());
					cw.setLemma(lemma);
					irrOut.add(cw);
				}
			}
		}
		if (irrOut.size() > 0)
			return irrOut;
		List<String> wcs = new ArrayList<String>();
		if (wc == null || wc.isEmpty()) {
			wcs = WordClassGuesser.guessLemma(lemma);
		}
		if (wcs.isEmpty()) {
			return _generate(lemma, wc, options);	
		} else {
			List<ConstructedWord> list = new ArrayList<ConstructedWord>();
			for (String wci : wcs) {
				list.addAll(_generate(lemma, wci, options));
			}
			return list;
		}
	}

	/**
	 * Internal generator method
	 * @param lemma lemma
	 * @param wc word class of lemma
	 * @param options options
	 * @return list of constructed words
	 */
	private static List<ConstructedWord> _generate (String lemma, String wc, String... options) {
		return StrategyManager.getStrategy(wc).apply(lemma, options);
	}
}
