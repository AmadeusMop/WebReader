package webReader;

import hashMap.HashMap2;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLParser {
	private String url, html;
	private boolean valid;
	
	public URLParser(String url) {
		setURL(url);
	}
	
	public URLParser() {
		this.url = "";
		this.html = "";
	}
	
	public void setURL(String url) {
		this.url = url;
		this.html = parseURL();
	}
	
	public HashMap2 getWordMap() {
		List<String> parsed;
		HashMap2 hashMap = new HashMap2();
		
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
	
	public boolean isValid() {
		return valid;
	}
	
	
	String parseURLTest() throws IllegalArgumentException {
		return parseURL();
	}
	
	String getHTMLTest(InputStream s) throws IOException {
		return getHTML(s);
	}
	
	List<String> parseHTMLTest(String s) throws IllegalArgumentException {
		return parseHTML(s);
	}
	
	
	private String parseURL() {
		try {
			URL urlObject = new URL(url);
			this.html = getHTML(urlObject.openStream());
			this.valid = true;
			return html;
		} catch(IOException e) {
			this.html = "";
			this.valid = false;
			throw new IllegalArgumentException(url);
		}
	}
	
	private String getHTML(InputStream s) throws IOException {
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
		return str;
	}
	
	private List<String> parseHTML(String s) {
		List<String> wordList = new ArrayList<String>();
		
		Pattern p = Pattern.compile("<body.*?>(.|\\n|\\r).*</body.*?>", Pattern.DOTALL);
		Matcher m = p.matcher(s);
		if(m.find()) {
			s = m.group();
			
			System.out.println(s);
			
			for(String sub : s.split("<script(.|\\n|\\r)*?/script>")) {
				for(String sub2 : sub.split("<(.|\\n|\\r)*?>")) {
					for(String word : sub2.split("[^a-zA-Z]+")) {
						if(word.equals("")) continue;
						wordList.add(word.toLowerCase());
					}
				}
			}
		} else {
			System.out.println("Hello 2!");
		}
		return wordList;
	}
}
