package de.unitrier.daalft.pali.morphology.paradigm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;


import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.element.OccurrenceManager;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.Irregular;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNouns;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNumerals;
import de.unitrier.daalft.pali.tools.StackReader;

/**
 * Class used to read paradigms from file
 * @author David
 *
 */
public class ParadigmReader
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	private final static String DEFAULT_PARADIGMS_FILEPATH = "./data/grammar/fullGrammar5.xml";
	private final static String IRREGULAR_NOUNS_FILEPATH = "./data/grammar/irregularNoun.txt";
	private final static String IRREGULAR_NUMERALS_FILEPATH = "./data/grammar/irregularNumerals.txt";

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	/**
	 * Paths to file
	 */
	private String paradigmsCfgFilePath;

	/**
	 * Paradigms
	 */
	private Paradigm paradigms;

	/**
	 * Irregular noun handler
	 */
	private static IrregularNouns irrNoun;
	/**
	 * Irregular numeral handler
	 */
	private static IrregularNumerals irrNum;
	/**
	 * Irregular paths
	 */

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	/**
	 * No-argument constructor
	 */
	public ParadigmReader () throws Exception
	{
		this(DEFAULT_PARADIGMS_FILEPATH);
	}

	/**
	 * Constructor
	 * @param paradigmsCfgFilePath path to grammar file
	 */
	public ParadigmReader (String paradigmsCfgFilePath) throws Exception
	{
		this.paradigmsCfgFilePath = paradigmsCfgFilePath;
		init();
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	/**
	 * Initializer
	 */
	private void init() throws Exception
	{
		paradigms = new Paradigm();
		irrNoun = new IrregularNouns();
		irrNum = new IrregularNumerals();
		read();
		readIrregular(IRREGULAR_NOUNS_FILEPATH, irrNoun);
		readIrregular(IRREGULAR_NUMERALS_FILEPATH, irrNum);
	}

	/**
	 * Reads irregular paradigm files
	 * @param filename file containing irregular paradigm
	 * @param i irregular handler object
	 * @throws IOException
	 */
	private void readIrregular (String filename, Irregular i) throws IOException { 
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
		String l = "";
		Paradigm current = new Paradigm();
		FeatureSet featMap = new FeatureSet();
		while ((l = br.readLine())!=null) {
			if (l.equals("=")) {
				i.add(current);
				current = new Paradigm();
				featMap = new FeatureSet();
				continue;
			}
			String features = l.substring(l.indexOf('{')+1, l.indexOf('}'));
			Morph morph = new Morph(l.substring(0, l.indexOf('{')));
			for (String feat : features.split(",")) {
				String attribute = feat.split("=")[0].trim();
				String value = feat.split("=")[1].trim();
				featMap.add(new Feature(attribute, value));
			}
			current.add(new Morpheme(featMap, morph));
			featMap = new FeatureSet();
		}
		br.close();
	}

	/**
	 * Reads a file and builds paradigms
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException 
	 */
	private void read() throws ParserConfigurationException, SAXException, IOException, TransformerException
	{
		File file = new File(paradigmsCfgFilePath);
		StackReader sr = new StackReader();
		paradigms = buildParadigm(sr.readFile(file), sr.getDelimiter(), sr.getOccurrenceDelimiter());
	}

	/**
	 * Builds paradigms from a list as returned by the 
	 * stack reader
	 * @param list list
	 * @param delim delimiter
	 * @return paradigms
	 */
	private Paradigm buildParadigm(List<String> list, String delim, String odelim) {
		Paradigm output = new Paradigm();
		for (String s : list) {		
			FeatureSet map = new FeatureSet();
			String[] info = s.split(delim);
			String morph = info[0];
			Morph m = null;
			if (morph.contains(odelim)) {
				m = new Morph(morph.split(odelim)[0], OccurrenceManager.getOccurrence(morph.split(odelim)[1]));
			} else {
				m = new Morph(morph);
			}
			// create map
			for (int i = 1; i < info.length; i++) {
				String[] keyvalue = info[i].split("=");
				if (keyvalue.length > 1) {
					map.add(new Feature(keyvalue[0].split(" ")[0], keyvalue[1].replaceAll("\"", "")));
				} else 
				{
					map.add(new Feature("subtype", info[i]));
				}
			}
			output.add(new Morpheme(copy(map), m));
		}
		return output;
	}

	/**
	 * Creates a deep copy of a feature set
	 * @param fs feature set
	 * @return deep copy
	 */
	private FeatureSet copy (FeatureSet fs) {
		FeatureSet copy = new FeatureSet();
		for (Feature f : fs) {
			copy.add(f);
		}
		return copy;
	}
	
	/**
	 * Returns a Paradigm that is a subset of all paradigms that satisfy the constraints
	 * specified by the feature map
	 * @param features feature map
	 * @return paradigms
	 */
	public Paradigm getParadigmsByFeatures (FeatureSet features) {
		Paradigm p = paradigms.getParadigmByFeatures(features);
		if (!( p == null) && !p.isEmpty())
			return p;

		return null;
	}

	/**
	 * Returns the generated paradigm
	 * @return paradigms
	 */
	public Paradigm getParadigm () {
		return paradigms;
	}

	/**
	 * Returns the full noun paradigm
	 * @return noun paradigm
	 */
	public Paradigm getNounParadigm () {
		FeatureSet map = new FeatureSet("paradigm", "noun");
		Paradigm p = getParadigmsByFeatures(map);
		return p;
	}

	/**
	 * Returns the full pronoun paradigm
	 * @return pronoun paradigm
	 */
	public Paradigm getPronounParadigm () {
		FeatureSet map = new FeatureSet("paradigm", "pronoun");
		Paradigm p = getParadigmsByFeatures(map);
		return p;
	}

	/**
	 * Returns the full verb paradigm
	 * @return verb paradigm
	 */
	public Paradigm getVerbParadigm () {
		FeatureSet map = new FeatureSet("paradigm", "verb");
		Paradigm p = getParadigmsByFeatures(map);

		return p;
	}

	/**
	 * Returns the full suffix paradigm
	 * @return suffix paradigm
	 */
	public Paradigm getSuffixParadigm () {
		FeatureSet map = new FeatureSet("paradigm", "affix");
		map.add(new Feature("subtype", "suffix"));
		Paradigm p = getParadigmsByFeatures(map);

		return p;
	}

	/**
	 * Returns the full prefix paradigm
	 * @return prefix paradigm
	 */
	public Paradigm getPrefixParadigm () {
		FeatureSet map = new FeatureSet("paradigm", "affix");
		map.add(new Feature("subtype", "prefix"));
		return getParadigmsByFeatures(map);
	}
	
	/**
	 * Returns the full numeral paradigm
	 * @return numeral paradigm
	 */
	public Paradigm getNumeralParadigm () {
		FeatureSet map = new FeatureSet("paradigm", "numeral");
		Paradigm p = getParadigmsByFeatures(map);

		return p;
	}

	/**
	 * Returns the irregular noun paradigms
	 * @return irregular noun paradigms
	 */
	public IrregularNouns getIrregularNouns () {
		return irrNoun;
	}

	/**
	 * Returns the irregular numeral paradigms
	 * @return irregular numeral paradigm
	 */
	public IrregularNumerals getIrregularNumerals () {
		return irrNum;
	}

	/**
	 * Returns the adjective paradigm
	 * @return adjective paradigm
	 */
	public Paradigm getAdjectiveParadigm() {
		return paradigms.getParadigmByFeatures(new FeatureSet("paradigm", "adjective"));
	}
}
