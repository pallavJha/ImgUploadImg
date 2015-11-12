package pl.imguploadimg.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

	@RequestMapping("home")
	public String functionWithoutName(){
		return "home";
	}
}
