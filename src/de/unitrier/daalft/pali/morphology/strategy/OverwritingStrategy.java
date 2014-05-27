package de.unitrier.daalft.pali.morphology.strategy;

import java.util.List;

import de.general.json.JProperty;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;

public class OverwritingStrategy extends AbstractStrategy {

	@Override
	public List<ConstructedWord> apply(String lemma, String... options) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ConstructedWord> apply (String lemma, AbstractStrategy main, List<JProperty> over, String... options)  {
		List<ConstructedWord> words = main.apply(lemma, options);
		for (ConstructedWord cw : words) {
			for (JProperty jp : over) {
				if (cw.getFeatureSet().contains(new Feature(jp.getName(), jp.getValue().toString()))) {
					
				}
			}
		}
		return words;
	}

}
