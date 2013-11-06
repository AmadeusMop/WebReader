package webReader;

import hashMap.HashMap2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.JTextComponent;


public class Screen {

	private JFrame frame;
	private JPanel panel;
	private JPanel submitField;
	private JPanel freqField;
	private JPanel wordsList;
	private JPanel errorSpace;
	private JButton submitButton;
	private JButton clearButton;
	private JSpinner freqbox;
	private JTextField textbox;
	private JScrollPane scrollPane;
	
	private URLParser parser;
	
	private int minFreq;
	
	public Screen(URLParser p) {
		parser = p;
		
		minFreq = 10;
		
		frame = new JFrame("Web Reader");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		submitField = new JPanel();
		freqField = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 1));
		wordsList = new JPanel(new GridLayout(0, 2));
		errorSpace = new JPanel();
		submitButton = new JButton("Submit");
		clearButton = new JButton("Clear");
		freqbox = new JSpinner(new SpinnerNumberModel(10, 0, 999, 1));
		textbox = new JTextField(30);
		scrollPane = new JScrollPane(wordsList);
		scrollPane.setPreferredSize(new Dimension(640, 480));
		
		submitButton.addActionListener(new URLSubmitButtonListener(this, parser, textbox, freqbox));
		clearButton.addActionListener(new ClearButtonListener(this));
		
		textbox.setText("en.wikipedia.org/wiki/Mandelbrot_Set");
		
		submitField.add(new Label("http://"));
		submitField.add(textbox);
		submitField.add(submitButton);
		submitField.add(clearButton);
		freqField.add(new Label("Hide words that appear fewer than"));
		freqField.add(freqbox);
		freqField.add(new Label("times."));
		
		panel.add(submitField);
		panel.add(freqField);
		panel.add(errorSpace);
		panel.add(scrollPane);
		
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(640, 480));
	}
	
	public void Update() {
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setWords(HashMap2 hashMap) {
		wordsList.removeAll();
		errorSpace.removeAll();
		List<String> l = hashMap.reverseSortedKeys();
		Iterator<String> iter = l.iterator();
		String s, v;
		boolean empty = true;
		
		while(iter.hasNext()) {
			s = iter.next();
			v = hashMap.get(s);
			if(Integer.parseInt(v) < minFreq) break;
			wordsList.add(new Label(s));
			wordsList.add(new Label(hashMap.get(s)));
			empty = false;
		}
		
		if(empty) {
			showError("No words found.");
		}
	}
	
	public void setMinFreq(int f) {
		this.minFreq = f;
	}
	
	public void showError(String s) {
		wordsList.removeAll();
		errorSpace.removeAll();
		Label l = new Label(s);
		l.setForeground(Color.red);
		errorSpace.add(l);
	}
	
	void clear() {
		wordsList.removeAll();
		errorSpace.removeAll();
	}
}

class ClearButtonListener implements ActionListener {
	Screen screen;
	
	public ClearButtonListener(Screen screen) {
		this.screen = screen;
	}
	
	public void actionPerformed(ActionEvent event) {
		screen.clear();
	}
}

class URLSubmitButtonListener implements ActionListener {
	Screen screen;
	URLParser parser;
	JTextComponent textbox;
	JSpinner freqbox;
	
	public URLSubmitButtonListener(Screen screen, URLParser parser, JTextComponent textbox, JSpinner freqbox) {
		this.screen = screen;
		this.parser = parser;
		this.textbox = textbox;
		this.freqbox = freqbox;
	}
	
	public void actionPerformed(ActionEvent event) {
		String url = "http://" + textbox.getText();
		int f = (Integer) freqbox.getValue();
		screen.setMinFreq(f);
		
		try {
			parser.setURL(url);
			screen.setWords(parser.getWordMap());
		} catch(IllegalArgumentException e) {
			screen.showError("\"" + url + "\" is not a valid URL.");
		} catch(Exception e) {
			screen.showError(e.toString());
			throw e;
		}
		screen.Update();
	}
}
