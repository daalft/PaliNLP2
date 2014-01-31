package de.unitrier.daalft.pali.morphology.paradigm.rule;

public class ReplacingRule implements DerivingRule {

	private String replace;
	
	public ReplacingRule (String rep) {
		replace = rep;
	}
	
	@Override
	public String apply(String lemma) {
		return replace;
	}

}
