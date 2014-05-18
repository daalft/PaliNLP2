package de.unitrier.daalft.pali.ngram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.unitrier.daalft.pali.tools.Segmenter;

public class NGramScorer {

	private String pathPrefix = "data/ngram/";
	private String uniPath = pathPrefix + "unigram_lower_all.data", 
			biPath = pathPrefix + "bigram_lower_all.data", 
			triPath = pathPrefix + "trigram_lower_all.data", 
			tetraPath = pathPrefix + "tetragram_lower_all.data";

	private Map<String, Double> unigram, bigram, trigram, tetragram;

	private double biBoost = 0.1, triBoost = 2.0, tetraBoost = 3.0;

	private static int debug = 0;

	private static NGramScorer instance = new NGramScorer();

	public static NGramScorer getInstance () {
		return instance;
	}

	private NGramScorer () {
		unigram = new HashMap<String, Double>();
		bigram = new HashMap<String, Double>();
		trigram = new HashMap<String, Double>();
		tetragram = new HashMap<String, Double>();
		if (debug > 1) {
			System.err.println("Scorer: Loading n-gram files");
		}
		loadFiles();
		if (debug > 1) {
			System.err.println("Scorer: Done loading n-gram files");
		}
	}

	private void loadFiles () {
		try {
			loadFile(uniPath, unigram);
			loadFile(biPath, bigram);
			loadFile(triPath, trigram);
			loadFile(tetraPath, tetragram);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadFile (String path, Map<String, Double> map) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String l = "";
		while ((l = br.readLine())!=null) {
			String[] kv = l.split("\t");
			map.put(kv[0], Double.parseDouble(kv[1]));
		}
		br.close();
	}

	public double ngramScore (String word, int n) {
		String[] segments = Segmenter.segmentToArray(word);
		double score = 1.0;
		if (segments.length < 3) {
			if (debug > 0)
				System.err.println("Could not apply " + n + "-gram analysis to word [" + word + "]");
			return score;
		}
		int slots = segments.length - (n - 1);
		for (int i = 0; i < slots; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < n; j++) {
				sb.append(segments[i+j]);
			}
			String key = sb.toString();
			//String reducedKey = reduce(key);
			if (debug > 2) {
				System.err.println("Key to find: " + key);
			}
			switch (n) {
			case 2: score *= (bigram.containsKey(key) ? 1.0/bigram.get(key) : 0.0);	
			break;
			case 3: score *= (trigram.containsKey(key) ? 1.0/trigram.get(key) : 0.0); 
			break;
			case 4: score *= (tetragram.containsKey(key) ? 1.0/tetragram.get(key) : 0.0); 
			break;
			default: break;
			}
		}
		return Math.pow(score, 1.0 / slots);
	}

	private String reduce (String key) {
		String[] seg = Segmenter.segmentToArray(key);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < seg.length-1; i++) {
			sb.append(seg[i]);
		}
		return sb.toString();
	}

	public double compositeScore (String word) {
		word = "§" + word + "*";
		double biScore = ngramScore(word, 2);
		if (debug > 2) {
			System.err.println("Bigram score: " + biBoost*biScore);
		}

		double triScore = ngramScore(word, 3);
		double triActual = Double.isNaN(triScore) || Double.isInfinite(triScore) || triScore == 0 ? 1.0 : triScore;
		if (debug > 2) {
			System.err.println("Trigram score: " + triBoost*triScore);
		}

		double tetraScore = ngramScore(word, 4);
		double tetraActual = Double.isNaN(tetraScore) || Double.isInfinite(tetraScore) || tetraScore == 0 ? 1.0 : tetraScore;
		if (debug > 2) {
			System.err.println("Tetragram score: " + tetraBoost*tetraScore);
		}

		return ((biBoost * biScore) + (triBoost * triActual) + (tetraBoost * tetraActual))/(3+(biBoost+triBoost+tetraBoost)/3)-1.0638297872340425;
	}

	public boolean testHypothesis (String word, String hyp) {
		// TODO implement english
		if (hyp.equals("en"))
			return false;
		if (word.length() < 2)
			return false;
		String[] segments = Segmenter.segmentToArray(word);
		for (int n = 2; n < 5; n++) {
			int slots = segments.length - (n - 1);
			for (int i = 0; i < slots; i++) {
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < n; j++) {
					sb.append(segments[i+j]);
				}
				String key = sb.toString();
				switch (n) {
				case 2: if (bigram.containsKey(key)) continue; else { System.out.println("Failed bigram test " + key);return false;}
				
				//case 3: if (trigram.containsKey(key)) continue; else { System.out.println("Failed trigram test " + key);return false;}
				
				//case 4: if (tetragram.containsKey(key)) continue; else { System.out.println("Failed quadrigram test " + key);return false;}
		
				default: break;
				}
			}
		}
		return true;
	}
}
