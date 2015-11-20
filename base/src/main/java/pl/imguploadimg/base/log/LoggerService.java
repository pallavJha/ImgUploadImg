package pl.imguploadimg.base.log;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("loggerService")
public class LoggerService {
	private static Logger projectLogger = LoggerFactory
			.getLogger("pl.imguploadimg");

	public void log(String str) {
		projectLogger.info(str);
	}

	public void spacedLog(String str) {
		projectLogger.info("\n**********************************\n\n" + str
				+ "\n\n**********************************\n");
	}
	
	public void logUrlData(URL aURL) {
		log("protocol = " + aURL.getProtocol());
		log("authority = " + aURL.getAuthority());
		log("host = " + aURL.getHost());
		log("port = " + aURL.getPort());
		log("path = " + aURL.getPath());
		log("query = " + aURL.getQuery());
		log("filename = " + aURL.getFile());
		log("ref = " + aURL.getRef());
	}
}