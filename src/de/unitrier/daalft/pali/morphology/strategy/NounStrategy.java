package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.List;

import de.general.log.*;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNouns;
import de.unitrier.daalft.pali.morphology.paradigm.rule.RightDeletingRule;


/**
 * Pre-configured class for nouns. Uses the general declension strategy. 
 * @author David
 *
 */
public class NounStrategy extends AbstractStrategy {
	
	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	private static boolean useAffixes = false;
	
	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	ParadigmAccessor pa;
	Paradigm emptyParadigm;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	public NounStrategy(ParadigmAccessor pa)
	{
		this.pa = pa;
		this.emptyParadigm = new Paradigm();
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	@Override
	public List<ConstructedWord> apply(ILogInterface log, String lemma, String... options)
	{
		GeneralDeclensionStrategy gds = new GeneralDeclensionStrategy();
		
		String gender = (options != null && options.length > 0 && options[0] != null) ? options[0].isEmpty()?null:options[0] : null;
		String nounDeclension = (options !=null && options.length > 1) ? options[1] : null;
		
		Paradigm nouns = pa.getNounParadigm();
		
		Paradigm a = nouns.getParadigmByFeatures(new FeatureSet("declension", "a"));
		//log.debug("declension 'a' : " + a);
		if (gender != null && can(a, gender))
			a = a.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			a = emptyParadigm;
		Paradigm as = nouns.getParadigmByFeatures(new FeatureSet("declension","as"));
		
		//log.debug("declension 'as' : " + as);
		if (gender != null && can(as, gender))
			as = as.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null) 
			as = emptyParadigm;
		Paradigm u = nouns.getParadigmByFeatures(new FeatureSet("declension", "u"));
		//log.debug("declension 'u' : " + u);
		if (gender != null && can(u, gender))
			u = u.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			u = emptyParadigm;
		Paradigm us = nouns.getParadigmByFeatures(new FeatureSet("declension", "us"));
		//log.debug("declension 'us' : " + us);
		if (gender != null && can(us, gender))
			us = us.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			us = emptyParadigm;
		Paradigm i = nouns.getParadigmByFeatures(new FeatureSet("declension", "i"));
		//log.debug("declension 'i' : " + i);
		if (gender != null && can (i, gender))
			i = i.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			i = emptyParadigm;
		Paradigm in = nouns.getParadigmByFeatures(new FeatureSet("declension", "in"));
		//log.debug("declension 'in' : " + in);
		if (gender != null && can(in, gender))
			in = in.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			in = emptyParadigm;
		Paradigm ar = nouns.getParadigmByFeatures(new FeatureSet("declension", "ar"));
		//log.debug("declension 'ar' : " + ar);
		if (gender != null && can(ar, gender))
			ar = ar.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			ar = emptyParadigm;
		Paradigm an = nouns.getParadigmByFeatures(new FeatureSet("declension", "an"));
		//log.debug("declension 'an' : " + an);
		if (gender != null && can(an, gender))
			an = an.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			an = emptyParadigm;
		Paradigm ant = nouns.getParadigmByFeatures(new FeatureSet("declension", "ant"));
		//log.debug("declension 'ant' : " + ant);
		if (gender != null && can(ant, gender))
			ant = ant.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			ant = emptyParadigm;
		Paradigm longa = nouns.getParadigmByFeatures(new FeatureSet("declension", "ā"));
		//log.debug("declension 'ā' : " + longa);
		if (gender != null && can(longa, gender))
			longa = longa.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			longa = emptyParadigm;
		Paradigm longi = nouns.getParadigmByFeatures(new FeatureSet("declension", "ī"));
		//log.debug("declension 'ī' : " + longi);
		if (gender != null && can(longi, gender))
			longi = longi.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			longi = emptyParadigm;
		Paradigm longu = nouns.getParadigmByFeatures(new FeatureSet("declension", "ū"));
		//log.debug("declension 'ū' : " + longu);
		if (gender != null && can (longu, gender))
			longu = longu.getParadigmByFeatures(new FeatureSet("gender", gender));
		else if (gender != null)
			longu = emptyParadigm;
		IrregularNouns irrn = pa.getIrregularNouns();
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		if (irrn.isIrregular(lemma)) {
			Paradigm forms = irrn.getForms(lemma);
			for (Morpheme m : forms.getMorphemes()) {
				for (Morph n : m.getAllomorphs()) {
					ConstructedWord cw1 = new ConstructedWord(n.getMorph(), m.getFeatureSet());
					out.add(cw1);
					// if (useAffixes)
						// out.addAll(AffixStrategy.apply(out));
				}
			}
			return out;
		}
		
		List<ConstructedWord> list = new ArrayList<ConstructedWord>();
		if (lemma.endsWith("o") || lemma.endsWith("as")) {
			if (lemma.endsWith("o")) {
				list = gds.apply(lemma, as, new RightDeletingRule(1));
			} else if (lemma.endsWith("as")) {
				list = gds.apply(lemma, as, new RightDeletingRule(2));
			}
		} else if (lemma.endsWith("a")) {
			List<ConstructedWord> ls = new ArrayList<ConstructedWord>();
			if (nounDeclension != null) {
				if (nounDeclension.equals("a")) {
					ls.addAll(gds.apply(lemma, a, new RightDeletingRule(1)));
				} else if (nounDeclension.equals("as")) {
					ls.addAll(gds.apply(lemma, as, new RightDeletingRule(1)));	
				}
			} else {
				// NOTE: Do not apply 'as' by default. Only if specified
				ls.addAll(gds.apply(lemma, a, new RightDeletingRule(1)));
			}
			list = ls;
		} else if (lemma.endsWith("u")) {
			List<ConstructedWord> ls = gds.apply(lemma, u, new RightDeletingRule(1));
			//ls.addAll(gds.apply(lemma, u, new RightDeletingRule(1)));
			list = ls;
		} else if (lemma.endsWith("us")) {
			list = gds.apply(lemma, us, new RightDeletingRule(2));
		} else if (lemma.endsWith("i")) {
			list = gds.apply(lemma, i, new RightDeletingRule(1));
		} else if (lemma.endsWith("in")) {
			list = gds.apply(lemma, in, new RightDeletingRule(2));
		} else if (lemma.endsWith("ar")) {
			list = gds.apply(lemma, ar, new RightDeletingRule(2));
		} else if (lemma.endsWith("an")) {
			list = gds.apply(lemma, an, new RightDeletingRule(2));
		} else if (lemma.endsWith("at") || lemma.endsWith("ant")) {
			if (lemma.endsWith("at")) {
				list = gds.apply(lemma, ant, new RightDeletingRule(2));
			} else if (lemma.endsWith("ant")) {
				list = gds.apply(lemma, ant, new RightDeletingRule(3));
			}
		} else if (lemma.endsWith("ā")) {
			List<ConstructedWord> ls = new ArrayList<ConstructedWord>();
			if (nounDeclension != null) {
				if (nounDeclension.equals("ā")) {
					ls.addAll(gds.apply(lemma, longa, new RightDeletingRule(1)));
				} else if (nounDeclension.equals("ar")) {
					ls.addAll(gds.apply(lemma, ar, new RightDeletingRule(1)));
				} else if (nounDeclension.equals("an")) {
					ls.addAll(gds.apply(lemma, an, new RightDeletingRule(1)));
				}
			} else {
				ls.addAll(gds.apply(lemma, longa, new RightDeletingRule(1)));
				ls.addAll(gds.apply(lemma, ar, new RightDeletingRule(1)));
				ls.addAll(gds.apply(lemma, an, new RightDeletingRule(1)));	
			}
			list = ls;
		} else if (lemma.endsWith("ī")) {
			list = gds.apply(lemma, longi, new RightDeletingRule(1));
		} else if (lemma.endsWith("ū")) {
			list = gds.apply(lemma, longu, new RightDeletingRule(1));
		} else if (lemma.endsWith("aṃ")) {
			list = gds.apply(lemma, ant, new RightDeletingRule(2));
		}
		// if (useAffixes)
		// 	list.addAll(AffixStrategy.apply(list));
		return list;
	}
	
	private boolean can(Paradigm p, String gender) {
		return p.getParadigmByFeatures(new FeatureSet("gender", gender)) != null;
	}
}
