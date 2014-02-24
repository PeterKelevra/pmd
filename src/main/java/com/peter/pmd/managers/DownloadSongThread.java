package com.peter.pmd.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peter.pmd.util.Constants;

//@Component
//@Scope("request")
public class DownloadSongThread extends Thread {
 
	private static final Logger LOG = LoggerFactory.getLogger(DownloadSongThread.class);
	
	private HttpManager manager;
	
	private String id;
	private String title;
	private String artist;
	private String code;
	
	
	public DownloadSongThread(HttpManager manager, String id, String title, String artist, String code) {
		super();
		this.manager = manager;
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.code = code;
	}

	@Override
	public void run() {
 
		try {
			String status = "";
			int cont = 0;
			do {
				status = manager.doPost(Constants.PROGRESS_URL, Constants.ID.concat(id));
				Thread.sleep(1000);
				cont++;
			}
			while (!status.equals("100") && cont < 10);
			
			manager.downloadSong(Constants.DOWNLOAD_URL.concat(code), title, artist);
			
		} catch (Exception e) {
			LOG.error("Error en la descarga.", e);
		}
	}

}