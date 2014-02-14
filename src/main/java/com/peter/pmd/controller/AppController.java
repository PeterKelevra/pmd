package com.peter.pmd.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.peter.pmd.managers.Constants;
import com.peter.pmd.managers.DownloadSongThread;
import com.peter.pmd.managers.HttpManager;


@Controller
//@RequestMapping("/pmd")
public class AppController {

	
	private static final Logger LOG = LoggerFactory.getLogger(AppController.class);
	
	@Autowired
	private HttpManager manager;
	
	//@Autowired
	private DownloadSongThread downloadService;
	
	
	@RequestMapping(value="/quality/{id}", method = RequestMethod.GET)
	public @ResponseBody String getQuality(@PathVariable String id, HttpSession session) {
		
		try {
			return  session.getServletContext().getContextPath() + manager.doPost(Constants.QUALITY_URL, Constants.ID.concat(id));
		} catch (Exception e) {
			return "";
		}

	}
	
	
	@RequestMapping(value="/download", method = RequestMethod.GET)
	public @ResponseBody String downloadSong(@RequestParam("id") String id, @RequestParam("data") String data) {
		
		try {
			System.out.println(">>>> data: "+ data);
			LOG.debug(">>>> data: {}", data);
			//return  manager.doPost(API_URL, FETCH_PARAMS.concat(data));
			String downloadCode = manager.doPost(Constants.FETCH_URL, "data="+data);
			downloadCode = downloadCode.replaceAll("\"", "");
			
			System.out.println(">>>> dowloadCode: "+ downloadCode);
			downloadService = new DownloadSongThread(manager, id, downloadCode);
			System.out.println(downloadService);
			//downloadService.setParams(id, downloadCode);
			downloadService.start();
			return downloadCode;
		} catch (Exception e) {
			LOG.error("Process error.", e);
			return "";
		}
	}
	
}