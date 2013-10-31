package webReader;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Screen {

	private JFrame frame;
	private JPanel panel;
	private JPanel wordsList;
	private JButton button;
	private JTextField textbox;
	private JScrollPane scrollPane;
	
	public Screen() {
		frame = new JFrame("Web Reader");
		panel = new JPanel();
		wordsList = new JPanel(new GridLayout(0, 2));
		button = new JButton("Submit");
		textbox = new JTextField();
		scrollPane = new JScrollPane(wordsList);
		scrollPane.setPreferredSize(new Dimension(640, 480));
	}
}
