package webReader;

import hashMap.HashMap2;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLParser {
	private String url, html;
	private List<String> words;
	private boolean valid;
	
	public static void main(String[] args) {
		
	}
	
	public URLParser(String url) {
		this.url = url;
		this.html = parseURL(url);
	}
	
	public void setURL(String url) {
		this.url = url;
		this.html = parseURL(url);
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
	
	public String parseURL(String s) {
		try {
			URL url = new URL(s);
			String html = getHTML(url.openStream());
			this.valid = true;
			return html;
		} catch(Exception e) { //TODO: Better exception handling.
			System.out.println(e);
			this.valid = false;
			return "";
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
		//str = 
		return str;
	}
	
	private List<String> parseHTML(String s) {
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
