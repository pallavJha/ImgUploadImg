package pl.imguploadimg.webapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${useSystemProxy}")
	Boolean useSystemProxy;

	@Value("${proxyURL}")
	String proxyURL;

	@Value("${proxyPort}")
	String proxyPort;

	@Value("${username}")
	String username;

	@Value("${password}")
	String password;

	static String HTML_TYPE = "HTML";

	static String HTML_CONTENT_TYPE = "text/html";

	static String IMAGE_TYPE = "IMAGE";

	static String NON_HTML_NON_IMAGE_TYPE = "OTHERS";

	public void findImagesInInputStream(String url) throws IOException {

		if (useSystemProxy) {
			System.setProperty("java.net.useSystemProxies", "true");
			System.getProperties().put("http.proxyHost", proxyURL);
			System.getProperties().put("http.proxyPort", proxyPort);
			System.getProperties().put("http.proxyUser", username);
			System.getProperties().put("http.proxyPassword", password);
		}

		URL postURL = new URL(url);
		loggerService.logUrlData(postURL);

		URLConnection urlConnection = postURL.openConnection();
		String mime = findMIME(urlConnection);
		if(mime == null){
			throw new NoContentTypeAvailabeException("Content Type found null.");
		}
		if (mime.equals(HTML_TYPE)) {

			InputStream inputStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			StringBuilder sb = new StringBuilder("");
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			findImagesFromHTML(sb.toString());

		}
	}

	public String findMIME(URLConnection urlConnection) throws IOException {
		String contentType = urlConnection.getContentType();
		loggerService.spacedLog(null,contentType);
		
		if(contentType == null){
			return null;
		}
		
		if (contentType.contains(HTML_CONTENT_TYPE)) {
			return HTML_TYPE;
		}
		
		Set<String> imgContentTypeSet = ImageTypeExtensionCombo.typeExtMap.keySet();
		ArrayList<String> imgContentTypeArrayList = new ArrayList<String>(imgContentTypeSet);
		for (int i = 0; i < imgContentTypeArrayList.size(); i++){
			if(contentType.contains(imgContentTypeArrayList.get(i))){
				return IMAGE_TYPE;
			}
		}
		return null;
	}
	
	public void findImagesFromHTML(String htmlString) {
		ArrayList<String> imgList = new ArrayList<String>();
		String str = htmlString;
		char[] cbuf = str.toCharArray();
		for (int i = 0; i < cbuf.length; i++) {
			if (cbuf[i] == '<' && cbuf[i + 1] == 'i' && cbuf[i + 2] == 'm' && cbuf[i + 3] == 'g') {
				for (int j = i; j < cbuf.length; j++) {
					if (cbuf[j] == '>') {
						char[] tempCharArr = new char[(j - i) + 1];
						System.arraycopy(cbuf, i, tempCharArr, 0, (j - i));
						imgList.add(new String(tempCharArr));
						i = j;
						break;
					}
				}
			}
		}
		loggerService.log(imgList.size() + " image(s) found.");
		for (String s : imgList) {
			loggerService.log(s);
		}
	}
}
