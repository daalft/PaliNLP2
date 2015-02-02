package de.unitrier.daalft.pali.morphology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.general.json.JObject;
import de.general.log.*;
import de.cl.dictclient.DictWord;
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
import de.unitrier.daalft.pali.morphology.strategy.*;
import de.unitrier.daalft.pali.morphology.tools.WordClassGuesser;
import de.unitrier.daalft.pali.morphology.tools.VerbHelper;
import de.unitrier.daalft.pali.morphology.strategy.NumeralStrategy;
import de.unitrier.daalft.pali.tools.WordConverter;


/**
 * Class responsible for the morphological analysis of words
 * @author David
 *
 */
public class MorphologyAnalyzer {

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	/**
	 * Word class specific endings
	 */
	private final static String 
			VERB_ENDING = "ti",
			NUMERAL_ENDING_5_18 = "a";
	/**
	 * Word class specific endings
	 */
	private final static String[] 
			NOUN_ENDINGS = {"as", "a", "u", "us", "i", "in", "ar", "an", "ant", "ā", "ī", "ū"},
			ADJ_ENDINGS = {"a", "i", "u", "ant", "ā", "ī", "ū"},
			NUMERAL_ENDING_19 = {"a", "i", "aṃ"};

	/**
	 * Separator between morphemes
	 */
	private final static String SEPARATOR = "_";

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	private ParadigmAccessor pa;
	private WordClassGuesser wcg;
	private AdverbStrategy as;
	private UnknownStrategy us;
	
	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	/**
	 * Constructor
	 */
	public MorphologyAnalyzer(ParadigmAccessor pa)
	{
		this.pa = pa;
		this.wcg = new WordClassGuesser(pa);
		as = new AdverbStrategy();
		us = new UnknownStrategy();
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	/**
	 * Analyze in offline mode
	 * <p>
	 * Returns possible analyses of a given word using paradigm information
	 * @param word word to analyze
	 * @return possible analyses
	 */
	public List<ConstructedWord> analyzeWithOptions(ILogInterface log, String word, String...options) {
		List<ConstructedWord> analyses = new ArrayList<ConstructedWord>();
		List<String> pos = new ArrayList<String>();
		if (options != null && options.length > 0 && !options[0].isEmpty()) {
			for (String opt : options)
				pos.add(opt);
		} else {

			pos = wcg.guessWordClassFromWordForm(word);

		}
		IrregularNouns irrnoun = pa.getIrregularNouns();
		IrregularNumerals irrnum = pa.getIrregularNumerals();
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
		for (String wci : pos) {
			if (wci.equals("adverb")) {
				
				analyses.addAll(as.apply(log, word));
				continue;
			}
			// retrieve relevant paradigm
			Paradigm p = pa.getParadigmsByFeatures(new FeatureSet("paradigm", wci));
			if (p == null) {
				
				analyses.addAll(us.apply(log,word));
				continue;
			}
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
						List<String> lemmata = lemmaFromStem(start, wci, dec);
						for (String lemma : lemmata) {
							ConstructedWord cw = new ConstructedWord();
							cw.setLemma(lemma);
							String pword = pre.isEmpty() ? "":pre+SEPARATOR;
							cw.setWord(pword + start + SEPARATOR + ending);
							cw.setFeatureSet(morpheme.getFeatureSet());
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
	 * Analyses a word in online context (with dictionary)
	 * @param log logger
	 * @param word word to analyze
	 * @param gramGrp grammar group node
	 * @return analyses
	 * @throws Exception
	 */
	public String analyzeWithDictionary(ILogInterface log, String word, JObject gramGrp) throws Exception {
		if (gramGrp == null) return __analyzeWithDictionary(log, word);
		String[] posPath = {"gramGrp", "PoS", "value"};
		String pos = gramGrp.getPropertyStringValueNormalized(posPath);
		String[] posArray = null;
		if (pos == null)
			posArray = gramGrp.getPropertyStringListValueNormalized(posPath);
		if (pos == null && posArray == null) {
			log.warn("No PoS found for entry " + word);
		}
		String[] posArray2 = new String[1];
		posArray2[0] = pos;
		String[] array = (pos == null) ? (posArray == null) ? null : posArray : posArray2;
		if (array == null) return __analyzeWithDictionary(log, word);
		return __analyzeWithDictionary(log, word, array);
	}
	
	/**
	 * Analyzes a word in offline context (without dictionary)
	 * @param log logger
	 * @param word word to analyze
	 * @param gramGrp grammar group node
	 * @return analyses
	 * @throws Exception
	 */
	public String analyzeWithoutDictionary(ILogInterface log, String word, JObject gramGrp) throws Exception {
		String[] ea = {};
		if (gramGrp == null) return __analyzeWithoutDictionary(log, word, ea);
		String[] posPath = {"gramGrp", "PoS", "value"};
		String pos = gramGrp.getPropertyStringValueNormalized(posPath);
		String[] posArray = null;
		if (pos == null)
			posArray = gramGrp.getPropertyStringListValueNormalized(posPath);
		if (pos == null && posArray == null) {
			log.warn("No PoS found for entry " + word);
		}
		String[] posArray2 = new String[1];
		posArray2[0] = pos;
		String[] array = (pos == null) ? (posArray == null) ? new String[1] : posArray : posArray2;
		
		return __analyzeWithoutDictionary(log, word, array);		
	}
	
	/**
	 * Analyze in offline mode
	 * <p>
	 * Wrapper method for DictWord
	 * <p>
	 * Returns possible analyses of a given word using paradigm information
	 * @param word word to analyze
	 * @return possible analyses
	 * @deprecated
	 */
	public List<ConstructedWord> analyze(DictWord word, ILogInterface log) {
		// extract class if applicable
		String pos = word.getValue("pos").toString();
		String w = word.getValue("word").toString();
		if (w == null || w.isEmpty()) 
			w = word.getValue("lemma").toString();
		return analyzeWithOptions(log, w, pos); 
	}
	
	/**
	 * Returns the lemma built from a given word stem and a given word class
	 *
	 * @param stem word stem
	 * @param pos word class
	 * @param strings options
	 * @return lemmata
	 */
	public List<String> lemmaFromStem (String stem, String pos, String...strings) {
		if (strings.length > 0 && !strings[0].isEmpty()) {
			String dec = strings[0];
			return Collections.singletonList(stem + dec);
		}
		List<String> out = new ArrayList<String>();
		switch (pos) {
		case "noun":
			// append all noun declensions
			for (String s : NOUN_ENDINGS)
				out.add(stem + s);
			break; 
		case "verb": 
			VerbHelper vh = new VerbHelper();
			// if we assume that "stem" is in fact a root
			List<String> stems = vh.stemFromRoot(stem);
			// and that "stem" could be the stem
			out.add(stem + VERB_ENDING);
			for (String s : stems)
				out.add(s + VERB_ENDING);
			// or that we have to derive a root from stem
			List<String> roots = vh.rootFromStem(stem);
			for (String s : roots)
				out.add(s + VERB_ENDING);
			break;
		case "numeral":
			// one to four should have been caught by analyzer before this point
			if (NumeralStrategy.isFiveTo18Stem(stem)) {
				out.add(stem + NUMERAL_ENDING_5_18);
			} else if (NumeralStrategy.is19upStem(stem)) {
				for (String s : NUMERAL_ENDING_19) {
					out.add(stem + s);
				}
			}
			break;
		case "adjective":
			for (String s : ADJ_ENDINGS)
				out.add(stem + s);
			break;
		case "adverb":
			out.add(stem); break;
		default: break;
		}
		return out;
	}
	
	//////////////////////////////////////////////////////////
	// Private methods
	//////////////////////////////////////////////////////////
	
	/**
	 * Helper method that accepts an array containing the options as last element
	 * @param log logger
	 * @param word word to analyze
	 * @param array options
	 * @return analyses
	 * @throws JsonProcessingException
	 */
	private String __analyzeWithoutDictionary(ILogInterface log, String word, String[] array) throws JsonProcessingException {
		return WordConverter.toJSONStringAnalyzer(analyzeWithOptions(log, word, array));
	}
	
	/**
	 * Analyze in online mode
	 * <p>
	 * Returns possible analyses of a given word using dictionary lookup. If 
	 * dictionary lookup fails, falls back to offline mode
	 * @param word word to analyze
	 * @return possible analyses
	 */
	private String __analyzeWithDictionary (ILogInterface log, String word, String... options) throws Exception {
		LexiconAdapter la = new LexiconAdapter();
		if (la.generatedContains(word)) {
			return la.getGenerated(word);
		} else {
			return WordConverter.toJSONStringAnalyzer(analyzeWithOptions(log, word, options));
		}
	}
	
	/**
	 * Constructs a new ConstructedWord using the given information
	 * @param match wordform
	 * @param lemma word lemma
	 * @param featureSet morpheme feature set
	 * @return constructed word
	 */
	private ConstructedWord constructWord(String match, String lemma, FeatureSet featureSet) {
		ConstructedWord cw = new ConstructedWord(match, featureSet);
		cw.setLemma(lemma);
		return cw;
	}
	
	public boolean isLemmaForm (String word) {
		StringBuilder nn = new StringBuilder(".*(");
		for (String s : NOUN_ENDINGS)
			nn.append(s+"|");
		nn.deleteCharAt(nn.length()-1);
		nn.append(")$");
		
		StringBuilder jj = new StringBuilder(".*(");
		for (String s : ADJ_ENDINGS)
			jj.append(s+"|");
		jj.deleteCharAt(jj.length()-1);
		jj.append(")$");
		
		StringBuilder nm = new StringBuilder(".*(");
		for (String s : NUMERAL_ENDING_19)
			nm.append(s+"|");
		nm.deleteCharAt(nm.length()-1);
		nm.append(")$");
		
		return word.endsWith(NUMERAL_ENDING_5_18) ||
				word.endsWith(VERB_ENDING) ||
				word.matches(nn.toString()) || //noun
				word.matches(jj.toString()) || //adjective
				word.matches(nm.toString()); //numeral
	}
}
