package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.general.log.*;

import de.unitrier.daalft.pali.general.Alphabet;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.paradigm.rule.NullDerivingRule;
import de.unitrier.daalft.pali.morphology.paradigm.rule.RightDeletingRule;
import de.unitrier.daalft.pali.tools.Patterner;


/**
 * Pre-configured strategy for adjectives. Uses the general declension strategy
 * @author David
 *
 */
public class AdjectiveStrategy extends AbstractStrategy {

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	private static boolean useAffixes = false;
	
	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	ParadigmAccessor pa;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	public AdjectiveStrategy(ParadigmAccessor pa)
	{
		this.pa = pa;
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	public List<ConstructedWord> apply(ILogInterface log, String lemma, String... options) {
		// General declension strategy
		GeneralDeclensionStrategy gds = new GeneralDeclensionStrategy();
		// Fetch all relevant paradigms
		Paradigm adjectives = pa.getAdjectiveParadigm();
		
		Paradigm a_masc = adjectives.getParadigmByFeatures(new FeatureSet("declension", "a")).getParadigmByFeatures(new FeatureSet("gender", "masculine"));
		Paradigm a_neut = adjectives.getParadigmByFeatures(new FeatureSet("declension", "a")).getParadigmByFeatures(new FeatureSet("gender", "neuter"));
		Paradigm long_a_fem = adjectives.getParadigmByFeatures(new FeatureSet("declension", "ā")).getParadigmByFeatures(new FeatureSet("gender", "feminine"));
		Paradigm i_masc = adjectives.getParadigmByFeatures(new FeatureSet("declension", "i")).getParadigmByFeatures(new FeatureSet("gender", "masculine"));
		Paradigm i_neut = adjectives.getParadigmByFeatures(new FeatureSet("declension", "i")).getParadigmByFeatures(new FeatureSet("gender", "neuter"));
		Paradigm long_i_fem = adjectives.getParadigmByFeatures(new FeatureSet("declension", "ī")).getParadigmByFeatures(new FeatureSet("gender", "feminine"));
		Paradigm long_i_masc = adjectives.getParadigmByFeatures(new FeatureSet("declension", "ī")).getParadigmByFeatures(new FeatureSet("gender", "masculine"));
		Paradigm u_masc = adjectives.getParadigmByFeatures(new FeatureSet("declension", "u")).getParadigmByFeatures(new FeatureSet("gender", "masculine"));
		Paradigm u_neut = adjectives.getParadigmByFeatures(new FeatureSet("declension", "u")).getParadigmByFeatures(new FeatureSet("gender", "neuter"));
		Paradigm long_u_masc = adjectives.getParadigmByFeatures(new FeatureSet("declension", "ū")).getParadigmByFeatures(new FeatureSet("gender", "masculine"));
		Paradigm ant = adjectives.getParadigmByFeatures(new FeatureSet("declension", "ant"));
		Paradigm as = adjectives.getParadigmByFeatures(new FeatureSet("declension", "as"));
		
		// Initialize output list
		List<ConstructedWord> out = new LinkedList<ConstructedWord>();
		// Initialize right deleting rule 1
		RightDeletingRule rdr1 = new RightDeletingRule(1);
		// Start
		if (lemma.endsWith("a")) {
			
			out.addAll(gds.apply(lemma, a_masc, rdr1));
	
			out.addAll(gds.apply(lemma, a_neut, rdr1));
	
			out.addAll(gds.apply(lemma, long_a_fem, rdr1));
			out.addAll(gds.apply(lemma, long_i_fem, rdr1));
			
		} else if (lemma.endsWith("i")) {
			
			out.addAll(gds.apply(lemma, i_masc, rdr1));
			
			out.addAll(gds.apply(lemma, i_neut, rdr1));
			
			out.addAll(gds.apply(lemma+"ni", long_i_fem, rdr1));
			
		} else if (lemma.endsWith("ī")) {
			
			out.addAll(gds.apply(lemma, long_i_masc, rdr1));
			
			out.addAll(gds.apply(lemma, i_neut, rdr1));
			
			String fem = lemma.substring(0, lemma.length()-1) + "ini";
			
			out.addAll(gds.apply(fem, long_i_fem, rdr1));
			
		} else if (lemma.endsWith("u")) {
			
			out.addAll(gds.apply(lemma, u_masc, rdr1));
			
			out.addAll(gds.apply(lemma, u_neut, rdr1));
			
			out.addAll(gds.apply(lemma + "ni", long_i_fem, rdr1));
			
		} else if (lemma.endsWith("ū")) {
			
			out.addAll(gds.apply(lemma, long_u_masc, rdr1));
			
			out.addAll(gds.apply(lemma, u_neut, rdr1));
			
			String fem = lemma.substring(0, lemma.length()-1) + "unī";
			
			out.addAll(gds.apply(fem, long_i_fem, rdr1));
			
		} else if (lemma.endsWith("at")) {
			
			out.addAll(gds.apply(lemma, ant, new RightDeletingRule(2)));
			
			out.addAll(gds.apply(lemma, long_i_fem, new NullDerivingRule()));
			
		} else if (lemma.endsWith("ant")) {
			
			out.addAll(gds.apply(lemma, ant, new RightDeletingRule(3)));
			
			out.addAll(gds.apply(lemma, long_i_fem, new NullDerivingRule()));
			
		} else if (lemma.endsWith("ā")) {
			
			String mn = lemma.substring(0, lemma.length()-1) + "ant";
			
			out.addAll(gds.apply(mn, ant, new RightDeletingRule(3)));
			
			out.addAll(gds.apply(mn, long_i_fem, new NullDerivingRule()));
		}
		// add positive information
		
		for (int i = 0; i < out.size(); i++) {
			ConstructedWord cw = out.get(i);
			cw.addFeature(new Feature("comparison", "positive"));
		}
		
		// create comparative + superlative in tara/tama
		String comp_a = lemma + "tara";
		String sup_a = lemma + "tama";
		List<ConstructedWord> comparative = new ArrayList<ConstructedWord>();
		
		comparative.addAll(gds.apply(comp_a, a_masc, rdr1));
		comparative.addAll(gds.apply(comp_a, a_neut, rdr1));
		comparative.addAll(gds.apply(comp_a, long_a_fem, rdr1));
		
		List<ConstructedWord> superlative = new ArrayList<ConstructedWord>();	
		
		superlative.addAll(gds.apply(sup_a, a_masc, rdr1));
		superlative.addAll(gds.apply(sup_a, a_neut, rdr1));
		superlative.addAll(gds.apply(sup_a, long_a_fem, rdr1));

		for (int i = 0; i < superlative.size(); i++) {
			ConstructedWord cw = superlative.get(i);
			cw.addFeature(new Feature("frequency", "rare"));
		}
		
		String comp_i = "";
		String comp_iy = "";
		String sup_i = "";
		String sup_iss = "";
		String supersuper = "";
		if (lemma.matches(".*"+Patterner.patternGroup(Alphabet.getVowels())+"$")) {
			String s = lemma.substring(0, lemma.length()-1);
			comp_i = s + "iya";
			comp_iy = s + "iyya";
			sup_i = s + "iṭṭha";
			sup_iss = s + "issika";
			supersuper = s + "iṭṭhatara";
		} else {
			comp_i = lemma + "iya";
			comp_iy = lemma + "iyya";
			sup_i = lemma + "iṭṭha";
			sup_iss = lemma + "issika";
			supersuper = lemma + "iṭṭhatara";
		}
		comparative.addAll(gds.apply(comp_i, as, rdr1));
		comparative.addAll(gds.apply(comp_iy, as, rdr1));
		
		superlative.addAll(gds.apply(sup_i, a_masc, rdr1));
		superlative.addAll(gds.apply(sup_i, a_neut, rdr1));
		superlative.addAll(gds.apply(sup_i, long_a_fem, rdr1));
		
		superlative.addAll(gds.apply(sup_iss, a_masc, rdr1));
		superlative.addAll(gds.apply(sup_iss, a_neut, rdr1));
		superlative.addAll(gds.apply(sup_iss, long_a_fem, rdr1));
		
		superlative.addAll(gds.apply(supersuper, a_masc, rdr1));
		superlative.addAll(gds.apply(supersuper, a_neut, rdr1));
		superlative.addAll(gds.apply(supersuper, long_a_fem, rdr1));
		
		for (int i = 0; i < comparative.size(); i++) {
			ConstructedWord cw = comparative.get(i);
			cw.addFeature(new Feature("comparison", "comparative"));
			cw.setLemma(lemma);
		}
		for (int i = 0; i < superlative.size(); i++) {
			ConstructedWord cw = superlative.get(i);
			cw.addFeature(new Feature("comparison", "superlative"));
			cw.setLemma(lemma);
		}
		out.addAll(comparative);
		out.addAll(superlative);
		// if (useAffixes)
		// 	out.addAll(AffixStrategy.apply(out));
		return out;
	
	}

}
