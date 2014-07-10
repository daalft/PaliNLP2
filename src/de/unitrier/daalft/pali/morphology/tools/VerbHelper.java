package de.unitrier.daalft.pali.morphology.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unitrier.daalft.pali.general.Alphabet;
import de.unitrier.daalft.pali.phonology.SandhiSplit;
import de.unitrier.daalft.pali.phonology.element.SplitResult;
import de.unitrier.daalft.pali.tools.Patterner;
import de.unitrier.daalft.pali.tools.Segmenter;
/**
 * Utility class providing methods to derive stems from roots and vice versa
 * @author David
 *
 */
public class VerbHelper {

	public static boolean debug = false;

	/**
	 * Returns possible stems given a root and a declension
	 * <p>
	 * It is possible to specify the declension
	 * of the root as a number. The following table
	 * illustrates possible declension numbers:
	 * <table border="1">
	 * <tr><th>Declension</th><th>Declension number</th></tr>
	 * <tr><td>First</td><td>1</td></tr>
	 * <tr><td>Second</td><td>2</td></tr>
	 * <tr><td>Third</td><td>3</td></tr>
	 * <tr><td>Fourth</td><td>4</td></tr>
	 * <tr><td>Fifth</td><td>5</td></tr>
	 * <tr><td>Sixth</td><td>6</td></tr>
	 * <tr><td>Seventh</td><td>7</td></tr>
	 * </table>
	 * The specified declension is used to derive the stems. 
	 * If an invalid declension number is specified,
	 * all possible stems are returned.<br/><br/>
	 * If the declension is <b>unknown</b> or all possible
	 * stems are to be generated, use {@link #stemFromRoot(String)}
	 * @param root root
	 * @param declension declension
	 * @return possible stems
	 * @see #stemFromRoot(String)
	 */
	public List<String> stemFromRoot (String root, int declension) {
		FromRootToStem frts = new FromRootToStem();
		switch (declension) {
		case 1: return frts.first(root);
		case 2: return frts.second(root);
		case 3: return frts.third(root);
		case 4: return frts.fourth(root);
		case 5: return frts.fifth(root);
		case 6: return frts.sixth(root);
		case 7: return frts.seventh(root);
		default: return frts.stemFromRoot(root);
		}
	}

	/**
	 * Returns possible stems given a root
	 * <p>
	 * Use this method, if the declension type is
	 * <b>unknown</b>. This method generates stems
	 * using all declension types.<br/>
	 * If the declension type <b>known</b>, use
	 * {@link #stemFromRoot(String, int)} instead
	 * @param root root
	 * @return possible stems
	 * @see #stemFromRoot(String, int)
	 */
	public List<String> stemFromRoot (String root) {
		return stemFromRoot(root, 0);
	}

	/**
	 * Returns possible roots given a stem
	 * <p>
	 * It is possible to specify the declension
	 * of the stem as a number. The following table
	 * illustrates possible declension numbers:
	 * <table border="1">
	 * <tr><th>Declension</th><th>Declension number</th></tr>
	 * <tr><td>First</td><td>1</td></tr>
	 * <tr><td>Second</td><td>2</td></tr>
	 * <tr><td>Third</td><td>3</td></tr>
	 * <tr><td>Fourth</td><td>4</td></tr>
	 * <tr><td>Fifth</td><td>5</td></tr>
	 * <tr><td>Sixth</td><td>6</td></tr>
	 * <tr><td>Seventh</td><td>7</td></tr>
	 * </table>
	 * The specified declension is used to derive the roots. 
	 * If an invalid declension number is specified,
	 * all possible roots are returned.<br/><br/>
	 * If the declension is <b>unknown</b> or all possible
	 * roots are to be generated, use {@link #rootFromStem(String)}
	 * @param stem stem
	 * @param declension declension
	 * @return possible roots
	 * @see #rootFromStem(String)
	 */
	public List<String> rootFromStem (String stem, int declension) {
		FromStemToRoot fstr = new FromStemToRoot();
		List<String> temp = new ArrayList<String>();
		switch (declension) {
		case 1: temp.addAll(fstr.first(stem)); break;
		case 2: temp.addAll(fstr.second(stem)); break;
		case 3: temp.addAll(fstr.third(stem)); break;
		case 4: temp.addAll(fstr.fourth(stem)); break;
		case 5: temp.addAll(fstr.fifth(stem)); break;
		case 6: temp.addAll(fstr.sixth(stem)); break;
		case 7: temp.addAll(fstr.seventh(stem)); break;
		default: temp.addAll(fstr.rootFromStem(stem)); break;
		}
		// filter out improbable roots
		List<String> out = new ArrayList<String>();
		for (String s : temp) {
			if (this.isPlausibleRoot(s))
				if (!out.contains(s))
					out.add(s);
			else 
				System.out.println("Discarded calculated root: " + s);
		}
		return out;
	}

	/**
	 * Returns possible roots given a stem
	 * <p>
	 * Use this method, if the declension type is
	 * <b>unknown</b>. This method generates roots
	 * using all declension types that can be applied.<br/>
	 * If the declension type <b>known</b>, use
	 * {@link #rootFromStem(String, int)} instead
	 * @param stem stem
	 * @return possible roots
	 * @see #rootFromStem(String, int)
	 */
	public List<String> rootFromStem (String stem) {
		return rootFromStem(stem, 0);
	}

	/**
	 * Checks whether the given root is a plausible root
	 * <p>
	 * Plausible roots consist of maximally three units
	 * and contain a vowel
	 * @param root root
	 * @return true if root is plausible
	 */
	public boolean isPlausibleRoot (String root) {
		return (Segmenter.segmentToArray(root).length < 4) && 
				(root.matches(".*"+Patterner.patternGroup(Alphabet.getVowels())+".*"));
	}

	/**
	 * Internal class responsible for stem calculation
	 * @author David
	 *
	 */
	private class FromRootToStem {
		/**
		 * Returns possible stems given a root 
		 * @param root root
		 * @return possible stems
		 */
		private List<String> stemFromRoot (String root) {
			List<String> output = new ArrayList<String>();
			List<String> output2 = new ArrayList<String>();
			output.addAll(first(root));
			output.addAll(second(root));
			output.addAll(third(root));
			output.addAll(fourth(root));
			output.addAll(fifth(root));
			output.addAll(sixth(root));
			output.addAll(seventh(root));
			for (String s : output) {
				if (!output2.contains(s))
					output2.add(s);
			}
			return output2;
		}

		/**
		 * Strengthens the vowel of a root
		 * @param root root
		 * @return root with strengthened vowel
		 */
		private String strengthenRoot (String root) {
			StringBuilder sb = new StringBuilder();
			String[] parts = Segmenter.segmentToArray(root);
			for (String s : parts) {
				if (Alphabet.isVowel(s))
					sb.append(Alphabet.getStrong(s));
				else 
					sb.append(s);
			}
			return sb.toString();
		}

		/**
		 * Reduplicates a root 
		 * <p>
		 * Reduplicating roots can be found in the first
		 * conjugation
		 * @param root root
		 * @return reduplicated root
		 */
		private List<String> reduplicate (String root) {
			List<String> output = new ArrayList<String>();
			String[] parts = Segmenter.segmentToArray(root);
			if (parts.length < 2) {
				return Collections.singletonList(root);
			}
			String one = null;
			String two = null;

			if (Alphabet.isVowel(parts[0])) {
				one = Alphabet.getStrong(parts[0]);
				two = "";
			} else if (Alphabet.isGuttural(parts[0])) {
				one = Alphabet.getPalatalForGuttural(parts[0]);
				if (Alphabet.isAspirated(one)) {
					one = one.substring(0, 1);
				}
			} else if (parts[0].equals("h")) {
				one = "j";
			} else if (parts[0].equals("v")) {
				one = "u";
				two = "";
			} else if (Alphabet.isAspirated(parts[0])) {
				one = parts[0].substring(0, 1);
			} else {
				one = parts[0];
			}
			if (two == null) {
				if (parts[1].equals("a") || parts[1].equals("ā")) {
					output.add(one + "a" + root);
					if (parts[1].equals("a"))
						output.add(one + "a" + strengthenRoot(root));
				} else if (parts[1].equals("i") || parts[1].equals("ī")) {
					output.add(one + "i" + root);
					output.add(one + "i" + strengthenRoot(root));
					if (parts[1].equals("i")) {
						output.add(one + "e" + root);
						output.add(one + "e" + strengthenRoot(root));
					}
				} else if (parts[1].equals("u") || parts[1].equals("ū")) {
					output.add(one + "u" + root);
					output.add(one + "a" + root);
					output.add(one + "u" + strengthenRoot(root));
					output.add(one + "a" + strengthenRoot(root));
					if (parts[1].equals("u")) {
						output.add(one + "o" + root);
						output.add(one + "o" + strengthenRoot(root));
					}
				}
			} else {
				output.add(one + two + root);
				output.add(one + two + strengthenRoot(root));
			}
			return output;
		}

		/**
		 * Applies the first conjugation to derive stems
		 * @param root root
		 * @return stems
		 */
		private List<String> first (String root) {
			List<String> output = new ArrayList<String>();
			if (root.matches("^.*"+Patterner.patternGroup(Alphabet.getVowels()) + "$")) {
				output.add(root);
				if (root.endsWith("i") || root.endsWith("ī")) {
					output.add(root.substring(0, root.length()-1) + "e");
					output.add(root.substring(0, root.length()-1) + "aya");
				}
				if (root.endsWith("u") || root.endsWith("ū")) {
					output.add(root.substring(0, root.length()-1) + "o");
					output.add(root.substring(0, root.length()-1) + "ava");
				}
			} else {
				output.add(root + "a");
				output.add(strengthenRoot(root) + "a");
			}
			for (String s : reduplicate(root)) {
				if (s.endsWith("a") || s.endsWith("ā"))
					output.add(s);
				else
					output.add(s + "a");
			}
			return output;
		}

		/**
		 * Applies the second conjugation to derive stems
		 * @param root root
		 * @return stems
		 */
		private List<String> second (String root) {
			List<String> output = new ArrayList<String>();
			String[] parts = Segmenter.segmentToArray(root);
			String lc = parts[parts.length-1];
			String nigg = Alphabet.getAssimilatedNiggahita(lc);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < parts.length-1; i++) {
				sb.append(parts[i]);
			}
			output.add(sb.append(nigg + lc + "a").toString());
			return output;
		}

		/**
		 * Applies the third conjugation to derive stems
		 * @param root root
		 * @return stems
		 */
		private List<String> third (String root) {
			List<String> output = new ArrayList<String>();
			String[] parts = Segmenter.segmentToArray(root);
			String pre = "";
			String post = "";
			if (parts.length == 3) {
				pre = parts[0] + parts[1];
				post = parts[2];
			} else if (parts.length == 2) {
				pre = parts[0];
				post = parts[1];
			} else {
				if (debug)
					System.err.println("Unexpected input for third declension: " + root);
			}
			output.add(pre + Alphabet.getAssimilatedWithYa(post));
			return output;
		}

		/**
		 * Applies the fourth conjugation to derive stems
		 * @param root root
		 * @return stems
		 */
		private List<String> fourth (String root) {
			List<String> output = new ArrayList<String>();
			if (root.matches("^.*"+Patterner.patternOr(Alphabet.getConsonants())+"$")) {
				output.add(root + "uṇu");
				output.add(root + "uṇo");
				output.add(root + "uṇā");
			} else {
				output.add(root + "ṇu");
				output.add(root + "ṇo");
				output.add(root + "ṇā");
			}
			return output;
		}

		/**
		 * Applies the fifth conjugation to derive stems
		 * @param root root
		 * @return stems
		 */
		private List<String> fifth (String root) {
			List<String> output = new ArrayList<String>();
			if (root.matches("^.*"+Patterner.patternGroup(Alphabet.getVowels()) + "$")) {
				output.add(root + "nā");
			}
			return output;
		}

		/**
		 * Applies the sixth conjugation to derive stems
		 * @param root root
		 * @return stems
		 */
		private List<String> sixth (String root) {
			List<String> output = new ArrayList<String>();
			output.add(root + "o");
			output.add(root + "av");
			return output;
		}

		/**
		 * Applies the seventh conjugation to derive stems
		 * @param root root
		 * @return stems
		 */
		private List<String> seventh (String root) {
			List<String> output = new ArrayList<String>();
			output.add(root + "aya");
			output.add(root + "e");
			output.add(strengthenRoot(root) + "aya");
			output.add(strengthenRoot(root) + "e");
			return output;
		}
	}

	/**
	 * Internal class responsible for root calculation
	 * @author David
	 *
	 */
	private class FromStemToRoot {
		/**
		 * Returns possible roots given a stem
		 * @param stem stem
		 * @return roots
		 */
		private List<String> rootFromStem (String stem) {
			List<String> output = new ArrayList<String>();
			output.addAll(first(stem));
			output.addAll(second(stem));
			output.addAll(third(stem));
			output.addAll(fourth(stem));
			output.addAll(fifth(stem));
			output.addAll(sixth(stem));
			output.addAll(seventh(stem));
			return output;
		}

		/**
		 * Weakens the vowel of a root
		 * @param root root
		 * @return root with weakened vowel
		 */
		private List<String> weakenRoot (String root) {
			List<String> out = new ArrayList<String>();
			List<String> vowels = new ArrayList<String>();
			String[] headtail = root.split(Patterner.patternGroup(Alphabet.getVowels()));
			String[] parts = Segmenter.segmentToArray(root);
			for (String s : parts) {
				if (Alphabet.isVowel(s))
					vowels.addAll(Alphabet.getWeak(s));
			}
			for (String s : vowels) {
				if (headtail.length > 1)
					out.add(headtail[0] + s + headtail[1]);
				else
					out.add(headtail[0] + s);
			}
			return out;
		}

		/**
		 * Undoes the reduplication from the first conjugation
		 * @param stem stem
		 * @return unduplicated stem
		 */
		private String unduplicate (String stem) {
			String[] split = Segmenter.segmentToArray(stem);
			if (split.length < 2) {
				return stem;
			}
			if (Alphabet.isConsonant(split[1])) {
				return stem.substring(1);
			}
			int index = 0;
			for (int i = 1; i < split.length; i++) {
				if (Alphabet.isConsonant(split[i])) {
					index = i;
					break;
				}
			}
			return stem.substring(index);
		}

		/**
		 * Applies the first conjugation to derive roots
		 * @param stem stem
		 * @return roots
		 */
		private List<String> first (String stem) {
			List<String> output = new ArrayList<String>();
			// use stem as root if it is a plausible root
			if (isPlausibleRoot(stem)) {
				output.add(stem);	
			}
			// undo reduplication
			String undup = unduplicate(stem);
			if (undup == null || undup.isEmpty()) {
				return output;
			}
			String sroot = undup.substring(0, undup.length()-1);
			output.add(sroot);
			output.addAll(weakenRoot(sroot));
			// string replacements 
			if (stem.endsWith("e")) {
				output.add(stem.substring(0, stem.length()-1) + "i");
				output.add(stem.substring(0, stem.length()-1) + "ī");
			} else if (stem.endsWith("o")) {
				output.add(stem.substring(0, stem.length()-1) + "u");
				output.add(stem.substring(0, stem.length()-1) + "ū");
			} else if (stem.endsWith("aya")) {
				output.add(stem.substring(0, stem.length()-3) + "i");
				output.add(stem.substring(0, stem.length()-3) + "ī");
			} else if (stem.endsWith("ava")) {
				output.add(stem.substring(0, stem.length()-3) + "u");
				output.add(stem.substring(0, stem.length()-3) + "ū");
			} else if (stem.endsWith("a")) {
				String root = stem.substring(0, stem.length()-1);
				output.add(root);
				output.addAll(weakenRoot(root));
			} else {
				if (debug)
					System.err.println("Could not apply first declension to derive root from " + stem);
			}
			return output;
		}

		/**
		 * Applies the second conjugation to derive roots
		 * @param stem stem
		 * @return roots
		 */
		private List<String> second (String stem) {
			String pattern = Patterner.patternGroup(Alphabet.getNasals())+"("+Patterner.patternOr(Alphabet.getConsonants())+")a";
			List<String> output = new ArrayList<String>();
			if (stem.matches(".*"+pattern)) { 
				output.add(stem.replaceAll(pattern, "$2"));
			} else {
				if (debug)
					System.err.println("Could not apply second declension to derive root from " + stem);
			}
			return output;
		}

		/**
		 * Applies the third conjugation to derive roots
		 * @param stem stem
		 * @return roots
		 */
		private List<String> third (String stem) {
			SandhiSplit ss = new SandhiSplit();
			List<String> output = new ArrayList<String>();
			if (Segmenter.segmentToArray(stem).length < 4) {
				if (debug)
					System.err.println("Could not apply third declension to derive root from " + stem);
				return output;
			}
			List<SplitResult> list = ss.split(stem, 1);
			if (list.isEmpty()) {
				if (debug)
					System.err.println("Could not apply third declension to derive root from " + stem);
			}
			for (SplitResult sr : list) {
				if (sr.getSplit().size() < 2)
					continue;
				if (sr.getSplit().get(1).equals("ya")) {
					output.add(sr.getSplit().get(0));
				}
			}
			return output;
		}

		/**
		 * Applies the fourth conjugation to derive roots
		 * @param stem stem
		 * @return roots
		 */
		private List<String> fourth (String stem) {
			List<String> output = new ArrayList<String>();
			if (stem.matches("^(.{2,})uṇ[uoā]$")) {
				Pattern p = Pattern.compile("^(.{2,})uṇ[uoā]$");
				Matcher m = p.matcher(stem);
				m.find();
				output.add(m.group(1));
			} else {
				if (stem.matches("^(.*)ṇ[uoā]$")) {
					Pattern p = Pattern.compile("^(.*)ṇ[uoā]$");
					Matcher m = p.matcher(stem);
					m.find();
					output.add(m.group(1));	
				} else {
					if (debug)
						System.err.println("Could not apply fourth declension to derive root from " + stem);
				}
			}
			return output;
		}

		/**
		 * Applies the fifth conjugation to derive roots
		 * @param stem stem
		 * @return root
		 */
		private List<String> fifth (String stem) {
			List<String> output = new ArrayList<String>();
			if (stem.matches("^.*" + Patterner.patternGroup(Alphabet.getVowels()) + "nā$")) {
				Pattern p = Pattern.compile("^(.*" + Patterner.patternGroup(Alphabet.getVowels()) + ")nā$");
				Matcher m = p.matcher(stem);
				m.find();
				output.add(m.group(1));
			} else {
				if (debug)
					System.err.println("Could not apply fifth declension to derive root from " + stem);
			}
			return output;
		}

		/**
		 * Applies the sixth conjugation to derive roots
		 * @param stem stem
		 * @return roots
		 */
		private List<String> sixth (String stem) {
			List<String> output = new ArrayList<String>();
			if (stem.matches("^.*o$")) {
				Pattern p = Pattern.compile("^(.*)o$");
				Matcher m = p.matcher(stem);
				m.find();
				output.add(m.group(1));
			} else if (stem.matches("^.*av$")) {
				Pattern p = Pattern.compile("^(.*)av$");
				Matcher m = p.matcher(stem);
				m.find();
				output.add(m.group(1));
			} else {
				if (debug)
					System.err.println("Could not apply sixth declension to derive root from " + stem);
			}
			return output;
		}

		/**
		 * Applies the seventh conjugation to derive roots
		 * @param stem stem
		 * @return roots
		 */
		private List<String> seventh (String stem) {
			List<String> output = new ArrayList<String>();
			if (stem.matches("^.*aya$")) {
				Pattern p = Pattern.compile("^(.*)aya$");
				Matcher m = p.matcher(stem);
				m.find();
				String strongRoot = m.group(1);
				output.add(strongRoot);
				output.addAll(weakenRoot(strongRoot));
			} else if (stem.matches("^.*e$")) {
				Pattern p = Pattern.compile("^(.*)e$");
				Matcher m = p.matcher(stem);
				m.find();
				String strongRoot = m.group(1);
				output.add(strongRoot);
				output.addAll(weakenRoot(strongRoot));
			} else {
				if (debug)
					System.err.println("Could not apply seventh declension to derive root from " + stem);
			}
			return output;
		}
	}
}
