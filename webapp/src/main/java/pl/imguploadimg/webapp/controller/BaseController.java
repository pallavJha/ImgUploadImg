package pl.imguploadimg.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

	@RequestMapping("home")
	public String functionWithoutName(HttpServletRequest req, HttpSession session){
		return "home";
	}
}
