package com.peter.pmd.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//@Component
//@Scope("request")
public class DownloadSongThread extends Thread {
 
	private HttpManager manager;
	
	private String id;
	private String code;
	
	
	public DownloadSongThread(HttpManager manager, String id, String code) {
		super();
		this.manager = manager;
		this.id = id;
		this.code = code;
	}

	public void setParams(HttpManager manager, String id, String code) {
		this.id = id;
		this.code = code;
	}

	@Override
	public void run() {
 
		System.out.println(getName() + " is running "+ manager);
		try {
			String status = "";
			int cont = 0;
			do {
				System.out.println(">>> Id: "+id);
				System.out.println(">>> Code: "+code);
				status = manager.doPost(Constants.PROGRESS_URL, Constants.ID.concat(id));
				System.out.println(">>> Status: "+status);
				Thread.sleep(1000);
				cont++;
			}
			while (!status.equals("100") && cont < 10);
			
			downloadSong();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(getName() + " end running");
	}
	
	
	private void downloadSong() {
		InputStream is = null;
		URL url = null;
		try {
			FileOutputStream bais = new FileOutputStream( new File("D:/Downloads/".concat(id).concat(".mp3") ) );
			url = new URL(Constants.DOWNLOAD_URL.concat(code));
		  is = url.openStream ();
		  byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
		  int n;

		  while ( (n = is.read(byteChunk)) > 0 ) {
		    bais.write(byteChunk, 0, n);
		  }
		  System.out.println(">>>> Bais"+ bais);
		  bais.close();
		}
		catch (Exception e) {
		  System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
		  e.printStackTrace ();
		  // Perform any other exception handling that's appropriate.
		}
		finally {
		  if (is != null) { try{is.close();}catch(Exception e){} }
		}
	}
 
}