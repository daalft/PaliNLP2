package de.unitrier.daalft.pali.ngram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.cl.dictclient.DictWord;
import de.unitrier.daalft.pali.general.Alphabet;
import de.unitrier.daalft.pali.lexicon.LexiconAdapter;
import de.unitrier.daalft.pali.tools.Segmenter;

public class NGramExtractor {

	private Map<String, Integer> unigram, bigram, trigram, tetragram;
	private final static String unigramOut = "unigram_lower_all.data", bigramOut = "bigram_lower_all.data", trigramOut = "trigram_lower_all.data", tetragramOut = "tetragram_lower_all.data";
	private final static boolean debug = false;

	public NGramExtractor () {
		unigram = new HashMap<String, Integer>();
		bigram = new HashMap<String, Integer>();
		trigram = new HashMap<String, Integer>();
		tetragram = new HashMap<String, Integer>();
	}

	public void extract (String path) throws IOException {

	}

	private void writeError (List<DictWord> l) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("error.out"),true));
		for(DictWord dw : l)
			bw.write(dw.toJSON());
		bw.close();
	}
	public void extractServer (String server) throws Exception {
		LexiconAdapter la = new LexiconAdapter();
		List<DictWord> l = la.getAllWordforms();
		List<DictWord> fault = new ArrayList<DictWord>();
		for (DictWord dw : l) {
			String word = "";
			if (dw.getPropertyStringValueNormalized("word")!=null) {
				word = dw.getPropertyStringValueNormalized("word");
			} else if (dw.getPropertyStringValueNormalized("lemma")!=null) {
				word = dw.getPropertyStringValueNormalized("lemma");
			}
			else if (dw.getPropertyStringValueNormalized("form.lemma")!=null) {
				word = dw.getPropertyStringValueNormalized("form.lemma");
			} else if (dw.getValue("lemma")!=null) {
				word = dw.getValue("lemma").toString();
			} else if (dw.getValue("form.lemma") != null) {
				word = dw.getValue("form.lemma").toString();

			} else {
				System.err.println(dw.toJSON());
				fault.add(dw);
			}
			int freq = dw.getPropertyIntValue("frequency") == null ? 1 : dw.getPropertyIntValue("frequency");
			//System.out.println(word + " " + freq);
			calculateNGram(word, 1, freq);
			//System.out.println("Done unigrams");
			calculateNGram(word, 2, freq);
			//System.out.println("Done bigrams");
			calculateNGram(word, 3, freq);//System.out.println("Done trigrams");

			calculateNGram(word, 4, freq);//System.out.println("Done tetragrams");
		}
		writeError(fault);
	}
	private void calculateNGram (String line, int n, int f) {

		String word = "§"+line.trim().toLowerCase()+"*";
		if (word.equals("§*"))
			return;
		String[] segments = Segmenter.segmentToArray(word);
		if (n > segments.length-2) {
			if (debug)
				System.out.println("Ignoring word " + word + " for " + n + "-gram count");
			return;
		}
		BUILDER:for (int j = 0; j < segments.length-(n-1); j++) {
			StringBuilder sb = new StringBuilder();
			for (int k = 0; k < n; k++) {
				String c = segments[j+k];
				// ignore words containing non-valid characters except start and end
				if (!Alphabet.contains(c) && !c.equals("*") && !c.equals("§")) {
					break BUILDER;
				}
				sb.append(segments[j+k]);
			}
			increaseCount(sb.toString(), n, f);
		}
	}

	private void increaseCount (String s, int n, int f) {
		switch (n) {
		case 1: unigram.put(s, unigram.containsKey(s) ? (unigram.get(s)+f) : f); break;
		case 2: bigram.put(s, bigram.containsKey(s) ? (bigram.get(s)+f) : f); break;
		case 3: trigram.put(s, trigram.containsKey(s) ? (trigram.get(s)+f) : f); break;
		case 4: tetragram.put(s, tetragram.containsKey(s) ? (tetragram.get(s) + f) : f); break;
		default: break;
		}
	}

	public void run (String path) throws Exception {

		extractServer(path);

		writeOutput();
	}

	private void writeOutput () throws IOException {
		int unigramTotal = 0;
		for (Integer i : unigram.values())
			unigramTotal += i;
		int bigramTotal = 0;
		for (Integer i : bigram.values()) {
			bigramTotal += i;
		}
		int trigramTotal = 0;
		for (Integer i : trigram.values()) {
			trigramTotal += i;
		}
		int tetragramTotal = 0;
		for (Integer i : tetragram.values()) {
			tetragramTotal += i;
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(unigramOut)));
		for (Entry<String, Integer> e : unigram.entrySet()) {
			bw.write(e.getKey() + "\t" +e.getValue()+"\t" +((double)e.getValue())/unigramTotal);
			bw.newLine();
		}
		bw.flush();
		bw.close();
		bw = new BufferedWriter(new FileWriter(new File(bigramOut)));
		for (Entry<String, Integer> e : bigram.entrySet()) {
			bw.write(e.getKey() + "\t" +e.getValue()+"\t" +((double)e.getValue())/bigramTotal);
			bw.newLine();
		}
		bw.flush();
		bw.close();
		bw = new BufferedWriter(new FileWriter(new File(trigramOut)));
		for (Entry<String, Integer> e : trigram.entrySet()) {
			bw.write(e.getKey() + "\t" +e.getValue()+"\t"+ ((double)e.getValue())/trigramTotal);
			bw.newLine();
		}
		bw.flush();
		bw.close();
		bw = new BufferedWriter(new FileWriter(new File(tetragramOut)));
		for (Entry<String, Integer> e : tetragram.entrySet()) {
			bw.write(e.getKey() + "\t"+e.getValue()+"\t" + ((double)e.getValue())/tetragramTotal);
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}

	public static void main(String[] args) {
		try {
			new NGramExtractor().run("c:/users/david/documents/palidata");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
