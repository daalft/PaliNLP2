package de.unitrier.daalft.pali.phonology.tools.interfaces;

import java.util.List;

import de.general.simpletokenizing.Token;

public interface LineReader {

	public List<Token> parse(String line);
	
}
