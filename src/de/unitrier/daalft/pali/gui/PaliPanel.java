package de.unitrier.daalft.pali.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.unitrier.daalft.pali.PaliNLP;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PaliPanel extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4523706587412518023L;
	private JSplitPane spSplitPane;
	private JScrollPane spScrollTop;
	private JButton bLemma;
	private JButton bGen;
	private JFileChooser jFileCh;
	private JLabel lInfo;
	private JTextArea fOutput;
	private JTextArea fInput;
	private JLabel lOutput;
	private JLabel lInput;
	private JTextField fFileOut;
	private JButton bFileOut;
	private JButton bChooseIn;
	private JTextField fFileIn;
	private JCheckBox cFileOut;
	private JCheckBox cFileIn;
	private JPanel pBottom;
	private JPanel pTop;
	private JButton bMerge;
	private JButton bSplit;
	private JButton bAna;
	private JButton bStem;
	private JLabel lTitle;
	private JScrollPane spScrollBottom;
	private static CancelGlassPane cancel;
	private PaliNLP nlp;
	/**
	 * Auto-generated main method to display this 
	 * JPanel inside a new JFrame.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new PaliPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		cancel = new CancelGlassPane(frame);
		frame.setGlassPane(cancel);
		frame.setTitle("Pali NLP");
	}

	public PaliPanel() {
		super();
		nlp = new PaliNLP();
		initGUI();
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0};
			thisLayout.rowHeights = new int[] {50, 7, 7, 15, 7, 15, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0};
			thisLayout.columnWidths = new int[] {7, 7, 20, 20, 20, 20, 20, 7};
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(750, 519));
			{
				jFileCh = new JFileChooser();
			}
			{
				spSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				this.add(spSplitPane, new GridBagConstraints(1, 1, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					spScrollTop = new JScrollPane();
					spSplitPane.add(spScrollTop, JSplitPane.TOP);
					spScrollTop.setPreferredSize(new java.awt.Dimension(734, 291));
					spScrollTop.setSize(644, 155);
					spScrollTop.getVerticalScrollBar().setUnitIncrement(20);
					{
						pTop = new JPanel();
						GridBagLayout pTopLayout = new GridBagLayout();
						spScrollTop.setViewportView(pTop);
						pTopLayout.rowWeights = new double[] {0.0, 0.0, 0.1, 0.0, 0.0, 0.1, 0.0};
						pTopLayout.rowHeights = new int[] {7, 15, 7, 7, 15, 7, 7};
						pTopLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
						pTopLayout.columnWidths = new int[] {7, 7, 7};
						pTop.setLayout(pTopLayout);
						{
							lInput = new JLabel();
							pTop.add(lInput, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							lInput.setText("Input");
						}
						{
							lOutput = new JLabel();
							pTop.add(lOutput, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							lOutput.setText("Output");
						}
						{
							fInput = new JTextArea();
							pTop.add(fInput, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						}
						{
							fOutput = new JTextArea();
							pTop.add(fOutput, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						}
					}
				}
				{
					spScrollBottom = new JScrollPane();
					spSplitPane.add(spScrollBottom, JSplitPane.BOTTOM);
					spScrollBottom.setPreferredSize(new java.awt.Dimension(640, 1));
					spScrollBottom.setSize(640, 1);
					spScrollBottom.getVerticalScrollBar().setUnitIncrement(16);
					{
						pBottom = new JPanel();
						GridBagLayout pBottomLayout = new GridBagLayout();
						spScrollBottom.setViewportView(pBottom);
						pBottomLayout.rowWeights = new double[] {0.0, 0.0, 0.0};
						pBottomLayout.rowHeights = new int[] {15, 7, 15};
						pBottomLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0};
						pBottomLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7};
						pBottom.setLayout(pBottomLayout);
						pBottom.setPreferredSize(new java.awt.Dimension(639, 119));
						pBottom.setSize(639, 228);
						{
							cFileIn = new JCheckBox();
							pBottom.add(cFileIn, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							cFileIn.setText("Input from file");
							cFileIn.addChangeListener(new ChangeListener() {
								@Override
								public void stateChanged(ChangeEvent e) {
									JCheckBox source = (JCheckBox) e.getSource();
									if (source.isSelected()) {
										fFileIn.setEnabled(true);
										bChooseIn.setEnabled(true);
									} else {
										fFileIn.setEnabled(false);
										bChooseIn.setEnabled(false);
									}
								}
							});
						}
						{
							cFileOut = new JCheckBox();
							pBottom.add(cFileOut, new GridBagConstraints(5, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							cFileOut.setText("Output to file");
							cFileOut.addChangeListener(new ChangeListener() {
								@Override
								public void stateChanged(ChangeEvent e) {
									JCheckBox source = (JCheckBox) e.getSource();
									if (source.isSelected()) {
										fFileOut.setEnabled(true);
										bFileOut.setEnabled(true);
									} else {
										fFileOut.setEnabled(false);
										bFileOut.setEnabled(false);
									}
								}
							});
						}
						{
							fFileIn = new JTextField();
							pBottom.add(fFileIn, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							fFileIn.setText("Input path");
							fFileIn.setEnabled(false);
						}
						{
							bChooseIn = new JButton();
							pBottom.add(bChooseIn, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							bChooseIn.setText("Choose path...");
							bChooseIn.setEnabled(false);
							bChooseIn.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									try {
										choose(fFileIn);
									} catch (IOException e1) {

									}
								}
							});
						}
						{
							bFileOut = new JButton();
							pBottom.add(bFileOut, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							bFileOut.setText("Choose path...");
							bFileOut.setEnabled(false);
							bFileOut.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									try {
										choose(fFileOut);
									} catch (IOException e1) {

									}
								}
							});
						}
						{
							fFileOut = new JTextField();
							pBottom.add(fFileOut, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							fFileOut.setText("Output path");
							fFileOut.setEnabled(false);
						}
					}
				}
			}
			{
				lTitle = new JLabel();
				this.add(lTitle, new GridBagConstraints(3, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				lTitle.setText("Pali NLP");
				lTitle.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				bLemma = new JButton();
				this.add(bLemma, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				bLemma.setText("Lemmatize");
				bLemma.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						lemma();
					}
				});
			}
			{
				bStem = new JButton();
				this.add(bStem, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				bStem.setText("Stem");
				bStem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						stem();
					}
				});
			}
			{
				bAna = new JButton();
				this.add(bAna, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				bAna.setText("Analyze");
				bAna.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						analyze();
					}
				});
			}
			{
				bGen = new JButton();
				this.add(bGen, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				bGen.setText("Generate");
				bGen.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						generate();
					}
				});
			}
			{
				bSplit = new JButton();
				this.add(bSplit, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				bSplit.setText("Split");
				bSplit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						split();
					}
				});
			}
			{
				bMerge = new JButton();
				this.add(bMerge, new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				bMerge.setText("Merge");
				bMerge.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						merge();
					}
				});
			}
			{
				lInfo = new JLabel();
				this.add(lInfo, new GridBagConstraints(1, 5, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				lInfo.setHorizontalAlignment(SwingConstants.TRAILING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// trick the text area fields
		// entering mānaṃ results in 'ṃ' being represented as a box
		// entering sinhalese signs results in the 'ṃ' being represented
		// correctly (and the sinhalese as boxes). 
		// Therefore, set the text to sinhalese and delete it again
		// so that everything is displaying correctly 
		fInput.setText("තමා");
		fOutput.setText("තමා");
		fInput.setText("");
		fOutput.setText("");
	}

	private void choose (JTextField field) throws IOException {
		int v = jFileCh.showOpenDialog(this);
		if (v == JFileChooser.APPROVE_OPTION) {
			field.setText(jFileCh.getSelectedFile().getCanonicalPath());
		}
	}
	
	private String[] readFile (String path) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		String l = "";
		StringBuffer sb = new StringBuffer();
		while ((l=br.readLine())!=null)
			sb.append(l).append(" ");
		br.close();
		return sb.toString().split(" ");
	}

	private void writeFile (List<Object> list, String path) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
		bw.write(beautify(list));
		bw.close();
	}

	private void operate (final int mode) {
		Thread t = new Thread(new Runnable () {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					cancel.setVisible(true);
					List<Object> container = new ArrayList<Object>();
					String modename = "";
					switch (mode) {
					case 1: modename = "Lemmatizing";break;
					case 2: modename = "Stemming";break;
					case 3: modename = "Analyzing";break;
					case 4: modename = "Generating";break;
					case 5: modename = "Splitting";break;
					case 6: modename = "Merging";break;
					default: break;
					}
					lInfo.setText(modename + "...");
					String[] words = null;
					if (cFileIn.isSelected()) {
						lInfo.setText("Reading file " + fFileIn.getText() +  "...");
						try {
							words = readFile(fFileIn.getText());
						} catch (IOException e) {
							System.err.println("Could not read file");
							return;
						}
					} else {
						words = fInput.getText().split(" ");
					}
					for (String word : words) {
						String[] split = null;
						if (word.contains(":")) {
							split = word.split(":");
						}
						String w = "";
						String pos = "";
						String g = "";
						if (split == null)
							w = word;
						else if (split.length == 2){
							w = split[0];
							pos = split[1];
						} else if (split.length == 3) {
							w = split[0];
							pos = split[1];
							g = split[2];
						} else {
							w = word;
						}
						if (Thread.currentThread().isInterrupted()) {
							lInfo.setText("Interrupted...");
							return;
						}
						switch (mode) {
						case 1: container.addAll(nlp.lemmatize(w, pos)); break;
						case 2: container.add(nlp.stem(w)); break;
						case 3: container.addAll(nlp.analyze(w, pos)); break;
						case 4: container.addAll(nlp.generate(w, pos, g)); break;
						case 5: container.addAll(nlp.split(w, 1)); break;
						case 6: container.addAll(nlp.merge(words)); break;
						default: break;
						}
						if (Thread.currentThread().isInterrupted()) {
							lInfo.setText("Interrupted...");
							return;
						}
					}
					if (cFileOut.isSelected()) {
						lInfo.setText("Writing file " + fFileOut.getText() +  "...");
						try {
							writeFile(container, fFileOut.getText());
						} catch (IOException e) {
							System.err.println("Could not write file");
							return;
						}
					} else {
						fOutput.setText(beautify(container));
					}
					cancel.setVisible(false);
					lInfo.setText("Done.");
					Thread.currentThread().interrupt();
				}
			}
		});
		t.setDaemon(true);
		cancel.setThread(t);
		t.start();
		lInfo.setText("Interrupted...");
	}

	private String beautify (List<Object> list) {
		StringBuilder sb = new StringBuilder();
		for (Object o : list) {
			sb.append(o.toString());
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	private void lemma () {
		operate(1);
	}

	private void stem () {
		operate(2);
	}

	private void analyze () {
		operate(3);
	}

	private void generate () {
		operate(4);
	}

	private void split () {
		operate(5);
	}

	private void merge () {
		if (fInput.getText().split(" ").length < 2) {
			fOutput.setText("For merging, you have to specify more than one word!");
			return;
		}
		operate(6);
	}

}
