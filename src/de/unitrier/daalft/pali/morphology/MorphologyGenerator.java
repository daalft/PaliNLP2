package de.unitrier.daalft.pali.morphology;

import java.util.*;

import de.general.log.*;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNouns;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNumerals;
import de.unitrier.daalft.pali.morphology.tools.WordClassGuesser;
import de.unitrier.daalft.pali.morphology.strategy.*;

/**
 * Given a lemma, returns all possible morphological word forms
 * @author David
 *
 */

public class MorphologyGenerator
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	private static boolean debug = false;

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	private HashMap<String, AbstractStrategy> wordClassStrategies;
	private NullStrategy wordClassNullStrategy;
	private ParadigmAccessor pa;
	private WordClassGuesser wcg;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	/**
	 * Default constructor.
	 */
	public MorphologyGenerator(ParadigmAccessor pa)
	{
		this.pa = pa;

		wordClassStrategies = new HashMap<>();

		wordClassStrategies.put("noun", new NounStrategy(pa));
		wordClassStrategies.put("verb", new AlternativeVerbStrategy(pa));
		wordClassStrategies.put("adjective", new AdjectiveStrategy(pa));
		wordClassStrategies.put("numeral", new NumeralStrategy(pa));
		wordClassStrategies.put("adverb", new AdverbStrategy());
		wordClassStrategies.put("pronoun", new PronounStrategy(pa));
		wordClassStrategies.put("indeclinable", new IndeclinableStrategy());

		wordClassNullStrategy = new NullStrategy();

		wcg = new WordClassGuesser(pa);
	}
	
	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	/**
	 * Returns all possible morphological word forms 
	 * for the given lemma and the given word class
	 * <p>
	 * <b>
	 * Please note that the input is expected to
	 * be a valid lemma
	 * </b>
	 * @param lemma		lemma
	 * @param wc		Word class of the lemma. This parameter is either a single string representing word class information or <code>null</code>.
	 * @param options	options
	 * @return			list of constructed words
	 */
	public List<ConstructedWord> generate(ILogInterface log, String lemma, String wc, String... options)
	{
		IrregularNouns ino = pa.getIrregularNouns();
		IrregularNumerals inu = pa.getIrregularNumerals();
		List<ConstructedWord> irrOut = new ArrayList<ConstructedWord>();

		Paradigm p = ino.getForms(lemma);
		if (p != null) {
			for (Morpheme m : p.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					ConstructedWord cw = new ConstructedWord(n.getMorph(), m.getFeatureSet());
					cw.setLemma(lemma);
					irrOut.add(cw);
				}
			}
		}

		p = inu.getForms(lemma);
		if (p != null) {
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
			wcs = wcg.guessWordClassFromLemma(lemma);
		}
		if (wcs.isEmpty()) {
			return _generate(log, lemma, wc, options);
		} else {
			List<ConstructedWord> list = new ArrayList<ConstructedWord>();
			for (String wci : wcs) {
				list.addAll(_generate(log, lemma, wci, options));
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
	private List<ConstructedWord> _generate(ILogInterface log, String lemma, String wc, String... options)
	{
		AbstractStrategy strategy = wordClassStrategies.get(wc);
		if (strategy != null) {
			return strategy.apply(log, lemma, options);
		}

		if (debug) log.warn("Could not find strategy for " + wc);
		return wordClassNullStrategy.apply(log, lemma, options);
	}

}
