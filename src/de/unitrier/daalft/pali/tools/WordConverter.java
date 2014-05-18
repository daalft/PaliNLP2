package de.unitrier.daalft.pali.tools;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.cl.dictclient.DictWord;
import de.general.json.JObject;
import de.general.json.JsonSerializer;
import de.unitrier.daalft.pali.lexicon.DictWordN;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.Feature;
import de.unitrier.daalft.pali.phonology.element.SplitResult;

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
	 * Converts the given JSON String to DictWord
	 * @param s json string
	 * @return DictWord
	 * @throws Exception
	 */
	public static DictWord toDictWord(String s) throws Exception {
		JObject jo = js.deserialize(s);
		return new DictWordN(jo);
	}
	
	public static JObject toJObject (String json) throws Exception {
		return js.deserialize(json);
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
	public static List<String> toJSONStringList(List<ConstructedWord> list)
			throws JsonProcessingException {
		List<String> out = new ArrayList<String>();
		ObjectMapper om = new ObjectMapper();
		ObjectNode toplevel = om.createObjectNode();
		
		ConstructedWord first = list.remove(0);
		
		
		toplevel.put("lemma", first.getLemma());
		
		ObjectNode forms = toplevel.putObject("forms");
		 
		
		ArrayNode array = forms.putArray(first.getInfo().getFeature("paradigm"));
		
		ObjectNode content = array.addObject();
		content.put("word", first.getWord());
		ObjectNode grammar = content.putObject("grammar");
		for (Feature f : first.getInfo()) {
			if (f.getKey().equals("paradigm"))
				continue;
			grammar.put(f.getKey(), f.getValue());
		}
		
		for (ConstructedWord cw : list) {
			ObjectNode c = array.addObject();	
			c.put("word", cw.getWord());
			ObjectNode g = c.putObject("grammar");
			for (Feature f : cw.getInfo()) {
				if (f.getKey().equals("paradigm"))
					continue;
				g.put(f.getKey(), f.getValue());
			}	
			
			//om = null;
		}
		out.add(om.writeValueAsString(toplevel));
		return out;
	}	
	
	public static String toJSONStringGenerator(List<ConstructedWord> list) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		ObjectNode toplevel = om.createObjectNode();
		
		ConstructedWord first = list.remove(0);
		
		
		toplevel.put("lemma", first.getLemma());
		
		ObjectNode forms = toplevel.putObject("forms");
		 
		
		ArrayNode array = forms.putArray(first.getInfo().getFeature("paradigm"));
		
		ObjectNode content = array.addObject();
		content.put("word", first.getWord());
		ObjectNode grammar = content.putObject("grammar");
		for (Feature f : first.getInfo()) {
			if (f.getKey().equals("paradigm"))
				continue;
			grammar.put(f.getKey(), f.getValue());
		}
		
		for (ConstructedWord cw : list) {
			ObjectNode c = array.addObject();	
			c.put("word", cw.getWord());
			ObjectNode g = c.putObject("grammar");
			for (Feature f : cw.getInfo()) {
				if (f.getKey().equals("paradigm"))
					continue;
				g.put(f.getKey(), f.getValue());
			}	
			
			//om = null;
		}
		return om.writeValueAsString(toplevel);
		
	}

	public static String toJSONStringLemmatizer(List<ConstructedWord> list) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		ObjectNode toplevel = om.createObjectNode();
		ConstructedWord first = list.remove(0);
		toplevel.put("word", first.getWord());
		ObjectNode forms = toplevel.putObject("forms");
		String initialPos = first.getInfo().getFeature("paradigm");
		ArrayNode array = forms.putArray(initialPos);
		ObjectNode content = array.addObject();
		content.put("lemma", first.getLemma());
		for (ConstructedWord cw : list) {
			if (!cw.getInfo().getFeature("paradigm").equals(initialPos)) {
				array = forms.putArray(cw.getInfo().getFeature("paradigm"));
			}
			ObjectNode c = array.addObject();	
			c.put("lemma", cw.getLemma());
		}
		return om.writeValueAsString(toplevel);
	}

	public static String toJSONStringAnalyzer(List<ConstructedWord> list) throws JsonProcessingException {
		List<String> analyses = new ArrayList<String>();
		ObjectMapper om = new ObjectMapper();
		ObjectNode toplevel = om.createObjectNode();
		
		ConstructedWord first = list.remove(0);
		
		String initialLemma = first.getLemma();
		
		toplevel.put("lemma", initialLemma);
		
		ObjectNode forms = toplevel.putObject("forms");
		 
		String initialPos = first.getInfo().getFeature("paradigm");
		ArrayNode array = forms.putArray(initialPos);
		
		ObjectNode content = array.addObject();
		content.put("word", first.getWord());
		ObjectNode grammar = content.putObject("grammar");
		for (Feature f : first.getInfo()) {
			if (f.getKey().equals("paradigm"))
				continue;
			grammar.put(f.getKey(), f.getValue());
		}
		
		for (ConstructedWord cw : list) {
			if (!cw.getInfo().getFeature("paradigm").equals(initialPos)) {
				array = forms.putArray(cw.getInfo().getFeature("paradigm"));
			}
			if (!cw.getLemma().equals(initialLemma)) {
				analyses.add(om.writeValueAsString(toplevel));
				om = new ObjectMapper();
				toplevel = om.createObjectNode();
				
				toplevel.put("lemma", cw.getLemma());
				
				forms = toplevel.putObject("forms");
				 
				initialPos = cw.getInfo().getFeature("paradigm");
				array = forms.putArray(initialPos);
				initialLemma = cw.getLemma();
			}
			ObjectNode c = array.addObject();	
			c.put("word", cw.getWord());
			ObjectNode g = c.putObject("grammar");
			for (Feature f : cw.getInfo()) {
				if (f.getKey().equals("paradigm"))
					continue;
				g.put(f.getKey(), f.getValue());
			}	
		}
		
		analyses.add(om.writeValueAsString(toplevel));
		StringBuilder sb = new StringBuilder("[");
		for (String a : analyses) {
			sb.append(a).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}

	public static String toJSONStringStemmer(String word, List<String> list) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		ObjectNode toplevel = om.createObjectNode();
		toplevel.put("word", word);
		ObjectNode forms = toplevel.putObject("forms");
		
		ArrayNode array = forms.putArray("stems");
		
		for (String cw : list) {
			ObjectNode c = array.addObject();	
			c.put("stem", cw);
		}
		return om.writeValueAsString(toplevel);

	}

	public static String toJSONStringMerger(String word, String word2, List<String> list) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		ObjectNode toplevel = om.createObjectNode();
		toplevel.put("word", word);
		toplevel.put("word2", word2);
		ObjectNode forms = toplevel.putObject("forms");
		
		ArrayNode array = forms.putArray("merged");
		
		for (String cw : list) {
			ObjectNode c = array.addObject();	
			c.put("form", cw);
		}
		return om.writeValueAsString(toplevel);
		
	}

	public static String toJSONStringSplitter(String word, List<SplitResult> list) throws Exception {
		ObjectMapper om = new ObjectMapper();
		ObjectNode toplevel = om.createObjectNode();
		toplevel.put("word", word);
		
		ObjectNode forms = toplevel.putObject("forms");
		
		ArrayNode splits = forms.putArray("splits");
		
		for (SplitResult cw : list) {
			ObjectNode node = splits.addObject();
			ArrayNode words = node.putArray("words");	
			for (String w : cw.getSplit())
				words.add(w);
			node.put("confidence", cw.getConfidence());
			
		}
		return om.writeValueAsString(toplevel);
		
	}
}
