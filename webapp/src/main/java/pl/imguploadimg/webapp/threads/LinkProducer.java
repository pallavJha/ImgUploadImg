package pl.imguploadimg.webapp.threads;

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;

import pl.imguploadimg.base.log.LoggerService;

public class LinkProducer extends Thread {

	private List<String> anchorList;
	private URL url;
	private String protocol;
	private String protocolHost;
	private UrlValidator urlValidator = new UrlValidator();
	private LoggerService loggerService;
	
	public LinkProducer(List<String> anchorList, URL url, String protocol,String protocolHost, LoggerService loggerService) {
		
		super(protocolHost.replace(protocol, "").replaceAll("/", ""));
		this.anchorList = anchorList;
		this.url = url;
		this.protocol = protocol;
		this.protocolHost = protocolHost;
		this.loggerService = loggerService;
		
	}
	
	public List<String> findAnchorsFromHTML(String htmlString, String protocol, String protocolHost) {
		List<String> anchorList = new LinkedList<String>();
		List<String> linksList = new LinkedList<String>();
		Set<String> links = new HashSet<String>();
		String str = htmlString;
		char[] cbuf = str.toCharArray();
		for (int i = 0; i < cbuf.length; i++) {
			if (cbuf[i] == '<' && cbuf[i + 1] == 'a') {
				for (int j = i; j < cbuf.length; j++) {
					if (cbuf[j] == '<' && cbuf[j+1] == '/' && cbuf[j+2] == 'a' && cbuf[j+3] == '>') {
						char[] tempCharArr = new char[(j - i) + 4];
						System.arraycopy(cbuf, i, tempCharArr, 0, (j + 3 - i));
						anchorList.add(new String(tempCharArr).trim()+">");
						i = j;
						break;
					}
				}
			}
		}
		loggerService.log(anchorList.size() + " [anchor/link](s) found.");
		for (String s : anchorList) {
			loggerService.log(s);
			complete:for (int i = 0; i < s.length(); i++) {
				if(s.charAt(i) == 'h' && s.charAt(i+1) == 'r'  && s.charAt(i+2) == 'e' && s.charAt(i+3) == 'f'){
					for (int j = i + 4; j < s.length(); j++) {
						if (s.charAt(j) == '=') {
							for (int k = j+1; k < s.length(); k++) {
								if(s.charAt(k) == '\"' || s.charAt(k) == '\''){
									for (int l = k+1; l < s.length(); l++) {
										if(s.charAt(l) == '\"' || s.charAt(l) == '\''){
											linksList.add(s.substring(k+1, l));
											break complete;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		for(int i = 0; i< linksList.size();i++){
			String s = linksList.get(i);
			if(s.length() > 2  && s.charAt(0) == '/' && s.charAt(1) == '/'){
				s = protocol + s;
				linksList.remove(i);
				linksList.add(i, s);
			}
			else if(s.length() > 2  && s.charAt(0) == '/'){
				s = protocolHost + s;
				linksList.remove(i);
				linksList.add(i, s);
			}
			if(!urlValidator.isValid(s)){
				linksList.remove(i);
			}
			else {
				links.add(linksList.get(i));
			}
		}
		
		for(String s : links){
			loggerService.log(s);
		}
		anchorList.clear();
		anchorList.addAll(links);
		return anchorList;
	}
}