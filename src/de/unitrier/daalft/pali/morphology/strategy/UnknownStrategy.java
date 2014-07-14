package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.List;

import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;

public class UnknownStrategy extends AbstractStrategy {

	@Override
	public List<ConstructedWord> apply(ILogInterface log, String lemma, String... options) {
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		ConstructedWord cw = new ConstructedWord(lemma);
		cw.addFeature(new Feature("paradigm", "unknown"));
		cw.setLemma(lemma);
		out.add(cw);
		return out;
	}

}
