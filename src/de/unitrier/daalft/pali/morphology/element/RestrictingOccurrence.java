package de.unitrier.daalft.pali.morphology.element;
/**
 * Class representing restricting occurrence objects
 * @author David
 *
 */
public class RestrictingOccurrence extends Occurrence {

	/**
	 * Constructor
	 * @param occ occurrence information
	 */
	public RestrictingOccurrence(String occ) {
		super(occ);
	}

	@Override
	public String apply(String word) {
		// cannot apply restriction to string
		return word;
	}

	@Override
	public ConstructedWord apply(ConstructedWord cw) {
		// TODO
		return cw;
	}

}
