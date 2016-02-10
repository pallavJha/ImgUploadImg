package pl.imguploadimg.webapp.service;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.imguploadimg.base.helper.ImageTypeExtensionCombo;
import pl.imguploadimg.base.log.LoggerService;
import pl.imguploadimg.webapp.threads.LinkConsumer;
import pl.imguploadimg.webapp.threads.LinkProducer;
import pl.imguploadimg.webapp.threads.LinkQueue;

@Service
@Qualifier("crawlerService")
public class CrawlerService {
	
	@Autowired
	@Qualifier("loggerService")
	LoggerService loggerService;
	
	@Autowired
	@Qualifier("imageTypeExtensionCombo")
	ImageTypeExtensionCombo imageTypeExtensionCombo;
	
	public List<String> startCrawler(List<String> links, List<String> images, URL url, String protocol, String protocolHost) throws Exception{
		LinkQueue queue = new LinkQueue(links);
		LinkProducer producer = new LinkProducer(links, url, protocol, protocolHost, queue, loggerService);
		LinkConsumer consumer = new LinkConsumer(links, images, url, protocol, protocolHost, loggerService, queue);
		producer.start();
		consumer.start();
		Thread.currentThread().join();
		return images;
	}
}