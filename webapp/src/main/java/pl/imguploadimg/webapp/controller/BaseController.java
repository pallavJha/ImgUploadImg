package pl.imguploadimg.webapp.controller;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class BaseController {

	@RequestMapping("home")
	public String functionWithoutName(HttpServletRequest req,
			HttpSession session) {
		return "home";
	}

	public String socketConnection(@RequestParam("url") String url,
			HttpServletRequest req, HttpSession session)
			throws MalformedURLException {
		URL postURL = new URL("http://jsonplaceholder.typicode.com/posts/");
		return null;
	}

	@ExceptionHandler(MalformedURLException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	Object handleExceptions(MalformedURLException mue,
			HttpServletRequest request, HttpServletResponse response) {

		//BaseLoggers.flowLogger.info(mue.toString());
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<byte[]> fileEntity = new HttpEntity<byte[]>(mue
					.getMessage().getBytes(), responseHeaders);
			return fileEntity;
		} else {
			request.setAttribute("errorMessageObject", mue.toString());
			request.setAttribute("errorMessage", "Internal Server Error");
			return "error";
		}

	}
}
