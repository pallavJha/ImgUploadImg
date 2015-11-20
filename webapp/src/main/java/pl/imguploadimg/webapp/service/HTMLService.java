package pl.imguploadimg.webapp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

	static String HTML_TYPE = "HTML";

	static String HTML_CONTENT_TYPE = "text/html";

	static String IMAGE_TYPE = "IMAGE";

	static String NON_HTML_NON_IMAGE_TYPE = "OTHERS";

	public void findImagesInInputStream(String url) throws IOException {

		URL postURL = new URL(url);
		loggerService.logUrlData(postURL);
		InputStream inputStream;

		HttpURLConnection urlConnection = (HttpURLConnection) postURL.openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");
		String mime = findMIME(urlConnection);
		if (mime == null) {
			urlConnection.disconnect();
			throw new NoContentTypeAvailabeException("Content Type not found for the url.");
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
			
			findImagesFromHTML(sb.toString());
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

	public void findImagesFromHTML(String htmlString) {
		ArrayList<String> imgList = new ArrayList<String>();
		String str = htmlString;
		char[] cbuf = str.toCharArray();
		for (int i = 0; i < cbuf.length; i++) {
			if (cbuf[i] == '<' && cbuf[i + 1] == 'i' && cbuf[i + 2] == 'm'
					&& cbuf[i + 3] == 'g') {
				for (int j = i; j < cbuf.length; j++) {
					if (cbuf[j] == '>') {
						char[] tempCharArr = new char[(j - i) + 2];
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
