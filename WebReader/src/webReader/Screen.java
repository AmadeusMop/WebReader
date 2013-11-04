package webReader;

import hashMap.HashMap2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;


public class Screen {

	private JFrame frame;
	private JPanel panel;
	private JPanel wordsList;
	private JPanel errorSpace;
	private JButton button;
	private JTextField textbox;
	private JScrollPane scrollPane;
	
	private URLParser parser;
	
	private static final int MIN_WORDS = 10; //TODO: Make this input, not a constant
	
	public Screen(URLParser p) {
		parser = p;
		
		frame = new JFrame("Web Reader");
		panel = new JPanel();
		wordsList = new JPanel(new GridLayout(0, 2));
		errorSpace = new JPanel();
		button = new JButton("Submit");
		textbox = new JTextField(30);
		scrollPane = new JScrollPane(wordsList);
		scrollPane.setPreferredSize(new Dimension(640, 480));
		
		button.addActionListener(new URLSubmitButtonListener(this, parser, textbox));
		
		textbox.setText("http://en.wikipedia.org/wiki/Mandelbrot_Set");
		
		panel.add(textbox);
		panel.add(button);
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
			if(Integer.parseInt(v) < MIN_WORDS) break;
			wordsList.add(new Label(s));
			wordsList.add(new Label(hashMap.get(s)));
			empty = false;
		}
		
		if(empty) {
			showError("No words found.");
		}
	}
	
	public void showError(String s) {
		wordsList.removeAll();
		errorSpace.removeAll();
		Label l = new Label(s);
		l.setForeground(Color.red);
		errorSpace.add(l);
	}
}

class URLSubmitButtonListener implements ActionListener {
	Screen screen;
	URLParser parser;
	JTextComponent textbox;
	
	public URLSubmitButtonListener(Screen screen, URLParser parser, JTextComponent textbox) {
		this.screen = screen;
		this.parser = parser;
		this.textbox = textbox;
	}
	
	public void actionPerformed(ActionEvent event) {
		String url = textbox.getText();
		
		try {
			parser.setURL(url);
			screen.setWords(parser.getWordMap());
		} catch(IllegalArgumentException e) {
			screen.showError("\"" + url + "\" is not a valid URL.");
		} catch(Exception e) {
			screen.showError(e.getMessage());
		}
		screen.Update();
	}
}
