package pl.imguploadimg.webapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.imguploadimg.base.exception.NoContentTypeAvailabeException;
import pl.imguploadimg.base.helper.ImageTypeExtensionCombo;
import pl.imguploadimg.base.log.LoggerService;

@Service
@Qualifier("htmlService")
public class HTMLService {

	@Autowired
	@Qualifier("loggerService")
	LoggerService loggerService;

	@Autowired
	@Qualifier("imageTypeExtensionCombo")
	ImageTypeExtensionCombo imageTypeExtensionCombo;
	
	@Autowired
	@Qualifier("crawlerService")
	CrawlerService crawlerService;

	static String HTML_TYPE = "HTML";

	static String HTML_CONTENT_TYPE = "text/html";

	static String IMAGE_TYPE = "IMAGE";

	static String NON_HTML_NON_IMAGE_TYPE = "OTHERS";
	
	static UrlValidator urlValidator = new UrlValidator();

	public void findImagesInInputStream(String url) throws IOException {

		URL postURL = new URL(url);
		String protocol = postURL.getProtocol();
		String protocolHost = postURL.getProtocol() + "://" + postURL.getHost() + (postURL.getPort() == -1 ? "" : ":"+postURL.getPort());
		loggerService.logUrlData(postURL);
		InputStream inputStream;

		HttpURLConnection urlConnection = (HttpURLConnection) postURL.openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
		String mime = findMIME(urlConnection);
		if (mime == null) {
			//urlConnection.disconnect();
			//throw new NoContentTypeAvailabeException("Content Type not found for the url.");
			mime = new String(HTML_TYPE);
		}
		if (mime.equals(NON_HTML_NON_IMAGE_TYPE)) {
			urlConnection.disconnect();
			throw new NoContentTypeAvailabeException("Image or HTML only.");
		}
		if (mime.equals(HTML_TYPE)) {
			
			String line;
			inputStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			StringBuilder sb = new StringBuilder("");
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			inputStream.close();
			br.close();
			
			/*List<String> anchorList = findAnchorsFromHTML(sb.toString(), protocol, protocolHost);
			if(anchorList != null && anchorList.size() > 0) {
				anchorList.add(0, url);
			}
			else {
				findImagesFromHTML(sb.toString());
			}*/
			findImagesFromHTML(sb.toString(), protocol, protocolHost);
			urlConnection.disconnect();
		}
	}

	public String findMIME(URLConnection urlConnection) throws IOException {
		String contentType = urlConnection.getContentType();
		loggerService.spacedLog(null, contentType);

		if (contentType == null) {
			return null;
		}

		if (contentType.contains(HTML_CONTENT_TYPE)) {
			return HTML_TYPE;
		}

		Set<String> imgContentTypeSet = ImageTypeExtensionCombo.typeExtMap.keySet();
		ArrayList<String> imgContentTypeArrayList = new ArrayList<String>(imgContentTypeSet);
		
		for (int i = 0; i < imgContentTypeArrayList.size(); i++) {
			if (contentType.contains(imgContentTypeArrayList.get(i))) {
				return IMAGE_TYPE;
			}
		}
		return NON_HTML_NON_IMAGE_TYPE;
	}

	public void findImagesFromHTML(String htmlString, String protocol, String protocolHost) {
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
				s = protocol + ":" + s;
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
