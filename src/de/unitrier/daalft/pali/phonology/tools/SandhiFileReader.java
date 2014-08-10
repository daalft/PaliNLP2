package de.unitrier.daalft.pali.phonology.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import de.general.simpletokenizing.Token;
import de.general.simpletokenizing.TokenValidator;
import de.general.simpletokenizing.TokenValidator.ShortType;
import de.unitrier.daalft.pali.phonology.element.Rule;
import de.unitrier.daalft.pali.phonology.tools.interfaces.LineReader;

public class SandhiFileReader {

	private LineReader lr;
	private String commentCharacter = "#";
	
	public SandhiFileReader() {
		lr = new SandhiLineReader();
	}
	
	public SandhiFileReader(String commentChar) {
		this();
		commentCharacter = commentChar;
	}
	
	public List<Rule> parse (File file) throws Exception {
		List<Rule> out = new ArrayList<Rule>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = br.readLine()) != null) {
			
			// ignore empty and whitespace lines
			if (line.trim().length() == 0) {
				continue;
			}
			// ignore comment lines
			if (line.startsWith(commentCharacter)) {
				continue;
			}
			// remove inline comments
			if (line.contains(commentCharacter)) {
				line = line.split(commentCharacter)[0].trim();
			}
			
			List<Token> ret = lr.parse(line);
			
			// remove EOS token
			ret.remove(ret.size()-1);
			
			if (ret.size() < 3) {
				br.close();
				throw new Exception("Malformed rule exception!");
			}
			if (!TokenValidator.is(ShortType.CDC, ret)) {
				br.close();
				throw new Exception("Malformed rule exception!");
			}
			out.add(new Rule(ret));
		}
		br.close();
		return out;
	}
	
	public List<Rule> parse (String filename) throws Exception {
		return parse(new File(filename));
	}
}
