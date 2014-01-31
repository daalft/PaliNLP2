package de.unitrier.daalft.pali.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class CancelGlassPane extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8073888035168183872L;
	private JFrame frame;
	private Thread t;

	public void setThread (Thread t) {
		this.t = t;
	}

	public CancelGlassPane(JFrame frame) {
		this.setLayout(new BorderLayout());
		ImageIcon loading = new ImageIcon("./data/img/processA.gif");
		this.frame = frame;
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				interrupt();
			}
		});
		this.add(new JLabel("", loading, JLabel.CENTER), BorderLayout.CENTER);
	}

	private void interrupt() {
		t.interrupt();
		try {
			t.join(1);
		} catch (InterruptedException e) {

		}
		this.setVisible(false);
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(0.1f,0.1f,0.5f,0.2f));
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.BOLD, 18));
		String t = "Click anywhere to cancel current operation";
		g.drawString(t, frame.getWidth()/2-mywidth(g,t), frame.getHeight()/2+40);

	}

	private int mywidth (Graphics g, String text) {
		g.setFont(new Font("Serif", Font.BOLD, 18));
		FontMetrics metrics = g.getFontMetrics();
		return metrics.stringWidth(text)/2;
	}
}

