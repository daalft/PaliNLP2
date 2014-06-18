package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.general.log.*;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNumerals;
import de.unitrier.daalft.pali.morphology.paradigm.rule.RightDeletingRule;


/**
 * Pre-configured strategy for numerals
 * @author David
 *
 */
public class NumeralStrategy extends AbstractStrategy {

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	/**
	 * Irregular numerals 1-4
	 * and "ubho" (both)
	 */
	private final static String[] a = {"eka", "dve", "ubho", "tayo", "cattāro"};
	/**
	 * Irregular numerals 5-18
	 */
	private final static String[] b = {
		"pañca",
		"cha",
		"satta",
		"aṭṭha",
		"nava",
		"dasa", "rasa", "lasa", "ḷasa", // spelling variation (10)
		"akārasa", "ekādasa", // spelling variation (11)
		"bārasa", "dvārasa", //...
		"tedasa", "terasa", "telasa",
		"catuddasa", "cuddasa", "coddasa",
		"pañcadasa", "paṇṇarasa", "pannarasa",
		"soḷasa", "sorasa",
		"sattadasa", "sattarasa",
		"aṭṭhādasa", "aṭṭhārasa"
		};
	private final static List<String> oneToFour = Arrays.asList(a);
	private final static List<String> fiveTo18 = Arrays.asList(b);
	
	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	ParadigmAccessor pa;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	public NumeralStrategy(ParadigmAccessor pa)
	{
		this.pa = pa;
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	@Override
	public List<ConstructedWord> apply(ILogInterface log, String lemma, String... options) {
		if (isOneToFour(lemma)) {
			return oneToFourDec(lemma);
		}
		GeneralDeclensionStrategy gds = new GeneralDeclensionStrategy();
		
		Paradigm num = pa.getNumeralParadigm();
		Paradigm fte = num.getParadigmByFeatures(new FeatureSet("restriction", "5to18"));
		Paradigm nu = num.getParadigmByFeatures(new FeatureSet("restriction", "19up"));
		Paradigm fte_a = fte.getParadigmByFeatures(new FeatureSet("declension", "a"));
		Paradigm nu_a = nu.getParadigmByFeatures(new FeatureSet("declension", "a"));
		Paradigm nu_i = nu.getParadigmByFeatures(new FeatureSet("declension", "i"));
		Paradigm nu_am = nu.getParadigmByFeatures(new FeatureSet("declension", "aṃ"));
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		if (lemma.endsWith("a")) {
			if (isFiveTo18(lemma)) {
				out= gds.apply(lemma, fte_a, new RightDeletingRule(1));
			} else if (is19up(lemma)) {
				out= gds.apply(lemma, nu_a, new RightDeletingRule(1));
			}
		} else if (lemma.endsWith("i")) {
			out= gds.apply(lemma, nu_i, new RightDeletingRule(1));
		} else if (lemma.endsWith("aṃ")) {
			out= gds.apply(lemma, nu_am, new RightDeletingRule(2));
		}
		// TODO include cardinals??
		for (int i = 0; i < out.size(); i++) {
			ConstructedWord cw = out.get(i);
			cw.setLemma(lemma);
		}
		return out;
	}

	/**
	 * Checks whether the given lemma is a number from
	 * 1 to 4
	 * @param lemma lemma
	 * @return true if lemma is number word from 1 to 4
	 */
	private static boolean isOneToFour (String lemma) {
		return oneToFour.contains(lemma);
	}
	
	/**
	 * Checks whether the given lemma is a number from
	 * 5 to 18
	 * @param lemma lemma
	 * @return true if lemma is number word from 5 to 18
	 */
	private static boolean isFiveTo18 (String lemma) {
		return fiveTo18.contains(lemma);
	}
	
	/**
	 * Checks whether the given lemma is a number greater
	 * than 19
	 * @param lemma lemma
	 * @return true if lemma is number word greater than 19
	 */
	private static boolean is19up (String lemma) {
		return !(oneToFour.contains(lemma)) && !(fiveTo18.contains(lemma));
	}
	
	/**
	 * Returns the declension of the number (1-4) specified by lemma
	 * @param lemma lemma
	 * @return morphological forms
	 */
	private List<ConstructedWord> oneToFourDec (String lemma) {
		IrregularNumerals in = pa.getIrregularNumerals();
		Paradigm p = in.getForms(lemma);
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		for (Morpheme m : p.getMorphemes()) {
			for (Morph n : m.getAllomorphs()) {
				ConstructedWord cw = new ConstructedWord(n.getMorph(), m.getFeatureSet());
				cw.setLemma(lemma);
				out.add(cw);
			}
		}
		return out;
	}
	
	/**
	 * Checks whether the given stem could be the
	 * stem of a number from 1 to 4
	 * @param stem stem
	 * @return true if stem could be number 1-4
	 */
	public static boolean isOneToFourStem (String stem) {
		if (isOneToFour(stem))
			return true;
		for (String s : oneToFour) {
			if (s.startsWith(stem) && (s.length() - stem.length() < 3))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the given stem could be
	 * the stem of a number from 5 to 18
	 * @param stem stem
	 * @return true if stem could be number 5-18
	 */
	public static boolean isFiveTo18Stem (String stem) {
		if (isFiveTo18(stem))
			return true;
		for (String s : fiveTo18) {
			if (s.startsWith(stem) && (s.length() - stem.length() < 3))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the given stem could be
	 * the stem of a number greater than 19
	 * @param stem stem
	 * @return true if stem could be number greater than 19
	 */
	public static boolean is19upStem (String stem) {
		return (!isOneToFourStem(stem)) && (!isFiveTo18Stem(stem));
	}
}
