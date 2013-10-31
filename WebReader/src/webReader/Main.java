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
	private static final int MIN_WORDS = 10;

	public static void main(String[] args) {
		//Scanner sc = new Scanner(System.in);
		//boolean validURL = false;
		String s, v;
		URLParser parser = new URLParser(DEFAULT_URL);
		List<String> words;
		Iterator<String> iter;
		HashMap2 hashMap = new HashMap2();
		Screen screen = new Screen();
		
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
		
		hashMap = parser.getWordMap();
		words = hashMap.reverseSortedKeys();
		iter = words.iterator();
		
		JPanel wordsList = new JPanel(new GridLayout(0, 2));
		
		while(iter.hasNext()) {
			s = iter.next();
			v = hashMap.get(s);
			if(Integer.parseInt(v) < MIN_WORDS) break;
			wordsList.add(new Label(s));
			wordsList.add(new Label(hashMap.get(s)));
		}
		
		screen.Update();
		screen.addWordsList(wordsList);
	}
}
