package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.paradigm.rule.ReplacingRule;
import de.unitrier.daalft.pali.morphology.tools.VerbHelper;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.phonology.SoundChanger;
/**
 * Pre-configured strategy for verbs
 * @author David
 *
 */
public class VerbStrategy extends AbstractStrategy {

	private static boolean useAffixes = false;
	
	public List<ConstructedWord> apply(String lemma, String... options) {
		int declension = 0;
		if (options.length > 0 && !options[0].isEmpty()) {
			declension = Integer.parseInt(options[0]);
		}
		// General declension strategy
		GeneralDeclensionStrategy gds = new GeneralDeclensionStrategy();
		// Retrieve relevant paradigms
		ParadigmAccessor pa = new ParadigmAccessor();
		Paradigm verbs = pa.getVerbParadigm();

		// If verb lemma does not end with "ti", probably not a verb lemma
		if (!lemma.endsWith("ti")) {
			System.err.println("Could not apply verbal strategy to lemma " + lemma);
			return null;
		}
		// Calculate stem
		String stem0 = lemma.substring(0, lemma.length()-2);
		// Calculate root
		VerbHelper vh = new VerbHelper();
		List<String> roots = new ArrayList<String>(new LinkedHashSet<String>(vh.rootFromStem(stem0, declension)));
		// Recalculate stems from root
		LinkedHashSet<String> stems = new LinkedHashSet<String>();
		for (String root : roots) {
			stems.addAll(vh.stemFromRoot(root, declension));
		}
		// Generate stem- and root forms
		List<ConstructedWord> stemforms = new ArrayList<ConstructedWord>();
		List<ConstructedWord> rootforms = new ArrayList<ConstructedWord>();
		for (String root : roots) {
			System.out.println("Root: " + root);
			rootforms.addAll(gds.apply(lemma, verbs, new ReplacingRule(root)));
		}
		for (String stem : stems) {
			System.out.println("Stem: " + stem);
			stemforms.addAll(gds.apply(lemma, verbs, new ReplacingRule(stem)));
		}
		// Initialize output list
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		// Add all results so far
		out.addAll(rootforms);
		out.addAll(stemforms);
		if (useAffixes)
			// Add affixes
			out.addAll(AffixStrategy.apply(out));
		
		// Append additional forms derived by conversion "ava" => "o" and "aya" => e
		List<ConstructedWord> additional = new ArrayList<ConstructedWord>();
		SoundChanger sc = new SoundChanger();
		for (ConstructedWord cw : out) {
			ConstructedWord cwa = (new ConstructedWord(sc.getCommonChange(cw.getWord()), cw.getFeatureSet()));
			cwa.setLemma(cw.getLemma());
			if (!out.contains(cwa))
				additional.add(cwa);
		}
		out.addAll(additional);
		return new ArrayList<ConstructedWord>(new LinkedHashSet<ConstructedWord>(out));
	}
}
