package de.unitrier.daalft.pali.morphology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lu.cl.dictclient.DictWord;
import de.unitrier.daalft.pali.lexicon.LexiconAdapter;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNouns;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNumerals;
import de.unitrier.daalft.pali.morphology.strategy.AdverbStrategy;
import de.unitrier.daalft.pali.morphology.tools.WordClassGuesser;
import de.unitrier.daalft.pali.tools.WordConverter;
/**
 * Class responsible for the morphological analysis of words
 * @author David
 *
 */
public class MorphologyAnalyzer {

	/**
	 * Separator between morphemes
	 */
	private final static String SEPARATOR = "_";
	/**
	 * Constructor
	 */
	public MorphologyAnalyzer () {

	}

	/**
	 * Analyze in offline mode
	 * <p>
	 * Returns possible analyses of a given word using paradigm information
	 * @param word word to analyze
	 * @return possible analyses
	 */
	public static List<ConstructedWord> analyze (DictWord word) {
		// extract class if applicable
		String pos = word.getValue("pos").toString();
		String w = word.getValue("word").toString();
		if (w == null || w.isEmpty()) 
			w = word.getValue("lemma").toString();
		return analyze(w, pos); 
	}

	/**
	 * Analyze in offline mode
	 * <p>
	 * Returns possible analyses of a given word using paradigm information
	 * @param word word to analyze
	 * @return possible analyses
	 */
	public static List<ConstructedWord> analyze (String word, String...options) {
		List<ConstructedWord> analyses = new ArrayList<ConstructedWord>();
		List<String> wc = null;
		if (options != null && options.length > 0 && !options[0].isEmpty()) {
			wc = Collections.singletonList(options[0]);
		} else {
			wc = WordClassGuesser.guess(word);
		}
		ParadigmAccessor pa = new ParadigmAccessor();
		IrregularNouns irrnoun = pa.getIrregularNouns();
		IrregularNumerals irrnum = pa.getIrregularNumerals();
		Lemmatizer l = new Lemmatizer();
		Paradigm pronoun = pa.getPronounParadigm();

		for (Morpheme mo : pronoun.getMorphemes()) {
			if (mo.exactly(word)) {
				FeatureSet feat = new FeatureSet();
				for (Feature f : mo.getFeatureSet()) {
					if (f.getKey().equals("case")) {
						feat.add(new Feature("case", "nominative"));
					} else if (f.getKey().equals("number")) {
						feat.add(new Feature("number", "singular"));
					} else {
						feat.add(f);
					}
				}
				Paradigm p = pronoun.getParadigmByFeatures(feat);
				for (Morph m : p.getEndings()) {
					String lemma = m.getMorph();
					analyses.add(constructWord(word, lemma, mo.getFeatureSet()));
				}
			}
			// TODO premature return?
		}
		if (irrnoun.isIrregular(word)) {
			Paradigm p = irrnoun.getLemma(word);

			for (Morpheme m : p.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					String lemma = n.getMorph();
					analyses.add(constructWord(word, lemma, irrnoun.getForms(word).getFeatureSet(word)));
				}
			}
			// premature?
		}

		if (irrnum.isIrregular(word)) {
			Paradigm p = irrnum.getLemma(word);
			for (Morpheme m : p.getMorphemes()) { 
				for (Morph n : m.getAllomorphs()) {
					String lemma = n.getMorph();
					analyses.add(constructWord(word, lemma, irrnum.getForms(word).getFeatureSet(word)));
				}
			}
			// TODO premature return?
		}
		// TODO affixes currently not used
		/*
		Paradigm prefix = pa.getPrefixParadigm();
		Paradigm suffix = pa.getSuffixParadigm();
		 */
		Map<String, String> map = new HashMap<String, String>();
		/* affix block - prefix
		for (Morpheme morpheme : prefix.getMorphemes()) {
			String pre = "";
			String pre_word = "";
			for (Morph morph : morpheme.getAllomorphs()) {
				if (word.startsWith(morph.getMorph())) {
					pre = morph.getMorph();
					pre_word = word.substring(pre.length());
					map.put(pre, pre_word);
				}
			}
		}*/
		// for each word class guess
		for (String wci : wc) {
			if (wci.equals("adverb")) {
				AdverbStrategy as = new AdverbStrategy();
				analyses.addAll(as.apply(word));
				continue;
			}
			// retrieve relevant paradigm
			Paradigm p = pa.getParadigmsByFeatures(new FeatureSet("paradigm", wci));
			// merge suffix paradigm in
			// TODO affix not used
			//p.getMorphemes().addAll(suffix.getMorphemes());
			// if no prefix was found, add word to map to avoid null verb
			if (map.isEmpty()) {
				map.put("", word);
			}
			for (Entry<String, String> e : map.entrySet()) {
				String pre = e.getKey();
				String pre_word = e.getValue();	
				for (Morpheme morpheme : p.getMorphemes()) {
					if (morpheme.isApplicable(word)) {
						// identify boundaries word<->ending
						String ending = morpheme.match(word);
						String stem = "";
						// set word to pre_word if prefix and suffix are not longer than word
						if (!pre.isEmpty() && pre.length() + ending.length() < word.length()) {
							stem = pre_word;
						} else {
							stem = word;
						}
						String start = stem.substring(0,stem.length()-ending.length());
						// derive lemma from start under "wci" assumption 
						// with declension info if present
						String dec = morpheme.getFeatureByName("declension");
						List<String> lemmata = l.lemmaFromStem(start, wci, dec);
						for (String lemma : lemmata) {
							ConstructedWord cw = new ConstructedWord();
							cw.setLemma(lemma);
							String pword = pre.isEmpty() ? "":pre+SEPARATOR;
							cw.setWord(pword + start + SEPARATOR + ending);
							cw.setInfo(morpheme.getFeatureSet());
							analyses.add(cw);
							cw = null;
						}
					}
				}
			}
		}
		return analyses;
	}

	/**
	 * Analyze in online mode
	 * <p>
	 * Returns possible analyses of a given word using dictionary lookup. If 
	 * dictionary lookup fails, falls back to offline mode
	 * @param word word to analyze
	 * @return possible analyses
	 */
	public static List<DictWord> analyzeWithDictionary (String word, String... options) throws Exception {
		LexiconAdapter la = new LexiconAdapter();
		if (la.generatedContains(word)) {
			return la.getGenerated(word);
		} else {
			System.err.println("Could not analyze " + word + " with dictionary. Falling back to offline mode.");
			return WordConverter.toDictWord(analyze(word, options));
		}
	}

	/**
	 * Analyze in online mode
	 * <p>
	 * Returns possible analyses of a given word using dictionary lookup. If 
	 * dictionary lookup fails, falls back to offline mode
	 * @param word word to analyze
	 * @return possible analyses
	 */
	public static List<DictWord> analyzeWithDictionary (DictWord word) throws Exception {
		String pos = word.getValue("pos").toString();
		String w = word.getValue("word").toString();
		if (w == null || w.isEmpty()) 
			w = word.getValue("lemma").toString();
		return analyzeWithDictionary(w, pos);
	}

	/**
	 * Constructs a new ConstructedWord using the given information
	 * @param match wordform
	 * @param lemma word lemma
	 * @param featureSet morpheme feature set
	 * @return constructed word
	 */
	private static ConstructedWord constructWord(String match, String lemma, FeatureSet featureSet) {
		ConstructedWord cw = new ConstructedWord(match, featureSet);
		cw.setLemma(lemma);
		return cw;
	}
}
