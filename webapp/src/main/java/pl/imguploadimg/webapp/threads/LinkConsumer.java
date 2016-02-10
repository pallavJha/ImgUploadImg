package pl.imguploadimg.webapp.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;

import pl.imguploadimg.base.exception.NoContentTypeAvailabeException;
import pl.imguploadimg.base.helper.ImageTypeExtensionCombo;
import pl.imguploadimg.base.log.LoggerService;

public class LinkConsumer extends Thread {

	private List<String> anchorList;
	private List<String> imageList;
	private URL url;
	private String protocol;
	private String protocolHost;
	private LinkQueue queue;
	private LoggerService loggerService;
	private UrlValidator urlValidator = new UrlValidator();
	
	private String HTML_TYPE = "HTML";

	private String HTML_CONTENT_TYPE = "text/html";

	private String IMAGE_TYPE = "IMAGE";

	private String NON_HTML_NON_IMAGE_TYPE = "OTHERS";

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
	
	public void run() {
		int  i = 0;
        while (!queue.isEmpty()) {
        	List<String> images = null;
            loggerService.log("Consumer Thread : " + (++i));
            try {
            	images = consume();
            } catch (Exception ex) {
            	loggerService.log("Exception occured in consumer thread : "+ ex.getMessage());
            	ex.printStackTrace();
            }
			if (images != null && images.size() > 0) {
				Iterator<String> iter = images.iterator();
				while (iter.hasNext()) {
					imageList.add(iter.next());
				}
			}
        }
    }

    private List<String> consume() throws Exception {
        if (queue.isEmpty()) {
            return null;
        }
        
        String currentUrl = null;
        synchronized (queue) {
        	currentUrl = queue.deQueue();
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
    			List<String> images = findImagesFromHTML(sb.toString(), protocol, protocolHost);
    			return images;
    		}
    	}
		return null;
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