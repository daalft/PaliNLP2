package de.unitrier.daalft.pali.morphology;

import java.util.*;

import de.general.json.JObject;
import de.general.json.JToken;
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
	public List<ConstructedWord> generate(ILogInterface log, String lemma, String pos, String... options)
	{
		// TODO remove
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

		// end remove

		List<String> wcs = new ArrayList<String>();

		if (pos == null || pos.isEmpty()) {
			wcs = wcg.guessWordClassFromLemma(lemma);

		}
		if (wcs.isEmpty()) {
			return _generate(log, lemma, pos, options);
		} else {
			List<ConstructedWord> list = new ArrayList<ConstructedWord>();
			for (String wci : wcs) {
				list.addAll(_generate(log, lemma, wci, options));
			}
			return list;
		}
	}

	public List<ConstructedWord> generate (ILogInterface log, String lemma, JObject gramgrp) {
		
		if (gramgrp == null) {
			return generate(log, lemma, "");
		}
		
		List<String> options = new ArrayList<String>();
		
		String[] posPath = {"gramGrp", "PoS", "value"};
		String pos = gramgrp.getPropertyStringValueNormalized(posPath);
		// NOTE: if PoS value is an array, pos will be null
		// 		 posArray will not be null in that case
		String[] posArray = gramgrp.getPropertyStringListValueNormalized(posPath);
		String[] genderPath = {"gramGrp", "genus", "value"};
		String gender = gramgrp.getPropertyStringValueNormalized(genderPath);
		// NOTE: same as above
		String[] genderArray = gramgrp.getPropertyStringListValueNormalized(genderPath);
		
		// TODO extract other grammar information
		
		if (gender != null) {
			options.add(expand(gender));
		} else if (genderArray != null && genderArray.length > 0) {
			for (String g : genderArray) {
				options.add(expand(g));
			}
		}
		List<ConstructedWord> tempList = new ArrayList<ConstructedWord>();
		if (pos == null && posArray != null && posArray.length > 0) {		
			for (int i = 0; i < posArray.length; i++) {
				tempList.addAll(this._generate(log, lemma, expand(posArray[i]), options.toArray(new String[1])));
			}
			return tempList;
		}
		return this._generate(log, lemma, expand(pos), options.toArray(new String[1]));
	}
	
	/**
	 * Internal generator method
	 * @param lemma lemma
	 * @param pos word class of lemma
	 * @param options options
	 * @return list of constructed words
	 */
	private List<ConstructedWord> _generate(ILogInterface log, String lemma, String pos, String... options)
	{
		AbstractStrategy strategy = wordClassStrategies.get(pos);
		if (strategy != null) {
			return strategy.apply(log, lemma, options);
		}

		if (debug) log.warn("Could not find strategy for " + pos);
		return wordClassNullStrategy.apply(log, lemma, options);
	}

	private String expand (String s) {
		switch (s) {
		case "n" : return "neuter";
		case "m" : return "masculine";
		case "f" : return "feminine";
		case "adj" : return "adjective";
		case "adv" : return "adverb";
		default: return s;
		}
	}
}
