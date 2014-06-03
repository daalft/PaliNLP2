package de.unitrier.daalft.pali.morphology.strategy;
/**
 * Class that manages strategies
 * @author David
 *
 */
public class StrategyManager {

	private static boolean debug = false;
	
	/**
	 * Returns a word class specific strategy
	 * @param wc word class
	 * @return strategy
	 */
	public static AbstractStrategy getStrategy(String wc) {
		if (wc.equals("noun"))
			return new NounStrategy();
		if (wc.equals("verb"))
			return new AlternativeVerbStrategy();
		if (wc.equals("adjective"))
			return new AdjectiveStrategy();
		if (wc.equals("numeral"))
			return new NumeralStrategy();
		if (wc.equals("adverb"))
			return new AdverbStrategy();
		if (wc.equals("pronoun"))
			return new PronounStrategy();
		if (debug)
			System.err.println("Could not find strategy for " + wc);
		return new NullStrategy();
	}
}
