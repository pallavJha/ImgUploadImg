package pl.imguploadimg.webapp.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;

import pl.imguploadimg.base.exception.NoContentTypeAvailabeException;
import pl.imguploadimg.base.helper.ImageTypeExtensionCombo;
import pl.imguploadimg.base.log.LoggerService;

public class LinkProducer extends Thread {

	private List<String> anchorList;
	private URL url;
	private String protocol;
	private String protocolHost;
	private UrlValidator urlValidator = new UrlValidator();
	private LinkQueue queue;
	private LoggerService loggerService;
	private int MAX_QUEUE_SIZE = 100;
	private boolean stopThread = false;

	private String HTML_TYPE = "HTML";
	private String HTML_CONTENT_TYPE = "text/html";
	private String IMAGE_TYPE = "IMAGE";
	private String NON_HTML_NON_IMAGE_TYPE = "OTHERS";
	
	public LinkProducer(List<String> anchorList, URL url, String protocol,String protocolHost, LinkQueue queue, LoggerService loggerService) {
		
		super(protocolHost.replace(protocol, "").replaceAll("/", ""));
		this.anchorList = anchorList;
		this.url = url;
		this.protocol = protocol;
		this.protocolHost = protocolHost;
		this.queue = queue;
		this.loggerService = loggerService;
		
	}
	
	
	public void run() {
		int i = 0;
        while(true) {
        	List<String> anchors = null;
            loggerService.log("Producer Thread : " + (++i));
            try {
            	anchors = produce();
            } catch (Exception ex) {
            	loggerService.log("Exception occured in producer thread : "+ ex.getMessage());
            	ex.printStackTrace();
            	if(stopThread){
            		break;
            	}
            }
            if(stopThread){
        		break;
        	}
            if(anchors != null && anchors.size() > 0){
            	Iterator<String> iter = anchors.iterator();
            	while(iter.hasNext()){
            		synchronized (queue) {
            			queue.enQueue(iter.next());
            		}
            	}
            }
        }
    }

    private List<String> produce() throws Exception {

        if (queue.size() == MAX_QUEUE_SIZE) {
        	stopThread = true;
            return null;
        }
        String currentUrl = null;
        synchronized (queue) {
        	currentUrl = queue.next();
        }
        if(currentUrl != null){

    		URL postURL = new URL(currentUrl);
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
    			throw new NoContentTypeAvailabeException("Image or HTML only."+postURL);
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
    			urlConnection.disconnect();
    			List<String> anchorList = findAnchorsFromHTML(sb.toString(), protocol, protocolHost);
    			return anchorList;
    		}
    	}
		return null;
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
}