package pl.imguploadimg.webapp.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.xml.serialize.HTMLSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.imguploadimg.base.exception.NoContentTypeAvailabeException;
import pl.imguploadimg.base.log.LoggerService;
import pl.imguploadimg.webapp.service.HTMLService;

@Controller
public class BaseController {

	@Autowired
	@Qualifier("loggerService")
	LoggerService loggerService;

	@Autowired
	@Qualifier("htmlService")
	HTMLService htmlService;

	@RequestMapping("home")
	public String functionWithoutName(HttpServletRequest req,
			HttpSession session) {
		loggerService.log(req.getLocalAddr() + " for home\n");
		return "home";
	}

	@RequestMapping(value = "url", method = RequestMethod.GET)
	public String socketConnection(@RequestParam("url") String url,
			HttpServletRequest req, HttpSession session) throws IOException {
		htmlService.findImagesInInputStream(url);

		// System.
		return null;
	}

	@ExceptionHandler(IOException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	Object handleExceptions(Exception mue, HttpServletRequest request,
			HttpServletResponse response) {

		String msg;
		loggerService.log(mue.toString());
		mue.printStackTrace();
		if (mue instanceof MalformedURLException) {
			msg = "Malformed URL has been sent.";
		} else if (mue instanceof NoContentTypeAvailabeException) {
			msg = mue.getMessage();
		} else if (mue instanceof IOException) {
			msg = "I/O anomaly.";
		} else {
			msg = "Some problem has occured.";
		}
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<byte[]> fileEntity = new HttpEntity<byte[]>(
					msg.getBytes(), responseHeaders);
			return fileEntity;
		} else {
			request.setAttribute("errorMessageObject", msg);
			request.setAttribute("errorMessage", "Internal Server Error");
			return "error500";
		}

	}
}
