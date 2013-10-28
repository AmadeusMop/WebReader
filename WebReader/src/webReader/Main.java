package webReader;

import hashMap.HashMap2;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

/*
 * TODO:
 *  -Add URL input functionality
 *  -Add Swing stuff
 *  -Add Swing input functionality
 *  -Remove SuppressWarnings
 *  -Move input to a separate method
 *  -Add unit tests:
 *  	-URL parse test
 *  	-HTML parse test
 *  	-Graceful input fail test
 */

@SuppressWarnings("deprecation")
public class Main {
	
	private static final String DEFAULT_URL = "http://en.wikipedia.org/wiki/Mandelbrot_set";

	public static void main(String[] args) {
		//Scanner sc = new Scanner(System.in);
		//boolean validURL = false;
		String url = DEFAULT_URL, s;
		List<String> words;
		Iterator<String> iter;
		HashMap2 hashMap = new HashMap2();
		
		/*do {
			System.out.print("URL: ");
			String url = sc.next();
			if(url.equals("d")) url = DEFAULT_URL;
			try {
				String html = getHTML(parseURL(url));
				parsed = parseHTML(html);
				validURL = true;
			} catch(Exception e) { //TODO: Handle various exceptions and print error text.
				System.out.println(e);
				validURL = false;
				parsed = new String[0];
			}
		} while(!validURL);*/
		
		hashMap = getWords(url);
		words = hashMap.reverseSortedKeys();
		iter = words.iterator();
		
		JFrame frame = new JFrame("Web Reader");
		JPanel panel = new JPanel();
		JPanel wordsList = new JPanel(new GridLayout(0, 2));
		JScrollPane scrollPane = new JScrollPane(wordsList);
		
		while(iter.hasNext()) {
			s = iter.next();
			System.out.println(s + ": " + hashMap.get(s));
			wordsList.add(new Label(s));
			wordsList.add(new Label(hashMap.get(s)));
		}
		
		panel.add(scrollPane);
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(640, 480));
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static HashMap2 getWords(String url) {
		List<String> parsed;
		String html;
		HashMap2 hashMap = new HashMap2();
		
		try {
			html = getHTML(parseURL(url));
		} catch(IOException e) {
			html = "";
		}
		
		parsed = parseHTML(html);
		
		for(String word : parsed) {
			if(hashMap.exists(word)) {
				hashMap.set(word, Integer.toString(Integer.parseInt(hashMap.get(word))+1));
			} else {
				hashMap.add(word, "1");
			}
		}
		
		return hashMap;
	}
	
	public static InputStream parseURL(String s) {
		try {
			URL url = new URL(s);
			return url.openStream();
		} catch(Exception e) { //TODO: Better exception handling.
			System.out.println(e);
			return new StringBufferInputStream(s);
		}
	}
	
	public static String getHTML(InputStream s) throws IOException {
		byte[] barray = new byte[20];
		byte[] temp;
		int i = 0, b = s.read();
		while(b != -1) {
			if(i == barray.length) {
				temp = Arrays.copyOf(barray, i+20);
				barray = temp;
			}
			barray[i] = (byte)b;
			i++;
			b = s.read();
		}
		String str = new String(barray);
		//str = 
		return str;
	}
	
	private static List<String> parseHTML(String s) {
		Pattern p = Pattern.compile("<body.*?>.*?</body>", Pattern.DOTALL);
		Matcher m = p.matcher(s);
		m.find();
		s = s.substring(m.start(), m.end());
		
		List<String> wordList = new ArrayList<String>();
		for(String sub : s.split("<script.*?>(.|\\n|\\r)*?</script>")) {
			for(String sub2 : sub.split("<(.|\\n|\\r)*?>")) {
				for(String word : sub2.split("[^a-zA-Z]+")) {
					if(word.equals("")) continue;
					wordList.add(word.toLowerCase());
				}
			}
		}
		return wordList;
	}
}
