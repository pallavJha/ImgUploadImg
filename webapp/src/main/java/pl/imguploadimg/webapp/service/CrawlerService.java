package pl.imguploadimg.webapp.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
		LinkConsumer consumer1 = new LinkConsumer(links, images, url, protocol, protocolHost, loggerService, queue);
		LinkConsumer consumer2 = new LinkConsumer(links, images, url, protocol, protocolHost, loggerService, queue);
		LinkConsumer consumer3 = new LinkConsumer(links, images, url, protocol, protocolHost, loggerService, queue);
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		executorService.submit(consumer1);
		executorService.submit(consumer2);
		executorService.submit(consumer3);
		producer.start();
		//consumer.start();
		//Thread.currentThread().join();
		executorService.shutdown();
		if (!executorService.awaitTermination(60, TimeUnit.SECONDS)){
		    System.err.println("Threads didn't finish in 60 seconds!");
		    return images;
		}
		return images;
	}
}