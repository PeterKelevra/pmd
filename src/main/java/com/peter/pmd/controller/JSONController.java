package com.peter.pmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.peter.pmd.managers.HttpManager;


@Controller
//@RequestMapping("/pmd")
public class JSONController {

	@Autowired
	private HttpManager manager;
	
	@RequestMapping(value="/quality/{id}", method = RequestMethod.GET)
	public @ResponseBody String getQuality(@PathVariable String id) {

		
		return "Quality: "+manager;

	}
	
}