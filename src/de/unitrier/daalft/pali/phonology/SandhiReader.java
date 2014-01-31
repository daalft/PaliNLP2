package de.unitrier.daalft.pali.phonology;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.unitrier.daalft.pali.phonology.element.ConstantDefinition;
import de.unitrier.daalft.pali.phonology.element.MethodDefinition;
import de.unitrier.daalft.pali.phonology.element.MyDictionary;
import de.unitrier.daalft.pali.phonology.element.SandhiRule;
/**
 * Class responsible for reading and storing sandhi information
 * @author David
 *
 */
public class SandhiReader {
	/**
	 * Sandhi rules
	 */
	private List<SandhiRule> rules, revRules, soundRules;
	/**
	 * Dictionary
	 */
	private MyDictionary md;
	/**
	 * Default paths
	 */
	private final static String defaultDictPath = "./data/sandhi/sandhiDictionary.in",
			defaultMergePath = "./data/sandhi/sandhiMerge.in",
			defaultSplitPath = "./data/sandhi/sandhiSplit.in",
			defaultSoundPath = "./data/sandhi/sandhiSound.in";
	
	/**
	 * Constructor
	 */
	public SandhiReader () {
		rules = new ArrayList<SandhiRule>();
		revRules = new ArrayList<SandhiRule>();
		soundRules = new ArrayList<SandhiRule>();
		md = new MyDictionary();
	}

	/**
	 * Runs the SandhiReader
	 * <p>
	 * Running the SandhiReader results in sandhi files
	 * being read and the information therein saved by the reader
	 * for later retrieval. The paths to the files (dictionary, merging, splitting)
	 * can be specified. If a path is not specified (passed as <b>null</b>), the default
	 * path value will be used (./data/sandhi/FILENAME)
	 * @param dict path to dictionary
	 * @param merge path to merge file
	 * @param split path to split file
	 * @throws IOException
	 */
	public void run (String dict, String merge, String split, String sound) throws IOException {
		String dictPath = dict==null?defaultDictPath:dict;
		String mergePath = merge==null?defaultMergePath:merge;
		String splitPath = split==null?defaultSplitPath:split;
		String soundPath = sound==null?defaultSoundPath:sound;
		// dict file does not need a list and should not contain
		// anything that would trigger the list.add function (otherwise: Null exception)
		read(dictPath, null);
		read(mergePath, rules);
		read(splitPath, revRules);
		read(soundPath, soundRules);
	}
	
	/**
	 * Reads the specified file into the specified list
	 * @param path path
	 * @param list list
	 * @throws IOException
	 */
	private void read (String path, List<SandhiRule> list) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
		String l = "";
		while ((l=br.readLine())!=null) {
			// ignore comments
			if (l.startsWith("#"))
				continue;
			// ignore empty/whitespace lines
			if (l.isEmpty() || l.matches("\\s+"))
				continue;
			// if line is constant definition
			if (l.startsWith("=")) {
				md.add(new ConstantDefinition(l.substring(1)));
			}
			// if line is method definition
			else if (l.startsWith("+")) {
				md.add(new MethodDefinition(l.substring(1)));
			} else {
				// create new rule
				list.add(new SandhiRule(l));
				
			}
		}
		br.close();
	}
	
	/**
	 * Returns merging rules
	 * @return merging rules
	 */
	public List<SandhiRule> getRules () {
		return rules;
	}
	
	/**
	 * Returns splitting rules
	 * @return splitting rules
	 */
	public List<SandhiRule> getReverseRules () {
		return revRules;
	}
	
	/**
	 * Returns sound replacing rules
	 * @return sound replacing rules
	 */
	public List<SandhiRule> getSoundRules () {
		return soundRules;
	}
	
	/**
	 * Returns dictionary
	 * @return dictionary
	 */
	public MyDictionary getDictionary () {
		return md;
	}
}