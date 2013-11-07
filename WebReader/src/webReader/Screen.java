package webReader;

import hashMap.IntegerHashMap2;

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
import javax.swing.JCheckBox;
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
	private JPanel optionsField;
	private JPanel wordsList;
	private JPanel errorSpace;
	private JPanel resultsField;
	private JCheckBox filterCheckbox;
	private JButton submitButton;
	private JButton clearButton;
	private JSpinner freqbox;
	private JTextField textbox;
	private JScrollPane scrollPane;
	
	private IntegerHashMap2 words;
	
	private URLParser parser;
	
	private int minFreq;
	
	public Screen(URLParser p) {
		parser = p;
		words = new IntegerHashMap2();
		minFreq = 10;
		
		frame = new JFrame("Web Reader");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		submitField = new JPanel();
		freqField = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 1));
		optionsField = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 1));
		wordsList = new JPanel(new GridLayout(0, 2));
		((GridLayout) wordsList.getLayout()).setVgap(2);
		errorSpace = new JPanel();
		resultsField = new JPanel();
		resultsField.setLayout(new BoxLayout(resultsField, BoxLayout.Y_AXIS));
		filterCheckbox = new JCheckBox("Filter common words", true);
		submitButton = new JButton("Submit");
		clearButton = new JButton("Clear All");
		freqbox = new JSpinner(new SpinnerNumberModel(10, 0, 999, 1));
		textbox = new JTextField(30);
		scrollPane = new JScrollPane(wordsList);
		scrollPane.setPreferredSize(new Dimension(640, 480));
		
		submitButton.addActionListener(new URLSubmitButtonListener(this, parser, textbox, freqbox, filterCheckbox));
		clearButton.addActionListener(new ClearButtonListener(this));
		
		textbox.setText("en.wikipedia.org/wiki/Mandelbrot_Set");
		
		submitField.add(new Label("http://"));
		submitField.add(textbox);
		submitField.add(submitButton);
		freqField.add(new Label("Hide words that appear fewer than"));
		freqField.add(freqbox);
		freqField.add(new Label("times"));
		optionsField.add(filterCheckbox);
		resultsField.add(clearButton);
		resultsField.add(scrollPane);
		
		panel.add(submitField);
		panel.add(freqField);
		panel.add(optionsField);
		panel.add(errorSpace);
		panel.add(resultsField);
		
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(640, 480));
	}
	
	public void Update() {
		frame.pack();
		frame.setVisible(true);
	}
	
	public void addWords(IntegerHashMap2 hashMap) {
		wordsList.removeAll();
		errorSpace.removeAll();
		words.add(hashMap);
		List<String> l = words.reverseSortedKeys();
		Iterator<String> iter = l.iterator();
		String s;
		int v;
		boolean empty = true;
		
		while(iter.hasNext()) {
			s = iter.next();
			v = words.get(s);
			if(v < minFreq) break;
			wordsList.add(new Label(s));
			wordsList.add(new Label(Integer.toString(v)));
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
		words = new IntegerHashMap2();
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
	JCheckBox checkbox;
	
	public URLSubmitButtonListener(Screen screen, URLParser parser, JTextComponent textbox, JSpinner freqbox, JCheckBox checkbox) {
		this.screen = screen;
		this.parser = parser;
		this.textbox = textbox;
		this.freqbox = freqbox;
		this.checkbox = checkbox;
	}
	
	public void actionPerformed(ActionEvent event) {
		String url = "http://" + textbox.getText();
		int f = (Integer) freqbox.getValue();
		screen.setMinFreq(f);
		
		try {
			parser.setURL(url);
			screen.addWords(parser.getWordMap(checkbox.isSelected()));
		} catch(IllegalArgumentException e) {
			screen.showError("\"" + url + "\" is not a valid URL.");
		} catch(Exception e) {
			screen.showError(e.toString());
			throw e;
		}
		screen.Update();
	}
}
