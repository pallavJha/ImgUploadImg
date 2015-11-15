package pl.imguploadimg.webapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.imguploadimg.base.log.LoggerService;

@Service
@Qualifier("htmlService")
public class HTMLService {

	@Autowired
	@Qualifier("loggerService")
	LoggerService loggerService;

	public void findImagesInInputStream(String url) throws IOException {
		URL postURL = new URL(url);
		InputStream inputStream = postURL.openStream();

		/*
		 * BufferedReader br = new BufferedReader(new InputStreamReader(
		 * inputStream)); String line; StringBuilder sb = new StringBuilder("");
		 * while ((line = br.readLine()) != null) { sb.append(line); }
		 * loggerService.log(sb.toString());
		 */

		findMIME(inputStream);
	}

	public void findMIME(InputStream inputStream) throws IOException {
		//inputStream.rea
		Integer a = inputStream.read();
		loggerService.log(a.toString());
		a = inputStream.read();
		loggerService.log(a.toString());
		a = inputStream.read();
		loggerService.log(a.toString());
		a = inputStream.read();
		loggerService.log(a.toString());
		
	}
}
