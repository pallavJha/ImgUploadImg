package pl.imguploadimg.webapp.threads;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import pl.imguploadimg.base.log.LoggerService;

public class LinkConsumer extends Thread {

	private List<String> anchorList;
	private List<String> imageList;
	private URL url;
	private String protocol;
	private String protocolHost;
	private LinkQueue queue;
	private LoggerService loggerService;
	static UrlValidator urlValidator = new UrlValidator();

	public LinkConsumer(List<String> anchorList, List<String> imageList, URL url, String protocol,String protocolHost, LoggerService loggerService, LinkQueue queue) {
		
		super(protocolHost.replace(protocol, "").replaceAll("/", ""));
		this.anchorList = anchorList;
		this.imageList = imageList;
		this.url = url;
		this.protocol = protocol;
		this.protocolHost = protocolHost;
		this.queue = queue;
		this.loggerService = loggerService;
	}
	
	public List<String> findImagesFromHTML(String htmlString, String protocol, String protocolHost) {
		LinkedList<String> imgList = new LinkedList<String>();
		LinkedList<String> imgUrlList = new LinkedList<String>();
		String str = htmlString;
		char[] cbuf = str.toCharArray();
		for (int i = 0; i < cbuf.length; i++) {
			if (cbuf[i] == '<' && cbuf[i + 1] == 'i' && cbuf[i + 2] == 'm'
					&& cbuf[i + 3] == 'g') {
				for (int j = i; j < cbuf.length; j++) {
					if (cbuf[j] == '>') {
						char[] tempCharArr = new char[(j - i) + 2];
						System.arraycopy(cbuf, i, tempCharArr, 0, (j - i));
						imgList.add(new String(tempCharArr).trim()+">");
						i = j;
						break;
					}
				}
			}
		}
		loggerService.log(imgList.size() + " image(s) found.");
		for (String s : imgList) {
			complete: for (int i = 0; i < s.length(); i++){
				if(s.charAt(i)=='s' && s.charAt(i+1) == 'r' && s.charAt(i+2) == 'c'){
					for (int j = i+3; j < s.length(); j++) {
						if(s.charAt(j) == '='){
							for (int k = j; k < s.length(); k++) {
								if(s.charAt(k) == '\"' || s.charAt(k) == '\''){
									for (int l = k+1; l < s.length(); l++) {
										if(s.charAt(l) == '\"' || s.charAt(l) == '\''){
											imgUrlList.add(s.substring(k+1, l).trim());
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
		imgList.clear();
		for(int i = 0; i< imgUrlList.size();i++){
			String s = imgUrlList.get(i);
			if(s.length() > 2  && s.charAt(0) == '/' && s.charAt(1) == '/'){
				s = protocol +":"+ s;
				imgUrlList.remove(i);
				imgUrlList.add(i, s);
			}
			else if(s.length() > 2  && s.charAt(0) == '/'){
				s = protocolHost + s;
				imgUrlList.remove(i);
				imgUrlList.add(i, s);
			}
			if(!urlValidator.isValid(s)){
				imgUrlList.remove(i);
			}
			else {
				imgList.add(imgUrlList.get(i));
			}
		}
		imgUrlList.clear();
		
		for(String s : imgList){
			StringBuilder imgElement = new StringBuilder("<img src=\"");
			imgElement.append(s);
			imgElement.append("\" width=\"200\" height=\"200\">");
			imgUrlList.add(imgElement.toString());
			loggerService.log(imgElement.toString());
		}
		return imgUrlList;
	}
}