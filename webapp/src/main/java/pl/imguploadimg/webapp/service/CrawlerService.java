package pl.imguploadimg.webapp.service;

import java.net.URL;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.imguploadimg.base.helper.ImageTypeExtensionCombo;
import pl.imguploadimg.base.log.LoggerService;

@Service
@Qualifier("crawlerService")
public class CrawlerService {
	
	@Autowired
	@Qualifier("loggerService")
	LoggerService loggerService;
	
	@Autowired
	@Qualifier("imageTypeExtensionCombo")
	ImageTypeExtensionCombo imageTypeExtensionCombo;
	
	public void startCrawl(Set<String> links, URL url, String protocol, String protocolHost){
		
	}
}