package de.unitrier.daalft.pali.phonology.tools;

import java.util.List;

import de.general.simpletokenizing.Token;
import de.general.simpletokenizing.Tokenizer;
import de.unitrier.daalft.pali.phonology.tools.interfaces.LineReader;

public class SandhiLineReader implements LineReader {

	private Tokenizer tokenizer;
	
	public SandhiLineReader() {
		tokenizer = new Tokenizer(false, 3, true, "āīūṃṅñṭḍṇḷ");
	}
	
	@Override
	public List<Token> parse(String line) {
		return tokenizer.tokenize(line);
	}
}
