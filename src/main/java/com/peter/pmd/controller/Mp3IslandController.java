package com.peter.pmd.controller;

import java.net.URLEncoder;

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

import com.peter.pmd.managers.DownloadSongThread;
import com.peter.pmd.managers.HttpManager;
import com.peter.pmd.util.AntiSamyFilter;
import com.peter.pmd.util.Constants;


@Controller
@RequestMapping("/mp3island")
public class Mp3IslandController {

	
	private static final Logger LOG = LoggerFactory.getLogger(Mp3IslandController.class);
	
	@Autowired
	private HttpManager manager;
	
	@Autowired
	private AntiSamyFilter filter;
	
	
	@RequestMapping(value="/search/{query}", method = RequestMethod.GET)
	public @ResponseBody String search(@PathVariable String query) {
		
		try {
			LOG.debug("Query: {}", query);
			String result =  manager.doPost(Constants.SEARCH_URL.concat(URLEncoder.encode(query, "UTF-8")));
			result = filter.filterString(result);
			LOG.debug("result: {}", result);
			return result;
		} catch (Exception e) {
			LOG.error("Error en la busqueda", e);
			return "";
		}
	}

	
	@RequestMapping(value="/quality/{id}", method = RequestMethod.GET)
	public @ResponseBody String getQuality(@PathVariable String id, HttpSession session) {
		
		try {
			return  session.getServletContext().getContextPath() + manager.doPost(Constants.QUALITY_URL, Constants.ID.concat(id));
		} catch (Exception e) {
			return "";
		}
	}
	
	
	@RequestMapping(value="/download", method = RequestMethod.POST)
	public @ResponseBody String downloadSong(@RequestParam("id") String id, @RequestParam("artist") String artist,
			@RequestParam("title") String title, @RequestParam("data") String data) {
		
		try {
			LOG.debug(">>>> data: {}", data);
			//return  manager.doPost(API_URL, FETCH_PARAMS.concat(data));
			String downloadCode = manager.doPost(Constants.FETCH_URL, "data="+data);
			downloadCode = downloadCode.replaceAll("\"", "");
			
			LOG.debug(">>>> dowloadCode:{}", downloadCode);
			//Esperamos 10 segundos para que se genere la canción
			Thread.sleep(10000);

			//Descargamos la canción
			manager.downloadSong(Constants.DOWNLOAD_URL.concat(downloadCode), title, artist);
			
			
			/*
			DownloadSongThread downloadService = new DownloadSongThread(manager, id, title, artist, downloadCode);
			downloadService.start();
			*/
			return "Downloaded: " + title + " - " + artist;
		} catch (Exception e) {
			LOG.error("Process error.", e);
			return "Error downloading: " + title + " - " + artist;
		}
	}
	
}