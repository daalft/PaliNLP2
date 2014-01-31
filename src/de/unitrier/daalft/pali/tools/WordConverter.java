package de.unitrier.daalft.pali.tools;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lu.cl.dictclient.DictWord;
import lu.general.json.JObject;
import lu.general.json.JsonSerializer;
import de.unitrier.daalft.pali.lexicon.DictWordN;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;

/**
 * Converts ConstructedWord to different other types
 * 
 * @author David
 * 
 */
public class WordConverter {
	private static JsonSerializer js = new JsonSerializer();
	/**
	 * Converts the given ConstructedWord to String
	 * 
	 * @param cw
	 *            constructed word
	 * @return string
	 */
	public static String toString(ConstructedWord cw) {
		return cw.toString();
	}

	/**
	 * Converts the given ConstructedWord to DictWord
	 * 
	 * @param cw
	 *            constructed word
	 * @return DictWord
	 * @throws Exception
	 */
	public static DictWord toDictWord(ConstructedWord cw) throws Exception {
		
		String json = toJSONString(cw);
		JObject jo = js.deserialize(json);
		DictWordN dwn = new DictWordN(jo);
		return dwn;
	}

	/**
	 * Converts the given list of ConstructedWord to list of DictWord
	 * 
	 * @param list
	 *            list of constructed words
	 * @return list of dict words
	 * @throws Exception
	 */
	public static List<DictWord> toDictWord(List<ConstructedWord> list)
			throws Exception {
		List<DictWord> out = new ArrayList<DictWord>();
		for (ConstructedWord cw : list)
			out.add(toDictWord(cw));
		return out;
	}

	/**
	 * Converts the given constructed word to JSON string
	 * 
	 * @param cw
	 *            constructed word
	 * @return JSON string
	 * @throws JsonProcessingException
	 */
	public static String toJSONString(ConstructedWord cw)
			throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		ObjectNode toplevel = om.createObjectNode();
		toplevel.put("word", cw.getWord());
		ObjectNode grammar = toplevel.putObject("grammar");
		ObjectNode morphology = grammar.putObject("morphology");
		morphology.put("lemma", cw.getLemma());
		ObjectNode paradigm = morphology.putObject("information");
		for (Feature f : cw.getInfo()) {
			paradigm.put(f.getKey(), f.getValue());
		}
		String mys = om.writeValueAsString(toplevel);
		return mys;
	}
	
	/**
	 * Converts the given list of constructed words to list of JSON strings
	 * 
	 * @param list
	 *            list of constructed words
	 * @return list of JSON strings
	 * @throws JsonProcessingException
	 */
	public static List<String> toJSONString(List<ConstructedWord> list)
			throws JsonProcessingException {
		List<String> out = new ArrayList<String>();
		for (ConstructedWord cw : list) {
			ObjectMapper om = new ObjectMapper();
			ObjectNode toplevel = om.createObjectNode();
			toplevel.put("word", cw.getWord());
			ObjectNode grammar = toplevel.putObject("grammar");
			ObjectNode morphology = grammar.putObject("morphology");
			morphology.put("lemma", cw.getLemma());
			ObjectNode paradigm = morphology.putObject("paradigm");
			if (cw.getInfo() == null)
				System.out.println("No info");
			for (Feature f : cw.getInfo()) {
				System.out.println(f);
				paradigm.put(f.getKey(), f.getValue());
			}
			out.add(om.writeValueAsString(toplevel));
			om = null;
		}
		return out;
	}	
}
