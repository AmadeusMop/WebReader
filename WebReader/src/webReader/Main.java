package webReader;

import hashMap.HashMap2;

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
		Scanner sc = new Scanner(System.in);
		//boolean validURL = false;
		List<String> parsed;
		String s;
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
		
		try {
			parsed = parseHTML(getHTML(parseURL(DEFAULT_URL)));
			
		} 
		catch(Exception e) {
			System.out.println(e);
			parsed = new ArrayList<String>();
		}
		
		sc.close();
			
		for(String word : parsed) {
			if(hashMap.exists(word)) {
				hashMap.set(word, Integer.toString(Integer.parseInt(hashMap.get(word))+1));
			} else {
				hashMap.add(word, "1");
			}
		}
		
		List<String> sorted = hashMap.reverseSortedKeys();
		Iterator<String> iter = sorted.iterator();
		while(iter.hasNext()) {
			s = iter.next();
			System.out.println(s + ": " + hashMap.get(s));
		}
		
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
