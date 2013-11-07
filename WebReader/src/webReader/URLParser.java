package webReader;

import hashMap.IntegerHashMap2;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLParser {
	private static final String[] stopWordsArray = {"", "a", "able", "about", "across", "after", "all", "almost", "also", "am", "among", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", "however", "i", "if", "in", "into", "is", "it", "its", "just", "least", "let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she", "should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your"};
	
	private static final List<String> stopWords = Arrays.asList(stopWordsArray);
	
	private String url, html;
	private boolean valid, filterStopWords;
	
	public URLParser(String url) {
		this();
		setURL(url);
	}
	
	public URLParser() {
		this.url = "";
		this.html = "";
		this.filterStopWords = true;
	}
	
	public void setURL(String url) {
		this.url = url;
		this.html = parseURL();
	}
	
	public IntegerHashMap2 getWordMap(boolean filter) {
		filterStopWords = filter;
		return getWordMap();
	}
	
	public IntegerHashMap2 getWordMap() {
		List<String> parsed;
		IntegerHashMap2 hashMap = new IntegerHashMap2();
		
		parsed = parseHTML(html);
		
		for(String word : parsed) {
			if(hashMap.exists(word)) {
				hashMap.set(word, hashMap.get(word)+1);
			} else {
				hashMap.add(word, 1);
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
		
		Pattern p = Pattern.compile("<body.*?>(.|\n|\r).*</body.*?>", Pattern.DOTALL);
		Matcher m = p.matcher(s);
		if(m.find()) {
			s = m.group();

			s = s.toLowerCase();
			s = s.replace("\n", " ");
			s = s.replaceAll("<script*?/script>", "");
			s = s.replaceAll("<.*?>", "");
			
			for(String word : s.split("[^a-zA-Z]+")) {
				if(filterStopWords) {
					if(stopWords.contains(word)) continue;
				}
				wordList.add(word.toLowerCase());
			}
		} else {
		}
		return wordList;
	}
}
