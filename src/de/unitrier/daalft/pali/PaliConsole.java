package de.unitrier.daalft.pali;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.cl.dictclient.DictWord;
import de.unitrier.daalft.pali.phonology.element.SplitResult;


public class PaliConsole {

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	private static final PaliNLP nlp;

	////////////////////////////////////////////////////////////////
	// Variabless
	////////////////////////////////////////////////////////////////

	private static int mode = -1;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	static
	{
		nlp = new PaliNLP();
	}
	
	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	private static int evaluate(String s) {
		if (s.equals("lemmatize") || s.equals("lemmatise")
				|| s.equals("lemmatizer") || s.equals("lemmatiser")
				|| s.equals("lemma"))
			return 1;
		if (s.equals("analyzer") || s.equals("analyser") || s.equals("analyze")
				|| s.equals("analyse") || s.equals("ana"))
			return 2;
		if (s.equals("generate") || s.equals("generator") || s.equals("gen"))
			return 3;
		if (s.equals("split") || s.startsWith("ss"))
			return 4;
		if (s.equals("merge") || s.startsWith("sm"))
			return 5;
		if (s.equals("stem"))
			return 6;
		return -1;
	}

	private static void printWelcome() {
		System.out.println("Welcome to Pali NLP tools.");
		System.out.println("To quit, enter \"quit\"");
		System.out.println("To change mode, enter \"chmod\"");
		System.out.println("Please choose your mode:");
	}

	private static String modeTitle() {
		switch (mode) {
		case 1:
			return "Lemmatizer";
		case 2:
			return "Analyzer";
		case 3:
			return "Generator";
		case 4:
			return "Sandhi splitter";
		case 5:
			return "Sandhi merger";
		case 6:
			return "Stemmer";
		default:
			return "Mode not set!";
		}
	}

	private static void printModes() {
		System.out.println();
		System.out.println("Mode code\tMode\nlemma\t\tlemmatizer");
		System.out.println("stem\t\tstemmer\nana\t\tanalyzer");
		System.out.println("gen\t\tgenerator\nss\t\tsandhi splitter");
		System.out.println("sm\t\tsandhi merger");
	}

	public static void main(String[] args) throws IOException {
		String c = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		printWelcome();
		printModes();
		// main loop
		MAIN: while (!c.equals("quit")) {
			c = br.readLine();
			System.out.println(c);
			if (c == null) {
				System.err.println("Could not parse input.\nPlease try again.");
				printModes();
				continue;
			}
			int action = evaluate(c);
			if (action == -1) {
				System.err.println("Unrecognized command: " + c);
				printModes();
				continue;
			}
			mode = action;
			System.out.println(modeTitle());
			System.out.println("Please enter your word(s):");
			String arg = "";
			// enter mode loop
			while (true) {
				arg = br.readLine();
				if (arg == null) {
					System.err.println("Could not parse input.\nPlease try again.");
					modeTitle();
					System.out.println("Please enter your word(s):");
					continue;
				}
				if (arg.equals("chmod"))
					break;
				if (arg.equals("quit"))
					break MAIN;
				busyMessage();
				try {
					methodCall(arg.split(" "));
				} catch (Exception e) {
					System.err.println("Method failed!");
					return;
				}
				System.out.println();
				System.out.println(modeTitle());
				System.out.println("Please enter your word(s):");
				continue;
			}
			System.out.println("Please choose your mode:");
			printModes();
		}
		System.out.println("Pali NLP has been closed");
		br.close();
	}

	private static void busyMessage() {
		System.out.println("Calculating...");
	}

	private static void methodCall(String[] args) throws Exception {
		String[] split = null;
		if (args[0].contains(":")) {
			split = args[0].split(":");
		}
		String w = split == null ? args[0] : split[0];
		String wc = split == null ? "" : split[1];
		String gender = split == null ? null : (split.length > 2 ? split[2] : null);
		switch (mode) {
		case 1:
			for (DictWord dw : nlp.lemmatize(w, wc)) {
				System.out.println(dw.toString());
			}
			break;
		case 2:
			for (DictWord dw : nlp.analyze(w, wc)) {
				System.out.println(dw.toString());
			}
			break;
		case 3:
			for (DictWord dw : nlp.generate(w, wc, gender)) {
				System.out.println(dw.toString());
			}
			break;
		case 4:
			for (SplitResult sr : nlp.split(w, 1)) {
				System.out.println(sr.toString());
			}
			break;
		case 5:
			for (String s : nlp.merge(args)) {
				System.out.println(s);
			}
			break;
		case 6:
			System.out.println(nlp.stem(w));
		default:
			break;
		}
	}
}
