package de.unitrier.daalft.pali.morphology.strategy;

import java.util.ArrayList;
import java.util.List;

import de.general.log.*;

import de.unitrier.daalft.pali.morphology.element.ConstructedWord;


/**
 * The null-strategy exhibits predictable behavior in all cases
 * <p>
 * For every input, returns null.
 * @author David
 *
 */
public class NullStrategy extends AbstractStrategy {

	@Override
	public List<ConstructedWord> apply(ILogInterface log, String lemma, String... options) {
		return new ArrayList<ConstructedWord>();
	}

}
