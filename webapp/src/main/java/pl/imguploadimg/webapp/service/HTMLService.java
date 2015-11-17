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

	public void findImagesInInputStream(String url) throws IOException {

		if (useSystemProxy) {
			System.setProperty("java.net.useSystemProxies", "true");
			System.getProperties().put("http.proxyHost", proxyURL);
			System.getProperties().put("http.proxyPort", proxyPort);
			System.getProperties().put("http.proxyUser", username);
			System.getProperties().put("http.proxyPassword", password);
		}

		URL postURL = new URL(url);
		/*
		 * InputStream inputStream = postURL.openStream(); BufferedReader br =
		 * new BufferedReader(new InputStreamReader( inputStream)); String line;
		 * StringBuilder sb = new StringBuilder(""); while ((line =
		 * br.readLine()) != null) { sb.append(line); }
		 * loggerService.log(sb.toString());
		 */

		URLConnection urlConnection = postURL.openConnection();
		urlConnection.getContentType();
		findMIME(urlConnection);
	}

	public void findMIME(URLConnection urlConnection) throws IOException {
		String contentType = urlConnection.getContentType();
		loggerService.log("*************\n\n\n\n");
		loggerService.log(contentType);
		loggerService.log("\n\n\n\n*************");
		loggerService.log("*************\n\n\n\n");
		loggerService.log(urlConnection.getRequestProperties().toString());
		loggerService.log("\n\n\n\n*************");
		InputStream inputStream = urlConnection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream));
		String line;
		StringBuilder sb = new StringBuilder("");
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		loggerService.log(sb.toString());
		inputStream.close();
	}
}
