package de.unitrier.daalft.pali.morphology.element;
/**
 * Information about the distributional occurrence (frequency)
 * @author David
 *
 */
public class DistributionOccurrence extends Occurrence {

	/**
	 * Constructor
	 * @param in information
	 */
	public DistributionOccurrence(String in) {
		super(in);
	}

	@Override
	public String apply(String word) {
		// return the word as-is with additional occurrence information?
		return word;
	}

	@Override
	public ConstructedWord apply(ConstructedWord cw) {
		
		Feature f = new Feature("frequency", occ);
		if (!cw.getFeatureSet().contains(f))
			cw.addFeature(f);
		
		return cw;
	}	
}
