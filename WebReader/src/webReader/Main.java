package webReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import hashMap.HashMap2;

public class Main {
	
	private static final String DEFAULT_URL = "http://en.wikipedia.org/wiki/Mandelbrot_set";

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean validURL = false;
		String[] parsed;
		HashMap2 hashMap = new HashMap2();
		
		do {
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
		} while(!validURL);
		
		for(String word : parsed) {
			if(hashMap.exists(word)) {
				hashMap.set(word, Integer.toString(Integer.parseInt(hashMap.get(word))+1));
			}
		}
		
		System.out.println(hashMap);
		
		sc.close();
	}
	
	public static InputStream parseURL(String s) {
		try {
			URL url = new URL(s);
			return url.openStream();
		} catch(Exception e) {
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
	
	private static String[] parseHTML(String s) {
		Pattern p = Pattern.compile("<body.*?>.*?</body>", Pattern.DOTALL);
		Matcher m = p.matcher(s);
		m.find();
		s = s.substring(m.start(), m.end());
		
		StringBuilder sb = new StringBuilder();
		for(String sub : s.split("<script.*?>(.|\\n|\\r)*?</script>")) {
			for(String sub2 : sub.split("<(.|\\n|\\r)*?>")) {
				for(String word : sub2.split("[^a-zA-Z]+")) {
					if(word.equals("")) continue;
					sb.append(word);
					sb.append(" ");
				}
			}
		}
		s = sb.toString();
		String[] strlist = s.split(" ");
		return strlist;
	}
}
