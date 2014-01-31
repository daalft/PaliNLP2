package de.unitrier.daalft.pali.morphology.element;
/**
 * Manages Occurrence Objects
 * @author David
 *
 */
public class OccurrenceManager {

	/**
	 * Returns a matching Occurrence Object based on 
	 * the type of occurrence
	 * <p>
	 * Occurrence information should start with<br/>
	 * <b>D</b> for <em>distributional occurrence</em><br/>
	 * <b>R</b> for <em>restricting occurrence</em><br/>
	 * <b>C</b> for <em>changing occurrence</em><br/>
	 * <br/><br/>
	 * Returns a NullOccurrence if no matching occurrence can
	 * be determined
	 * @param in occurrence information
	 * @return Occurrence Object
	 */
	public static Occurrence getOccurrence (String in) {
		String occ = in.substring(1);
		if (in.startsWith("D"))
			return new DistributionOccurrence(occ);
		if (in.startsWith("R"))
			return new RestrictingOccurrence(occ);
		if (in.startsWith("C"))
			return new ChangingOccurrence(occ);
		return new NullOccurrence(occ);
	}
}
