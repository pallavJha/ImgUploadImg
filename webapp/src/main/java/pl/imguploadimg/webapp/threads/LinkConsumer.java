package pl.imguploadimg.webapp.threads;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pl.imguploadimg.base.log.LoggerService;

public class LinkConsumer extends Thread {

	private List<String> anchorList;
	private URL url;
	private String protocol;
	private String protocolHost;
	private LoggerService loggerService;

	public LinkConsumer(List<String> anchorList, URL url, String protocol,String protocolHost, LoggerService loggerService) {
		
		super(protocolHost.replace(protocol, "").replaceAll("/", ""));
		this.anchorList = anchorList;
		this.url = url;
		this.protocol = protocol;
		this.protocolHost = protocolHost;
		this.loggerService = loggerService;
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
						imgList.add(new String(tempCharArr).trim()+">");
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
