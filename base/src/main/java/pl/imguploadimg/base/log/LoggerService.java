package pl.imguploadimg.base.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("loggerService")
public class LoggerService {
	private static Logger projectLogger = LoggerFactory.getLogger("pl.imguploadimg");

	public void log(String str) {
		projectLogger.info(str);
	}
}
