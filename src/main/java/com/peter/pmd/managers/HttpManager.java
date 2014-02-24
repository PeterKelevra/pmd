package com.peter.pmd.managers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.peter.pmd.util.Constants;


@Service("httpManager")
public class HttpManager {
	

	private static final Logger LOG = LoggerFactory.getLogger(HttpManager.class);
	
	private final static String USER_AGENT = "Mozilla/5.0";
	
	
	public String doPost(String url) throws Exception{
		return doPost(url, null);
	}
	/**
	 * Realiza una llamada post
	 * @param url
	 * @param urlParameters
	 */
	public String doPost(String url, String urlParameters) throws Exception{
 
	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection)obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		if (urlParameters != null) {
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
		}
 
		int responseCode = con.getResponseCode();
		LOG.debug("Sending 'POST' request to URL : " + url);
		LOG.debug("Post parameters : " + urlParameters);
		LOG.debug("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		return response.toString();
 
	}	

	
	/**
	 * Descarga una canción y la guarda en en disco
	 * @param stUrl
	 * @param title
	 * @throws Exception
	 */
	public void downloadSong(String stUrl, String title ) throws Exception {
		downloadSong(stUrl, title, null);
	}
	
	/**
	 * Descarga una canción y la guarda en en disco
	 * @param stUrl
	 * @param title
	 * @param artist
	 * @throws Exception
	 */
	public void downloadSong(String stUrl, String title, String artist ) throws Exception{
		LOG.debug(">>> downloadSong");
		InputStream is = null;
		try {

			//Eliminamos el caracter / por evitar fallo al almacenar en disco
			title = title.replaceAll("/", "-");
			if (artist != null) {
				artist = artist.replaceAll("/", "-");
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//FileOutputStream bais = new FileOutputStream( new File(Mp3IslandConstants.DOWNLOAD_FOLDER.concat(title).concat(" - ").concat(artist).concat(".mp3") ) );
			URL url = new URL(stUrl);
			is = url.openStream ();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			int n;

			while ( (n = is.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}
			baos.close();
			String fileName = Constants.DOWNLOAD_FOLDER.concat(StringUtils.trimTrailingWhitespace(title));
			if (artist != null){
				fileName = fileName.concat(" - ").concat(StringUtils.trimTrailingWhitespace(artist));
			}
			fileName = fileName.concat(".mp3");
			
			baos.writeTo(new FileOutputStream( new File( fileName ) ) );
			LOG.debug(">>> downloadSong Finish!!");
		
		} finally {
		  if (is != null) { try{is.close();}catch(Exception e){} }
		}
	}
	
}
