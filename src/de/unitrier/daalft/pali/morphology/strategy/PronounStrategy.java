package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.List;

import de.general.log.*;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;


public class PronounStrategy extends AbstractStrategy
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	private final static byte debugLevel = 2;
	
	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	ParadigmAccessor pa;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	public PronounStrategy(ParadigmAccessor pa)
	{
		this.pa = pa;
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	@Override
	public List<ConstructedWord> apply(ILogInterface log, String lemma, String... options) {
		// main filter
		FeatureSet filter = new FeatureSet();
		// auxiliary filter
		FeatureSet helpFilter = null;
		switch(lemma) {
		case "ayaṃ": // return subtype demonstrative specification ay/i all genera 
			filter.add(new Feature("subtype", "demonstrative"));
			filter.add(new Feature("specification", "ay/i"));
			break;
		case "ahaṃ": // return personal all genera
			filter.add(new Feature("subtype", "personal"));
			break;
		case "asu": // return demonstrative asu/amu masc + fem
			filter.add(new Feature("subtype", "demonstrative"));
			filter.add(new Feature("specification", "asu/amu"));
			filter.add(new Feature("gender", "masculine"));
			helpFilter = new FeatureSet();
			helpFilter.add(new Feature("subtype", "demonstrative"));
			helpFilter.add(new Feature("specification", "asu/amu"));
			helpFilter.add(new Feature("gender", "feminine"));
			break;
		case "aduṃ": // return demonstrative asu/amu neuter
			filter.add(new Feature("subtype", "demonstrative"));
			filter.add(new Feature("specification", "asu/amu"));
			filter.add(new Feature("gender", "neuter"));
			break;
		//REMARK: not a lemma entry
		case "ko": // return interrogative all?
			// fall through
		case "ka°": // return interrogative all?
			filter.add(new Feature("subtype", "interrogative"));
			break;
		case "kā": // return interrogative fem
			filter.add(new Feature("subtype", "interrogative"));
			filter.add(new Feature("gender", "feminine"));
			break;
		case "kiṃ": // return interrogative neuter
			filter.add(new Feature("subtype", "interrogative"));
			filter.add(new Feature("gender", "neuter"));
			break;
		// REMARK: not a lemma entry	
		case "yo": // return relative all?
			// fall through
		case "ya°": // return relative all?
			filter.add(new Feature("subtype", "relative"));
			break;
		case "yā": // return relative fem
			filter.add(new Feature("subtype", "relative"));
			filter.add(new Feature("gender", "feminine"));
			break;
		case "yaṃ": // return relative neuter
			// fall through
		case "yad": // return relative neuter
			filter.add(new Feature("subtype", "relative"));
			filter.add(new Feature("gender", "neuter"));
			break;
		default:
			filter = null;
			break;
		}
		if (debugLevel > 1 && filter == null) {
			System.err.println(lemma + " is not recognized as valid pronoun lemma!");
			System.err.println("Falling back to paradigmatic analysis.");
		}
		Paradigm pronoun = pa.getPronounParadigm();
		for (Morpheme mo : pronoun.getMorphemes()) {
			if (mo.exactly(lemma)) {
				// get the sub paradigm and specification if possible
				String sub = mo.getFeatureByName("subtype");
				String spec = mo.getFeatureByName("specification");
				FeatureSet feat = new FeatureSet();
				feat.add(new Feature("subtype", sub));
				if (!spec.isEmpty())
					feat.add(new Feature("specification", spec));
				Paradigm p = pa.getParadigmsByFeatures(feat);
				return constructWords(p, lemma);
			}
		}
		if (debugLevel > 1) {
			System.err.println("Could not apply pronoun strategy to " + lemma);
		}
		return null;
	}

	private List<ConstructedWord> constructWords(Paradigm p, String lemma) {
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		for (Morpheme m : p.getMorphemes()) {
			for (Morph mo : m.getAllomorphs()) {
				String morph = mo.getMorph();
				FeatureSet fs = m.getFeatureSet();
				ConstructedWord cw = new ConstructedWord();
				cw.setFeatureSet(fs);
				cw.setWord(morph);
				cw.setLemma(lemma);
				if (!out.contains(cw))
					out.add(cw);
			}
		}
		return out;
	}
}
