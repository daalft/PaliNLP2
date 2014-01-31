package de.unitrier.daalft.pali.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unitrier.daalft.pali.general.Alphabet;

/**
 * Reads XML strings in a linear fashion using a stack
 * @author David
 *
 */
public class StackReader {

	/**
	 * Stack used for reading
	 */
	private Stack<String> stack;
	/**
	 * Delimiter
	 */
	private final static String DELIMITER = ";", OCCURRENCE_DELIMITER = "#";

	/**
	 * Constructor
	 */
	public StackReader () {
		stack = new Stack<String>();
	}

	/**
	 * Reads the file at the specified path and returns
	 * a list of morphological information
	 * <p>
	 * The structure of the returned list is:<br/>
	 * Each entry represents one morphological feature
	 * with all morphological information encoded by that 
	 * feature appended to it<br/>
	 * The information is separated by the standard delimiter
	 * ";", the morphological feature is always the first
	 * element of the entry (at position zero)
	 * @param path path to file
	 * @return list of morphological information
	 * @throws IOException
	 */
	public List<String> readFile (String path) throws IOException {
		return readFile(new File(path));
	}

	/**
	 * Reads the specified file and returns
	 * a list of morphological information
	 * <p>
	 * The structure of the returned list is:<br/>
	 * Each entry represents one morphological feature
	 * with all morphological information encoded by that 
	 * feature appended to it<br/>
	 * The information is separated by the standard delimiter
	 * ";", the morphological feature is always the first
	 * element of the entry (at position zero)
	 * @param file file
	 * @return list of morphological information
	 * @throws IOException
	 */
	public List<String> readFile (File file) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String l = "";
		while ((l = br.readLine())!=null)
			sb.append(l).append(System.lineSeparator());
		br.close();
		return readString(sb.toString());
	}

	/**
	 * Reads the specified string and returns
	 * a list of morphological information
	 * <p>
	 * The structure of the returned list is:<br/>
	 * Each entry represents one morphological feature
	 * with all morphological information encoded by that 
	 * feature appended to it<br/>
	 * The information is separated by the standard delimiter
	 * ";", the morphological feature is always the first
	 * element of the entry (at position zero)
	 * @param in string
	 * @return list of morphological information
	 * @throws IOException
	 */
	public List<String> readString (String in) throws IOException  {
		// initialize output
		List<String> output = new ArrayList<String>();
		Scanner scanner = new Scanner(in);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if (line.startsWith("<!") || line.startsWith("<?") || line.equals("<paradigms>"))
				continue;
			if (!line.isEmpty()) {
				// remove < >
				String raw = line.substring(1, line.length()-1);
				// if line is closing element
				if (raw.startsWith("/")) {
					if (stack.isEmpty()) {
						// System.err.println("Stack empty! Attempting to pop " + raw);
					} else // pop from stack
						stack.pop();
					// if line is ending
				} else if (raw.startsWith("ending")) {
					// if ending has occurrence information
					if (raw.matches("ending type.+")) {
						// create pattern for occurrence extraction
						Pattern o = Pattern.compile("(?<=type=\").+?(?=\">)");
						Matcher mo = o.matcher(raw);
						if (!mo.find()) {
							System.err.println("Error! Cannot find match for " + raw);
							scanner.close();
							return null;
						}
						String occurrence = mo.group();
						// create pattern for ending extraction
						Pattern p = Pattern.compile("(?<=>).+?(?=<)");
						Matcher m = p.matcher(raw);
						if (!m.find()) {
							System.err.println("Error! Cannot find match for " + raw);
							scanner.close();
							return null;
						}
						String ending = m.group();
						String morphology = getStackToString();
						output.add(ending + OCCURRENCE_DELIMITER + occurrence + DELIMITER + morphology);
						
					} else {
						// create pattern for ending extraction
						Pattern p = Pattern.compile("(?<=ending>).+?(?=</ending)");
						Matcher m = p.matcher(raw);
						// if no match is found
						if (!m.find()) {
							System.err.println("Error! Cannot find match for " + raw);
							scanner.close();
							return null;
						}
						// get ending
						String ending = m.group();
						// get stack representation
						String morphology = getStackToString();
						// prepend ending to its morphological information
						// add to output
						output.add(ending + DELIMITER + morphology);
					}
					// if line is opening element
				} else if (raw.matches(".*[-\\w]+(\\stype=\"[-/\\w\\d" +Patterner.linearize(Alphabet.getConsonants()) + Patterner.linearize(Alphabet.getVowels())+ "-]+\")?.*")) {	
					// create pattern for element extraction
					Pattern p = Pattern.compile("[-\\w]+(\\stype=\"[-/\\w\\d" +Patterner.linearize(Alphabet.getConsonants()) + Patterner.linearize(Alphabet.getVowels())+ "]+\")?");
					Matcher m = p.matcher(raw);
					if (!m.find()) {
						System.err.println("Error! Cannot find match for " + raw);
						scanner.close();
						return null;
					}
					// get element
					String result = m.group();
					// push element to stack
					stack.push(result);
				} 
				// else ignore line
			}
		}
		scanner.close();
		return output;
	}

	/**
	 * Returns a representation of all the objects currently on the stack
	 * @return string representation
	 */
	private String getStackToString () {
		StringBuilder sb = new StringBuilder();
		for (String s : stack) {
			sb.append(s);
			sb.append(DELIMITER);
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}

	/**
	 * Returns the delimiter used by the stack reader
	 * @return delimiter
	 */
	public String getDelimiter () {
		return DELIMITER;
	}
	
	/**
	 * Returns the occurrence delimiter used by the stack reader
	 * @return delimiter
	 */
	public String getOccurrenceDelimiter () {
		return OCCURRENCE_DELIMITER;
	}
}
