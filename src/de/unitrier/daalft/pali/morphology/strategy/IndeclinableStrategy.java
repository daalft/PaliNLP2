package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.List;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;

public class IndeclinableStrategy extends AbstractStrategy {

	@Override
	public List<ConstructedWord> apply(String lemma, String... options) {
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		ConstructedWord cw = new ConstructedWord(lemma);
		cw.addFeature(new Feature("paradigm", "indeclinable"));
		cw.setLemma(lemma);
		//cw.addFeature(new Feature("subtype", "indeclinable"));
		out.add(cw);
		return out;
	}

}
