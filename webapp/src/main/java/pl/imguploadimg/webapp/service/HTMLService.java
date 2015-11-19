package pl.imguploadimg.webapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.imguploadimg.base.log.LoggerService;

@Service
@Qualifier("htmlService")
public class HTMLService {

	@Autowired
	@Qualifier("loggerService")
	LoggerService loggerService;

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
		urlData(postURL);
		/*
		 * InputStream inputStream = postURL.openStream(); BufferedReader br =
		 * new BufferedReader(new InputStreamReader( inputStream)); String line;
		 * StringBuilder sb = new StringBuilder(""); while ((line =
		 * br.readLine()) != null) { sb.append(line); }
		 * loggerService.log(sb.toString());
		 */
		
		URLConnection urlConnection = postURL.openConnection();
		findMIME(urlConnection);
	}

	public void findMIME(URLConnection urlConnection) throws IOException {
		String contentType = urlConnection.getContentType();
		loggerService.spacedLog(contentType);
	}
	
	public void urlData(URL aURL){
		loggerService.log("protocol = " + aURL.getProtocol());
        loggerService.log("authority = " + aURL.getAuthority());
        loggerService.log("host = " + aURL.getHost());
        loggerService.log("port = " + aURL.getPort());
        loggerService.log("path = " + aURL.getPath());
        loggerService.log("query = " + aURL.getQuery());
        loggerService.log("filename = " + aURL.getFile());
        loggerService.log("ref = " + aURL.getRef());
	}
}
