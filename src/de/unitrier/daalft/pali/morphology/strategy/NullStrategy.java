package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.List;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;

/**
 * The null-strategy exhibits predictable behavior in all cases
 * <p>
 * For every input, nothing happens.
 * @author David
 *
 */
public class NullStrategy extends AbstractStrategy {

	@Override
	public List<ConstructedWord> apply(String lemma, String... options) {
		List<ConstructedWord> list = 
		new ArrayList<ConstructedWord>();
		ConstructedWord cw = new ConstructedWord();
		cw.setWord(lemma);
		cw.setLemma(lemma);
		cw.addInfo(new Feature("pos", "unknown"));
		list.add(cw);
		
		return list;
	}

}
