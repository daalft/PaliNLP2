package de.unitrier.daalft.pali.morphology.paradigm;

import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNouns;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNumerals;
/**
 * Class handling access to paradigms
 * @author David
 *
 */
public class ParadigmAccessor {

	/**
	 * Paradigm Reader
	 */
	private ParadigmReader pr;
	
	/**
	 * Constructor
	 */
	public ParadigmAccessor () {
		pr = new ParadigmReader();
	}
	
	/**
	 * Returns a list of Paradigms that satisfy the constraints
	 * specified by the feature map
	 * @param features feature map
	 * @return paradigms
	 */
	public Paradigm getParadigmsByFeatures (FeatureSet features) {
		return pr.getParadigmsByFeatures(features);
	}
	
	/**
	 * Returns the generated paradigm
	 * @return paradigms
	 */
	public Paradigm getParadigms () {
		return pr.getParadigm();
	}
	
	/**
	 * Returns the full noun paradigm
	 * @return noun paradigm
	 */
	public Paradigm getNounParadigm () {
		return pr.getNounParadigm();
	}
	
	/**
	 * Returns the full pronoun paradigm
	 * @return pronoun paradigm
	 */
	public Paradigm getPronounParadigm () {
		return pr.getPronounParadigm();
	}
	
	/**
	 * Returns the full verb paradigm
	 * @return verb paradigm
	 */
	public Paradigm getVerbParadigm () {
		return pr.getVerbParadigm();
	}
	
	/**
	 * Returns the full suffix paradigm
	 * @return suffix paradigm
	 */
	public Paradigm getSuffixParadigm () {
		return pr.getSuffixParadigm();
	}
	
	/**
	 * Returns the full prefix paradigm
	 * @return prefix paradigm
	 */
	public Paradigm getPrefixParadigm () {
		return pr.getPrefixParadigm();
	}
	
	/**
	 * Returns the full numeral paradigm
	 * @return numeral paradigm
	 */
	public Paradigm getNumeralParadigm () {
		return pr.getNumeralParadigm();
	}
	
	/**
	 * Returns the irregular noun paradigms
	 * @return irregular noun paradigms
	 */
	public IrregularNouns getIrregularNouns () {
		return pr.getIrregularNouns();
	}
	
	/**
	 * Returns the irregular numeral paradigms
	 * @return irregular numeral paradigms
	 */
	public IrregularNumerals getIrregularNumerals () {
		return pr.getIrregularNumerals();
	}

	/**
	 * Returns the full adjective paradigm
	 * @return adjective paradigm
	 */
	public Paradigm getAdjectiveParadigm() {
		return pr.getAdjectiveParadigm();
	}

}
