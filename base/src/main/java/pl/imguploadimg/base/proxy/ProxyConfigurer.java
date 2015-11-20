package pl.imguploadimg.base.proxy;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.imguploadimg.base.log.LoggerService;

@Component()
public class ProxyConfigurer {

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

	@PostConstruct
	public void configure() {
		loggerService.log("Starting to configure proxy....");
		if (useSystemProxy) {
			System.setProperty("java.net.useSystemProxies", "true");
			System.getProperties().put("http.proxyHost", proxyURL.trim());
			System.getProperties().put("http.proxyPort", proxyPort.trim());
			System.getProperties().put("http.proxyUser", username.trim());
			System.getProperties().put("http.proxyPassword", password.trim());
			loggerService.log("Proxy configured.");
		} else {
			loggerService.log("Proxy configuration cancelled.");
		}
	}
}
