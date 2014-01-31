package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.phonology.SandhiMerge;
/**
 * Class implementing functionality related to affixes
 * @author David
 *
 */
public class AffixStrategy extends AbstractStrategy {

	/**
	 * Appends prefixes and suffixes to each element of the input list
	 * @param list list
	 * @return list with affixes
	 */
	public static List<ConstructedWord> apply (List<ConstructedWord> list) {
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		ParadigmAccessor pa = new ParadigmAccessor();
		Paradigm prefix = pa.getPrefixParadigm();
		Paradigm suffix = pa.getSuffixParadigm();
		for (Morpheme mo : suffix.getMorphemes()) {
			for (Morph m : mo.getAllomorphs()) {
				for (ConstructedWord cws : list) {
					Set<String> merges = new HashSet<String>(SandhiMerge.merge(cws.getWord(), m.getMorph()));
					for (String s : merges) {
						ConstructedWord copy = new ConstructedWord(s, union(cws.getInfo(), mo.getFeatureSet()));
						copy.setLemma(cws.getLemma());
						out.add(copy);
					}
				}
			}
		}
		for (Morpheme mo : prefix.getMorphemes()) {
			for (Morph m : mo.getAllomorphs()) {
				for (ConstructedWord cws : list) {
					Set<String> merges = new HashSet<String>(SandhiMerge.merge(m.getMorph(),cws.getWord()));
					for (String s : merges) {
						ConstructedWord copy = new ConstructedWord(s, union(cws.getInfo(), mo.getFeatureSet()));
						copy.setLemma(cws.getLemma());
						out.add(copy);
					}
				}
			}
		}
		return out;
	}

	/**
	 * <b>NOTE</b>:This strategy doesn't use the override method. Instead it uses its own
	 * method that takes a list as input. See {@link #apply(List)}
	 * @see #apply(List)
	 */
	@Override
	public List<ConstructedWord> apply(String lemma, String... options) {
		// unused method
		return null;
	}
}
